package com.yd.price.workflow.service.api;

import com.yd.price.workflow.db.entity.view.ProductBasePriceView;

public interface ProductService {

    ProductBasePriceView getProductPrice(Long productId);
}
