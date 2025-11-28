package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.cache.api.PriceAdjustmentCache;
import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceAdjustmentCacheServiceImplTest {
    private static final Long PRODUCT_ID = 1L;

    @Mock
    private PriceAdjustmentCache priceAdjustmentCache;

    @Mock
    private PriceAdjustmentService priceAdjustmentService;

    private PriceAdjustmentCacheServiceImpl cacheService;

    @BeforeEach
    void setUp() {
        this.cacheService = Mockito.spy(new PriceAdjustmentCacheServiceImpl(priceAdjustmentCache, priceAdjustmentService));
    }

    @Test
    void testSave() {
        //Given
        PriceAdjustment priceAdjustment = new PriceAdjustment();
        doNothing().when(cacheService).registerSynchronization(any(PriceAdjustmentCacheServiceImpl.SaveAfterCommit.class));

        //When
        cacheService.save(PRODUCT_ID, priceAdjustment);

        //Then
        verify(cacheService).save(PRODUCT_ID, priceAdjustment);
        verify(priceAdjustmentService).save(PRODUCT_ID, priceAdjustment);
        verify(cacheService).registerSynchronization(any(PriceAdjustmentCacheServiceImpl.SaveAfterCommit.class));
        verifyNoMoreInteractions(cacheService);
    }

    @Test
    void testGetAllFromCache() {
        //Given
        Set<PriceAdjustment> adjustments = Set.of(new PriceAdjustment());

        when(priceAdjustmentCache.getAll(PRODUCT_ID)).thenReturn(adjustments);

        //When
        cacheService.getAll(PRODUCT_ID);

        //Then
        verify(priceAdjustmentCache).getAll(PRODUCT_ID);
        verifyNoMoreInteractions(priceAdjustmentCache, priceAdjustmentService);
    }

    @NullSource
    @EmptySource
    @ParameterizedTest
    void testGetAllFromDB(Set<PriceAdjustment> adjustments) {
        //Given
        Set<PriceAdjustment> adjustmentsFromDb = Set.of(new PriceAdjustment());

        when(priceAdjustmentCache.getAll(PRODUCT_ID)).thenReturn(adjustments);
        when(priceAdjustmentService.getAll(PRODUCT_ID)).thenReturn(adjustmentsFromDb);

        //When
        cacheService.getAll(PRODUCT_ID);

        //Then
        verify(priceAdjustmentCache).getAll(PRODUCT_ID);
        verify(priceAdjustmentService).getAll(PRODUCT_ID);
        verify(priceAdjustmentCache).addAll(PRODUCT_ID, adjustmentsFromDb);
        verifyNoMoreInteractions(priceAdjustmentCache, priceAdjustmentService);
    }

    @NullSource
    @EmptySource
    @ParameterizedTest
    void testGetAllFromDBEmptyResponseFromDB(Set<PriceAdjustment> adjustmentsFromDb) {
        //Given
        when(priceAdjustmentCache.getAll(PRODUCT_ID)).thenReturn(null);
        when(priceAdjustmentService.getAll(PRODUCT_ID)).thenReturn(adjustmentsFromDb);

        //When
        cacheService.getAll(PRODUCT_ID);

        //Then
        verify(priceAdjustmentCache).getAll(PRODUCT_ID);
        verify(priceAdjustmentService).getAll(PRODUCT_ID);
        verify(priceAdjustmentCache, never()).addAll(PRODUCT_ID, adjustmentsFromDb);
        verifyNoMoreInteractions(priceAdjustmentCache, priceAdjustmentService);
    }

    @Nested
    class SaveAfterCommitTest {
        private PriceAdjustment priceAdjustment;
        private PriceAdjustmentCacheServiceImpl.SaveAfterCommit saveAfterCommit;

        @BeforeEach
        void setUp() {
            this.priceAdjustment = mock(PriceAdjustment.class);
            this.saveAfterCommit = cacheService.new SaveAfterCommit(PRODUCT_ID, priceAdjustment);
        }

        @Test
        void testAfterCommit() {
            //Given
            when(priceAdjustment.getId()).thenReturn(PRODUCT_ID);

            //When
            saveAfterCommit.afterCommit();

            //Then
            verify(priceAdjustmentCache).put(PRODUCT_ID, priceAdjustment);
        }

        @Test
        void testAfterCompletionWithRolledBackStatus() {
            //When
            saveAfterCommit.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK);

            //Then
            verifyNoMoreInteractions(priceAdjustment, priceAdjustmentCache);
        }

        @Test
        void testAfterCompletionWithAnotherStatus() {
            //When
            saveAfterCommit.afterCompletion(TransactionSynchronization.STATUS_UNKNOWN);

            //Then
            verify(priceAdjustment, never()).getId();
        }
    }
}