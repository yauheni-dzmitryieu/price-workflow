package com.yd.price.workflow.cache.l1;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;

import java.util.concurrent.TimeUnit;

public class ProductCacheL1Caffeine implements ProductCache {

    private final Cache<Long, ProductBasePriceView> cache;
    private final boolean enabled = true; // TODO move to property

    public ProductCacheL1Caffeine() {
        this.cache = Caffeine.newBuilder() // Move to bean
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public ProductBasePriceView get(Long productId) {
        return enabled ? cache.getIfPresent(productId) : null;
    }

    @Override
    public void put(ProductBasePriceView productBasePriceView) {
        if (!enabled) {
            return;
        }

        cache.put(productBasePriceView.id(), productBasePriceView);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
