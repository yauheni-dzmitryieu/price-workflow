package com.yd.price.workflow.service.impl;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.repository.PriceAdjustmentMapper;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Primary
@Service
public class PriceAdjustmentServiceImpl implements PriceAdjustmentService {
    public static final Logger log = LoggerFactory.getLogger(PriceAdjustmentServiceImpl.class);

    @Autowired
    private PriceAdjustmentMapper mapper;

    @Autowired
    @Qualifier("priceAdjustmentCacheServiceImpl")
    private PriceAdjustmentService priceAdjustmentCacheService;

    @Override
    @Transactional
    public PriceAdjustment addToProduct(Long productId, PriceAdjustment priceAdjustment) {
        mapper.create(priceAdjustment);
        log.info("DB: Price Adjustment added to product: {} with id: {}", productId, priceAdjustment.getId());

        priceAdjustmentCacheService.addToProduct(productId, priceAdjustment);
        return priceAdjustment;
    }

    /**
     * @param productId
     * @return
     */
    @Override
    public Set<PriceAdjustment> getAll(Long productId) {
        Set<PriceAdjustment> cachedPriceAdjustments = priceAdjustmentCacheService.getAll(productId);
        if (!CollectionUtils.isEmpty(cachedPriceAdjustments)) {
            return cachedPriceAdjustments;
        }

        Set<PriceAdjustment> storedPriceAdjustments = mapper.findAllByProductId(productId);
        priceAdjustmentCacheService.addAll(productId, storedPriceAdjustments);
        return storedPriceAdjustments;
    }
}
