package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MxRecord(
        @JsonProperty("priority") int priority,
        @JsonProperty("hostname") String hostname,
        @JsonProperty("ttl") long ttl) {
}
