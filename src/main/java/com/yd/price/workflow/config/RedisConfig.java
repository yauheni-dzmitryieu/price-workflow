package com.yd.price.workflow.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ConstantDelay;
import org.redisson.config.SentinelServersConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class RedisConfig {

    //    //TODO use properties
    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
        sentinelServersConfig
                .setMasterName("mymaster")
                .setDatabase(0)
                .addSentinelAddress("redis://127.0.0.1:26379",
                        "redis://127.0.0.1:26380",
                        "redis://127.0.0.1:26381")
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setSentinelsDiscovery(true)
                .setCheckSentinelsList(false)
                .setRetryDelay(new ConstantDelay(Duration.ofSeconds(10)));

        return Redisson.create(config);
    }

//    @Bean
//    public CaffeineCacheManager caffeineCacheManager() {
//        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
//        Caffeine<Object, Object> caffeineConfig = Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES) // TTL
//                .maximumSize(1000);
//        caffeineCacheManager.setCaffeine(caffeineConfig);
//        return caffeineCacheManager;
//    }

//    @Bean
//    public RedissonSpringCacheManager redissonCacheManager(RedissonClient redissonClient) {
//        return new RedissonSpringCacheManager(redissonClient);
//    }


//    @Bean
//    @Primary
//    public CacheManager cacheManager(CaffeineCacheManager caffeineCacheManager, RedissonSpringCacheManager redissonCacheManager) {
//        CompositeCacheManager compositeCacheManager = new CompositeCacheManager(caffeineCacheManager, redissonCacheManager);
//        compositeCacheManager.setFallbackToNoOpCache(false);
//        return compositeCacheManager;
//    }
}
