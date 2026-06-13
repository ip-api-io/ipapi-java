package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TorDetection(
        @JsonProperty("ip") String ip,
        @JsonProperty("is_tor") boolean isTor,
        @JsonProperty("tor_node_count") int torNodeCount) {
}
