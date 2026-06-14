# Fraud detection & risk scoring

Collapse every signal — geolocation, proxy/VPN/Tor flags, datacenter hosting,
disposable email, syntax — into a single 0–100 risk score you can act on at sign-up,
checkout or login. Or pull the raw [IP reputation](https://ip-api.io/ip-reputation)
record when you want to build your own rules.

Powers the [fraud detection API](https://ip-api.io/fraud-detection-api),
[risk score](https://ip-api.io/risk-score) and
[IP reputation](https://ip-api.io/ip-reputation) products.

## `riskScore(ip)` / `riskScore()` — score an IP

Returns a `score()` (0–100) and a human `riskLevel()`, plus the `factors()` that drove
it. The no-arg `riskScore()` scores the caller's own IP.

```java
import io.ipapi.IpApiClient;
import io.ipapi.model.RiskScore;

IpApiClient client = IpApiClient.builder().apiKey("YOUR_API_KEY").build();

RiskScore risk = client.riskScore("185.220.101.1");

System.out.println(risk.score());        // 88.0
System.out.println(risk.riskLevel());    // "high"
if (risk.factors().ipFactors() != null) {
    System.out.println(risk.factors().ipFactors().isTorNode());
    System.out.println(risk.factors().ipFactors().isDatacenter());
}

if (risk.score() >= 75) {
    // block, or send to manual review / step-up auth
}
```

### Response (`RiskScore`)

| Accessor | Type | Description |
|---|---|---|
| `score()` | `double` | Risk score, 0 (safe) – 100 (high risk) |
| `riskLevel()` | `String` | Bucketed level, e.g. `"low"`, `"medium"`, `"high"` |
| `ip()` | `String` | Scored IP (when applicable) |
| `email()` | `String` | Scored email (when applicable) |
| `factors()` | `RiskScoreFactors` | `ipFactors()` and/or `emailFactors()` (may be null) |

`IpFactors`: `isProxy()`, `isVpn()`, `isTorNode()`, `isSpam()`, `isDatacenter()`,
`riskContribution()`.
`EmailFactors`: `isDisposable()`, `isValidSyntax()`, `riskContribution()`.

## `emailRiskScore(email)` — score an email

Same 0–100 scale, driven by email signals (disposable provider, invalid syntax).
Use it to grade leads or gate sign-ups by address quality.

```java
RiskScore risk = client.emailRiskScore("user@mailinator.com");

System.out.println(risk.score() + " " + risk.riskLevel()); // 90.0 high
if (risk.factors().emailFactors() != null) {
    System.out.println(risk.factors().emailFactors().isDisposable()); // true
}
```

## `ipReputation(ip)` — raw reputation record

Returns the underlying reputation data for an IP as a `Map<String, Object>` — use it
when you want the source signals rather than a computed score.

```java
import java.util.Map;

Map<String, Object> reputation = client.ipReputation("185.220.101.1");
System.out.println(reputation);
```

## See also

- [IP geolocation & bulk lookup](ip-geolocation.md) — `suspiciousFactors()` per IP
- [VPN, proxy & Tor detection](vpn-proxy-tor.md) — the individual checks behind the score
- [Email validation & verification](email-validation.md) — deliverability before scoring
- Product pages: [Fraud detection](https://ip-api.io/fraud-detection-api) · [Risk score](https://ip-api.io/risk-score) · [IP reputation](https://ip-api.io/ip-reputation)
