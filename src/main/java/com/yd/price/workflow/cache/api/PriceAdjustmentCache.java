package com.yd.price.workflow.cache.api;


import com.yd.price.workflow.db.entity.PriceAdjustment;

import java.util.Set;

public interface PriceAdjustmentCache {
    String REFRESH_TOPIC = "cache.refresh.priceAdjustment";
    String EVICT_TOPIC = "cache.evict.priceAdjustment";

    Set<PriceAdjustment> getAll(Long productId);

    void put(Long productId, PriceAdjustment result);

    //TODO remove productId parameter as extra
    int addAll(Long productId, Set<PriceAdjustment> source);

    void evict(Long productId);

}
