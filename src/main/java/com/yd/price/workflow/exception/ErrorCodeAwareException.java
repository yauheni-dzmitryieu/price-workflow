package com.yd.price.workflow.exception;

public class ErrorCodeAwareException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] arguments;


    public ErrorCodeAwareException(ErrorCode code, Object... arguments) {
        this.errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeAwareException(ErrorCode code, Object[] arguments, Throwable cause) {
        super(cause);
        this.errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
