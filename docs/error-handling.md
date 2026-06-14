# Errors, rate limits & usage

The client throws a typed unchecked exception for every HTTP failure and **never
retries** — you stay in control of back-off. It also exposes your current quota so you
can throttle before you hit a limit.

## Exception taxonomy

Every exception extends `io.ipapi.IpApiException` (a `RuntimeException`), which exposes
`getStatusCode()` and `getBody()`. Catch the specific subclass you care about:

| Exception | HTTP status | Meaning |
|---|---|---|
| `AuthenticationException` | 401, 403 | Missing or invalid API key |
| `RateLimitException` | 429 | Quota exhausted (see below) |
| `InvalidRequestException` | 400, 404, 422 | Malformed input or unknown resource |
| `ServerException` | 5xx | ip-api.io server-side failure |
| `IpApiException` | other / transport | Base / fallback |

```java
import io.ipapi.IpApiException;
import io.ipapi.AuthenticationException;
import io.ipapi.RateLimitException;
import io.ipapi.InvalidRequestException;
import io.ipapi.ServerException;
import io.ipapi.model.IpInfo;

try {
    IpInfo info = client.lookup("8.8.8.8");
    System.out.println(info.location().country());
} catch (RateLimitException e) {
    System.out.println("quota hit — resets at " + e.getReset());
} catch (AuthenticationException e) {
    System.out.println("check your API key");
} catch (InvalidRequestException e) {
    System.out.println("bad request: " + e.getMessage());
} catch (ServerException e) {
    System.out.println("ip-api.io is having trouble, try later");
} catch (IpApiException e) {
    System.out.println("error " + e.getStatusCode() + ": " + e.getMessage());
}
```

Transport failures (DNS, connection, timeout, decode) are wrapped in `IpApiException`
with a `null` status code.

## Rate limits

On HTTP 429 the client throws `RateLimitException`, parsed from the `x-ratelimit-*`
response headers. Because the client never retries, **`getReset()` tells you when to**:

```java
try {
    client.lookup("8.8.8.8");
} catch (RateLimitException e) {
    System.out.println(e.getLimit());     // your quota for the window
    System.out.println(e.getRemaining()); // requests left (0 here)
    System.out.println(e.getReset());     // unix timestamp when quota renews
    // schedule a retry at getReset() instead of hammering the API
}
```

## `rateLimit()` — check quota proactively

Read your current limits without triggering a 429, so you can throttle in advance.

```java
import io.ipapi.model.RateLimitInfo;

RateLimitInfo rl = client.rateLimit();

System.out.println(rl.planName());
System.out.println(rl.ipApi().remaining() + " / " + rl.ipApi().limit());
System.out.println(rl.emailApi().usagePercent() + " % used");
System.out.println(rl.nextRenewalDate());
```

`RateLimitInfo`: `planId()`, `planName()`, `ipApi()` and `emailApi()`
(`ApiLimitInfo`: `limit()`, `remaining()`, `used()`, `usagePercent()`),
`intervalSeconds()`, `nextRenewalDate()`, `status()`.

## `usageSummary()` — account usage

Aggregate usage for the current period — handy for dashboards and internal alerts.

```java
import io.ipapi.model.UsageSummary;

UsageSummary usage = client.usageSummary();

System.out.println(usage.totalRequests() + " " + usage.successfulRequests());
System.out.println(usage.rateLimitedRequests() + " " + usage.quotaConsumed());
System.out.println(usage.periodStart() + " -> " + usage.periodEnd());
```

`UsageSummary`: `apiKey()`, `apiType()`, `periodStart()`, `periodEnd()`,
`totalRequests()`, `successfulRequests()`, `rateLimitedRequests()`, `quotaConsumed()`,
`batchOperations()`, `avgRequestDurationMs()`.

## See also

- [IP geolocation & bulk lookup](ip-geolocation.md) — the most common call
- API reference: https://ip-api.io/api-docs.html
- Get a free API key: https://ip-api.io
