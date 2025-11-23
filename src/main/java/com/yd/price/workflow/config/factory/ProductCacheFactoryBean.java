package com.yd.price.workflow.config.factory;

import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.cache.composite.ProductCompositeCache;
import com.yd.price.workflow.cache.l1.ProductCacheL1Caffeine;
import com.yd.price.workflow.cache.l2.ProductCacheL2Redis;
import com.yd.price.workflow.config.CacheProperties;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.util.Assert;

public class ProductCacheFactoryBean implements FactoryBean<ProductCache> {

    private final CacheProperties properties;
    private final RedissonClient redissonClient;

    public ProductCacheFactoryBean(CacheProperties properties, RedissonClient redissonClient) {
        Assert.notNull(properties, "properties must not be null");
        this.properties = properties;
        this.redissonClient = redissonClient;

        if (properties.getL2().isEnabled()) {
            Assert.notNull(redissonClient, "redissonClient must not be null");
        }
    }

    @Override
    public ProductCache getObject() throws Exception {
        if (properties.getL1().isEnabled()) {
            if (properties.getL2().isEnabled()) {
                return new ProductCompositeCache(new ProductCacheL1Caffeine(), new ProductCacheL2Redis(redissonClient));
            } else {
                return new ProductCacheL1Caffeine();
            }
        }

        if (properties.getL2().isEnabled()) {
            return new ProductCacheL2Redis(redissonClient);
        }

        throw new FactoryBeanNotInitializedException("ProductCacheFactoryBean not initialized. All caches disabled: " + properties);
    }

    @Override
    public Class<?> getObjectType() {
        return ProductCache.class;
    }
}
