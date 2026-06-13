package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Location(
        @JsonProperty("country") String country,
        @JsonProperty("country_code") String countryCode,
        @JsonProperty("city") String city,
        @JsonProperty("latitude") Double latitude,
        @JsonProperty("longitude") Double longitude,
        @JsonProperty("zip") String zip,
        @JsonProperty("timezone") String timezone,
        @JsonProperty("local_time") String localTime,
        @JsonProperty("local_time_unix") Long localTimeUnix,
        @JsonProperty("is_daylight_savings") Boolean isDaylightSavings) {
}
