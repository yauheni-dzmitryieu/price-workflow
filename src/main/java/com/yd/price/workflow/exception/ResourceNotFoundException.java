package com.yd.price.workflow.exception;

import com.yd.price.workflow.model.PriceWorkflowErrorCode;

public class ResourceNotFoundException extends ErrorCodeAwareException {

    public ResourceNotFoundException(Long id, PriceWorkflowErrorCode code) {
        super("Resource not found: " + id, code.getCode());// TODO
    }
}
