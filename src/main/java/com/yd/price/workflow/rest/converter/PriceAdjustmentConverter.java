package com.yd.price.workflow.rest.converter;

import com.yd.price.workflow.persistence.entity.PriceAdjustment;
import com.yd.price.workflow.rest.dto.PriceAdjustmentRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PriceAdjustmentConverter implements Converter<PriceAdjustmentRequest,PriceAdjustment> {

    @Override
    public PriceAdjustment convert(final PriceAdjustmentRequest source) {
        return new PriceAdjustment();
    }
}
