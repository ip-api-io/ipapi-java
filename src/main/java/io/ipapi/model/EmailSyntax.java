package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmailSyntax(
        @JsonProperty("domain") String domain,
        @JsonProperty("username") String username,
        @JsonProperty("is_valid") boolean isValid,
        @JsonProperty("error_reasons") List<String> errorReasons) {
}
