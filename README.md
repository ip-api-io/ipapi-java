# ipapi — Official Java client for [ip-api.io](https://ip-api.io)

[![Maven Central](https://img.shields.io/maven-central/v/io.ip-api/ipapi)](https://central.sonatype.com/artifact/io.ip-api/ipapi)
[![test](https://github.com/ip-api-io/ipapi-java/actions/workflows/test.yml/badge.svg)](https://github.com/ip-api-io/ipapi-java/actions/workflows/test.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

The official Java client for the [ip-api.io](https://ip-api.io) IP intelligence
platform. One client covers [IP geolocation](https://ip-api.io/what-is-my-ip),
[email validation](https://ip-api.io/email-validation) and [verification](https://ip-api.io/email-verification-api)
(syntax, MX, SMTP deliverability), [fraud detection](https://ip-api.io/fraud-detection-api)
and [risk scoring](https://ip-api.io/risk-score),
[VPN](https://ip-api.io/vpn-detection-api)/[proxy](https://ip-api.io/proxy-detection-api)/[Tor detection](https://ip-api.io/tor-detection),
[disposable email detection](https://ip-api.io/disposable-email-checker), [ASN lookup](https://ip-api.io/asn-lookup),
[WHOIS](https://ip-api.io/whois-lookup), [reverse DNS](https://ip-api.io/reverse-dns-lookup),
[MX records](https://ip-api.io/mx-record-lookup) and [domain age](https://ip-api.io/domain-age-checker).

Built on `java.net.http.HttpClient` (Java 17+) with Jackson as the only dependency.

## Install

```xml
<dependency>
    <groupId>io.ip-api</groupId>
    <artifactId>ipapi</artifactId>
    <version>1.0.0</version>
</dependency>
```

Gradle: `implementation("io.ip-api:ipapi:1.0.0")`

## Quickstart

```java
import io.ipapi.IpApiClient;
import io.ipapi.model.IpInfo;
import io.ipapi.model.RiskScore;

IpApiClient client = IpApiClient.builder()
        .apiKey("YOUR_API_KEY") // free key at https://ip-api.io
        .build();

// Where is this IP, and is it risky?
IpInfo info = client.lookup("8.8.8.8");
System.out.println(info.location().country());        // "United States"
System.out.println(info.suspiciousFactors().isVpn()); // false

RiskScore risk = client.riskScore("8.8.8.8");
System.out.println(risk.score() + " " + risk.riskLevel()); // 0.0 low
```

An API key is required — the API rejects keyless requests with `401`. Sign up at
[ip-api.io](https://ip-api.io) for a free key.

## Documentation

Each guide documents the methods for one capability, with runnable examples and a link
to the matching ip-api.io product page:

- **[IP geolocation & bulk lookup](docs/ip-geolocation.md)** — `lookup`, `lookupBatch`
- **[Email validation & verification](docs/email-validation.md)** — `emailInfo`, `validateEmail`, `validateEmailBatch`
- **[Fraud detection & risk scoring](docs/fraud-risk-scoring.md)** — `riskScore`, `emailRiskScore`, `ipReputation`
- **[VPN, proxy & Tor detection](docs/vpn-proxy-tor.md)** — `torCheck`, `suspiciousFactors`
- **[ASN & DNS lookups](docs/asn-and-dns.md)** — `asn`, `whois`, `reverseDns`, `forwardDns`, `mxRecords`
- **[Domain age checker](docs/domain-age.md)** — `domainAge`, `domainAgeBatch`
- **[Errors, rate limits & usage](docs/error-handling.md)** — exception types, `rateLimit`, `usageSummary`

## Methods

Every method maps to one ip-api.io endpoint and its product page:

| Method | Endpoint | Product page |
|---|---|---|
| `lookup()` / `lookup(ip)` | `GET /api/v1/ip[/{ip}]` | [IP geolocation](https://ip-api.io/what-is-my-ip) |
| `lookupBatch(ips)` | `POST /api/v1/ip/batch` (≤100 IPs) | [Bulk IP lookup](https://ip-api.io/bulk-ip-lookup) |
| `emailInfo(email)` | `GET /api/v1/email/{email}` | [Email validation](https://ip-api.io/email-validation) |
| `validateEmail(email)` | `GET /api/v1/email/advanced/{email}` | [Advanced email validation](https://ip-api.io/advanced-email-validation) |
| `validateEmailBatch(emails)` | `POST /api/v1/email/advanced/batch` (≤100) | [Email list cleaning](https://ip-api.io/email-list-cleaning) |
| `riskScore()` / `riskScore(ip)` | `GET /api/v1/risk-score[/{ip}]` | [Risk score](https://ip-api.io/risk-score) |
| `emailRiskScore(email)` | `GET /api/v1/risk-score/email/{email}` | [Fraud detection](https://ip-api.io/fraud-detection-api) |
| `ipReputation(ip)` | `GET /api/v1/ip-reputation/{ip}` | [IP reputation](https://ip-api.io/ip-reputation) |
| `torCheck(ip)` | `GET /api/v1/tor/{ip}` | [Tor detection](https://ip-api.io/tor-detection) |
| `asn(ip)` | `GET /api/v1/asn/{ip}` | [ASN lookup](https://ip-api.io/asn-lookup) |
| `whois(domain)` | `GET /api/v1/dns/whois/{domain}` | [WHOIS lookup](https://ip-api.io/whois-lookup) |
| `reverseDns(ip)` | `GET /api/v1/dns/reverse/{ip}` | [Reverse DNS](https://ip-api.io/reverse-dns-lookup) |
| `forwardDns(hostname)` | `GET /api/v1/dns/forward/{hostname}` | — |
| `mxRecords(domain)` | `GET /api/v1/dns/mx/{domain}` | [MX record lookup](https://ip-api.io/mx-record-lookup) |
| `domainAge(domain)` | `GET /api/v1/domain/age/{domain}` | [Domain age checker](https://ip-api.io/domain-age-checker) |
| `domainAgeBatch(domains)` | `POST /api/v1/domain/age/batch` | [Domain age checker](https://ip-api.io/domain-age-checker) |
| `rateLimit()` | `GET /api/v1/ratelimit` | — |
| `usageSummary()` | `GET /api/v1/usage/summary` | — |

All responses are typed records in `io.ipapi.model`. Nullable JSON fields map to
reference types that may be `null`.

## Error handling

The client throws typed unchecked exceptions and **never retries** —
`RateLimitException.getReset()` tells you when your quota renews:

```java
import io.ipapi.AuthenticationException;
import io.ipapi.RateLimitException;

try {
    client.lookup("8.8.8.8");
} catch (RateLimitException e) {
    System.out.printf("limit=%d remaining=%d resetsAt=%d%n",
            e.getLimit(), e.getRemaining(), e.getReset());
} catch (AuthenticationException e) {
    System.out.println("invalid API key");
}
```

See [docs/error-handling.md](docs/error-handling.md) for the full exception taxonomy.

## Links

- Website: https://ip-api.io
- API reference: https://ip-api.io/api-docs.html
- OpenAPI spec: https://ip-api.io/openapi.json
- Get a free API key: https://ip-api.io

---

`io.ip-api:ipapi` is the official client for [ip-api.io](https://ip-api.io).
It is not affiliated with ip-api.com or ipapi.com.
