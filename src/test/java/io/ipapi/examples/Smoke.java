package io.ipapi.examples;

import io.ipapi.IpApiClient;
import io.ipapi.model.IpInfo;
import io.ipapi.model.RateLimitInfo;

/**
 * Live smoke test against https://ip-api.io.
 * Usage: IPAPI_API_KEY=... mvn -q test-compile exec:java -Dexec.mainClass=io.ipapi.examples.Smoke -Dexec.classpathScope=test
 * The API requires a key; without IPAPI_API_KEY this program skips.
 */
public final class Smoke {
    private Smoke() {
    }

    public static void main(String[] args) {
        String apiKey = System.getenv("IPAPI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("SKIPPED: set IPAPI_API_KEY to run the live smoke test");
            return;
        }

        IpApiClient client = IpApiClient.builder().apiKey(apiKey).build();

        IpInfo info = client.lookup("8.8.8.8");
        if (!"8.8.8.8".equals(info.ip())) {
            throw new IllegalStateException("unexpected response: " + info);
        }
        System.out.printf("lookup(8.8.8.8): %s / %s%n", info.location().country(), info.asn());

        RateLimitInfo rateLimit = client.rateLimit();
        System.out.printf("rate_limit: plan=%s ip_api remaining=%d%n",
                rateLimit.planId(), rateLimit.ipApi().remaining());

        System.out.println("smoke OK");
    }
}
