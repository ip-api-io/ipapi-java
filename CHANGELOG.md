# Changelog

All notable changes to this project will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.0.0] - 2026-06-12

### Added
- Initial release: full client for the ip-api.io v1 API — IP geolocation and
  threat intelligence (single + batch), email validation (basic, advanced,
  batch), risk scoring, IP reputation, Tor detection, ASN lookup, WHOIS,
  reverse/forward DNS, MX records, domain age (single + batch), rate limit
  and usage info.
- Typed record models, builder-based client on `java.net.http.HttpClient`
  (Java 17+), Jackson as the only dependency.
- Typed exceptions (`AuthenticationException`, `RateLimitException` with
  x-ratelimit header values, `InvalidRequestException`, `ServerException`).
