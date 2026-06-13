package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BatchIpLookupResponse(
        @JsonProperty("results") Map<String, IpInfo> results,
        @JsonProperty("total_processed") int totalProcessed,
        @JsonProperty("successful_lookups") int successfulLookups,
        @JsonProperty("failed_lookups") int failedLookups) {
}
