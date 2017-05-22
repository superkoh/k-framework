package me.superkoh.kframework.mvc.exception;

import me.superkoh.kframework.core.exception.KRuntimeException;

public class NotLoginException extends KRuntimeException {
    public NotLoginException() {
        super(4031, "not login");
    }
}
