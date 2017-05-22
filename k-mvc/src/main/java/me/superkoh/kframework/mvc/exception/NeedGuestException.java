package me.superkoh.kframework.mvc.exception;

import me.superkoh.kframework.core.exception.KRuntimeException;

public class NeedGuestException extends KRuntimeException {
    public NeedGuestException() {
        super(4032, "need guest");
    }
}
