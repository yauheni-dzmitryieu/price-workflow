package com.yd.price.workflow.db.repository;

import com.yd.price.workflow.db.entity.view.ProductBasePriceView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

    ProductBasePriceView findBasePriceByProductId(@Param("id") Long id);

}
