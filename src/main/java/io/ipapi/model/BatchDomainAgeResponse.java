package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BatchDomainAgeResponse(
        @JsonProperty("results") Map<String, DomainAge> results) {
}
