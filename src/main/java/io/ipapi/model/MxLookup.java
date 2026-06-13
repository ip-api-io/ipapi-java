package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MxLookup(
        @JsonProperty("domain") String domain,
        @JsonProperty("mx_records") List<MxRecord> mxRecords) {
}
