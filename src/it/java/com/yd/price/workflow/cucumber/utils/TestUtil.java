package com.yd.price.workflow.cucumber.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class TestUtil {

    private TestUtil() {
    }

    public static String resourceToString(String path) {
        try {
            return IOUtils.resourceToString("/" + path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
