package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AdvancedGravatar(
        @JsonProperty("has_gravatar") boolean hasGravatar,
        @JsonProperty("gravatar_url") String gravatarUrl) {
}
