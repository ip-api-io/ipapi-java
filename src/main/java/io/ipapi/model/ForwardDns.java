package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForwardDns(
        @JsonProperty("hostname") String hostname,
        @JsonProperty("addresses") List<ForwardLookupRecord> addresses) {
}
