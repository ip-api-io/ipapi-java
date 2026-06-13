package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IpFactors(
        @JsonProperty("is_proxy") boolean isProxy,
        @JsonProperty("is_tor_node") boolean isTorNode,
        @JsonProperty("is_spam") boolean isSpam,
        @JsonProperty("is_vpn") boolean isVpn,
        @JsonProperty("is_datacenter") boolean isDatacenter,
        @JsonProperty("risk_contribution") double riskContribution) {
}
