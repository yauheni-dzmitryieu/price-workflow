package com.yd.price.workflow.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

@ConfigurationProperties(prefix = "app.cache")
public class CacheProperties {
    private final L1 l1 = new L1();
    private final L2 l2 = new L2();

    public L1 getL1() {
        return l1;
    }

    public L2 getL2() {
        return l2;
    }

    public static class L1 {
        private boolean enabled;
        private Map<CacheName, Parameters> caches;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Map<CacheName, Parameters> getCaches() {
            return caches;
        }

        public void setCaches(Map<CacheName, Parameters> caches) {
            this.caches = caches;
        }

        public static class Parameters {
            private long maximumSize;
            private Duration expireAfterWrite;

            public long getMaximumSize() {
                return maximumSize;
            }

            public void setMaximumSize(long maximumSize) {
                this.maximumSize = maximumSize;
            }

            public Duration getExpireAfterWrite() {
                return expireAfterWrite;
            }

            public void setExpireAfterWrite(Duration expireAfterWrite) {
                this.expireAfterWrite = expireAfterWrite;
            }
        }
    }

    public static class L2 {
        private boolean enabled;
        private Map<CacheName, Parameters> caches;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Map<CacheName, Parameters> getCaches() {
            return caches;
        }

        public void setCaches(Map<CacheName, Parameters> caches) {
            this.caches = caches;
        }

        public static class Parameters {
            private Duration expire;

            public Duration getExpire() {
                return expire;
            }

            public void setExpire(Duration expire) {
                this.expire = expire;
            }
        }
    }

    public enum CacheName {
        PRODUCT_CACHE,
        PRICE_ADJUSTMENT_CACHE
    }
}
