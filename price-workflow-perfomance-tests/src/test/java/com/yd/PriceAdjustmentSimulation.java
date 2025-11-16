package com.yd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PriceAdjustmentSimulation extends Simulation {

    // ============================================
    // CONFIGURABLE PARAMETERS
    // ============================================
    private final int USERS = Integer.parseInt(System.getProperty("users", "50"));
    private final int DURATION_SEC = Integer.parseInt(System.getProperty("duration", "60"));
    private final int TOTAL_REQUESTS = Integer.parseInt(System.getProperty("count", "1000"));
    private final int CONCURRENCY = Integer.parseInt(System.getProperty("concurrency", "10"));

    private final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ============================================
    // RANDOM JSON BODY GENERATOR
    // ============================================
    private static String randomAdjustmentJson(long productId) {
        int modeChoice = ThreadLocalRandom.current().nextInt(2);
        String mode = (modeChoice == 0) ? "ABSOLUTE" : "PERCENT";

        String type = switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0 -> "PROMO";
            case 1 -> "TAX";
            default -> "FEE";
        };

        int value = mode.equals("ABSOLUTE")
                ? ThreadLocalRandom.current().nextInt(1, 5000)
                : ThreadLocalRandom.current().nextInt(1, 99);

        try {
            return objectMapper.writeValueAsString(
                    Map.of(
                            "type", type,
                            "value", value,
                            "mode", mode
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ============================================
    // FEEDER
    // ============================================

    Iterator<Map<String, Object>> feeder =
            java.util.stream.Stream.generate(() -> {
                        long productId = ThreadLocalRandom.current().nextLong(1, 100_000);
                        return Map.<String, Object>of(
                                "productId", productId,
                                "body", randomAdjustmentJson(productId)
                        );
                    })
                    .limit(TOTAL_REQUESTS)
                    .iterator();


    // ============================================
    // HTTP PROTOCOL
    // ============================================
    HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .contentTypeHeader("application/json")
            .acceptHeader("application/json");

    // ============================================
    // SCENARIO
    // ============================================
    ScenarioBuilder scn = scenario("Add Price Adjustment Load Test")
            .feed(feeder)
            .exec(
                    http("Add Adjustment")
                            .post("/api/v1/admin/products/#{productId}/adjustments")
                            .body(StringBody("#{body}"))
                            .check(status().is(200))
            );

    {
        // ============================================
        // SETUP
        // ============================================
        setUp(
                scn.injectOpen(
                        rampUsers(USERS).during(DURATION_SEC),
                        constantUsersPerSec(CONCURRENCY).during(DURATION_SEC)
                )
        ).protocols(httpProtocol);
    }
}
