package com.yd.price.workflow.cache.health;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Holds the current availability status of the Redis connection.
 * <p>
 * This class is a lightweight, thread-safe state holder used by Redis monitoring
 * components (such as {@code RedisHealthCheckScheduler} or connection listeners)
 * to indicate whether Redis is considered available or unavailable at the moment.
 * </p>
 */
public class RedisConnectionHolder {
    private final AtomicBoolean available = new AtomicBoolean(true);

    //TODO add check for each L2 operation
    /**
     * Returns whether Redis is currently considered available.
     *
     * @return {@code true} if Redis is available, otherwise {@code false}
     */
    public boolean isAvailable() {
        return available.get();
    }

    /**
     * Marks Redis as available.
     * Should be called when a Redis ping or operation succeeds.
     */
    public void markAvailable() {
        available.set(true);
    }

    /**
     * Marks Redis as unavailable.
     * Should be called when Redis operations fail or a connection loss is detected.
     */
    public void markUnavailable() {
        available.set(false);
    }
}
