package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReverseDns(
        @JsonProperty("ip") String ip,
        @JsonProperty("hostname") String hostname,
        @JsonProperty("ptr_record") String ptrRecord,
        @JsonProperty("ttl") Long ttl) {
}
