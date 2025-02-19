package org.dg;

import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WiremockTestResource  implements QuarkusTestResourceLifecycleManager {

    public static WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();

        // create some stubs

        return Map.of("https://forms.googleapis.com/v1/forms", "localhost:" + wireMockServer.port());
    }

    @Override
    public synchronized void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            wireMockServer = null;
        }
    }

}
