package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WhoisStatus(
        @JsonProperty("code") String code,
        @JsonProperty("humanized") String humanized) {
}
