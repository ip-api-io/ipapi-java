package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RateLimitInfo(
        @JsonProperty("plan_id") String planId,
        @JsonProperty("plan_name") String planName,
        @JsonProperty("ip_api") ApiLimitInfo ipApi,
        @JsonProperty("email_api") ApiLimitInfo emailApi,
        @JsonProperty("interval_seconds") long intervalSeconds,
        @JsonProperty("next_renewal_date") String nextRenewalDate,
        @JsonProperty("status") String status) {
}
