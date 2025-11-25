package com.yd.price.workflow;

import com.yd.price.workflow.config.cache.CacheProperties;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableConfigurationProperties(value = {CacheProperties.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication(exclude = {
        RedisAutoConfiguration.class,
        RedissonAutoConfigurationV2.class
})
public class PriceWorkflowApp {

    public static void main(String[] args) {
        SpringApplication.run(PriceWorkflowApp.class, args);
    }
}
