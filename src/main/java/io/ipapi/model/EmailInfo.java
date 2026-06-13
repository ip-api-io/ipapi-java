package io.ipapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmailInfo(
        @JsonProperty("email") String email,
        @JsonProperty("is_disposable") boolean isDisposable,
        @JsonProperty("has_mx_records") boolean hasMxRecords,
        @JsonProperty("mx_records") List<MxRecord> mxRecords,
        @JsonProperty("syntax") EmailSyntax syntax) {
}
