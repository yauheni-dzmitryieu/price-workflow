package com.yd.price.workflow.rest.converter;

import com.yd.price.workflow.model.PriceAdjustment;
import com.yd.price.workflow.rest.dto.PriceAdjustmentMode;
import com.yd.price.workflow.rest.dto.PriceAdjustmentType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PriceAdjustmentResponseConverter implements Converter<com.yd.price.workflow.db.entity.PriceAdjustment, PriceAdjustment> {

    @Override
    public PriceAdjustment convert(com.yd.price.workflow.db.entity.PriceAdjustment source) {
        PriceAdjustmentType type = PriceAdjustmentType.valueOf(source.getType().name());
        PriceAdjustmentMode mode = PriceAdjustmentMode.valueOf(source.getMode().name());

        return new PriceAdjustment(source.getId(), source.getProductId(), type, source.getValue(), mode);
    }
}
