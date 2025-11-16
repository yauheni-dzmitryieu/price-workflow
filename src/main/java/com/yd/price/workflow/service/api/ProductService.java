package com.yd.price.workflow.service.api;

import com.yd.price.workflow.model.ProductPrice;

public interface ProductService {

    ProductPrice getProductPrice(Long productId);
}
