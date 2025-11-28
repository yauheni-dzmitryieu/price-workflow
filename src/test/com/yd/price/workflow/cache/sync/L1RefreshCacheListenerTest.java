package com.yd.price.workflow.cache.sync;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class L1RefreshCacheListenerTest {

    @Mock
    private PriceAdjustmentCache l1;

    @Mock
    private RedissonClient redissonClient;

    @InjectMocks
    private L1RefreshCacheListener listener;

    @Test
    void testSubscribe() {
        //Given
        RTopic rTopic = mock(RTopic.class);
        when(redissonClient.getTopic(PriceAdjustmentCache.REFRESH_TOPIC)).thenReturn(rTopic);

        //When
        listener.subscribe();

        //Then
        verify(redissonClient).getTopic(PriceAdjustmentCache.REFRESH_TOPIC);
        verify(rTopic).addListener(eq(RefreshMessage.class), any(L1RefreshCacheListener.RefreshListener.class));
        verifyNoMoreInteractions(redissonClient, redissonClient);
    }

    @Nested
    class TestRefreshListener {

        L1RefreshCacheListener.RefreshListener refreshListener;

        @BeforeEach
        void setUp() {
            this.refreshListener = listener.new RefreshListener();
        }

        @Test
        void testOnMessage() {
            //Given
            Long productId = 1L;
            CharSequence charSequence = mock(CharSequence.class);
            PriceAdjustment priceAdjustment = new PriceAdjustment();
            priceAdjustment.setProductId(productId);

            RefreshMessage refreshMessage = new RefreshMessage("test-host", priceAdjustment);

            //When
            this.refreshListener.onMessage(charSequence, refreshMessage);

            //Then
            verify(l1).put(productId, priceAdjustment);
        }
    }
}