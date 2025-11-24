package com.yd.price.workflow.exception;

public class ResourceNotFoundException extends ErrorCodeAwareException {
    public ResourceNotFoundException(ErrorCode code, Object... arguments) {
        super(code, arguments);
    }

    public ResourceNotFoundException(ErrorCode code, Object[] arguments, Throwable cause) {
        super(code, arguments, cause);
    }
}
