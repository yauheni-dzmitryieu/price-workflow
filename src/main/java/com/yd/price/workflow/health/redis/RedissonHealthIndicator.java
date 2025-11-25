package com.yd.price.workflow.health.redis;

import org.redisson.api.ClusterNode;
import org.redisson.api.Node;
import org.redisson.api.RedissonClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class RedissonHealthIndicator implements HealthIndicator {
    private final RedissonClient redissonClient;

    public RedissonHealthIndicator(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public Health health() {
        try {
            String pingResult = redissonClient.getBucket("health-check").getCodec().getValueEncoder().toString();

            for (Node node : redissonClient.getNodesGroup().getNodes()) {
                if (node instanceof ClusterNode clusterNode) {
                    if (!clusterNode.ping()) {
                        return Health.down()
                                .withDetail("node", clusterNode.getAddr())
                                .withDetail("status", "PING FAILED")
                                .build();
                    }
                } else {
                    if (!node.ping()) {
                        return Health.down()
                                .withDetail("node", node.getAddr())
                                .withDetail("status", "PING FAILED")
                                .build();
                    }
                }
            }

            return Health.up().withDetail("status", "OK").build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
