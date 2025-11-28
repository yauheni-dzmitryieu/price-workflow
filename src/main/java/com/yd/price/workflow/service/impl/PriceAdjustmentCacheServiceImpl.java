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
    public PriceAdjustment save(Long productId, PriceAdjustment priceAdjustment) {
        priceAdjustmentService.save(productId, priceAdjustment);

        this.registerSynchronization(new PriceAdjustmentCacheServiceImpl.SaveAfterCommit(productId, priceAdjustment));
        return priceAdjustment;
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        log.debug("Fetching all price adjustments for productId={}", productId);

        Set<PriceAdjustment> cachedPriceAdjustments = priceAdjustmentCache.getAll(productId);
        if (!CollectionUtils.isEmpty(cachedPriceAdjustments)) {
            log.info("Cache HIT for productId={} ({} items)", productId, cachedPriceAdjustments.size());
            return cachedPriceAdjustments;
        }

        log.info("Cache MISS for productId={}. Loading from DB…", productId);
        Set<PriceAdjustment> priceAdjustments = priceAdjustmentService.getAll(productId);

        if (!CollectionUtils.isEmpty(priceAdjustments)) {
            log.debug("Caching {} price adjustments for productId={}", priceAdjustments.size(), productId);
            priceAdjustmentCache.addAll(productId, priceAdjustments);
        } else {
            log.debug("No price adjustments found in DB for productId={}", productId);
        }

        return cachedPriceAdjustments;
    }

    void registerSynchronization(TransactionSynchronization synchronization) {
        TransactionSynchronizationManager.registerSynchronization(synchronization);
    }

    class SaveAfterCommit implements TransactionSynchronization {
        private final Long productId;
        private final PriceAdjustment priceAdjustment;

        public SaveAfterCommit(Long productId, PriceAdjustment priceAdjustment) {
            this.productId = productId;
            this.priceAdjustment = priceAdjustment;
        }

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
    }

}
