package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AdvancedSyntax(
        @JsonProperty("username") String username,
        @JsonProperty("domain") String domain,
        @JsonProperty("valid") boolean valid) {
}
