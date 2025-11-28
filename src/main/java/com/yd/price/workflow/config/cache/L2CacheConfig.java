package com.yd.price.workflow.config.cache;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.cache.l2.PriceAdjustmentCacheL2Redis;
import com.yd.price.workflow.cache.l2.ProductCacheL2Redis;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static com.yd.price.workflow.config.cache.CacheProperties.CacheName.PRICE_ADJUSTMENT_CACHE;
import static com.yd.price.workflow.config.cache.CacheProperties.CacheName.PRODUCT_CACHE;

@Configuration
@ConditionalOnBooleanProperty(prefix = "app.cache.l2", value = "enabled", havingValue = true)
public class L2CacheConfig {

    private final RedissonClient redissonClient;
    private final CacheProperties properties;

    public L2CacheConfig(RedissonClient redissonClient, CacheProperties properties) {
        this.redissonClient = redissonClient;
        this.properties = properties;
    }

    @Bean
    public PriceAdjustmentCache l2PriceAdjustmentCache() {
        Duration expire = properties.getL2().getCaches().get(PRICE_ADJUSTMENT_CACHE).getExpire();
        return new PriceAdjustmentCacheL2Redis(redissonClient, expire);
    }

    @Bean
    public ProductCache l2ProductCache() {
        Duration expire = properties.getL2().getCaches().get(PRODUCT_CACHE).getExpire();
        return new ProductCacheL2Redis(redissonClient, expire);
    }
}
