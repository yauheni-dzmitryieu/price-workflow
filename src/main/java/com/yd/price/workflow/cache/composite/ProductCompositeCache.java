package com.yd.price.workflow.cache.composite;

import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.cache.l1.ProductCacheL1Caffeine;
import com.yd.price.workflow.cache.l2.ProductCacheL2Redis;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;

public class ProductCompositeCache implements ProductCache {

    private final ProductCacheL1Caffeine l1Caffeine;
    private final ProductCacheL2Redis l2Redis;
    //TODO add publisher in case implementing adding product


    public ProductCompositeCache(ProductCacheL1Caffeine l1Caffeine, ProductCacheL2Redis l2Redis) {
        this.l1Caffeine = l1Caffeine;
        this.l2Redis = l2Redis;
    }

    @Override
    public ProductBasePriceView get(Long productId) {
        if (l1Caffeine.isEnabled()) {
            ProductBasePriceView view = l1Caffeine.get(productId);
            if (view != null) {
                return view;
            }
        }

        if (l2Redis.isEnabled()) {
            ProductBasePriceView view = l2Redis.get(productId);
            if (view != null) {
                if (l2Redis.isEnabled()) {
                    l1Caffeine.put(view);
                }
                return view;
            }
        }

        return null;
    }

    @Override
    public void put(ProductBasePriceView productBasePriceView) {
        if (l2Redis.isEnabled()) {
            l2Redis.put(productBasePriceView);
        }

        if (l1Caffeine.isEnabled()) {
            l1Caffeine.put(productBasePriceView);
        }

        //TODO publish refresh
    }

    @Override
    public boolean isEnabled() {
        return l1Caffeine.isEnabled() || l2Redis.isEnabled();
    }
}
