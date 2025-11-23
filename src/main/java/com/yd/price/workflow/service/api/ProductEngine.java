package com.yd.price.workflow.service.api;

import com.yd.price.workflow.model.ProductPrice;

public interface ProductEngine {

    ProductPrice getProductPrice(Long productId);
}
