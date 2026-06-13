package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DomainAge(
        @JsonProperty("domain") String domain,
        @JsonProperty("is_valid") boolean isValid,
        @JsonProperty("registration_date") String registrationDate,
        @JsonProperty("age_in_years") Integer ageInYears,
        @JsonProperty("age_in_days") Long ageInDays,
        @JsonProperty("error") String error) {
}
