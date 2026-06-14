# VPN, proxy & Tor detection

Catch traffic that hides behind anonymizers. Every `lookup` already returns the
`suspiciousFactors` flags for proxy, VPN, Tor, datacenter, spam and crawler; the
dedicated `torCheck` adds live Tor exit-node confirmation.

Powers [VPN detection](https://ip-api.io/vpn-detection-api),
[proxy detection](https://ip-api.io/proxy-detection-api) and
[Tor detection](https://ip-api.io/tor-detection).

## `suspiciousFactors()` — flags on every lookup

No extra call needed: read the flags from a normal [`lookup`](ip-geolocation.md).

```java
import io.ipapi.IpApiClient;
import io.ipapi.model.IpInfo;
import io.ipapi.model.SuspiciousFactors;

IpApiClient client = IpApiClient.builder().apiKey("YOUR_API_KEY").build();

IpInfo info = client.lookup("185.220.101.1");
SuspiciousFactors f = info.suspiciousFactors();

System.out.println(f.isVpn());         // VPN service
System.out.println(f.isProxy());       // open / anonymizing proxy
System.out.println(f.isTorNode());     // Tor node
System.out.println(f.isDatacenter());  // hosting / datacenter IP (often a bot)
System.out.println(f.isSpam());        // known spam source
System.out.println(f.isCrawler());     // known crawler / bot
System.out.println(f.isThreat());      // listed on a threat feed

if (f.isVpn() || f.isProxy() || f.isTorNode()) {
    // require step-up verification
}
```

### `SuspiciousFactors`

| Accessor | Type | Meaning |
|---|---|---|
| `isProxy()` | `boolean` | Open or anonymizing proxy |
| `isVpn()` | `boolean` | Commercial VPN endpoint |
| `isTorNode()` | `boolean` | Part of the Tor network |
| `isDatacenter()` | `boolean` | Hosting / datacenter range |
| `isSpam()` | `boolean` | Known spam source |
| `isCrawler()` | `boolean` | Known crawler / bot |
| `isThreat()` | `boolean` | Listed on a threat feed |

## `torCheck(ip)` — confirm a Tor exit node

A dedicated check against the live Tor node list, with a count of matching nodes.

```java
import io.ipapi.model.TorDetection;

TorDetection tor = client.torCheck("185.220.101.1");

System.out.println(tor.isTor());        // true
System.out.println(tor.torNodeCount()); // number of matching Tor nodes
```

### Response (`TorDetection`)

| Accessor | Type | Description |
|---|---|---|
| `ip()` | `String` | The checked IP |
| `isTor()` | `boolean` | Whether the IP is a Tor node |
| `torNodeCount()` | `int` | Matching nodes for the IP |

> Want one number instead of individual flags? See
> [Fraud detection & risk scoring](fraud-risk-scoring.md) — `riskScore` folds all of
> these signals into a 0–100 score.

## See also

- [IP geolocation & bulk lookup](ip-geolocation.md) — where `suspiciousFactors()` comes from
- [Fraud detection & risk scoring](fraud-risk-scoring.md) — combine the flags into a score
- Product pages: [VPN detection](https://ip-api.io/vpn-detection-api) · [Proxy detection](https://ip-api.io/proxy-detection-api) · [Tor detection](https://ip-api.io/tor-detection)
