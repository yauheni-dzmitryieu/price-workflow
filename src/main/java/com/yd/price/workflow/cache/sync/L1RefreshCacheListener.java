package com.yd.price.workflow.cache.sync;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.utils.HostUtils;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class L1RefreshCacheListener {
    private static final Logger log = LoggerFactory.getLogger(L1RefreshCacheListener.class);

    private final PriceAdjustmentCache l1;
    private final RedissonClient redisson;

    public L1RefreshCacheListener(RedissonClient redisson, PriceAdjustmentCache l1) {
        Assert.notNull(redisson, "redisson must not be null");
        Assert.notNull(l1, "l1 must not be null");

        this.l1 = l1.getL1();
        this.redisson = redisson;
    }

    @PostConstruct
    public void subscribe() {
        redisson.getTopic(PriceAdjustmentCache.REFRESH_TOPIC)
                .addListener(RefreshMessage.class, new RefreshListener());
    }


    public class RefreshListener implements MessageListener<RefreshMessage> {

        @Override
        public void onMessage(CharSequence channel, RefreshMessage msg) {
            if (HostUtils.getUniqueHostname().equals(msg.hostname())) {
                log.info("Accepted message skipped as already processed on another layer.");
                return;
            }

            PriceAdjustment priceAdjustment = msg.priceAdjustment();
            l1.put(priceAdjustment.getProductId(), priceAdjustment);
        }
    }
}
