package com.yd.price.workflow.rest.validation;

import com.yd.price.workflow.rest.dto.PriceAdjustmentRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PriceAdjustmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PriceAdjustmentRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PriceAdjustmentRequest toValidate = (PriceAdjustmentRequest) target;
    }
}
