package com.yd.price.workflow.cache.api;


import com.yd.price.workflow.db.entity.PriceAdjustment;

import java.util.Set;

//TODO use independent dto for work with cache
public interface PriceAdjustmentCache {
    String REFRESH_TOPIC = "cache.refresh.priceAdjustment";
    String EVICT_TOPIC = "cache.evict.priceAdjustment";

    Set<PriceAdjustment> getAll(Long productId);

    void put(Long productId, PriceAdjustment result);

    int addAll(Long productId, Set<PriceAdjustment> source);

    void evict(Long productId);

}
