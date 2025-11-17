package com.yd.price.workflow.model;

import com.yd.price.workflow.db.entity.PriceAdjustment;

import java.math.BigDecimal;
import java.util.Collection;

public record ProductPrice(
        Long productId,
        BigDecimal basePrice,
        Collection<PriceAdjustment> priceAdjustments,
        BigDecimal finalPrice
) {
}
