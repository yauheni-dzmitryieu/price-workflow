package com.yd.price.workflow.cache.l2;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.Set;

public class PriceAdjustmentCacheL2Redis implements PriceAdjustmentCache {

    private final RedissonClient redisson;
    private final Duration expire;

    public PriceAdjustmentCacheL2Redis(RedissonClient redisson, Duration expire) {
        this.redisson = redisson;
        this.expire = expire;
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        return getCache(productId).readAll();
    }

    @Override
    public void put(Long productId, PriceAdjustment result) {
        RSet<PriceAdjustment> cache = getCache(productId);
        cache.add(result);
        cache.expire(this.expire);
    }

    @Override
    public int addAll(Long productId, Set<PriceAdjustment> source) {
        RSet<PriceAdjustment> cache = getCache(productId);
        int added = cache.addAllCounted(source);
        cache.expire(this.expire);
        return added;
    }

    @Override
    public void evict(Long productId) {
        getCache(productId).clear(); //TODO check
    }

    private RSet<PriceAdjustment> getCache(Long productId) {
        return redisson.getSet("price-adjustment-cache:" + productId); //TODO move to key generator
    }
}
