package com.yd.price.workflow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.cache")
public class CacheProperties {
    private final Cache l1 = new Cache();
    private final Cache l2 = new Cache();

    public Cache getL1() {
        return l1;
    }

    public Cache getL2() {
        return l2;
    }

    public static class Cache {
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
