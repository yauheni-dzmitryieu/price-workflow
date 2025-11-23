package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.repository.PriceAdjustmentMapper;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Set;

@Service
public class PriceAdjustmentServiceImpl implements PriceAdjustmentService {
    public static final Logger log = LoggerFactory.getLogger(PriceAdjustmentServiceImpl.class);

    private final PriceAdjustmentMapper mapper;

    public PriceAdjustmentServiceImpl(PriceAdjustmentMapper mapper) {
        Assert.notNull(mapper, "PriceAdjustmentMapper is required");
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public PriceAdjustment addToProduct(Long productId, PriceAdjustment priceAdjustment) {
        mapper.create(priceAdjustment);
        log.debug("DB: Price Adjustment added to product: {} with id: {}", productId, priceAdjustment.getId());

        return priceAdjustment;
    }

    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        return mapper.findAllByProductId(productId);
    }
}
