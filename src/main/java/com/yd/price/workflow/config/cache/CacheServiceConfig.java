package com.yd.price.workflow.config.cache;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import com.yd.price.workflow.service.api.ProductService;
import com.yd.price.workflow.service.impl.PriceAdjustmentCacheServiceImpl;
import com.yd.price.workflow.service.impl.ProductCacheServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnExpression("${app.cache.l1.enabled:false} or ${app.cache.l2.enabled:false}")
public class CacheServiceConfig {

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
