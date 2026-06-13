package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiLimitInfo(
        @JsonProperty("limit") long limit,
        @JsonProperty("remaining") long remaining,
        @JsonProperty("used") long used,
        @JsonProperty("usage_percent") double usagePercent) {
}
