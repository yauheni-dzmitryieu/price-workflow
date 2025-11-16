package com.yd.price.workflow.model;

import java.math.BigDecimal;
import java.util.Collection;

public record ProductPrice(
        Long productId,
        BigDecimal basePrice,
        Collection<PriceAdjustment> priceAdjustments,
        BigDecimal finalPrice
) {
}
