package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsageSummary(
        @JsonProperty("apiKey") String apiKey,
        @JsonProperty("apiType") String apiType,
        @JsonProperty("periodStart") String periodStart,
        @JsonProperty("periodEnd") String periodEnd,
        @JsonProperty("totalRequests") long totalRequests,
        @JsonProperty("successfulRequests") long successfulRequests,
        @JsonProperty("rateLimitedRequests") long rateLimitedRequests,
        @JsonProperty("quotaConsumed") long quotaConsumed,
        @JsonProperty("batchOperations") long batchOperations,
        @JsonProperty("avgRequestDurationMs") Double avgRequestDurationMs) {
}
