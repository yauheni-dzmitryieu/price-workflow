package com.yd.price.workflow.cache.composite;

import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.cache.health.RedisConnectionHolder;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductCompositeCache implements ProductCache {
    private static final Logger log = LoggerFactory.getLogger(PriceAdjustmentCompositeCache.class);

    private final ProductCache l1Caffeine;
    private final ProductCache l2Redis;
    private final RedisConnectionHolder redisConnectionHolder;

    public ProductCompositeCache(ProductCache l1Caffeine, ProductCache l2Redis, RedisConnectionHolder redisConnectionHolder) {
        this.l1Caffeine = l1Caffeine;
        this.l2Redis = l2Redis;
        this.redisConnectionHolder = redisConnectionHolder;
    }

    @Override
    public ProductBasePriceView get(Long productId) {
        if (!redisConnectionHolder.isAvailable()) {
            log.warn("Trying to get product by productId:{}, but L2 cache is not available.", productId);
            return null;
        }

        ProductBasePriceView view = l1Caffeine.get(productId);
        if (view != null) {
            return view;
        }

        view = l2Redis.get(productId);
        if (view != null) {
            l1Caffeine.put(view);
            return view;
        }

        return null;
    }

    @Override
    public void put(ProductBasePriceView productBasePriceView) {
        if (!redisConnectionHolder.isAvailable()) {
            log.warn("Trying to save product: {}, but L2 cache is not available.", productBasePriceView);
            return;
        }

        l2Redis.put(productBasePriceView);
        l1Caffeine.put(productBasePriceView);
    }
}
