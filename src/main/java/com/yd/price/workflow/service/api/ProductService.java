package com.yd.price.workflow.service.api;

import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import com.yd.price.workflow.model.BaseProductPrice;

public interface ProductService<T extends BaseProductPrice> {

    T getProductPrice(Long productId);

    //TODO rework models
    void save(ProductBasePriceView productPrice);
}
