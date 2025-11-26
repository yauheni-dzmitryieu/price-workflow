package com.yd.price.workflow.cache.l2;

import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCacheL2RedisTest {

    private static final Long PRODUCT_ID = 1L;
    private static final String KEY = "product-cache:" + PRODUCT_ID;

    @Mock
    private RedissonClient redisson;

    @Mock
    private Duration expire;

    @InjectMocks
    private ProductCacheL2Redis productCacheL2Redis;

    @Test
    void testGet() {
        //Given
        RBucket cache = mock(RBucket.class);
        ProductBasePriceView view = mock(ProductBasePriceView.class);

        when(redisson.getBucket(KEY)).thenReturn(cache);
        when(cache.get()).thenReturn(view);

        //When
        ProductBasePriceView result = productCacheL2Redis.get(PRODUCT_ID);

        //Then
        assertThat(result).isEqualTo(view);

        verify(redisson).getBucket(KEY);
        verify(cache).get();
    }

    @Test
    void testPut() {
        //Given
        RBucket cache = mock(RBucket.class);
        ProductBasePriceView view = new ProductBasePriceView(PRODUCT_ID, new BigDecimal(100));

        when(redisson.getBucket(KEY)).thenReturn(cache);
        //When

        productCacheL2Redis.put(view);

        //Then
        verify(redisson).getBucket(KEY);
        verify(cache).set(view, expire);
    }

    @Test
    void testPutWithNullId() {
        //Given
        RBucket cache = mock(RBucket.class);
        ProductBasePriceView view = new ProductBasePriceView(null, new BigDecimal(100));

        //When

        productCacheL2Redis.put(view);

        //Then
        verifyNoInteractions(cache, redisson, expire);
    }

    @Test
    void testPutWithNullValue() {
        //Given
        RBucket cache = mock(RBucket.class);
        ProductBasePriceView view = null;

        //When

        productCacheL2Redis.put(view);

        //Then
        verifyNoInteractions(cache, redisson, expire);
    }
}