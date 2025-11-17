package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Set;


//TODO think how better inject + L1 + L2 feature flags
@Service
public class PriceAdjustmentCacheServiceImpl implements PriceAdjustmentService {
    public static final Logger log = LoggerFactory.getLogger(PriceAdjustmentCacheServiceImpl.class);

    private final PriceAdjustmentCache priceAdjustmentCache;

    public PriceAdjustmentCacheServiceImpl(@Qualifier("priceAdjustmentCacheComposite")
                                           PriceAdjustmentCache priceAdjustmentCache) {
        this.priceAdjustmentCache = priceAdjustmentCache;
    }

    @Override
    public PriceAdjustment addToProduct(Long productId, PriceAdjustment priceAdjustment) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

                @Override
                public void afterCommit() {
                    priceAdjustmentCache.put(productId, priceAdjustment);
                    log.info("Cache: Price Adjustment added to product: {} in cache with id: {}", productId, priceAdjustment.getId());
                }

                @Override
                public void afterCompletion(int status) {
                    if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                        log.warn("Cache: Transaction was rolled back. Adding data to cache for productId: {} was skipped.", productId);
                    }
                }
            });
        }

        return priceAdjustment;
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        return priceAdjustmentCache.getAll(productId);
    }

    @Override
    public void addAll(Long productId, Set<PriceAdjustment> priceAdjustments) {
        priceAdjustmentCache.addAll(productId, priceAdjustments);
    }
}
