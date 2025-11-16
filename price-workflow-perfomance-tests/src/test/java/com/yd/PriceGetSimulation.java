package com.yd;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PriceGetSimulation extends Simulation {

    // ============================================
    // CONFIGURABLE PARAMETERS (override via -Dxxx)
    // ============================================
    private final int USERS = Integer.parseInt(System.getProperty("users", "50"));
    private final int DURATION_SEC = Integer.parseInt(System.getProperty("duration", "60"));
    private final int TOTAL_REQUESTS = Integer.parseInt(System.getProperty("count", "5000"));
    private final int CONCURRENCY = Integer.parseInt(System.getProperty("concurrency", "20"));

    private final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080");

    // ============================================
    // FEEDER: RANDOM PRODUCT IDS
    // ============================================
    Iterator<Map<String, Object>> feeder =
            Stream.generate(() -> Map.<String, Object>of(
                            "productId", ThreadLocalRandom.current().nextLong(1004, 100_000)
                    ))
                    .limit(TOTAL_REQUESTS)
                    .iterator();

    // ============================================
    // HTTP CONFIGURATION
    // ============================================
    HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .acceptHeader("application/json")
            .enableHttp2()
            .shareConnections();

    // ============================================
    // SCENARIO
    // ============================================
    ScenarioBuilder scn = scenario("Get Product Price Load Test")
            .feed(feeder)
            .exec(
                    http("Get Product Price")
                            .get("/api/v1/products/#{productId}/price")
                            .check(status().is(200))
            );

    {
        // ============================================
        // PERFORMANCE TEST SETUP
        // ============================================
        OpenInjectionStep rampStep = rampUsers(USERS).during(DURATION_SEC);
        OpenInjectionStep.ConstantRate.ConstantRateOpenInjectionStep mainStep = constantUsersPerSec(CONCURRENCY).during(DURATION_SEC);

        PopulationBuilder populationBuilder = scn.injectOpen(rampStep, mainStep);

        setUp(populationBuilder)
                .protocols(httpProtocol);
    }
}
