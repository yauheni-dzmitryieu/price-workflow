package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.cache.api.ProductCache;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import com.yd.price.workflow.service.api.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCacheServiceImplTest {
    private static final Long PRODUCT_ID = 1L;

    @Mock
    private ProductService productService;

    @Mock
    private ProductCache productCache;

    private ProductCacheServiceImpl productCacheService;

    @BeforeEach
    void setUp() {
        this.productCacheService = spy(new ProductCacheServiceImpl(productService, productCache));
    }

    @Test
    void testGetProductPriceFromCache() {
        //Given
        ProductBasePriceView view = new ProductBasePriceView(PRODUCT_ID, new BigDecimal(100L));
        when(productCache.get(PRODUCT_ID)).thenReturn(view);

        //When
        ProductBasePriceView productPrice = productCacheService.getProductPrice(PRODUCT_ID);

        //Then
        assertThat(productPrice).isEqualTo(view);

        verify(productCache).get(PRODUCT_ID);
        verifyNoMoreInteractions(productCache, productService);
    }


    @Test
    void testGetProductPriceFromDb() {
        //Given
        ProductBasePriceView view = new ProductBasePriceView(PRODUCT_ID, new BigDecimal(100L));
        when(productCache.get(PRODUCT_ID)).thenReturn(null);
        when(productService.getProductPrice(PRODUCT_ID)).thenReturn(view);

        //When
        ProductBasePriceView productPrice = productCacheService.getProductPrice(PRODUCT_ID);

        //Then
        assertThat(productPrice).isEqualTo(view);

        verify(productCache).get(PRODUCT_ID);
        verify(productService).getProductPrice(PRODUCT_ID);
        verify(productCache).put(view);
        verifyNoMoreInteractions(productCache, productService);
    }
}