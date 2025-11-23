package com.yd.price.workflow.cache.l1;

import com.github.benmanes.caffeine.cache.Cache;
import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.springframework.util.Assert;

public class ProductCacheL1Caffeine implements ProductCache {

    private final Cache<Long, ProductBasePriceView> cache;

    public ProductCacheL1Caffeine(Cache<Long, ProductBasePriceView> cache) {
        Assert.notNull(cache, "Cache must not be null");
        this.cache = cache;
    }

    @Override
    public ProductBasePriceView get(Long productId) {
        return cache.getIfPresent(productId);
    }

    @Override
    public void put(ProductBasePriceView productBasePriceView) {
        cache.put(productBasePriceView.id(), productBasePriceView);
    }
}
