package com.yd;

import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public final class SimulationUtils {

    private SimulationUtils() {
    }

    public static HttpProtocolBuilder geHttpProtocol() {
        return HttpDsl.http
                .baseUrl("http://127.0.0.1") // for docker only
                .header("Host", "price-workflow.localhost") // Fod docker only
                .disableCaching()
                .contentTypeHeader("application/json")
                .acceptHeader("application/json")
                .enableHttp2()
                .shareConnections();

    }
}
