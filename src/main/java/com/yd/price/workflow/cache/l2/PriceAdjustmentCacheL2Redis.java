package com.yd.price.workflow.cache.l2;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Set;

public class PriceAdjustmentCacheL2Redis implements PriceAdjustmentCache {

    private static final Logger log = LoggerFactory.getLogger(PriceAdjustmentCacheL2Redis.class);

    private final RedissonClient redisson;
    private final Duration expire;

    public PriceAdjustmentCacheL2Redis(RedissonClient redisson, Duration expire) {
        this.redisson = redisson;
        this.expire = expire;
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        if (productId == null) {
            log.warn("Attempted to getAll with null productId from L2 cache");
            return Set.of();
        }

        RSet<PriceAdjustment> cache = getCache(productId);
        Set<PriceAdjustment> result = cache.readAll();

        if (result.isEmpty()) {
            log.debug("L2 cache MISS for productId={}", productId);
        } else {
            log.debug("L2 cache HIT for productId={} size={}", productId, result.size());
        }

        return result;
    }

    @Override
    public void put(Long productId, PriceAdjustment result) {
        if (productId == null || result == null) {
            log.warn("Attempted to put PriceAdjustment with null productId: {} or null value: {}", productId, result);
            return;
        }

        RSet<PriceAdjustment> cache = getCache(productId);
        cache.add(result);
        cache.expire(this.expire);

        log.debug("L2 cache PUT productId={} value={}", productId, result);
    }

    @Override
    public int addAll(Long productId, Set<PriceAdjustment> source) {
        if (productId == null || CollectionUtils.isEmpty(source)) {
            log.warn("Attempted to add set of PriceAdjustment to L2 cache with null productId or empty source set");
            return 0;
        }

        RSet<PriceAdjustment> cache = getCache(productId);
        int added = cache.addAllCounted(source);
        cache.expire(this.expire);

        log.debug("L2 cache ADD_ALL productId={} added={} totalAfter={}", productId, added, cache.size());
        return added;
    }

    @Override
    public void evict(Long productId) {
        if (productId == null) {
            log.warn("Attempted to evict null productId");
            return;
        }

        RSet<PriceAdjustment> cache = getCache(productId);
        int before = cache.size();
        cache.clear();

        log.debug("L2 cache EVICT productId={} removed={}", productId, before);
    }

    private RSet<PriceAdjustment> getCache(Long productId) {
        String key = "price-adjustment-cache:" + productId;
        return redisson.getSet(key);
    }
}
