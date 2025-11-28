package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import com.yd.price.workflow.service.api.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

public class ProductCacheServiceImpl implements ProductService {
    public static final Logger log = LoggerFactory.getLogger(PriceAdjustmentCacheServiceImpl.class);

    private final ProductService productService;
    private final ProductCache productCache;

    public ProductCacheServiceImpl(ProductService productService, ProductCache productCache) {
        Assert.notNull(productService, "productService must not be null");
        Assert.notNull(productCache, "productCache must not be null");
        this.productService = productService;
        this.productCache = productCache;
    }

    @Override
    public ProductBasePriceView getProductPrice(Long productId) {
        ProductBasePriceView product = productCache.get(productId);
        if (product != null) {
            return product;
        }

        product = productService.getProductPrice(productId);
        productCache.put(product);
        return product;
    }
}
