package com.yd;

public final class ParameterUtils {
    public static final int USERS = Integer.parseInt(System.getProperty("users", "50"));
    public static final int DURATION_SEC = Integer.parseInt(System.getProperty("duration", "60"));
    public static final int TOTAL_REQUESTS = Integer.parseInt(System.getProperty("count", "1000"));
    public static final int CONCURRENCY = Integer.parseInt(System.getProperty("concurrency", "10"));
    public static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080");

    private ParameterUtils() {
    }

}
