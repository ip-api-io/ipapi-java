package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RiskScoreFactors(
        @JsonProperty("ip_factors") IpFactors ipFactors,
        @JsonProperty("email_factors") EmailFactors emailFactors) {
}
