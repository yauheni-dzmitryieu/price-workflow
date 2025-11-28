package com.yd.price.workflow.cache.composite;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.sync.CacheInvalidationPublisher;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class PriceAdjustmentCompositeCache implements PriceAdjustmentCache {
    private final PriceAdjustmentCache l1Caffeine;
    private final PriceAdjustmentCache l2Redis;
    private final CacheInvalidationPublisher publisher; //TODO replace use interfaces + qualifiers

    public PriceAdjustmentCompositeCache(PriceAdjustmentCache l1Caffeine, PriceAdjustmentCache l2Redis, CacheInvalidationPublisher publisher) {
        this.l1Caffeine = l1Caffeine;
        this.l2Redis = l2Redis;
        this.publisher = publisher;
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        Set<PriceAdjustment> l1PriceAdjustments = l1Caffeine.getAll(productId);
        if (!CollectionUtils.isEmpty(l1PriceAdjustments)) {
            return l1PriceAdjustments;
        }

        Set<PriceAdjustment> l2PriceAdjustments = l2Redis.getAll(productId);
        if (!CollectionUtils.isEmpty(l2PriceAdjustments)) {
            l1Caffeine.addAll(productId, l2PriceAdjustments);

            return l2PriceAdjustments;
        }
        return null;
    }

    @Override
    public void put(Long productId, PriceAdjustment value) {
        l2Redis.put(productId, value);

        l1Caffeine.put(productId, value);

        publisher.publishRefresh(value);
    }

    //TODO think is it really need update L1 immediately
    @Override
    public int addAll(Long productId, Set<PriceAdjustment> source) {
        int added = 0;
        added = l2Redis.addAll(productId, source);

        added = l1Caffeine.addAll(productId, source);
        //TODO Think about: publish refresh L1 for each nodes
        return added;
    }

    @Override
    public void evict(Long productId) {
        l1Caffeine.evict(productId);

        l2Redis.evict(productId);

        publisher.publishEvict(productId);
    }

    @Override
    public PriceAdjustmentCache getL1() {
        return this.l1Caffeine;
    }
}
