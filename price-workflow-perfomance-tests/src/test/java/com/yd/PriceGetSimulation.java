package com.yd;

import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.incrementUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.nothingFor;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PriceGetSimulation extends Simulation {

    ScenarioBuilder scn = scenario("Get Product Price Load Test")
            .feed(getFeeder())
            .exec(http("Get Product Price")
                    .get("/api/v1/products/#{productId}/price")
                    .check(status().is(200))
            );

    {
        int maxUsers = 250;
        int intervalSeconds = 30;
        int totalTestSeconds = 600;

        int usersPerInterval = (int) Math.ceil((double) maxUsers / (totalTestSeconds / intervalSeconds)); // = 13

        OpenInjectionStep injectionProfile = incrementUsersPerSec(usersPerInterval)
                .times(totalTestSeconds / intervalSeconds)
                .eachLevelLasting(Duration.ofSeconds(intervalSeconds))
                .startingFrom(0d);

        PopulationBuilder populationBuilder2 = scn.injectOpen(
                rampUsers(250).during(300),   // ramp-up over 5 minutes
                nothingFor(60),               // ramp-up complete / wait for 1 minute
                constantUsersPerSec(250).during(240) // steady load for 4 minutes
        );

        PopulationBuilder populationBuilder = scn.injectOpen(endpointInjectionProfile());

        setUp(populationBuilder)
                .protocols(SimulationUtils.geHttpProtocol())
                .assertions(
                        global().responseTime().percentile3().lte(10), //95th percentile <= 10 ms
                        global().responseTime().percentile4().lte(20), //p99th percentile <= 20 ms
                        global().successfulRequests().percent().gt(99.00)
                );
    }

    private Iterator<Map<String, Object>> getFeeder() {
        return Stream.generate(() -> Map.<String, Object>of("productId", ThreadLocalRandom.current().nextLong(1, 100_000)))
                .iterator();
    }

    private OpenInjectionStep.RampRate.RampRateOpenInjectionStep endpointInjectionProfile() {
        int totalDesiredUserCount = 1_000;
        double userRampUpPerInterval = 50;
        double rampUpIntervalSeconds = 30;
        int totalRampUptimeSeconds = 120;
        int steadyStateDurationSeconds = 300;

        return rampUsersPerSec(userRampUpPerInterval / (rampUpIntervalSeconds / 60))
                .to(totalDesiredUserCount)
                .during(Duration.ofSeconds(totalRampUptimeSeconds + steadyStateDurationSeconds));
    }
}
