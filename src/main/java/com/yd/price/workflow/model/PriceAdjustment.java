package com.yd.price.workflow.model;

import com.yd.price.workflow.rest.dto.PriceAdjustmentMode;
import com.yd.price.workflow.rest.dto.PriceAdjustmentType;

import java.math.BigDecimal;

public record PriceAdjustment(
        Long id,
        Long productId,
        PriceAdjustmentType type,
        BigDecimal value,
        PriceAdjustmentMode mode
) {

}
