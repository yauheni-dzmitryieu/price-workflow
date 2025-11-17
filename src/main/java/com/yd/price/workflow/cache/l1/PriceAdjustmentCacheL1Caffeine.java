package com.yd.price.workflow.cache.l1;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

@Component
public class PriceAdjustmentCacheL1Caffeine implements PriceAdjustmentCache {

    private final Cache<Long, Set<PriceAdjustment>> cache;
    private final boolean enabled = true; // TODO move to property

    public PriceAdjustmentCacheL1Caffeine() {
        this.cache = Caffeine.newBuilder() // Move to bean
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        return enabled ? cache.getIfPresent(productId) : null;
    }

    @Override
    public void put(Long productId, PriceAdjustment result) {
        if (enabled) {
            Set<PriceAdjustment> collection = cache.getIfPresent(productId);
            if (collection == null) {
                //Set<PriceAdjustment> set = new ConcurrentSkipListSet<>();
                Set<PriceAdjustment> set = new CopyOnWriteArraySet<>();
                set.add(result);
                cache.put(productId, set);
            } else {
                collection.add(result);
            }
        }
    }

    @Override
    public int addAll(Long productId, Set<PriceAdjustment> source) {
        if (!enabled) {
            return 0;
        }

        Set<PriceAdjustment> currentCache = this.cache.getIfPresent(productId);
        if (currentCache == null) {
            currentCache = new CopyOnWriteArraySet<>(source);
            this.cache.put(productId, currentCache);
        } else {
            currentCache.addAll(source);
        }
        return 0;
    }

    @Override
    public void evict(Long productId) {
        if (enabled) {
            cache.invalidate(productId);
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
