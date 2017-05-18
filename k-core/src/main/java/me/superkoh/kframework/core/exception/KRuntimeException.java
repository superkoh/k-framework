package me.superkoh.kframework.core.exception;

/**
 * Created by KOH on 2017/2/5.
 * <p>
 * webFramework
 */
public class KRuntimeException extends RuntimeException {
    private int code = -1;

    public KRuntimeException() {
        super("系统错误,请稍后重试");
    }

    public KRuntimeException(String message) {
        super(message);
    }

    public KRuntimeException(int code, String message) {
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
