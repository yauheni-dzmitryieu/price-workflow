package com.yd.price.workflow.rest.dto;

import java.math.BigDecimal;
import java.util.Collection;

public class ProductPriceResponse {
    private String productId;
    private BigDecimal basePrice;
    private BigDecimal finalPrice;
    private Collection<PriceAdjustmentRequest> priceAdjustments;
}
