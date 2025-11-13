package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.persistence.entity.PriceAdjustment;
import com.yd.price.workflow.persistence.repository.PriceAdjustmentMapper;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceAdjustmentServiceImpl implements PriceAdjustmentService {

    @Autowired
    private PriceAdjustmentMapper mapper;

    @Override
    public PriceAdjustment addToProduct(Long productId, PriceAdjustment priceAdjustment) {
        return null;
    }
}
