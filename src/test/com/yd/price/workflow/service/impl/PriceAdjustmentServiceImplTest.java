package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.entity.PriceAdjustmentMode;
import com.yd.price.workflow.db.repository.PriceAdjustmentMapper;
import org.assertj.core.api.Assertions;
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
    private PriceAdjustmentServiceImpl service;

    @Test
    void testConstructor() {
        //When
        Throwable throwable = Assertions.catchThrowable(() -> new PriceAdjustmentServiceImpl(null));

        //Then
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("PriceAdjustmentMapper is required");
    }

    @Test
    void testAddToProduct() {
        //Given
        PriceAdjustment priceAdjustment = new PriceAdjustment(PriceAdjustmentMode.PERCENT, new BigDecimal(123));
        when(mapper.create(priceAdjustment)).thenReturn(1);

        //When
        PriceAdjustment savedPriceAdjustment = service.save(PRODUCT_ID, priceAdjustment);

        //Then
        assertThat(savedPriceAdjustment).isEqualTo(priceAdjustment);
    }

    @Test
    void testGetAll() {
        //Given
        Set<PriceAdjustment> expectedPA = Set.of(new PriceAdjustment(PriceAdjustmentMode.PERCENT, new BigDecimal(123)));
        when(mapper.findAllByProductId(PRODUCT_ID)).thenReturn(expectedPA);

        //When
        Set<PriceAdjustment> priceAdjustments = service.getAll(PRODUCT_ID);

        //Then
        assertThat(priceAdjustments)
                .hasSize(1)
                .containsAll(expectedPA);
    }
}