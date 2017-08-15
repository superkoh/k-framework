package me.superkoh.kframework.lib.db.mybatis.test;

/**
 * Created by zhangyh on 16/7/28.
 */
public enum UserSex {
    UNKNOWN(0),
    MALE(1),
    FEMALE(2);

    private int value;

    UserSex(int value) {
        this.value = value;
    }

    public static UserSex getByValue(int value) {
        switch (value){
            case 2:
                return FEMALE;
            case 1:
                return MALE;
            case 0:
                return UNKNOWN;
            default:
                break;
        }
        return UNKNOWN;
    }

    public int getValue() {
        return value;
    }
}
