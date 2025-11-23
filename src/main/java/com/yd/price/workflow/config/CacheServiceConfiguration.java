package com.yd.price.workflow.config;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.cache.sync.CacheInvalidationPublisher;
import com.yd.price.workflow.cache.sync.L1RefreshCacheListener;
import com.yd.price.workflow.config.factory.PriceAdjustmentCacheFactoryBean;
import com.yd.price.workflow.config.factory.ProductCacheFactoryBean;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import com.yd.price.workflow.service.api.ProductService;
import com.yd.price.workflow.service.impl.PriceAdjustmentCacheServiceImpl;
import com.yd.price.workflow.service.impl.ProductCacheServiceImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnExpression("${app.cache.l1.enabled:false} or ${app.cache.l2.enabled:false}")
public class CacheServiceConfiguration {

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

    @Bean
    @Primary
    public PriceAdjustmentService priceAdjustmentService(
            @Qualifier("priceAdjustmentServiceImpl")
            PriceAdjustmentService priceAdjustmentService,
            PriceAdjustmentCache priceAdjustmentCache) {
        return new PriceAdjustmentCacheServiceImpl(priceAdjustmentCache, priceAdjustmentService);
    }

    @Bean
    @Primary
    public ProductService productService(@Qualifier("productServiceImpl")
                                         ProductService productService,
                                         ProductCache productCache) {
        return new ProductCacheServiceImpl(productService, productCache);
    }
}
