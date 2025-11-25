package com.yd.price.workflow.config.cache;

import com.yd.price.workflow.health.redis.RedissonHealthIndicator;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.io.IOException;

@Configuration
@ConditionalOnBooleanProperty(prefix = "app.cache.l2", value = "enabled", havingValue = true)
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        Config config = Config.fromYAML(getClass().getClassLoader().getResource("redisson.yml"));

        return Redisson.create(config);
    }

    @Bean
    public HealthIndicator redissonHealthIndicator(RedissonClient redissonClient) {
        return new RedissonHealthIndicator(redissonClient);
    }
}
