package com.yd.price.workflow.rest.validator;

import com.yd.price.workflow.rest.dto.PriceAdjustmentMode;
import com.yd.price.workflow.rest.dto.PriceAdjustmentRequest;
import com.yd.price.workflow.rest.dto.PriceAdjustmentType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

//TODO update Swagger API + update response errors. + pass error codes
@Component
public class PriceAdjustmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PriceAdjustmentRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PriceAdjustmentRequest req = (PriceAdjustmentRequest) target;

        if (req.productId() == null) {
            errors.rejectValue("productId", "productId.null", "productId must not be null");
        } else if (req.productId() <= 0) {
            errors.rejectValue("productId", "productId.invalid", "productId must be greater than 0");
        }


        if (req.type() == null) {
            errors.rejectValue("type", "type.null", "type must not be null");
        }


        if (req.value() == null) {
            errors.rejectValue("value", "value.null", "value must not be null");
        } else if (req.value().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("value", "value.invalid", "value must be greater than zero");
        }


        if (req.mode() == null) {
            errors.rejectValue("mode", "mode.null", "mode must not be null");
        }


        if (req.mode() != null && req.value() != null && PriceAdjustmentMode.PERCENT.equals(req.mode())) {
            // Percent must be between 0 and 100
            if (req.value().compareTo(BigDecimal.ZERO) <= 0 || req.value().compareTo(BigDecimal.valueOf(100)) > 0) {
                errors.rejectValue("value", "value.percent.range", "For PERCENT mode, value must be between 0 and 100");
            }

        }

        //TAX cannot be 0%
        if (PriceAdjustmentType.TAX.equals(req.type())
                && PriceAdjustmentMode.PERCENT.equals(req.mode())
                && (req.value() != null && BigDecimal.ZERO.compareTo(req.value()) == 0)) {
            errors.rejectValue("value", "value.tax.zero", "TAX percentage must be greater than 0");
        }
    }
}
