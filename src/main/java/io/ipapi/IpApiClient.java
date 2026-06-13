package io.ipapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ipapi.model.AdvancedEmailValidation;
import io.ipapi.model.AsnLookup;
import io.ipapi.model.BatchDomainAgeResponse;
import io.ipapi.model.BatchEmailValidationResponse;
import io.ipapi.model.BatchIpLookupResponse;
import io.ipapi.model.DomainAge;
import io.ipapi.model.EmailInfo;
import io.ipapi.model.ForwardDns;
import io.ipapi.model.IpInfo;
import io.ipapi.model.MxLookup;
import io.ipapi.model.RateLimitInfo;
import io.ipapi.model.ReverseDns;
import io.ipapi.model.RiskScore;
import io.ipapi.model.TorDetection;
import io.ipapi.model.UsageSummary;
import io.ipapi.model.Whois;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Client for the ip-api.io IP intelligence and email validation API.
 *
 * <pre>{@code
 * IpApiClient client = IpApiClient.builder().apiKey("YOUR_API_KEY").build();
 * IpInfo info = client.lookup("8.8.8.8");
 * }</pre>
 *
 * <p>An API key is required by the live API — get a free key at
 * <a href="https://ip-api.io">ip-api.io</a>.
 */
public final class IpApiClient {
    public static final String VERSION = "1.0.0";

    /** Documented per-request limit for batch endpoints. */
    public static final int MAX_BATCH_SIZE = 100;

    private static final String USER_AGENT = "ipapi-io-java/" + VERSION;

    private final String apiKey;
    private final String baseUrl;
    private final Duration timeout;
    private final java.net.http.HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private IpApiClient(Builder builder) {
        this.apiKey = builder.apiKey;
        this.baseUrl = builder.baseUrl.replaceAll("/+$", "");
        this.timeout = builder.timeout;
        this.httpClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(builder.timeout)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link IpApiClient}. */
    public static final class Builder {
        private String apiKey;
        private String baseUrl = "https://ip-api.io";
        private Duration timeout = Duration.ofSeconds(10);

        /** API key, sent as the api_key query parameter. Free key at https://ip-api.io. */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /** Override the API origin (testing). */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /** Per-request timeout (default 10s). */
        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public IpApiClient build() {
            return new IpApiClient(this);
        }
    }

    // -- IP intelligence ------------------------------------------------------

    /** Geolocation + threat intelligence for the caller's IP. */
    public IpInfo lookup() {
        return get("/api/v1/ip", IpInfo.class);
    }

    /** Geolocation + threat intelligence for a specific IP. */
    public IpInfo lookup(String ip) {
        return get("/api/v1/ip/" + encode(ip), IpInfo.class);
    }

    /** Look up to 100 IP addresses in a single request. */
    public BatchIpLookupResponse lookupBatch(List<String> ips) {
        checkBatch(ips, "ips");
        return post("/api/v1/ip/batch", Map.of("ips", ips), BatchIpLookupResponse.class);
    }

    /** IP reputation check (untyped map — shape may evolve). */
    @SuppressWarnings("unchecked")
    public Map<String, Object> ipReputation(String ip) {
        return get("/api/v1/ip-reputation/" + encode(ip), Map.class);
    }

    /** Whether an IP is a Tor exit node. */
    public TorDetection torCheck(String ip) {
        return get("/api/v1/tor/" + encode(ip), TorDetection.class);
    }

    /** Autonomous system lookup for an IP. */
    public AsnLookup asn(String ip) {
        return get("/api/v1/asn/" + encode(ip), AsnLookup.class);
    }

    // -- Email validation -------------------------------------------------------

    /** Syntax, disposability and MX analysis of an email address. */
    public EmailInfo emailInfo(String email) {
        return get("/api/v1/email/" + encode(email), EmailInfo.class);
    }

    /** Advanced validation including SMTP deliverability checks. */
    public AdvancedEmailValidation validateEmail(String email) {
        return get("/api/v1/email/advanced/" + encode(email), AdvancedEmailValidation.class);
    }

    /** Advanced-validate up to 100 email addresses in a single request. */
    public BatchEmailValidationResponse validateEmailBatch(List<String> emails) {
        checkBatch(emails, "emails");
        return post("/api/v1/email/advanced/batch", Map.of("emails", emails),
                BatchEmailValidationResponse.class);
    }

    // -- Risk scoring -----------------------------------------------------------

    /** Fraud risk score for the caller's IP. */
    public RiskScore riskScore() {
        return get("/api/v1/risk-score", RiskScore.class);
    }

