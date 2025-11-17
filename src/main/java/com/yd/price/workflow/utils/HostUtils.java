package com.yd.price.workflow.utils;

import java.lang.management.ManagementFactory;

public final class HostUtils {

    private HostUtils() {}

    public static String getUniqueHostname() {
        return ManagementFactory.getRuntimeMXBean().getName();
    }
}
