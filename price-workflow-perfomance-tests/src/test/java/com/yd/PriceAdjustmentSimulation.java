package com.yd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;
import static java.util.stream.Stream.generate;

public class PriceAdjustmentSimulation extends Simulation {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    {
        var scn = scenario("Add Price Adjustment Load Test")
                .feed(getFeeder())
                .exec(
                        http("Add Adjustment")
                                .post("/api/v1/admin/products/#{productId}/adjustments")
                                .body(StringBody("#{body}"))
                                .check(status().is(200))
                )
                .injectOpen(endpointInjectionProfile());

        setUp(scn)
                .protocols(SimulationUtils.geHttpProtocol())
                .assertions(
                        global().responseTime().percentile4().lte(20), //p99th percentile <= 20 ms
                        global().successfulRequests().percent().gt(99.00)
                );
    }


    private OpenInjectionStep.RampRate.RampRateOpenInjectionStep endpointInjectionProfile() {
        int totalDesiredUserCount = 250;
        double userRampUpPerInterval = 50;
        double rampUpIntervalSeconds = 30;
        int totalRampUptimeSeconds = 120;
        int steadyStateDurationSeconds = 300;

        return rampUsersPerSec(userRampUpPerInterval / (rampUpIntervalSeconds / 60))
                .to(totalDesiredUserCount)
                .during(Duration.ofSeconds(totalRampUptimeSeconds + steadyStateDurationSeconds));
    }


    private String randomAdjustmentJson(long productId) {
        int modeChoice = ThreadLocalRandom.current().nextInt(2);
        String mode = (modeChoice == 0) ? "ABSOLUTE" : "PERCENT";


        int value = mode.equals("ABSOLUTE")
                ? ThreadLocalRandom.current().nextInt(1, 5000)
                : ThreadLocalRandom.current().nextInt(1, 99);

        try {
            return objectMapper.writeValueAsString(
                    Map.of(
                            "productId", productId,
                            "type", this.getType(),
                            "value", value,
                            "mode", mode
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getType() {
        return switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0 -> "PROMO";
            case 1 -> "TAX";
            default -> "FEE";
        };
    }

    private Iterator<Map<String, Object>> getFeeder() {
        return generate(() -> {
            long productId = ThreadLocalRandom.current().nextLong(1, 100_000);
            return Map.<String, Object>of(
                    "productId", productId,
                    "body", randomAdjustmentJson(productId)
            );
        }).iterator();
    }
}
