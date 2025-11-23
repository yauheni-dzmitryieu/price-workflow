package com.yd.price.workflow.config.cache.factory;

import com.github.benmanes.caffeine.cache.Cache;
import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.composite.PriceAdjustmentCompositeCache;
import com.yd.price.workflow.cache.l1.PriceAdjustmentCacheL1Caffeine;
import com.yd.price.workflow.cache.l2.PriceAdjustmentCacheL2Redis;
import com.yd.price.workflow.cache.sync.CacheInvalidationPublisher;
import com.yd.price.workflow.config.cache.CacheProperties;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

public class PriceAdjustmentCacheFactoryBean implements FactoryBean<PriceAdjustmentCache> {

    private final CacheProperties properties;
    private final Optional<Cache<Long, Set<PriceAdjustment>>> l1cache;
    private final Optional<RedissonClient> redissonClient;
    private final Optional<CacheInvalidationPublisher> publisher;

    public PriceAdjustmentCacheFactoryBean(CacheProperties properties,
                                           Optional<Cache<Long, Set<PriceAdjustment>>> l1cache,
                                           Optional<RedissonClient> redissonClient,
                                           Optional<CacheInvalidationPublisher> publisher) {
        this.properties = properties;
        this.l1cache = l1cache;
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
                return new PriceAdjustmentCompositeCache(new PriceAdjustmentCacheL1Caffeine(l1cache.get()),  this.getL2Redis(), publisher.get());
            } else {
                return new PriceAdjustmentCacheL1Caffeine(l1cache.get());
            }
        }

        if (properties.getL2().isEnabled()) {
            return this.getL2Redis();
        }

        throw new FactoryBeanNotInitializedException("PriceAdjustmentCacheFactoryBean not initialized. All caches disabled: " + properties);
    }

    private PriceAdjustmentCacheL2Redis getL2Redis() {
        Duration expire = properties.getL2().getCaches().get(CacheProperties.CacheName.PRICE_ADJUSTMENT_CACHE).getExpire();
        Assert.notNull(expire, "expire must not be null");

        return new PriceAdjustmentCacheL2Redis(redissonClient.get(), expire);
    }

    @Override
    public Class<?> getObjectType() {
        return PriceAdjustmentCache.class;
    }
}
