package com.yd.price.workflow.db.repository;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.entity.view.PriceAdjustmentView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface PriceAdjustmentMapper {

    int create(PriceAdjustment entity);

    Set<PriceAdjustment> findAllByProductId(@Param("productId") Long productId);
}
