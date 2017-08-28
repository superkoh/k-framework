package me.superkoh.kframework.lib.db.mybatis.builder;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import me.superkoh.kframework.core.utils.ACU;
import me.superkoh.kframework.lib.db.mybatis.annotation.Column;
import me.superkoh.kframework.lib.db.mybatis.annotation.Ignore;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KOH on 2017/8/16.
 */
public class SqlBuilderUtils {
    private static Boolean mapUnderscoreToCamelCase;
    private static Map<Class, List<Field>> classFieldMap = new HashMap<>();

    static boolean mapUnderscoreToCamelCase() {
        if (null == mapUnderscoreToCamelCase) {
            mapUnderscoreToCamelCase = ACU.getProperty("mybatis.configuration.map-underscore-to-camel-case", Boolean.class, false);
        }
        return mapUnderscoreToCamelCase;
    }

    static void setValueOfField(Object obj, String fieldName, Object value) {
        try {
            obj.getClass().getMethod("set" + StringUtils.capitalize(fieldName)).invoke(obj, value);
        } catch (Exception ignored) {
        }
    }


    static Object valueOfField(Object obj, String fieldName) {
        try {
            return obj.getClass().getMethod("get" + StringUtils.capitalize(fieldName)).invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    static String columnName(String str) {
        if (mapUnderscoreToCamelCase()) {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
        }
        return str;
    }

    static String fieldExpression(String fieldName, Column annotation) {
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

    static List<Field> getAllDeclaredFields(Class clazz) {
        if (classFieldMap.containsKey(clazz)) {
            return classFieldMap.get(clazz);
        }
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length < 1) {
            return Collections.emptyList();
        }
        List<Field> fieldList = Lists.newArrayList(fields);
        if (null != clazz.getSuperclass()) {
            fieldList.addAll(getAllDeclaredFields(clazz.getSuperclass()));
        }
        classFieldMap.put(clazz, fieldList);
        return fieldList;
    }

    static void queryWHERE(SQL sql, Object query, String orderBy) {
        getAllDeclaredFields(query.getClass()).stream()
                .filter(field -> !field.isAnnotationPresent(Ignore.class))
                .forEach(field -> {
                    String fieldName = field.getName();
                    String columnName;
                    String fieldExpression;
                    if (field.isAnnotationPresent(Column.class)) {
                        Column annotation = field.getAnnotation(Column.class);
                        columnName = annotation.column().trim();
                        fieldExpression = fieldExpression("query." + fieldName, annotation);
                    } else {
                        columnName = columnName(fieldName);
                        fieldExpression = fieldExpression("query." + fieldName, null);
                    }
                    Object value = valueOfField(query, fieldName);
                    if (null != value) {
                        if (value instanceof String) {
                            if (!((String) value).isEmpty() && !value.equals("%%")) {
                                String modifiedValue = (String) value;
                                if (!modifiedValue.startsWith("%")) {
                                    modifiedValue = "%" + modifiedValue;
                                }
                                if (!modifiedValue.endsWith("%")) {
                                    modifiedValue += "%";
                                }
                                setValueOfField(query, fieldName, modifiedValue);
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
                });
        if (null != orderBy && !orderBy.isEmpty()) {
            sql.ORDER_BY(orderBy);
        }
    }

    static SQL selectByQueryInternal(String tableName, Class clazz) {
        SQL sql = new SQL().FROM(tableName);
        getAllDeclaredFields(clazz).stream()
                .filter(field -> !field.isAnnotationPresent(Ignore.class))
                .forEach(field -> {
                    if (field.isAnnotationPresent(Column.class)) {
                        sql.SELECT(field.getAnnotation(Column.class).column().trim());
                    } else {
                        sql.SELECT(columnName(field.getName()));
                    }
                });
        return sql;
    }

    static SQL countByQueryInternal(String tableName) {
        return new SQL().FROM(tableName)
                .SELECT("count(*)");
    }
}
