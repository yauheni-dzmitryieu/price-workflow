package com.yd.price.workflow.cache.l2;

import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.time.Duration;

public class ProductCacheL2Redis implements ProductCache {
    private final RedissonClient redisson;
    private boolean enabled = true; //TODO replace use property

    public ProductCacheL2Redis(RedissonClient redisson) {
        this.redisson = redisson;
    }

    @Override
    public ProductBasePriceView get(Long productId) {
        return enabled ? getCache(productId).get() : null;
    }

    @Override
    public void put(ProductBasePriceView productBasePriceView) {
        if (enabled) {
            getCache(productBasePriceView.id()).set(productBasePriceView, Duration.ofMinutes(30)); //TODO move to property
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    private RBucket<ProductBasePriceView> getCache(Long productId) {
        return redisson.getBucket("product-cache:" + productId); //TODO move to key generator
    }
}
