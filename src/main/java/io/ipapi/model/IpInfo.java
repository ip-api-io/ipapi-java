package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IpInfo(
        @JsonProperty("ip") String ip,
        @JsonProperty("isp") String isp,
        @JsonProperty("asn") String asn,
        @JsonProperty("suspicious_factors") SuspiciousFactors suspiciousFactors,
        @JsonProperty("location") Location location) {
}
