package com.yd.price.workflow.cache.health;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

public class RedisHealthCheckScheduler {
    private static final Logger log = LoggerFactory.getLogger(RedisHealthCheckScheduler.class);

    private final RedissonClient redissonClient;
    private final RedisConnectionHolder redisConnectionHolder;

    public RedisHealthCheckScheduler(RedissonClient redissonClient, RedisConnectionHolder redisConnectionHolder) {
        this.redissonClient = redissonClient;
        this.redisConnectionHolder = redisConnectionHolder;
    }

    @Scheduled(fixedRateString = "${app.cache.l2.scheduler-health-check-fixed-rate:1}", timeUnit = TimeUnit.SECONDS)
    public void checkHealth() {
        boolean wasAvailable = redisConnectionHolder.isAvailable();

        try {
            RBucket<String> bucket = redissonClient.getBucket("health_check_key");
            boolean redisIsUp = bucket.isExists();

            if (!wasAvailable) {
                log.info("Redis connection restored");
                redisConnectionHolder.markAvailable();
            }

        } catch (Exception e) {
            if (wasAvailable) {
                log.warn("Redis connection lost: {}", e.getMessage());
                redisConnectionHolder.markUnavailable();
            }
        }
    }
}
