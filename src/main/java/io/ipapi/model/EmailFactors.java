package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmailFactors(
        @JsonProperty("is_disposable") boolean isDisposable,
        @JsonProperty("is_valid_syntax") boolean isValidSyntax,
        @JsonProperty("risk_contribution") double riskContribution) {
}
