package com.yd.price.workflow.cache.composite;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.l1.PriceAdjustmentCacheL1Caffeine;
import com.yd.price.workflow.cache.l2.PriceAdjustmentCacheL2Redis;
import com.yd.price.workflow.cache.sync.CacheInvalidationPublisher;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class PriceAdjustmentCompositeCache implements PriceAdjustmentCache {
    private final PriceAdjustmentCacheL1Caffeine l1Caffeine;
    private final PriceAdjustmentCacheL2Redis l2Redis;
    private final CacheInvalidationPublisher publisher; //TODO replace use interfaces + qualifiers

    public PriceAdjustmentCompositeCache(PriceAdjustmentCacheL1Caffeine l1Caffeine, PriceAdjustmentCacheL2Redis l2Redis, CacheInvalidationPublisher publisher) {
        this.l1Caffeine = l1Caffeine;
        this.l2Redis = l2Redis;
        this.publisher = publisher;
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        if (l1Caffeine.isEnabled()) {
            Set<PriceAdjustment> l1PriceAdjustments = l1Caffeine.getAll(productId);
            if (!CollectionUtils.isEmpty(l1PriceAdjustments)) {
                return l1PriceAdjustments;
            }
        }

        if (l2Redis.isEnabled()) {
            Set<PriceAdjustment> l2PriceAdjustments = l2Redis.getAll(productId);
            if (!CollectionUtils.isEmpty(l2PriceAdjustments)) {
                if (l1Caffeine.isEnabled()) {
                    l1Caffeine.addAll(productId, l2PriceAdjustments);
                }

                return l2PriceAdjustments;
            }
        }
        return null;
    }

    @Override
    public void put(Long productId, PriceAdjustment value) {
        if (l2Redis.isEnabled()) {
            l2Redis.put(productId, value);
        }

        if (l1Caffeine.isEnabled()) {
            l1Caffeine.put(productId, value);
        }

        publisher.publishRefresh(value);
    }

    //TODO think is it really need update L1 immediately
    @Override
    public int addAll(Long productId, Set<PriceAdjustment> source) {
        int added = 0;
        if (l2Redis.isEnabled()) {
            added = l2Redis.addAll(productId, source);
        }

        if (l1Caffeine.isEnabled()) {
            added = l1Caffeine.addAll(productId, source);
        }
        //TODO Think about: publish refresh L1 for each nodes
        return added;
    }

    @Override
    public void evict(Long productId) {
        if (l1Caffeine.isEnabled()) {
            l1Caffeine.evict(productId);
        }

        if (l2Redis.isEnabled()) {
            l2Redis.evict(productId);
        }

        publisher.publishEvict(productId);
    }

    @Override
    public boolean isEnabled() {
        return l1Caffeine.isEnabled() || l2Redis.isEnabled();
    }
}
