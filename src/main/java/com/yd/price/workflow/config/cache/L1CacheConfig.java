package com.yd.price.workflow.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
