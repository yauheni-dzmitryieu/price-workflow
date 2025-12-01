package com.yd.price.workflow.cache.composite;

import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.cache.health.RedisConnectionHolder;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCompositeCacheTest {
    private static final Long PRODUCT_ID = 42L;

    @Mock(name = "l1Cache")
    private ProductCache l1Cache;

    @Mock(name = "l2Cache")
    private ProductCache l2Cache;

    @Mock
    private RedisConnectionHolder redisConnectionHolder;

    private ProductCompositeCache cache;

    @BeforeEach
    void setUp() {
        this.cache = new ProductCompositeCache(l1Cache, l2Cache, redisConnectionHolder);
    }

    @Test
    void testGetReturnsValueFromL1Cache() {
        // Given
        var sample = new ProductBasePriceView(PRODUCT_ID, new BigDecimal(100));

        when(l1Cache.get(PRODUCT_ID)).thenReturn(sample);

        // When
        var result = cache.get(PRODUCT_ID);

        // Then
        assertThat(result).isEqualTo(sample);
        verify(l1Cache).get(PRODUCT_ID);
        verifyNoInteractions(l2Cache);
    }

    @Test
    void testGetFallsBackToL2AndPopulatesL1() {
        // Given
        var sample = new ProductBasePriceView(1L, new BigDecimal(100));
        when(l1Cache.get(PRODUCT_ID)).thenReturn(null);
        when(l2Cache.get(PRODUCT_ID)).thenReturn(sample);

        // When
        var result = cache.get(PRODUCT_ID);

        // Then
        assertThat(result).isEqualTo(sample);
        verify(l1Cache).get(PRODUCT_ID);
        verify(l2Cache).get(PRODUCT_ID);
        verify(l1Cache).put(sample);
    }

    @Test
    void testGetReturnsNullWhenBothCachesMiss() {
        // Given
        when(l1Cache.get(PRODUCT_ID)).thenReturn(null);
        when(l2Cache.get(PRODUCT_ID)).thenReturn(null);

        // When
        var result = cache.get(PRODUCT_ID);

        // Then
        assertThat(result).isNull();

        verify(l1Cache).get(PRODUCT_ID);
        verify(l2Cache).get(PRODUCT_ID);
        verify(l1Cache, never()).put(any());
    }

    @Test
    void testPutWritesToL2ThenL1() {
        // Given
        var sample = new ProductBasePriceView(1L, new BigDecimal(100));

        // When
        cache.put(sample);

        // Then
        var order = inOrder(l2Cache, l1Cache);
        order.verify(l2Cache).put(sample);
        order.verify(l1Cache).put(sample);
    }
}