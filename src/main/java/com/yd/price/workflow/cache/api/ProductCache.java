package com.yd.price.workflow.cache.api;

import com.yd.price.workflow.db.entity.view.ProductBasePriceView;

public interface ProductCache {

    ProductBasePriceView get(Long productId);

    void put(ProductBasePriceView productBasePriceView);

    boolean isEnabled();
}
