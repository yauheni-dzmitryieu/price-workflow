package com.yd.price.workflow.cache.sync;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CacheInvalidationListener {
    private final RedissonClient client;
    //private final CacheManager cacheManager;
    private final List<PriceAdjustmentCache> caches;

    public CacheInvalidationListener(RedissonClient client, List<PriceAdjustmentCache> caches) {
        this.client = client;
        this.caches = caches;
    }

    //TODO
    @PostConstruct
    public void subscribe() {
        RTopic topic = client.getTopic(PriceAdjustmentCache.EVICT_TOPIC);

        topic.addListener(Long.class, (channel, productId) -> {
            for (PriceAdjustmentCache cache : caches) {
                cache.evict(productId);
            }
        });
    }
}
