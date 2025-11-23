package com.yd.price.workflow.cache.l2;

import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.time.Duration;

public class ProductCacheL2Redis implements ProductCache {
    private final RedissonClient redisson;
    private final Duration expire;

    public ProductCacheL2Redis(RedissonClient redisson, Duration expire) {
        this.redisson = redisson;
        this.expire = expire;
    }

    @Override
    public ProductBasePriceView get(Long productId) {
        return getCache(productId).get();
    }

    @Override
    public void put(ProductBasePriceView productBasePriceView) {
        getCache(productBasePriceView.id()).set(productBasePriceView, this.expire);
    }

    private RBucket<ProductBasePriceView> getCache(Long productId) {
        return redisson.getBucket("product-cache:" + productId); //TODO move to key generator
    }
}
