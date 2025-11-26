package com.yd.price.workflow.cache.l1;

import com.github.benmanes.caffeine.cache.Cache;
import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ProductCacheL1Caffeine implements ProductCache {
    private static final Logger log = LoggerFactory.getLogger(ProductCacheL1Caffeine.class);

    private final Cache<Long, ProductBasePriceView> cache;

    public ProductCacheL1Caffeine(Cache<Long, ProductBasePriceView> cache) {
        Assert.notNull(cache, "Cache must not be null");
        this.cache = cache;
    }

    @Override
    public ProductBasePriceView get(Long productId) {
        if (productId == null) {
            log.warn("Attempted to get product with null ID");
            return null;
        }

        ProductBasePriceView result = cache.getIfPresent(productId);
        if (result == null) {
            log.debug("L1 cache MISS for productId={}", productId);
        } else {
            log.debug("L1 cache HIT for productId={}, cacheValue={}", productId, result);
        }
        return result;
    }

    @Override
    public void put(ProductBasePriceView productBasePriceView) {
        if (productBasePriceView == null) {
            log.warn("Attempted to put null ProductBasePriceView into L1 cache");
            return;
        }

        Long id = productBasePriceView.id();
        if (id == null) {
            log.warn("Attempted to put product with null ID into L1 cache: {}", productBasePriceView);
            return;
        }

        cache.put(id, productBasePriceView);
        log.debug("L1 cache PUT for productId={}, product={}", id, productBasePriceView);
    }
}
