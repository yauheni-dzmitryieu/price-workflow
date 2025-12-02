package com.yd.price.workflow.cache.api;

import com.yd.price.workflow.db.entity.view.ProductBasePriceView;

//TODO use independent dto for work with cache
public interface ProductCache {

    ProductBasePriceView get(Long productId);

    void put(ProductBasePriceView productBasePriceView);

}
