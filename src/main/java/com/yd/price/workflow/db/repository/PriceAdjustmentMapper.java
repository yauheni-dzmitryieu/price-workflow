package com.yd.price.workflow.db.repository;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PriceAdjustmentMapper {

    int insert(PriceAdjustment entity);
}
