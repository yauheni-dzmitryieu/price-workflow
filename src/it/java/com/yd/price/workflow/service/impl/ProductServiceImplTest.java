package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import com.yd.price.workflow.db.repository.ProductMapper;
import com.yd.price.workflow.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    private static final Long PRODUCT_ID = 1L;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testGetProductPrice() {
        //Given
        ProductBasePriceView expectedValue = new ProductBasePriceView(PRODUCT_ID, new BigDecimal(123));

        when(productMapper.findBasePriceByProductId(PRODUCT_ID)).thenReturn(expectedValue);

        ///When
        ProductBasePriceView productPrice = productService.getProductPrice(PRODUCT_ID);

        //Then
        assertThat(productPrice)
                .isEqualTo(expectedValue);
    }

    @Test
    void testGetProductPriceIfNotExist() {
        //Given
        when(productMapper.findBasePriceByProductId(PRODUCT_ID)).thenReturn(null);

        ///When
        Throwable throwable = catchThrowable(() -> productService.getProductPrice(PRODUCT_ID));

        //Then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(ResourceNotFoundException.class);
    }
}