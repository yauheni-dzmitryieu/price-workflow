package com.yd.price.workflow.rest.converter;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.entity.PriceAdjustmentMode;
import com.yd.price.workflow.db.entity.PriceAdjustmentType;
import com.yd.price.workflow.rest.dto.PriceAdjustmentRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class PriceAdjustmentConverter implements Converter<PriceAdjustmentRequest,PriceAdjustment> {

    @Override
    public PriceAdjustment convert(final PriceAdjustmentRequest source) {
        var priceAdjustment = new PriceAdjustment();
        priceAdjustment.setProductId(source.productId());
        priceAdjustment.setValue(source.value());
        priceAdjustment.setMode(PriceAdjustmentMode.valueOf(source.mode().name()));
        priceAdjustment.setType(PriceAdjustmentType.valueOf(source.type().name()));
        priceAdjustment.setUpdatedAt(LocalDateTime.now()); //FIXME remove
        return priceAdjustment;
    }
}
