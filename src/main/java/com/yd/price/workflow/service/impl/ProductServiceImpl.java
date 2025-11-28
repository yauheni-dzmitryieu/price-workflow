package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import com.yd.price.workflow.db.repository.ProductMapper;
import com.yd.price.workflow.exception.ErrorCode;
import com.yd.price.workflow.exception.ResourceNotFoundException;
import com.yd.price.workflow.service.api.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        Assert.notNull(productMapper, "productMapper must not be null");
        this.productMapper = productMapper;
    }

    @Override
    public ProductBasePriceView getProductPrice(Long productId) {
        ProductBasePriceView product = productMapper.findBasePriceByProductId(productId);

        if (product == null) {
            throw new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND, productId);
        }
        return product;
    }
}
