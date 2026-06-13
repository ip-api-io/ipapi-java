package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BatchEmailValidationResponse(
        @JsonProperty("results") Map<String, AdvancedEmailValidation> results,
        @JsonProperty("totalProcessed") int totalProcessed,
        @JsonProperty("successfulValidations") int successfulValidations,
        @JsonProperty("failedValidations") int failedValidations) {
}
