package com.yd.price.workflow.config;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.sync.CacheInvalidationPublisher;
import com.yd.price.workflow.cache.sync.L1RefreshCacheListener;
import com.yd.price.workflow.config.factory.PriceAdjustmentCacheFactoryBean;
import com.yd.price.workflow.config.factory.ProductCacheFactoryBean;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class ServiceConfiguration {

    @Configuration
    @EnableConfigurationProperties(CacheProperties.class)
    @ConditionalOnExpression("${app.cache.l1.enabled:false} or ${app.cache.l2.enabled:false}")
    public static class CacheConfiguration {

        @Autowired
        private Optional<RedissonClient> redissonClient;

        @Autowired
        private CacheProperties cacheProperties;

        @Bean
        @ConditionalOnBooleanProperties(value = {
                @ConditionalOnBooleanProperty(prefix = "app.cache.l1", value = "enabled", havingValue = true),
                @ConditionalOnBooleanProperty(prefix = "app.cache.l2", value = "enabled", havingValue = true)
        })
        public CacheInvalidationPublisher cacheInvalidationPublisher() {
            return new CacheInvalidationPublisher(redissonClient.get());
        }

        @Bean
        @ConditionalOnBooleanProperty(prefix = "app.cache.l1", value = "enabled", havingValue = true)
        public L1RefreshCacheListener l1RefreshCacheListener(PriceAdjustmentCache priceAdjustmentCache) {
            return new L1RefreshCacheListener(redissonClient.orElse(null), priceAdjustmentCache);
        }

        @Bean
        public ProductCacheFactoryBean productCache() {
            return new ProductCacheFactoryBean(cacheProperties, redissonClient.orElse(null));
        }

        @Bean
        public PriceAdjustmentCacheFactoryBean priceAdjustmentCache(Optional<CacheInvalidationPublisher> publisher) {
            return new PriceAdjustmentCacheFactoryBean(cacheProperties, redissonClient.orElse(null), publisher.orElse(null));
        }
    }
}
