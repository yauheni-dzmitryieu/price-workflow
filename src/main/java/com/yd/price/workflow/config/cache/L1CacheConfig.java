package com.yd.price.workflow.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.cache.l1.PriceAdjustmentCacheL1Caffeine;
import com.yd.price.workflow.cache.l1.ProductCacheL1Caffeine;
import com.yd.price.workflow.cache.sync.CacheInvalidationPublisher;
import com.yd.price.workflow.cache.sync.L1RefreshCacheListener;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.Set;

@Configuration
@ConditionalOnBooleanProperty(prefix = "app.cache.l1", value = "enabled", havingValue = true)
public class L1CacheConfig {

    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public Cache<Long, Set<PriceAdjustment>> priceAdjustmentL1Cache() {
        CacheProperties.L1.Parameters parameters = cacheProperties.getL1().getCaches().get(CacheProperties.CacheName.PRICE_ADJUSTMENT_CACHE);
        return Caffeine.newBuilder()
                .maximumSize(parameters.getMaximumSize())
                .expireAfterWrite(parameters.getExpireAfterWrite())
                .build();
    }

    @Bean
    public Cache<Long, ProductBasePriceView> productL1Cache() {
        CacheProperties.L1.Parameters parameters = cacheProperties.getL1().getCaches().get(CacheProperties.CacheName.PRODUCT_CACHE);
        return Caffeine.newBuilder()
                .maximumSize(parameters.getMaximumSize())
                .expireAfterWrite(parameters.getExpireAfterWrite())
                .build();
    }

    @Bean
    public ProductCache l1productCache(Cache<Long, ProductBasePriceView> cache) {
        return new ProductCacheL1Caffeine(cache);
    }

    @Bean
    public PriceAdjustmentCache l1PriceAdjustmentCache(Cache<Long, Set<PriceAdjustment>> priceAdjustmentL1Cache) {
        return new PriceAdjustmentCacheL1Caffeine(priceAdjustmentL1Cache);
    }


    @Bean
    @ConditionalOnBooleanProperty(prefix = "app.cache.l2", value = "enabled", havingValue = true)
    public CacheInvalidationPublisher cacheInvalidationPublisher(Optional<RedissonClient> redissonClient) {
        return new CacheInvalidationPublisher(redissonClient);
    }

    @Bean
    @ConditionalOnBooleanProperty(prefix = "app.cache.l2", value = "enabled", havingValue = true)
    public L1RefreshCacheListener l1RefreshCacheListener(Optional<RedissonClient> redissonClient, PriceAdjustmentCache priceAdjustmentCache) {
        return new L1RefreshCacheListener(redissonClient.orElse(null), priceAdjustmentCache);
    }

}
