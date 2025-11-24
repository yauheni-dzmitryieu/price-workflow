package com.yd.price.workflow.exception;

public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),

    INVALID_PARAMETER(4_000),
    MALFORMED_PAYLOAD(4_001),

    PRODUCT_NOT_FOUND(1_000);


    private int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
