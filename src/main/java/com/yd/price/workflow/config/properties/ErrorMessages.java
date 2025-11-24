package com.yd.price.workflow.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties
public class ErrorMessages {
    private Map<Integer, ResponseMessage> errors;

    public void setErrors(Map<Integer, ResponseMessage> errors) {
        this.errors = errors;
    }

    public ResponseMessage getResponseMessage(int code) {
        return errors.get(code);
    }

    public static class ResponseMessage {
        private String message;
        private int code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
