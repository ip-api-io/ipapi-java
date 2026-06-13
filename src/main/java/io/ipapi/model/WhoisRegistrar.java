package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WhoisRegistrar(
        @JsonProperty("name") String name,
        @JsonProperty("url") String url,
        @JsonProperty("iana_id") String ianaId) {
}
