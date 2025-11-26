package com.yd.price.workflow.config.cache;

import com.yd.price.workflow.health.redis.RedissonHealthIndicator;
import org.redisson.api.RedissonClient;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBooleanProperty(prefix = "app.cache.l2", value = "enabled", havingValue = true)
public class RedisConfig {

    @Bean
    public HealthIndicator redissonHealthIndicator(RedissonClient redissonClient) {
        return new RedissonHealthIndicator(redissonClient);
    }
}
