package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.entity.PriceAdjustmentMode;
import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import com.yd.price.workflow.model.ProductPrice;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import com.yd.price.workflow.service.api.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductEngineImplTest {

    private static final Long PRODUCT_ID = 101L;
    private static final BigDecimal BASE_PRICE = new BigDecimal("100.00");
    private static final ProductBasePriceView BASE_PRICE_VIEW = new ProductBasePriceView(PRODUCT_ID, BASE_PRICE);

    @Mock
    private ProductService productService;

    @Mock
    private PriceAdjustmentService priceAdjustmentService;

    @InjectMocks
    private ProductEngineImpl productEngine;

    @Test
    void testGetProductPriceWithoutAdjustments() {
        // Given
        when(productService.getProductPrice(PRODUCT_ID)).thenReturn(BASE_PRICE_VIEW);
        when(priceAdjustmentService.getAll(PRODUCT_ID)).thenReturn(Collections.emptySet());

        // When
        ProductPrice result = productEngine.getProductPrice(PRODUCT_ID);

        // Then
        assertThat(result.getFinalPrice())
                .as("Final price should equal base price when no adjustments are applied")
                .isEqualByComparingTo(BASE_PRICE);
    }

    @Test
    void testGetProductPriceWithSingleAdjustment() {
        // Given
        BigDecimal absoluteValue = new BigDecimal("20.00");
        Set<PriceAdjustment> adjustments = Set.of(new PriceAdjustment(PriceAdjustmentMode.ABSOLUTE, absoluteValue));

        when(productService.getProductPrice(PRODUCT_ID)).thenReturn(BASE_PRICE_VIEW);
        when(priceAdjustmentService.getAll(PRODUCT_ID)).thenReturn(adjustments);

        BigDecimal expectedFinalPrice = BASE_PRICE.add(absoluteValue); // 120.00

        // When
        ProductPrice result = productEngine.getProductPrice(PRODUCT_ID);

        // Then
        assertThat(result.getFinalPrice())
                .as("Final price should be base price plus absolute adjustment")
                .isEqualByComparingTo(expectedFinalPrice);
    }

    @Test
    void testGetProductPriceWithMixedAdjustments() {
        // Given
        Set<PriceAdjustment> adjustments = Set.of(
                new PriceAdjustment(PriceAdjustmentMode.ABSOLUTE, new BigDecimal("20.00")),
                new PriceAdjustment(PriceAdjustmentMode.PERCENT, new BigDecimal("0.5")),
                new PriceAdjustment(PriceAdjustmentMode.ABSOLUTE, new BigDecimal("10.00"))
        );
        BigDecimal expectedFinalPrice = new BigDecimal("180.00");

        when(productService.getProductPrice(PRODUCT_ID)).thenReturn(BASE_PRICE_VIEW);
        when(priceAdjustmentService.getAll(PRODUCT_ID)).thenReturn(adjustments);

        // When
        ProductPrice result = productEngine.getProductPrice(PRODUCT_ID);

        // Then
        assertThat(result.getFinalPrice())
                .as("Final price should equal the result of sequential application of mixed adjustments")
                .isEqualByComparingTo(expectedFinalPrice);

        assertThat(result.getPriceAdjustments())
                .hasSize(3);
    }
}