package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.view.PriceAdjustmentView;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import com.yd.price.workflow.db.repository.PriceAdjustmentMapper;
import com.yd.price.workflow.db.repository.ProductMapper;
import com.yd.price.workflow.exception.ResourceNotFoundException;
import com.yd.price.workflow.model.PriceWorkflowErrorCode;
import com.yd.price.workflow.model.ProductPrice;
import com.yd.price.workflow.service.api.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PriceAdjustmentMapper priceAdjustmentMapper;

    @Override
    public ProductPrice getProductPrice(Long productId) {
        ProductBasePriceView product = productMapper.findBasePriceByProductId(productId);

        if (product == null) {
            throw new ResourceNotFoundException(productId, PriceWorkflowErrorCode.PRODUCT_NOT_FOUND_BY_ID);
        }

        List<PriceAdjustmentView> allByProductId = priceAdjustmentMapper.findAllByProductId(productId);
        System.out.println(allByProductId.size());

        return new ProductPrice(productId, product.basePrice(), null, null);
    }
}
