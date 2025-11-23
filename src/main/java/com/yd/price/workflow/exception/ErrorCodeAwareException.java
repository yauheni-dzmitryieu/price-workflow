package com.yd.price.workflow.exception;

import java.util.List;

public class ErrorCodeAwareException extends RuntimeException {

    private final List<Integer> codes;

    public ErrorCodeAwareException(Integer code) {
        this.codes = List.of(code);
    }

    public ErrorCodeAwareException(String message, Integer code) {
        super(message);
        this.codes = List.of(code);
    }

    public ErrorCodeAwareException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.codes = List.of(code);
    }

    public ErrorCodeAwareException(Throwable cause, Integer code) {
        super(cause);
        this.codes = List.of(code);
    }

    public ErrorCodeAwareException(List<Integer> codes) {
        this.codes = codes;
    }

    public ErrorCodeAwareException(String message, List<Integer> codes) {
        super(message);
        this.codes = codes;
    }

    public ErrorCodeAwareException(String message, Throwable cause, List<Integer> codes) {
        super(message, cause);
        this.codes = codes;
    }

    public ErrorCodeAwareException(Throwable cause, List<Integer> codes) {
        super(cause);
        this.codes = codes;
    }

    public List<Integer> getCodes() {
        return codes;
    }
}
