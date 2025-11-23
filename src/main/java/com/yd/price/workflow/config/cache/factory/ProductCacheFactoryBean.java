package com.yd.price.workflow.config.cache.factory;

import com.github.benmanes.caffeine.cache.Cache;
import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.cache.composite.ProductCompositeCache;
import com.yd.price.workflow.cache.l1.ProductCacheL1Caffeine;
import com.yd.price.workflow.cache.l2.ProductCacheL2Redis;
import com.yd.price.workflow.config.cache.CacheProperties;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Optional;

public class ProductCacheFactoryBean implements FactoryBean<ProductCache> {

    private final CacheProperties properties;
    private final Optional<Cache<Long, ProductBasePriceView>> l1cache;
    private final Optional<RedissonClient> redissonClient;

    public ProductCacheFactoryBean(CacheProperties properties, Optional<Cache<Long, ProductBasePriceView>> l1cache, Optional<RedissonClient> redissonClient) {
        Assert.notNull(properties, "properties must not be null");
        this.properties = properties;
        this.l1cache = l1cache;
        this.redissonClient = redissonClient;

        if (properties.getL2().isEnabled()) {
            Assert.notNull(redissonClient, "redissonClient must not be null");
        }
    }

    //TODO throw exception in case Optional null
    @Override
    public ProductCache getObject() throws Exception {
        if (properties.getL1().isEnabled()) {
            if (properties.getL2().isEnabled()) {
                return new ProductCompositeCache(new ProductCacheL1Caffeine(l1cache.get()), this.getL2Redis());
            } else {
                return new ProductCacheL1Caffeine(l1cache.get());
            }
        }

        if (properties.getL2().isEnabled()) {
            return this.getL2Redis();
        }

        throw new FactoryBeanNotInitializedException("ProductCacheFactoryBean not initialized. All caches disabled: " + properties);
    }

    private ProductCacheL2Redis getL2Redis() {
        Duration expire = properties.getL2().getCaches().get(CacheProperties.CacheName.PRODUCT_CACHE).getExpire();

        Assert.notNull(expire, "expire must not be null");
        return new ProductCacheL2Redis(redissonClient.get(), expire);
    }

    @Override
    public Class<?> getObjectType() {
        return ProductCache.class;
    }
}
