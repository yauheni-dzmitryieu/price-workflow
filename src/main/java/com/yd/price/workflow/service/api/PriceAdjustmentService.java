package com.yd.price.workflow.service.api;

import com.yd.price.workflow.persistence.entity.PriceAdjustment;

public interface PriceAdjustmentService {

    PriceAdjustment addToProduct(Long productId, PriceAdjustment priceAdjustment);
}
