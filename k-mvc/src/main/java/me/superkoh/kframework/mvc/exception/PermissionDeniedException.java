package me.superkoh.kframework.mvc.exception;

import me.superkoh.kframework.core.exception.KRuntimeException;

public class PermissionDeniedException extends KRuntimeException {
    public PermissionDeniedException() {
        super("permission denied!");
    }
}
