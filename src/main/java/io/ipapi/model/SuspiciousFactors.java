package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SuspiciousFactors(
        @JsonProperty("is_proxy") boolean isProxy,
        @JsonProperty("is_tor_node") boolean isTorNode,
        @JsonProperty("is_spam") boolean isSpam,
        @JsonProperty("is_crawler") boolean isCrawler,
        @JsonProperty("is_datacenter") boolean isDatacenter,
        @JsonProperty("is_vpn") boolean isVpn,
        @JsonProperty("is_threat") boolean isThreat) {
}
