package com.yd.price.workflow.config.cache;

import com.yd.price.workflow.cache.health.RedisConnectionHolder;
import com.yd.price.workflow.cache.health.RedisHealthCheckScheduler;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnBooleanProperty(prefix = "app.cache.l2", value = "enabled", havingValue = true)
public class RedisConfig extends RedissonAutoConfiguration {

    @Bean
    public RedisConnectionHolder redisConnectionHolder() {
        return new RedisConnectionHolder();
    }

    @Bean
    public RedisHealthCheckScheduler redisHealthCheckScheduler(RedissonClient redisson, RedisConnectionHolder holder) {
        return new RedisHealthCheckScheduler(redisson, holder);
    }

}
