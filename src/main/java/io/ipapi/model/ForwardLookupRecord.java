package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForwardLookupRecord(
        @JsonProperty("type") String type,
        @JsonProperty("address") String address,
        @JsonProperty("ttl") long ttl) {
}
