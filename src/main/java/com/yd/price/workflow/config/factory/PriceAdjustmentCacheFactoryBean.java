package com.yd.price.workflow.config.factory;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.composite.PriceAdjustmentCompositeCache;
import com.yd.price.workflow.cache.l1.PriceAdjustmentCacheL1Caffeine;
import com.yd.price.workflow.cache.l2.PriceAdjustmentCacheL2Redis;
import com.yd.price.workflow.cache.sync.CacheInvalidationPublisher;
import com.yd.price.workflow.config.CacheProperties;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.util.Assert;

public class PriceAdjustmentCacheFactoryBean implements FactoryBean<PriceAdjustmentCache> {

    private final CacheProperties properties;
    private final RedissonClient redissonClient;
    private final CacheInvalidationPublisher publisher;

    public PriceAdjustmentCacheFactoryBean(CacheProperties properties, RedissonClient redissonClient, CacheInvalidationPublisher publisher) {
        this.properties = properties;
        this.redissonClient = redissonClient;
        this.publisher = publisher;

        if (properties.getL2().isEnabled()) {
            Assert.notNull(redissonClient, "redissonClient must not be null");
            Assert.notNull(publisher, "publisher must not be null");
        }
    }

    @Override
    public PriceAdjustmentCache getObject() throws Exception {
        if (properties.getL1().isEnabled()) {
            if (properties.getL2().isEnabled()) {
                return new PriceAdjustmentCompositeCache(new PriceAdjustmentCacheL1Caffeine(), new PriceAdjustmentCacheL2Redis(redissonClient), publisher);
            } else {
                return new PriceAdjustmentCacheL1Caffeine();
            }
        }

        if (properties.getL2().isEnabled()) {
            return new PriceAdjustmentCacheL2Redis(redissonClient);
        }

        throw new FactoryBeanNotInitializedException("PriceAdjustmentCacheFactoryBean not initialized. All caches disabled: " + properties);
    }

    @Override
    public Class<?> getObjectType() {
        return PriceAdjustmentCache.class;
    }
}
