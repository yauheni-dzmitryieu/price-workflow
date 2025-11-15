package com.yd.price.workflow.service.api;

import com.yd.price.workflow.db.entity.PriceAdjustment;

public interface PriceAdjustmentService {

    PriceAdjustment addToProduct(Long productId, PriceAdjustment priceAdjustment);
}
