package com.yd.price.workflow.cache.l1;

import com.github.benmanes.caffeine.cache.Cache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceAdjustmentCacheL1CaffeineTest {
    private static final Long PRODUCT_ID = 1L;

    @Mock
    private Cache<Long, Set<PriceAdjustment>> cache;

    @InjectMocks
    private PriceAdjustmentCacheL1Caffeine cacheL1Caffeine;


    @NullSource
    @ParameterizedTest
    @CsvSource(value = {"123"})
    void testGetAll(Long productId) {
        //Given
        Set<PriceAdjustment> expectedResult = Set.of();

        when(cache.getIfPresent(productId)).thenReturn(expectedResult);

        //When
        Set<PriceAdjustment> priceAdjustments = cacheL1Caffeine.getAll(productId);

        //Then
        assertThat(priceAdjustments).isEqualTo(expectedResult);
        verify(cache).getIfPresent(productId);
    }

    @Test
    void testPutIfAbsent() {
        //Given
        PriceAdjustment priceAdjustment = new PriceAdjustment();
        Set<PriceAdjustment> expectedResult = new CopyOnWriteArraySet<>();
        expectedResult.add(priceAdjustment);

        when(cache.getIfPresent(PRODUCT_ID)).thenReturn(null);

        //When
        cacheL1Caffeine.put(PRODUCT_ID, priceAdjustment);

        //Then
        verify(cache).getIfPresent(PRODUCT_ID);
        verify(cache).put(PRODUCT_ID, expectedResult);
    }

    @Test
    void testPutIfCacheNotEmpty() {
        Set<PriceAdjustment> storage = new CopyOnWriteArraySet<>();
        PriceAdjustment priceAdjustment = new PriceAdjustment();

        when(cache.getIfPresent(PRODUCT_ID)).thenReturn(storage);

        //When
        cacheL1Caffeine.put(PRODUCT_ID, priceAdjustment);

        //Then
        verify(cache).getIfPresent(PRODUCT_ID);

        assertThat(storage)
                .hasSize(1)
                .contains(priceAdjustment);
    }

    @Test
    void testAddAllIfAbsent() {
        //Given
        PriceAdjustment priceAdjustment = new PriceAdjustment();

        Set<PriceAdjustment> source = Set.of(priceAdjustment);
        Set<PriceAdjustment> expectedResult = new CopyOnWriteArraySet<>(source);
        when(cache.getIfPresent(PRODUCT_ID)).thenReturn(null);

        //When
        cacheL1Caffeine.addAll(PRODUCT_ID, source);

        //Then
        verify(cache).getIfPresent(PRODUCT_ID);
        verify(cache).put(PRODUCT_ID, expectedResult);
    }

    @Test
    void testAddAllIfCacheNotEmpty() {
        //Given
        PriceAdjustment priceAdjustment = new PriceAdjustment();

        Set<PriceAdjustment> source = Set.of(priceAdjustment);
        Set<PriceAdjustment> storage = new CopyOnWriteArraySet<>();
        when(cache.getIfPresent(PRODUCT_ID)).thenReturn(storage);

        //When
        cacheL1Caffeine.addAll(PRODUCT_ID, source);

        //Then
        verify(cache).getIfPresent(PRODUCT_ID);
        assertThat(storage)
                .hasSize(1)
                .contains(priceAdjustment);
    }

    @Test
    void testEvict() {
        //When
        cacheL1Caffeine.evict(PRODUCT_ID);

        //Then
        verify(cache).invalidate(PRODUCT_ID);
    }
}