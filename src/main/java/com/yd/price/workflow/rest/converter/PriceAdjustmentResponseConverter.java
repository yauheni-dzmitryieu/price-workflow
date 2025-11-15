package com.yd.price.workflow.rest.converter;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.rest.dto.PriceAdjustmentResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PriceAdjustmentResponseConverter implements Converter<PriceAdjustment, PriceAdjustmentResponse> {

    @Override
    public PriceAdjustmentResponse convert(PriceAdjustment source) {
        return new PriceAdjustmentResponse();
    }
}
