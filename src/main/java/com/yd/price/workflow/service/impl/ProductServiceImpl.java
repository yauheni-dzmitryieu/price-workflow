package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import com.yd.price.workflow.db.repository.ProductMapper;
import com.yd.price.workflow.exception.ResourceNotFoundException;
import com.yd.price.workflow.model.BaseProductPrice;
import com.yd.price.workflow.model.PriceWorkflowErrorCode;
import com.yd.price.workflow.model.ProductPrice;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import com.yd.price.workflow.service.api.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Primary
@Service
public class ProductServiceImpl implements ProductService<ProductPrice> {

    @Autowired
    @Qualifier("productCacheServiceImpl")
    private ProductService<BaseProductPrice> productCacheService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PriceAdjustmentService priceAdjustmentService;


    @Override
    public ProductPrice getProductPrice(Long productId) {
        BaseProductPrice productPrice = productCacheService.getProductPrice(productId);
        BigDecimal basePrice;

        if (productPrice == null) {
            ProductBasePriceView product = productMapper.findBasePriceByProductId(productId);

            if (product == null) {
                throw new ResourceNotFoundException(productId, PriceWorkflowErrorCode.PRODUCT_NOT_FOUND_BY_ID);
            }
            productCacheService.save(product);
            basePrice = product.basePrice();
        } else {
            basePrice = productPrice.getBasePrice();
        }

        //TODO think about calculating in separate service
        BigDecimal finalPrice = basePrice;
        Set<PriceAdjustment> priceAdjustments = priceAdjustmentService.getAll(productId);
        for (PriceAdjustment adjustment : priceAdjustments) {
            switch (adjustment.getMode()) {
                case ABSOLUTE -> finalPrice = finalPrice.add(adjustment.getValue());
                case PERCENT ->
                        finalPrice = finalPrice.add(basePrice).multiply(adjustment.getValue()).divide(BigDecimal.valueOf(100));
            }
        }


        return new ProductPrice(productId, finalPrice, priceAdjustments, finalPrice);
    }

    @Override
    public void save(ProductBasePriceView productPrice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
