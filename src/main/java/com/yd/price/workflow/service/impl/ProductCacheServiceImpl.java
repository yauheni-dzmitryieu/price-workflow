package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import com.yd.price.workflow.model.BaseProductPrice;
import com.yd.price.workflow.service.api.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class ProductCacheServiceImpl implements ProductService<BaseProductPrice> {
    public static final Logger log = LoggerFactory.getLogger(PriceAdjustmentCacheServiceImpl.class);
    private final ProductCache productCache;

    public ProductCacheServiceImpl(@Qualifier("productCacheComposite") ProductCache productCache) {
        this.productCache = productCache;
    }

    @Override
    public BaseProductPrice getProductPrice(Long productId) {
        ProductBasePriceView view = productCache.get(productId);
        if (view == null) {
            return null;
        }

        return new BaseProductPrice(productId, view.basePrice());
    }

    @Override
    public void save(ProductBasePriceView productPrice) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

                @Override
                public void afterCommit() {
                    productCache.put(productPrice);
                    log.info("Cache: Product saved to cache with id: {}", productPrice.id());
                }

                @Override
                public void afterCompletion(int status) {
                    if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                        log.warn("Cache: Transaction was rolled back. Adding data to cache for productId: {} was skipped.", productPrice.id());
                    }
                }
            });
        } else {
            productCache.put(productPrice);
        }
    }
}
