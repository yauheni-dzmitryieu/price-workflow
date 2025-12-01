package com.yd.price.workflow.cache.composite;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.health.RedisConnectionHolder;
import com.yd.price.workflow.cache.sync.CacheInvalidationPublisher;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class PriceAdjustmentCompositeCache implements PriceAdjustmentCache {
    private static final Logger log = LoggerFactory.getLogger(PriceAdjustmentCompositeCache.class);

    private final PriceAdjustmentCache l1Caffeine;
    private final PriceAdjustmentCache l2Redis;
    private final CacheInvalidationPublisher publisher; //TODO replace use interfaces + qualifiers
    private final RedisConnectionHolder redisConnectionHolder;

    public PriceAdjustmentCompositeCache(PriceAdjustmentCache l1Caffeine, PriceAdjustmentCache l2Redis,
                                         CacheInvalidationPublisher publisher,
                                         RedisConnectionHolder redisConnectionHolder) {
        this.l1Caffeine = l1Caffeine;
        this.l2Redis = l2Redis;
        this.publisher = publisher;
        this.redisConnectionHolder = redisConnectionHolder;
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        if (!redisConnectionHolder.isAvailable()) {
            log.warn("Trying to get all data by productId:{}, but L2 cache is not available.", productId);
            return null;
        }

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
        if (!redisConnectionHolder.isAvailable()) {
            log.warn("Trying to save data for productId:{}, but L2 cache is not available.", productId);
            return;
        }

        l2Redis.put(productId, value);
        l1Caffeine.put(productId, value);

        publisher.publishRefresh(value);
    }

    @Override
    public int addAll(Long productId, Set<PriceAdjustment> source) {
        if (!redisConnectionHolder.isAvailable()) {
            log.warn("Trying to addAll data for productId:{}, but L2 cache is not available.", productId);
            return 0;
        }

        l2Redis.addAll(productId, source);
        return l1Caffeine.addAll(productId, source);
    }

    @Override
    public void evict(Long productId) {
        l1Caffeine.evict(productId);

        l2Redis.evict(productId);

        publisher.publishEvict(productId);
    }
}
