package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Set;


public class PriceAdjustmentCacheServiceImpl implements PriceAdjustmentService {
    public static final Logger log = LoggerFactory.getLogger(PriceAdjustmentCacheServiceImpl.class);

    private final PriceAdjustmentCache priceAdjustmentCache;
    private final PriceAdjustmentService priceAdjustmentService;

    public PriceAdjustmentCacheServiceImpl(PriceAdjustmentCache priceAdjustmentCache, PriceAdjustmentService priceAdjustmentService) {
        Assert.notNull(priceAdjustmentCache, "priceAdjustmentCache must not be null");
        Assert.notNull(priceAdjustmentService, "priceAdjustmentService must not be null");
        this.priceAdjustmentCache = priceAdjustmentCache;
        this.priceAdjustmentService = priceAdjustmentService;
    }

    @Override
    @Transactional
    public PriceAdjustment addToProduct(Long productId, PriceAdjustment priceAdjustment) {
        priceAdjustmentService.addToProduct(productId, priceAdjustment);

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
        Set<PriceAdjustment> cachedPriceAdjustments = priceAdjustmentCache.getAll(productId);
        if (!CollectionUtils.isEmpty(cachedPriceAdjustments)) {
            return cachedPriceAdjustments;
        }

        Set<PriceAdjustment> priceAdjustments = priceAdjustmentService.getAll(productId);
        this.saveAll(productId, priceAdjustments);

        return cachedPriceAdjustments;
    }

    @Override
    public void saveAll(Long productId, Set<PriceAdjustment> priceAdjustments) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

                @Override
                public void afterCommit() {
                    priceAdjustmentCache.addAll(productId, priceAdjustments);
                    log.info("Cache: Price Adjustment added to product: {} in cache", productId);
                }

                @Override
                public void afterCompletion(int status) {
                    if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                        log.warn("Cache: Transaction was rolled back. Adding data to cache for productId: {} was skipped.", productId);
                    }
                }
            });
        } else {
            priceAdjustmentCache.addAll(productId, priceAdjustments);
        }
    }
}
