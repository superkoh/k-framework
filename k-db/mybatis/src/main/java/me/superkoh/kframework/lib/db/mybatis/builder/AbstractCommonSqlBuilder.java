package me.superkoh.kframework.lib.db.mybatis.builder;

import me.superkoh.kframework.core.type.Page;
import me.superkoh.kframework.lib.db.mybatis.annotation.Column;
import me.superkoh.kframework.lib.db.mybatis.annotation.Ignore;
import me.superkoh.kframework.lib.db.mybatis.annotation.PK;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static me.superkoh.kframework.lib.db.mybatis.builder.SqlBuilderUtils.*;

/**
 * Created by KOH on 2017/5/13.
 * <p>
 * webFramework
 */
abstract public class AbstractCommonSqlBuilder {

    private static Logger logger = LoggerFactory.getLogger(AbstractCommonSqlBuilder.class);

    protected abstract String getTableName();

    public String insert(Object record) {
        SQL sql = new SQL().INSERT_INTO(this.getTableName());
        getAllDeclaredFields(record.getClass()).stream()
                .filter(field -> !field.isAnnotationPresent(Ignore.class))
                .forEach(field -> {
                    String fieldName = field.getName();
                    Object value = valueOfField(record, fieldName);
                    if (null != value) {
                        if (field.isAnnotationPresent(Column.class)) {
                            Column annotation = field.getAnnotation(Column.class);
                            sql.VALUES(annotation.column().trim(), fieldExpression(fieldName, annotation));
                        } else {
                            sql.VALUES(columnName(fieldName), fieldExpression(fieldName, null));
                        }
                    }
                });
        if (logger.isDebugEnabled()) {
            logger.debug(sql.toString());
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Object record) {
        final String[] primaryKeyName = {null};
        final Column[] primaryKeyAnnotation = {null};
        SQL sql = new SQL().UPDATE(this.getTableName());
        getAllDeclaredFields(record.getClass()).stream()
                .filter(field -> !field.isAnnotationPresent(Ignore.class))
                .forEach(field -> {
                    String fieldName = field.getName();
                    if (field.isAnnotationPresent(PK.class)) {
                        primaryKeyName[0] = field.getName();
                        if (field.isAnnotationPresent(Column.class)) {
                            primaryKeyAnnotation[0] = field.getAnnotation(Column.class);
                        }
                    }
                    Object value = valueOfField(record, fieldName);
                    if (null != value) {
                        if (field.isAnnotationPresent(Column.class)) {
                            Column annotation = field.getAnnotation(Column.class);
                            sql.SET(annotation.column().trim() + "=" + fieldExpression(fieldName, annotation));
                        } else {
                            sql.SET(columnName(fieldName) + "=" + fieldExpression(fieldName, null));
                        }
                    }
                });
        if (null == primaryKeyName[0]) {
            return null;
        }
        if (null != primaryKeyAnnotation[0]) {
            sql.WHERE(primaryKeyAnnotation[0].column().trim()
                    + "=" + fieldExpression(primaryKeyName[0], primaryKeyAnnotation[0]));
        } else {
            sql.WHERE(columnName(primaryKeyName[0])
                    + "=" + fieldExpression(primaryKeyName[0], null));
        }
        return sql.toString();
    }

    public String selectByQuery(Class clazz, @Param("query") Object query, String orderBy) {
        SQL sql = selectByQueryInternal(this.getTableName(), clazz);
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
        SQL sql = selectByQueryInternal(this.getTableName(), clazz);
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
        SQL sql = countByQueryInternal(this.getTableName());
        queryWHERE(sql, params.get("query"), null);
        if (logger.isDebugEnabled()) {
            logger.debug(sql.toString());
        }
        return sql.toString();
    }

}
