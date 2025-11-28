package com.yd.price.workflow.cache.sync;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.utils.HostUtils;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Optional;

public class CacheInvalidationPublisher {
    private static final Logger log = LoggerFactory.getLogger(CacheInvalidationPublisher.class);
    private final RTopic refreshTopic;
    private final RTopic evictTopic;

    public CacheInvalidationPublisher(Optional<RedissonClient> redisson) {
        Assert.isTrue(redisson.isPresent(), "redisson must not be null");
        this.refreshTopic = redisson.get().getTopic(PriceAdjustmentCache.REFRESH_TOPIC);
        this.evictTopic = redisson.get().getTopic(PriceAdjustmentCache.EVICT_TOPIC);

        log.info("CacheInvalidationPublisher initialized: refreshTopic='{}', evictTopic='{}'", PriceAdjustmentCache.REFRESH_TOPIC, PriceAdjustmentCache.EVICT_TOPIC);
    }

    public void publishRefresh(PriceAdjustment priceAdjustment) {
        if (priceAdjustment == null) {
            log.warn("publishRefresh called with null PriceAdjustment — ignored");
            return;
        }

        var refreshMessage = new RefreshMessage(HostUtils.getUniqueHostname(), priceAdjustment);

        log.debug("Publishing REFRESH message. host={}, productId={}, priceAdjustmentId={}",
                refreshMessage.hostname(),
                priceAdjustment.getProductId(),
                priceAdjustment.getId());


        //TODO handle exception cases when Redis unavailable
        refreshTopic.publish(refreshMessage);
    }

    public void publishEvict(Long productId) {
        evictTopic.publish(productId);
    }

}
