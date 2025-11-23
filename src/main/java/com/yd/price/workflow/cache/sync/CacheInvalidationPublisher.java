package com.yd.price.workflow.cache.sync;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.utils.HostUtils;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.util.Assert;

import java.util.Optional;

public class CacheInvalidationPublisher {
    private final RTopic refreshTopic;
    private final RTopic evictTopic;

    public CacheInvalidationPublisher(Optional<RedissonClient> redisson) {
        Assert.isTrue(redisson.isPresent(), "redisson must not be null");
        this.refreshTopic = redisson.get().getTopic(PriceAdjustmentCache.REFRESH_TOPIC);
        this.evictTopic = redisson.get().getTopic(PriceAdjustmentCache.EVICT_TOPIC);
    }

    public void publishRefresh(PriceAdjustment priceAdjustment) {
        var refreshMessage = new RefreshMessage(HostUtils.getUniqueHostname(), priceAdjustment);
        refreshTopic.publish(refreshMessage);
    }

    public void publishEvict(Long productId) {
        evictTopic.publish(productId);
    }

}
