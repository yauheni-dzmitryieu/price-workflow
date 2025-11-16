package com.yd.price.workflow.rest.converter;

import com.yd.price.workflow.model.PriceAdjustment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PriceAdjustmentResponseConverter implements Converter<com.yd.price.workflow.db.entity.PriceAdjustment, PriceAdjustment> {

    @Override
    public PriceAdjustment convert(com.yd.price.workflow.db.entity.PriceAdjustment source) {
        return new PriceAdjustment(null, null, null, null, null);
    }
}
