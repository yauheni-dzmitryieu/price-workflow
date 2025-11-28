package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import com.yd.price.workflow.model.ProductPrice;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import com.yd.price.workflow.service.api.ProductEngine;
import com.yd.price.workflow.service.api.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class ProductEngineImpl implements ProductEngine {
    private static final Logger log = LoggerFactory.getLogger(ProductEngineImpl.class);

    private final ProductService productService;
    private final PriceAdjustmentService priceAdjustmentService;

    public ProductEngineImpl(ProductService productService, PriceAdjustmentService priceAdjustmentService) {
        this.productService = productService;
        this.priceAdjustmentService = priceAdjustmentService;
    }

    @Override
    public ProductPrice getProductPrice(Long productId) {
        log.info("Calculating final price for product ID: {}", productId);
        ProductBasePriceView productPrice = productService.getProductPrice(productId);

        BigDecimal basePrice = productPrice.basePrice();
        BigDecimal finalPrice = basePrice;

        Set<PriceAdjustment> priceAdjustments = priceAdjustmentService.getAll(productId);

        if (CollectionUtils.isEmpty(priceAdjustments)) {
            log.info("No price adjustments found for product {}", productId);
        } else {
            for (PriceAdjustment adjustment : priceAdjustments) {
                switch (adjustment.getMode()) {
                    case ABSOLUTE -> finalPrice = finalPrice.add(adjustment.getValue());
                    case PERCENT -> {
                        BigDecimal multiply = basePrice.multiply(adjustment.getValue()).divide(new BigDecimal(100));
                        finalPrice = finalPrice.add(multiply);
                    }
                }
            }
        }

        log.info("Final calculated price for product {} is: {}", productId, finalPrice);
        return new ProductPrice(productId, finalPrice, priceAdjustments, finalPrice);
    }
}
