package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AsnLookup(
        @JsonProperty("ip") String ip,
        @JsonProperty("asn") Long asn,
        @JsonProperty("organization") String organization,
        @JsonProperty("network") String network,
        @JsonProperty("is_datacenter") boolean isDatacenter,
        @JsonProperty("country") String country,
        @JsonProperty("country_code") String countryCode) {
}
