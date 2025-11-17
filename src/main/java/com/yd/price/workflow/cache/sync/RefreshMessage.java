package com.yd.price.workflow.cache.sync;


import com.yd.price.workflow.db.entity.PriceAdjustment;

import java.lang.management.ManagementFactory;

public record RefreshMessage(String hostname, PriceAdjustment priceAdjustment) {

    public RefreshMessage(PriceAdjustment priceAdjustment) {
        this(ManagementFactory.getRuntimeMXBean().getName(), priceAdjustment);
    }
}
