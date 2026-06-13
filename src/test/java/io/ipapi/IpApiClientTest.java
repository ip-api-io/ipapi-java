package io.ipapi;

import com.sun.net.httpserver.HttpServer;
import io.ipapi.model.IpInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IpApiClientTest {
    // IpInfoV1Dto example from https://ip-api.io/openapi.json
    private static final String IP_INFO_FIXTURE = """
            {
              "ip": "203.0.113.195",
              "isp": "Comcast Cable Communications",
              "asn": "AS7922",
              "suspicious_factors": {
                "is_proxy": false, "is_tor_node": false, "is_spam": false,
                "is_crawler": false, "is_datacenter": true, "is_vpn": false, "is_threat": false
              },
              "location": {
                "country": "United States", "country_code": "US", "city": "San Francisco",
                "latitude": 37.7749, "longitude": -122.4194, "zip": "94105",
                "timezone": "America/Los_Angeles", "local_time": "2023-06-21T14:30:00-07:00",
                "local_time_unix": 1687385400, "is_daylight_savings": true
              }
            }
            """;

    private HttpServer server;
    private int status = 200;
    private String responseBody = "{}";
    private Map<String, String> responseHeaders = Map.of();

    private record Recorded(String method, String uri, String userAgent, String contentType, String body) {
    }

    private final List<Recorded> requests = Collections.synchronizedList(new ArrayList<>());

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            requests.add(new Recorded(
                    exchange.getRequestMethod(),
                    exchange.getRequestURI().toString(),
                    exchange.getRequestHeaders().getFirst("User-Agent"),
                    exchange.getRequestHeaders().getFirst("Content-Type"),
                    body));
            responseHeaders.forEach((name, value) -> exchange.getResponseHeaders().set(name, value));
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            byte[] payload = responseBody.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status, payload.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(payload);
            }
        });
        server.start();
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    private IpApiClient client() {
        return clientBuilder().build();
    }

    private IpApiClient.Builder clientBuilder() {
        return IpApiClient.builder()
                .baseUrl("http://127.0.0.1:" + server.getAddress().getPort());
    }

    @Test
    void lookupParsesResponseAndSendsUserAgent() {
        responseBody = IP_INFO_FIXTURE;

        IpInfo info = client().lookup("203.0.113.195");

        assertEquals("203.0.113.195", info.ip());
        assertEquals("United States", info.location().country());
        assertTrue(info.suspiciousFactors().isDatacenter());
        assertEquals("AS7922", info.asn());

        Recorded request = requests.get(0);
        assertEquals("GET", request.method());
        assertEquals("/api/v1/ip/203.0.113.195", request.uri());
        assertEquals("ipapi-io-java/" + IpApiClient.VERSION, request.userAgent());
    }

    @Test
    void apiKeySentAsQueryParam() {
        clientBuilder().apiKey("secret123").build().lookup();

        assertEquals("/api/v1/ip?api_key=secret123", requests.get(0).uri());
    }

    @Test
    void emailPathIsUrlEncoded() {
        responseBody = """
                {"email":"user+tag@example.com","reachable":"yes",
                 "syntax":{"username":"user+tag","domain":"example.com","valid":true},
                 "disposable":false,"role_account":false,"free":false,"has_mx_records":true}
                """;

        client().validateEmail("user+tag@example.com");

        assertEquals("/api/v1/email/advanced/user%2Btag%40example.com", requests.get(0).uri());
    }

    @Test
    void batchPostSendsJsonBody() {
        responseBody = "{\"results\": {}}";

        client().lookupBatch(List.of("8.8.8.8", "1.1.1.1"));

        Recorded request = requests.get(0);
        assertEquals("POST", request.method());
        assertEquals("/api/v1/ip/batch", request.uri());
        assertEquals("{\"ips\":[\"8.8.8.8\",\"1.1.1.1\"]}", request.body());
        assertEquals("application/json", request.contentType());
    }

    @Test
    void batchSizeIsValidated() {
        IpApiClient client = client();

        assertThrows(IllegalArgumentException.class, () -> client.lookupBatch(List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> client.lookupBatch(Collections.nCopies(101, "1.1.1.1")));
        assertThrows(IllegalArgumentException.class, () -> client.validateEmailBatch(List.of()));
    }

    @Test
    void rateLimitExceptionExposesHeaders() {
        status = 429;
        responseBody = "{\"message\": \"Rate limit exceeded\"}";
        responseHeaders = Map.of(
                "x-ratelimit-limit", "1000",
                "x-ratelimit-remaining", "0",
                "x-ratelimit-reset", "1718200000");

        RateLimitException error =
                assertThrows(RateLimitException.class, () -> client().lookup("8.8.8.8"));

        assertEquals(429, error.getStatusCode());
        assertEquals(1000L, error.getLimit());
        assertEquals(0L, error.getRemaining());
        assertEquals(1718200000L, error.getReset());
        assertTrue(error.getMessage().contains("Rate limit exceeded"));
    }

    @Test
    void authenticationExceptionOn401() {
        status = 401;
        responseBody = "{\"error\": \"Invalid API key\"}";

        AuthenticationException error = assertThrows(AuthenticationException.class,
                () -> clientBuilder().apiKey("bad").build().lookup());
        assertEquals(401, error.getStatusCode());
    }

    @Test
    void invalidRequestExceptionOn400() {
        status = 400;
        responseBody = "{\"message\": \"Invalid IP address\"}";

        assertThrows(InvalidRequestException.class, () -> client().lookup("not-an-ip"));
    }

    @Test
    void serverExceptionOn500() {
        status = 500;

        ServerException error = assertThrows(ServerException.class, () -> client().lookup());
        assertEquals(500, error.getStatusCode());
        assertNull(error.getCause());
    }
}
