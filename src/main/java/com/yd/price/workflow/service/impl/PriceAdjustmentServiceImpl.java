package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.repository.PriceAdjustmentMapper;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PriceAdjustmentServiceImpl implements PriceAdjustmentService {

    @Autowired
    private PriceAdjustmentMapper mapper;

    @Override
    @Transactional
    public PriceAdjustment addToProduct(Long productId, PriceAdjustment priceAdjustment) {
        mapper.insert(priceAdjustment);
        return priceAdjustment;
    }
}
