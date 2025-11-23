package com.yd.price.workflow.cache.l1;

import com.github.benmanes.caffeine.cache.Cache;
import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PriceAdjustmentCacheL1Caffeine implements PriceAdjustmentCache {

    private final Cache<Long, Set<PriceAdjustment>> cache;

    public PriceAdjustmentCacheL1Caffeine(Cache<Long, Set<PriceAdjustment>> cache) {
        Assert.notNull(cache, "Cache must not be null");
        this.cache = cache;
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        return cache.getIfPresent(productId);
    }

    @Override
    public void put(Long productId, PriceAdjustment result) {
        Set<PriceAdjustment> collection = cache.getIfPresent(productId);
        if (collection == null) {
            Set<PriceAdjustment> set = new CopyOnWriteArraySet<>();
            set.add(result);
            cache.put(productId, set);
        } else {
            collection.add(result);
        }
    }

    @Override
    public int addAll(Long productId, Set<PriceAdjustment> source) {
        Set<PriceAdjustment> currentCache = this.cache.getIfPresent(productId);
        if (currentCache == null) {
            currentCache = new CopyOnWriteArraySet<>(source);
            this.cache.put(productId, currentCache);
        } else {
            currentCache.addAll(source);
        }
        return source.size();
    }

    @Override
    public void evict(Long productId) {
        cache.invalidate(productId);
    }
}
