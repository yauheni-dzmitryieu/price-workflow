package com.yd.price.workflow.config.cache;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.cache.composite.PriceAdjustmentCompositeCache;
import com.yd.price.workflow.cache.composite.ProductCompositeCache;
import com.yd.price.workflow.cache.health.RedisConnectionHolder;
import com.yd.price.workflow.cache.sync.CacheInvalidationPublisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnBooleanProperties(value = {
        @ConditionalOnBooleanProperty(prefix = "app.cache.l1", value = "enabled", havingValue = true),
        @ConditionalOnBooleanProperty(prefix = "app.cache.l2", value = "enabled", havingValue = true)
})
public class CompositeCacheConfig {

    @Bean
    @Primary
    public PriceAdjustmentCache compositePriceAdjustmentCache(
            @Qualifier("l1PriceAdjustmentCache") PriceAdjustmentCache l1,
            @Qualifier("l2PriceAdjustmentCache") PriceAdjustmentCache l2,
            CacheInvalidationPublisher publisher,
            RedisConnectionHolder holder
    ) {
        return new PriceAdjustmentCompositeCache(l1, l2, publisher, holder);
    }

    @Bean
    @Primary
    public ProductCache compositeProductCache(
            @Qualifier("l1productCache") ProductCache l1,
            @Qualifier("l2ProductCache") ProductCache l2,
            RedisConnectionHolder holder) {
        return new ProductCompositeCache(l1, l2, holder);
    }
}
