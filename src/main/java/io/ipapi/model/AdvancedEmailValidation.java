package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AdvancedEmailValidation(
        @JsonProperty("email") String email,
        @JsonProperty("reachable") String reachable,
        @JsonProperty("syntax") AdvancedSyntax syntax,
        @JsonProperty("smtp") AdvancedSmtp smtp,
        @JsonProperty("gravatar") AdvancedGravatar gravatar,
        @JsonProperty("suggestion") String suggestion,
        @JsonProperty("disposable") boolean disposable,
        @JsonProperty("role_account") boolean roleAccount,
        @JsonProperty("free") boolean free,
        @JsonProperty("has_mx_records") boolean hasMxRecords) {
}
