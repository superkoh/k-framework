package me.superkoh.kframework.core.exception;

public class KException extends Exception {
    private int code = -1;

    public KException() {
        super("系统错误,请稍后重试");
    }

    public KException(String message) {
        super(message);
    }

    public KException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
