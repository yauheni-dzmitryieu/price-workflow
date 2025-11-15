package com.yd.price.workflow.rest.dto;


import java.math.BigDecimal;

public record PriceAdjustmentRequest(
        Long productId,
        PriceAdjustmentType type,
        BigDecimal value,
        PriceAdjustmentMode mode
) {
}
