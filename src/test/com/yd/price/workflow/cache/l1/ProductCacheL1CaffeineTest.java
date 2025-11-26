package com.yd.price.workflow.cache.l1;

import com.github.benmanes.caffeine.cache.Cache;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCacheL1CaffeineTest {
    private static final Long PRODUCT_ID = 1L;

    @Mock
    private Cache<Long, ProductBasePriceView> cache;


    @InjectMocks
    private ProductCacheL1Caffeine cacheL1Caffeine;


    @Test
    void testGet() {
        //Given
        ProductBasePriceView view = new ProductBasePriceView(1L, new BigDecimal("100.00"));
        when(cache.getIfPresent(PRODUCT_ID)).thenReturn(view);

        //When
        ProductBasePriceView result = cacheL1Caffeine.get(PRODUCT_ID);

        //Then
        assertThat(result).isEqualTo(view);
        verify(cache).getIfPresent(PRODUCT_ID);
    }

    @Test
    void testGetIfNullKey() {
        //Given
        when(cache.getIfPresent(PRODUCT_ID)).thenReturn(null);

        //When
        ProductBasePriceView result = cacheL1Caffeine.get(PRODUCT_ID);

        //Then
        assertThat(result).isNull();
        verify(cache).getIfPresent(PRODUCT_ID);
    }

    @Test
    void testPut() {
        //Given
        ProductBasePriceView view = new ProductBasePriceView(1L, new BigDecimal("100.00"));
        //When

        cacheL1Caffeine.put(view);

        //Then
        verify(cache).put(PRODUCT_ID, view);
    }


    static Stream<Arguments> putDataProvider() {
        return Stream.of(
                Arguments.arguments(new ProductBasePriceView(null, new BigDecimal("100.00")))
        );
    }

    @NullSource
    @ParameterizedTest
    @MethodSource("putDataProvider")
    void testPut(ProductBasePriceView view) {
        cacheL1Caffeine.put(view);

        //Then
        verify(cache, never()).put(PRODUCT_ID, view);
    }
}