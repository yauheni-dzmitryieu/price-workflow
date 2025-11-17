package com.yd.price.workflow.cache.l2;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

@Component
public class PriceAdjustmentCacheL2Redis implements PriceAdjustmentCache {

    private final RedissonClient redisson;
    private boolean enabled = true; //TODO replace use property

    public PriceAdjustmentCacheL2Redis(RedissonClient redisson) {
        this.redisson = redisson;
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        return enabled ? getCache(productId).readAll() : null;
    }

    @Override
    public void put(Long productId, PriceAdjustment result) {
        if (enabled) {
            RSet<PriceAdjustment> cache = getCache(productId);
            cache.add(result);
            cache.expire(Duration.ofMinutes(30)); //TODO configure via property
        }
    }

    @Override
    public int addAll(Long productId, Set<PriceAdjustment> source) {
        if (!enabled) {
            return 0;
        }
        RSet<PriceAdjustment> cache = getCache(productId);
        int added = cache.addAllCounted(source);
        cache.expire(Duration.ofMinutes(30));//TODO
        return added;
    }

    @Override
    public void evict(Long productId) {
        if (enabled) {
            getCache(productId).clear(); //TODO check
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    private RSet<PriceAdjustment> getCache(Long productId) {
        return redisson.getSet("price-adjustment-cache:" + productId); //TODO move to key generator
    }
}
