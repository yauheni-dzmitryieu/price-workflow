package com.yd.price.workflow.rest.dto;

import com.yd.price.workflow.persistence.entity.PriceAdjustmentMode;
import com.yd.price.workflow.persistence.entity.PriceAdjustmentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceAdjustmentRequest(
        Long productId,
        PriceAdjustmentType type,
        BigDecimal value,
        PriceAdjustmentMode mode,
        LocalDateTime updatedAt
) {
}
