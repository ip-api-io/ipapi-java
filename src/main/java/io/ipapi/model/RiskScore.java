package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RiskScore(
        @JsonProperty("score") double score,
        @JsonProperty("risk_level") String riskLevel,
        @JsonProperty("ip") String ip,
        @JsonProperty("email") String email,
        @JsonProperty("factors") RiskScoreFactors factors) {
}
