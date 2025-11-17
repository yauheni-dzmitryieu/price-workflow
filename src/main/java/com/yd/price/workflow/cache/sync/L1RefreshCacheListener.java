package com.yd.price.workflow.cache.sync;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.cache.l1.PriceAdjustmentCacheL1Caffeine;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.utils.HostUtils;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class L1RefreshCacheListener {

    private final PriceAdjustmentCacheL1Caffeine l1;
    private final RedissonClient redisson;

    public L1RefreshCacheListener(RedissonClient redisson, PriceAdjustmentCacheL1Caffeine l1) {
        this.l1 = l1;
        this.redisson = redisson;
    }

    //TODO add check feature flags
    @PostConstruct
    public void subscribe() {
        redisson.getTopic(PriceAdjustmentCache.REFRESH_TOPIC)
                .addListener(RefreshMessage.class, new RefreshListener());
    }


    public class RefreshListener implements MessageListener<RefreshMessage> {

        @Override
        public void onMessage(CharSequence channel, RefreshMessage msg) {
            if (HostUtils.getUniqueHostname().equals(msg.hostname())) {
                System.out.println("Already updated");
                return;
            }

            PriceAdjustment priceAdjustment = msg.priceAdjustment();
            l1.put(priceAdjustment.getProductId(), priceAdjustment);
        }
    }
}
