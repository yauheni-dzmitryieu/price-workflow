package com.yd.price.workflow.cache.l2;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceAdjustmentCacheL2RedisTest {

    private static final Long PRODUCT_ID = 1L;
    private static final String KEY = "price-adjustment-cache:" + PRODUCT_ID;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private Duration expire;

    @InjectMocks
    private PriceAdjustmentCacheL2Redis priceAdjustmentCacheL2Redis;

    @Test
    void testGetAll() {
        //Given
        RSet cache = mock(RSet.class);
        Set<PriceAdjustment> adjustments = Set.of(mock(PriceAdjustment.class));

        when(redissonClient.getSet(KEY)).thenReturn(cache);
        when(cache.readAll()).thenReturn(adjustments);

        //When
        Set<PriceAdjustment> priceAdjustments = priceAdjustmentCacheL2Redis.getAll(PRODUCT_ID);

        //Then
        assertThat(priceAdjustments)
                .containsExactlyElementsOf(adjustments);

        verify(redissonClient).getSet(KEY);
        verify(cache).readAll();
    }

    @Test
    void testPut() {
        //Given
        RSet cache = mock(RSet.class);
        PriceAdjustment priceAdjustment = mock(PriceAdjustment.class);

        when(redissonClient.getSet(KEY)).thenReturn(cache);

        //When
        priceAdjustmentCacheL2Redis.put(PRODUCT_ID, priceAdjustment);

        //Then
        verify(redissonClient).getSet(KEY);
        verify(cache).add(priceAdjustment);
        verify(cache).expire(expire);
    }

    @Test
    void testAddAll() {
        //Given
        RSet cache = mock(RSet.class);
        Set<PriceAdjustment> priceAdjustments = Set.of(mock(PriceAdjustment.class));

        when(redissonClient.getSet(KEY)).thenReturn(cache);
        when(cache.addAllCounted(priceAdjustments)).thenReturn(1);

        //When
        int added = priceAdjustmentCacheL2Redis.addAll(PRODUCT_ID, priceAdjustments);

        //Then
        assertThat(added).isEqualTo(1);

        verify(redissonClient).getSet(KEY);
        verify(cache).addAllCounted(priceAdjustments);
        verify(cache).expire(expire);
    }

    @Test
    void testEvict() {
        //Given
        RSet cache = mock(RSet.class);
        when(redissonClient.getSet(KEY)).thenReturn(cache);

        //When
        priceAdjustmentCacheL2Redis.evict(PRODUCT_ID);


        //Then
        verify(redissonClient).getSet(KEY);
        verify(cache).clear();
    }
}