    /** Fraud risk score for a specific IP. */
    public RiskScore riskScore(String ip) {
        return get("/api/v1/risk-score/" + encode(ip), RiskScore.class);
    }

    /** Fraud risk score for an email address. */
    public RiskScore emailRiskScore(String email) {
        return get("/api/v1/risk-score/email/" + encode(email), RiskScore.class);
    }

    // -- DNS & domains ----------------------------------------------------------

    public Whois whois(String domain) {
        return get("/api/v1/dns/whois/" + encode(domain), Whois.class);
    }

    public ReverseDns reverseDns(String ip) {
        return get("/api/v1/dns/reverse/" + encode(ip), ReverseDns.class);
    }

    public ForwardDns forwardDns(String hostname) {
        return get("/api/v1/dns/forward/" + encode(hostname), ForwardDns.class);
    }

    public MxLookup mxRecords(String domain) {
        return get("/api/v1/dns/mx/" + encode(domain), MxLookup.class);
    }

    public DomainAge domainAge(String domain) {
        return get("/api/v1/domain/age/" + encode(domain), DomainAge.class);
    }

    public BatchDomainAgeResponse domainAgeBatch(List<String> domains) {
        if (domains.isEmpty()) {
            throw new IllegalArgumentException("domains must not be empty");
        }
        return post("/api/v1/domain/age/batch", Map.of("domains", domains),
                BatchDomainAgeResponse.class);
    }

    // -- Account ----------------------------------------------------------------

    public RateLimitInfo rateLimit() {
        return get("/api/v1/ratelimit", RateLimitInfo.class);
    }

    public UsageSummary usageSummary() {
        return get("/api/v1/usage/summary", UsageSummary.class);
    }

    // -- Internals ------------------------------------------------------------

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private static void checkBatch(List<String> items, String name) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException(name + " must not be empty");
        }
        if (items.size() > MAX_BATCH_SIZE) {
            throw new IllegalArgumentException(
                    name + " must contain at most " + MAX_BATCH_SIZE + " items");
        }
    }

    private <T> T get(String path, Class<T> type) {
        return request("GET", path, null, type);
    }

    private <T> T post(String path, Object body, Class<T> type) {
        return request("POST", path, body, type);
    }

    private <T> T request(String method, String path, Object body, Class<T> type) {
        String url = baseUrl + path;
        if (apiKey != null) {
            url += "?api_key=" + encode(apiKey);
        }

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "application/json");
        if (body != null) {
            String payload = serialize(body);
            requestBuilder
                    .header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(payload));
        } else {
            requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());
        }

        HttpResponse<String> response;
        try {
            response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new IpApiException("transport error: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IpApiException("request interrupted", e);
        }

        int status = response.statusCode();
        if (status < 200 || status >= 300) {
            throw errorFrom(response);
        }
        try {
            return objectMapper.readValue(response.body(), type);
        } catch (IOException e) {
            throw new IpApiException("failed to decode response: " + e.getMessage(), e);
        }
    }

    private String serialize(Object body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (IOException e) {
            throw new IpApiException("failed to encode request body: " + e.getMessage(), e);
        }
    }

    private IpApiException errorFrom(HttpResponse<String> response) {
        int status = response.statusCode();
        String body = response.body() == null ? "" : response.body();
        String message = extractMessage(status, body);

        if (status == 401 || status == 403) {
            return new AuthenticationException(message, status, body);
        }
        if (status == 429) {
            return new RateLimitException(
                    message,
                    body,
                    headerLong(response, "x-ratelimit-limit"),
                    headerLong(response, "x-ratelimit-remaining"),
                    headerLong(response, "x-ratelimit-reset"));
        }
        if (status == 400 || status == 404 || status == 422) {
            return new InvalidRequestException(message, status, body);
        }
        if (status >= 500) {
            return new ServerException(message, status, body);
        }
        return new IpApiException(message, status, body);
    }

    private String extractMessage(int status, String body) {
        String message = "";
        try {
            var parsed = objectMapper.readTree(body);
            if (parsed.isObject()) {
                if (parsed.hasNonNull("message")) {
                    message = parsed.get("message").asText();
                } else if (parsed.hasNonNull("error")) {
                    message = parsed.get("error").asText();
                }
            }
        } catch (IOException e) {
            message = body.strip();
            if (message.length() > 200) {
                message = message.substring(0, 200);
            }
        }
        return message.isEmpty() ? "HTTP " + status + " from ip-api.io" : message;
    }

    private static Long headerLong(HttpResponse<String> response, String name) {
        Optional<String> value = response.headers().firstValue(name);
        try {
            return value.map(Long::parseLong).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
