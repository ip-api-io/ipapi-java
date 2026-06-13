package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AdvancedSmtp(
        @JsonProperty("host_exists") boolean hostExists,
        @JsonProperty("full_inbox") boolean fullInbox,
        @JsonProperty("catch_all") boolean catchAll,
        @JsonProperty("deliverable") boolean deliverable,
        @JsonProperty("disabled") boolean disabled) {
}
