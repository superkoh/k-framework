package me.superkoh.kframework.lib.db.common.domain;

import me.superkoh.kframework.core.lang.BaseObject;

public class BaseQuery extends BaseObject {

    protected static String likeExpr(String value) {
        return null == value ? null : "%" + value + "%";
    }

}
