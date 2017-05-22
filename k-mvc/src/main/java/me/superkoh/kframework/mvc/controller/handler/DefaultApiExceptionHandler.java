package me.superkoh.kframework.mvc.controller.handler;

import me.superkoh.kframework.core.exception.KException;
import me.superkoh.kframework.core.exception.KRuntimeException;
import me.superkoh.kframework.mvc.controller.annotation.KController;
import me.superkoh.kframework.mvc.controller.response.ErrorRes;
import me.superkoh.kframework.mvc.exception.NeedGuestException;
import me.superkoh.kframework.mvc.exception.NotLoginException;
import me.superkoh.kframework.mvc.exception.PermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice(annotations = {KController.class})
final public class DefaultApiExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger("apiException");

    @ExceptionHandler({PermissionDeniedException.class})
    @ResponseBody
    public ErrorRes permissionDeniedExceptionHandler(HttpServletResponse response, PermissionDeniedException e) {
        response.setStatus(403);
        return new ErrorRes(e);
    }

    @ExceptionHandler({NotLoginException.class})
    @ResponseBody
    public ErrorRes notLoginExceptionHandler(HttpServletResponse response, NotLoginException e) {
        response.setStatus(403);
        return new ErrorRes(e);
    }

    @ExceptionHandler({NeedGuestException.class})
    @ResponseBody
    public ErrorRes needGuestExceptionHandler(HttpServletResponse response, NeedGuestException e) {
        response.setStatus(403);
        return new ErrorRes(e);
    }

    @ExceptionHandler({KException.class})
    @ResponseBody
    public ErrorRes bizExceptionHandler(HttpServletResponse response, KException e) {
        response.setStatus(400);
        return new ErrorRes(e);
    }

    @ExceptionHandler({KRuntimeException.class})
    @ResponseBody
    public ErrorRes bizExceptionHandler(HttpServletResponse response, KRuntimeException e) {
        response.setStatus(400);
        return new ErrorRes(e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ErrorRes constraintViolationExceptionHandler(HttpServletResponse response, ConstraintViolationException e) {
        response.setStatus(400);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        if (!violations.isEmpty()) {
            ConstraintViolation<?> violation = violations.iterator().next();
            return new ErrorRes(-1, violation.getPropertyPath().toString() + violation.getMessage());
        }
        return new ErrorRes();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorRes exceptionHandler(HttpServletResponse response, MethodArgumentNotValidException e) {
        response.setStatus(400);
        return new ErrorRes(-1, e.getBindingResult().getFieldError().getField() + ":" + e.getBindingResult()
                .getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ErrorRes exceptionHandler(HttpServletResponse response, HttpMessageNotReadableException e) {
        response.setStatus(400);
        String message = "参数不正确";
        return new ErrorRes(-1, message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorRes exceptionHandler(HttpServletResponse response, Exception e) {
        response.setStatus(500);
        logger.error(e.getMessage(), e);
        String message = e.getMessage();
        if (null == e.getMessage() || e.getMessage().isEmpty()) {
            message = "未知错误";
        }
        return new ErrorRes(-1, message);
    }
}
