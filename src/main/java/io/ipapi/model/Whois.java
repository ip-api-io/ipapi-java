package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Whois(
        @JsonProperty("domain") String domain,
        @JsonProperty("registrar") WhoisRegistrar registrar,
        @JsonProperty("registered_on") String registeredOn,
        @JsonProperty("expires_on") String expiresOn,
        @JsonProperty("updated_on") String updatedOn,
        @JsonProperty("name_servers") List<String> nameServers,
        @JsonProperty("status") List<WhoisStatus> status,
        @JsonProperty("raw") String raw,
        @JsonProperty("error") String error) {
}
