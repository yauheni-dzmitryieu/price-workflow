package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.entity.PriceAdjustmentMode;
import com.yd.price.workflow.db.repository.PriceAdjustmentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceAdjustmentServiceImplTest {
    private static final Long PRODUCT_ID = 1L;

    @Mock
    private PriceAdjustmentMapper mapper;

    @InjectMocks
    private PriceAdjustmentServiceImpl priceAdjustmentService;

    @Test
    void testAddToProduct() {
        //Given
        PriceAdjustment priceAdjustment = new PriceAdjustment(PriceAdjustmentMode.PERCENT, new BigDecimal(123));
        when(mapper.create(priceAdjustment)).thenReturn(1);

        //When
        PriceAdjustment savedPriceAdjustment = priceAdjustmentService.addToProduct(PRODUCT_ID, priceAdjustment);

        //Then
        assertThat(savedPriceAdjustment).isEqualTo(priceAdjustment);
    }

    @Test
    void testGetAll() {
        //Given
        Set<PriceAdjustment> expectedPA = Set.of(new PriceAdjustment(PriceAdjustmentMode.PERCENT, new BigDecimal(123)));
        when(mapper.findAllByProductId(PRODUCT_ID)).thenReturn(expectedPA);

        //When
        Set<PriceAdjustment> priceAdjustments = priceAdjustmentService.getAll(PRODUCT_ID);

        //Then
        assertThat(priceAdjustments)
                .hasSize(1)
                .containsAll(expectedPA);
    }
}