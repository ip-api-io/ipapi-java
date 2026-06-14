# ASN & DNS lookups

Resolve the network and DNS layer behind an IP or domain: which autonomous system
owns an address, who registered a domain, what a host's PTR record is, and which mail
servers a domain uses.

Powers [ASN lookup](https://ip-api.io/asn-lookup),
[WHOIS lookup](https://ip-api.io/whois-lookup),
[reverse DNS](https://ip-api.io/reverse-dns-lookup) and
[MX record lookup](https://ip-api.io/mx-record-lookup).

## `asn(ip)` — autonomous system for an IP

Returns the ASN, owning organization, network range and country for an IP — and
whether it belongs to a datacenter.

```java
import io.ipapi.IpApiClient;
import io.ipapi.model.AsnLookup;

IpApiClient client = IpApiClient.builder().apiKey("YOUR_API_KEY").build();

AsnLookup asn = client.asn("8.8.8.8");

System.out.println(asn.asn());          // 15169
System.out.println(asn.organization()); // "Google LLC"
System.out.println(asn.network());      // "8.8.8.0/24"
System.out.println(asn.isDatacenter()); // true
System.out.println(asn.countryCode());  // "US"
```

### Response (`AsnLookup`)
`ip()`, `asn()`, `organization()`, `network()`, `isDatacenter()`, `country()`,
`countryCode()`.

## `whois(domain)` — domain registration

WHOIS record for a domain: registrar, registration/expiry/update dates, name servers,
status codes and the raw WHOIS text.

```java
import io.ipapi.model.Whois;

Whois whois = client.whois("example.com");

if (whois.registrar() != null) {
    System.out.println(whois.registrar().name());
}
System.out.println(whois.registeredOn());  // "1995-08-14"
System.out.println(whois.nameServers());
```

### Response (`Whois`)
`domain()`, `registrar()` (`name()`, `url()`, `ianaId()`), `registeredOn()`,
`expiresOn()`, `updatedOn()`, `nameServers()`, `status()` (`code()`, `humanized()`),
`raw()`, `error()`.

## `reverseDns(ip)` — PTR record for an IP

```java
import io.ipapi.model.ReverseDns;

ReverseDns rdns = client.reverseDns("8.8.8.8");

System.out.println(rdns.hostname());   // "dns.google"
System.out.println(rdns.ptrRecord());
```

### Response (`ReverseDns`)
`ip()`, `hostname()`, `ptrRecord()`, `ttl()`.

## `forwardDns(hostname)` — resolve a hostname to addresses

```java
import io.ipapi.model.ForwardDns;

ForwardDns fdns = client.forwardDns("dns.google");

fdns.addresses().forEach(record ->
        System.out.println(record.type() + " " + record.address() + " " + record.ttl()));
```

### Response (`ForwardDns`)
`hostname()`, `addresses()` (each `type()`, `address()`, `ttl()`).

## `mxRecords(domain)` — mail servers for a domain

```java
import io.ipapi.model.MxLookup;

MxLookup mx = client.mxRecords("example.com");

mx.mxRecords().forEach(record ->
        System.out.println(record.priority() + " " + record.hostname()));
```

### Response (`MxLookup`)
`domain()`, `mxRecords()` (each `priority()`, `hostname()`, `ttl()`).

## See also

- [IP geolocation & bulk lookup](ip-geolocation.md) — geolocation for the same IP
- [Email validation & verification](email-validation.md) — MX records feed deliverability
- [Domain age checker](domain-age.md) — registration age from WHOIS data
- Product pages: [ASN lookup](https://ip-api.io/asn-lookup) · [WHOIS lookup](https://ip-api.io/whois-lookup) · [Reverse DNS](https://ip-api.io/reverse-dns-lookup) · [MX record lookup](https://ip-api.io/mx-record-lookup)
