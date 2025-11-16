package com.yd.price.workflow.db.entity.view;

import com.yd.price.workflow.db.entity.PriceAdjustmentMode;

import java.math.BigDecimal;

public record PriceAdjustmentView(Long id, PriceAdjustmentMode mode, BigDecimal value) {
}
