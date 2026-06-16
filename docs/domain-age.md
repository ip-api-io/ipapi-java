# Domain age checker

Newly registered domains are a strong fraud and spam signal. `domainAge` returns how
long ago a domain was registered, derived from WHOIS data, so you can flag or block
domains created days ago.

Powers the [domain age checker](https://ip-api.io/domain-age-checker).

## `domainAge(domain)` — age of one domain

```java
import io.ipapi.IpApiClient;
import io.ipapi.model.DomainAge;

IpApiClient client = IpApiClient.builder().apiKey("YOUR_API_KEY").build();

DomainAge age = client.domainAge("example.com");

System.out.println(age.isValid());          // true
System.out.println(age.registrationDate());  // "1995-08-14"
System.out.println(age.ageInYears());        // 30
System.out.println(age.ageInDays());         // 11000+

if (age.ageInDays() != null && age.ageInDays() < 30) {
    // treat brand-new domains as higher risk
}
```

### Response (`DomainAge`)

| Accessor | Type | Description |
|---|---|---|
| `domain()` | `String` | The domain checked |
| `isValid()` | `boolean` | Whether age could be determined |
| `registrationDate()` | `String` | First registration date |
| `ageInYears()` | `Integer` | Age in whole years |
| `ageInDays()` | `Long` | Age in days |
| `error()` | `String` | Reason when `isValid()` is false |

## `domainAgeBatch(domains)` — many domains at once

Check a list of domains in one request (non-empty; throws
`IllegalArgumentException` if empty).

```java
import io.ipapi.model.BatchDomainAgeResponse;
import java.util.List;

BatchDomainAgeResponse batch = client.domainAgeBatch(
        List.of("example.com", "brand-new-domain.xyz"));

batch.results().forEach((domain, age) ->
        System.out.println(domain + " " + age.ageInDays()));
```

### Response (`BatchDomainAgeResponse`)
`results()` — a `Map<String, DomainAge>` mapping each domain to its age.

## See also

- [ASN & DNS lookups](asn-and-dns.md) — `whois` for the full registration record
- [Fraud detection & risk scoring](fraud-risk-scoring.md) — combine age with other signals
- Product page: [Domain age checker](https://ip-api.io/domain-age-checker)
- [Full tutorial on ip-api.io](https://ip-api.io/docs/sdk/java/domain-age)
