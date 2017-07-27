package me.superkoh.kframework.lib.db.mybatis.builder;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import me.superkoh.kframework.core.type.Page;
import me.superkoh.kframework.lib.db.mybatis.annotation.Column;
import me.superkoh.kframework.lib.db.mybatis.annotation.Ignore;
import me.superkoh.kframework.lib.db.mybatis.annotation.PK;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by KOH on 2017/5/13.
 * <p>
 * webFramework
 */
abstract public class AbstractCommonSqlBuilder {

    private static Logger logger = LoggerFactory.getLogger(AbstractCommonSqlBuilder.class);

    protected static Object valueOfField(Object obj, String fieldName) {
        try {
            return obj.getClass().getMethod("get" + StringUtils.capitalize(fieldName)).invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    protected static String camelToUnderScore(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }

    protected static String fieldExpression(String fieldName, Column annotation) {
        StringBuilder sb = new StringBuilder("#{").append(fieldName);
        if (null != annotation) {
            if (!JdbcType.UNDEFINED.equals(annotation.jdbcType())) {
                sb.append(",jdbcType=").append(annotation.jdbcType().name());
            }
            if (!UnknownTypeHandler.class.equals(annotation.typeHandler())) {
                sb.append(",typeHandler=").append(annotation.typeHandler().getCanonicalName());
            }
        }
        sb.append("}");
        return sb.toString();
    }

    protected abstract String getTableName();

    public String insert(Object record) {
        SQL sql = new SQL().INSERT_INTO(this.getTableName());
//        Field[] fields = record.getClass().getDeclaredFields();
        List<Field> fields = getAllDeclaredFields(record.getClass());
        for (Field field : fields) {
            if (field.isAnnotationPresent(Ignore.class)) {
                continue;
            }
            String fieldName = field.getName();
            Object value = valueOfField(record, fieldName);
            if (null != value) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column annotation = field.getAnnotation(Column.class);
                    sql.VALUES(annotation.column().trim(), fieldExpression(fieldName, annotation));
                } else {
                    sql.VALUES(camelToUnderScore(fieldName), fieldExpression(fieldName, null));
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(sql.toString());
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Object record) {
        String primaryKeyName = null;
        Column primaryKeyAnnotation = null;
        SQL sql = new SQL().UPDATE(this.getTableName());
//        Field[] fields = record.getClass().getDeclaredFields();
        List<Field> fields = getAllDeclaredFields(record.getClass());
        for (Field field : fields) {
            if (field.isAnnotationPresent(Ignore.class)) {
                continue;
            }
            String fieldName = field.getName();
            if (field.isAnnotationPresent(PK.class)) {
                primaryKeyName = field.getName();
                if (field.isAnnotationPresent(Column.class)) {
                    primaryKeyAnnotation = field.getAnnotation(Column.class);
                }
            }
            Object value = valueOfField(record, fieldName);
            if (null != value) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column annotation = field.getAnnotation(Column.class);
                    sql.SET(annotation.column().trim() + "=" + fieldExpression(fieldName, annotation));
                } else {
                    sql.SET(camelToUnderScore(fieldName) + "=" + fieldExpression(fieldName, null));
                }
            }
        }
        if (null == primaryKeyName) {
            return null;
        }
        if (null != primaryKeyAnnotation) {
            sql.WHERE(primaryKeyAnnotation.column().trim()
                    + "=" + fieldExpression(primaryKeyName, primaryKeyAnnotation));
        } else {
            sql.WHERE(camelToUnderScore(primaryKeyName)
                    + "=" + fieldExpression(primaryKeyName, null));
        }
        return sql.toString();
    }

    public String selectByQuery(Class clazz, @Param("query") Object query, String orderBy) {
        SQL sql = selectByQueryInternal(clazz);
        queryWHERE(sql, query, orderBy);
        if (logger.isDebugEnabled()) {
            logger.debug(sql.toString());
        }
        return sql.toString();
    }

    public String selectPageByQuery(Class clazz, @Param("query") Object query, Page page) {
        String orderBy = null;
        if (null != page) {
            orderBy = page.getOrderBy();
        }
        SQL sql = selectByQueryInternal(clazz);
        queryWHERE(sql, query, orderBy);
        String sqlStr = sql.toString();
        if (null != page) {
            long limit = page.getPageSize();
            long offset = limit * (page.getPageNo() - 1);
            sqlStr += " limit " + offset + "," + limit;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(sqlStr);
        }
        return sqlStr;
    }

    public String countByQuery(Map<String, Object> params) {
        SQL sql = countByQueryInternal();
        queryWHERE(sql, params.get("query"), null);
        if (logger.isDebugEnabled()) {
            logger.debug(sql.toString());
        }
        return sql.toString();
    }

    private SQL selectByQueryInternal(Class clazz) {
        SQL sql = new SQL().FROM(this.getTableName());
        Field[] retFields = clazz.getDeclaredFields();
        for (Field field : retFields) {
            if (field.isAnnotationPresent(Ignore.class)) {
                continue;
            }
            if (field.isAnnotationPresent(Column.class)) {
                sql.SELECT(field.getAnnotation(Column.class).column().trim());
            } else {
                sql.SELECT(camelToUnderScore(field.getName()));
            }
        }
        return sql;
    }

    private SQL countByQueryInternal() {
        return new SQL().FROM(this.getTableName())
                .SELECT("count(*)");
    }

    private void queryWHERE(SQL sql, Object query, String orderBy) {
        Field[] queryFields = query.getClass().getDeclaredFields();
        for (Field field : queryFields) {
            if (field.isAnnotationPresent(Ignore.class)) {
                continue;
            }
            String fieldName = field.getName();
            String columnName;
            String fieldExpression;
            if (field.isAnnotationPresent(Column.class)) {
                Column annotation = field.getAnnotation(Column.class);
                columnName = annotation.column().trim();
                fieldExpression = fieldExpression("query." + fieldName, annotation);
            } else {
                columnName = camelToUnderScore(fieldName);
                fieldExpression = fieldExpression("query." + fieldName, null);
            }
            Object value = valueOfField(query, fieldName);
            if (null != value) {
                if (value instanceof String) {
                    if (!((String) value).isEmpty() && !value.equals("%%")) {
                        sql.WHERE(columnName + " like " + fieldExpression);
                    }
                } else if (value instanceof Number
                        || value instanceof LocalDate
                        || value instanceof LocalDateTime) {
                    if (fieldName.startsWith("min")) {
                        sql.WHERE(columnName + ">=" + fieldExpression);
                    } else if (field.getName().startsWith("max")) {
                        sql.WHERE(columnName + "<=" + fieldExpression);
                    } else {
                        sql.WHERE(columnName + "=" + fieldExpression);
                    }
                } else {
                    sql.WHERE(columnName + "=" + fieldExpression);
                }
            }
        }
        if (null != orderBy && !orderBy.isEmpty()) {
            sql.ORDER_BY(orderBy);
        }
    }

    private List<Field> getAllDeclaredFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length < 1) {
            return Collections.emptyList();
        }
        List<Field> fieldList = Lists.newArrayList(fields);
        if (null != clazz.getSuperclass()) {
            fieldList.addAll(getAllDeclaredFields(clazz.getSuperclass()));
        }
        return fieldList;
    }

}
