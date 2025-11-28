package com.yd.price.workflow.cache.sync;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.utils.HostUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheInvalidationPublisherTest {

    private RedissonClient redissonClient;

    private RTopic refreshTopic;
    private RTopic evictTopic;

    private CacheInvalidationPublisher publisher;

    @BeforeEach
    void setUp() {
        this.refreshTopic = mock(RTopic.class, "refreshTopic");
        this.evictTopic = mock(RTopic.class, "evictTopic");
        this.redissonClient = mock(RedissonClient.class);

        when(redissonClient.getTopic(PriceAdjustmentCache.REFRESH_TOPIC)).thenReturn(refreshTopic);
        when(redissonClient.getTopic(PriceAdjustmentCache.EVICT_TOPIC)).thenReturn(evictTopic);

        this.publisher = new CacheInvalidationPublisher(Optional.of(redissonClient));
    }

    @Test
    void testPublishRefreshIfNull() {
        //When
        publisher.publishRefresh(null);

        //Then
        verifyNoInteractions(refreshTopic, evictTopic);
    }

    @Test
    void testPublishRefresh() {
        //Given
        PriceAdjustment priceAdjustment = new PriceAdjustment();
        RefreshMessage refreshMessage = new RefreshMessage(HostUtils.getUniqueHostname(), priceAdjustment);

        //When
        publisher.publishRefresh(priceAdjustment);

        //Then
        verify(redissonClient).getTopic(PriceAdjustmentCache.REFRESH_TOPIC);
        verify(redissonClient).getTopic(PriceAdjustmentCache.EVICT_TOPIC);
        verify(refreshTopic).publish(refreshMessage);
        verifyNoMoreInteractions(redissonClient, refreshTopic, evictTopic);
    }

    @Test
    void testPublishEvict() {
        //Given
        Long productId = 1L;

        //When
        publisher.publishEvict(productId);

        //Then
        verify(evictTopic).publish(productId);
    }
}