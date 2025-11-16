package com.yd.price.workflow.model;

public enum PriceWorkflowErrorCode {
    PRODUCT_NOT_FOUND_BY_ID(1000);


    private final int code;

    PriceWorkflowErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
