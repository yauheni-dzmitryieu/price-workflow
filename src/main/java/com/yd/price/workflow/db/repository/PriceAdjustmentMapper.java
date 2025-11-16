package com.yd.price.workflow.db.repository;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import com.yd.price.workflow.db.entity.view.PriceAdjustmentView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PriceAdjustmentMapper {

    int insert(PriceAdjustment entity);

    List<PriceAdjustmentView> findAllByProductId(@Param("productId") Long productId);
}
