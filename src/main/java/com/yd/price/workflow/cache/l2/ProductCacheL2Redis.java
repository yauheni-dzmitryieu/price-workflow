package com.yd.price.workflow.cache.l2;

import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class ProductCacheL2Redis implements ProductCache {

    private static final Logger log = LoggerFactory.getLogger(ProductCacheL2Redis.class);

    private final RedissonClient redisson;
    private final Duration expire;

    public ProductCacheL2Redis(RedissonClient redisson, Duration expire) {
        this.redisson = redisson;
        this.expire = expire;
    }

    @Override
    public ProductBasePriceView get(Long productId) {
        if (productId == null) {
            log.warn("Attempted to get product with null productId");
            return null;
        }

        RBucket<ProductBasePriceView> bucket = getCache(productId);
        ProductBasePriceView result = bucket.get();

        if (result == null) {
            log.debug("L2 cache MISS for productId={}", productId);
        } else {
            log.debug("L2 cache HIT for productId={} value={}", productId, result);
        }

        return result;
    }

    @Override
    public void put(ProductBasePriceView productBasePriceView) {
        if (productBasePriceView == null) {
            log.warn("Attempted to put null ProductBasePriceView into L2 cache");
            return;
        }

        if (productBasePriceView.id() == null) {
            log.warn("Attempted to put product with null ID into L2 cache: {}", productBasePriceView);
            return;
        }

        Long id = productBasePriceView.id();
        RBucket<ProductBasePriceView> bucket = getCache(id);
        bucket.set(productBasePriceView, this.expire);

        log.debug("L2 cache PUT productId={} value={} expire={}", id, productBasePriceView, expire);
    }

    private RBucket<ProductBasePriceView> getCache(Long productId) {
        String key = "product-cache:" + productId;
        return redisson.getBucket(key); // TODO move to key generator
    }
}
