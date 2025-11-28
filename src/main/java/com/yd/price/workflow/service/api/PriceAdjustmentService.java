package com.yd.price.workflow.service.api;

import com.yd.price.workflow.db.entity.PriceAdjustment;

import java.util.Set;

public interface PriceAdjustmentService {

    PriceAdjustment save(Long productId, PriceAdjustment priceAdjustment);

    Set<PriceAdjustment> getAll(Long productId);
}
