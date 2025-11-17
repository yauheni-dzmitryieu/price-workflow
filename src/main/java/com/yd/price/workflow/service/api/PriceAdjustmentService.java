package com.yd.price.workflow.service.api;

import com.yd.price.workflow.db.entity.PriceAdjustment;

import java.util.Set;

public interface PriceAdjustmentService {

    PriceAdjustment addToProduct(Long productId, PriceAdjustment priceAdjustment);

    Set<PriceAdjustment> getAll(Long productId);

    default void addAll(Long productId, Set<PriceAdjustment> priceAdjustments) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
