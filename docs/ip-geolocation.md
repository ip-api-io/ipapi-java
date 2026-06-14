# IP geolocation & bulk lookup

Turn any IP address into geolocation, network and threat intelligence. A single
`lookup` returns the country, city, coordinates, timezone, ISP and ASN of an IP,
plus the `suspiciousFactors` flags used for fraud screening (proxy, VPN, Tor,
datacenter, spam, crawler, threat).

Powers the [IP geolocation API](https://ip-api.io/what-is-my-ip) and the
[bulk IP lookup](https://ip-api.io/bulk-ip-lookup) product.

## `lookup(ip)` / `lookup()` — geolocate one IP

`lookup(ip)` geolocates a specific address; the no-arg `lookup()` geolocates the
caller's own IP. Responses are immutable records — read fields via accessor methods.

```java
import io.ipapi.IpApiClient;
import io.ipapi.model.IpInfo;

IpApiClient client = IpApiClient.builder().apiKey("YOUR_API_KEY").build();

IpInfo info = client.lookup("8.8.8.8");

System.out.println(info.ip());                       // "8.8.8.8"
System.out.println(info.isp());                      // "Google LLC"
System.out.println(info.location().country());       // "United States"
System.out.println(info.location().city());          // "Mountain View"
System.out.println(info.location().timezone());      // "America/Los_Angeles"
System.out.println(info.suspiciousFactors().isDatacenter()); // true

// Geolocate the machine making the request
IpInfo me = client.lookup();
System.out.println(me.ip());
```

### Response (`IpInfo`)

| Accessor | Type | Description |
|---|---|---|
| `ip()` | `String` | The looked-up address |
| `isp()` | `String` | Internet service provider |
| `asn()` | `String` | Autonomous system the IP belongs to |
| `location()` | `Location` | `country()`, `countryCode()`, `city()`, `latitude()`, `longitude()`, `zip()`, `timezone()`, `localTime()`, `localTimeUnix()`, `isDaylightSavings()` |
| `suspiciousFactors()` | `SuspiciousFactors` | `isProxy()`, `isVpn()`, `isTorNode()`, `isDatacenter()`, `isSpam()`, `isCrawler()`, `isThreat()` |

> The `suspiciousFactors()` block is the fastest way to flag risky traffic in one call.
> For a single 0–100 score, see [Fraud detection & risk scoring](fraud-risk-scoring.md);
> for the individual checks, see [VPN, proxy & Tor detection](vpn-proxy-tor.md).

## `lookupBatch(ips)` — geolocate up to 100 IPs

Look up to 100 addresses in one request — ideal for enriching logs, sign-up events or
historical data without a round trip per IP. Throws `IllegalArgumentException` if the
list is empty or longer than 100.

```java
import io.ipapi.model.BatchIpLookupResponse;
import java.util.List;

BatchIpLookupResponse batch = client.lookupBatch(List.of("8.8.8.8", "1.1.1.1", "9.9.9.9"));

System.out.println(batch.totalProcessed());    // 3
System.out.println(batch.successfulLookups());  // 3
System.out.println(batch.failedLookups());      // 0

batch.results().forEach((ip, info) ->
        System.out.println(ip + " " + info.suspiciousFactors().isVpn()));
```

### Response (`BatchIpLookupResponse`)

| Accessor | Type | Description |
|---|---|---|
| `results()` | `Map<String, IpInfo>` | Map of IP → info |
| `totalProcessed()` | `int` | IPs received |
| `successfulLookups()` | `int` | IPs resolved |
| `failedLookups()` | `int` | IPs that could not be resolved |

## See also

- [Fraud detection & risk scoring](fraud-risk-scoring.md) — turn the flags into a score
- [VPN, proxy & Tor detection](vpn-proxy-tor.md) — the individual threat checks
- [ASN & DNS lookups](asn-and-dns.md) — network ownership for an IP
- Product pages: [IP geolocation](https://ip-api.io/what-is-my-ip) · [Bulk IP lookup](https://ip-api.io/bulk-ip-lookup)
