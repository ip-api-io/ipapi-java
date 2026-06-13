package io.ipapi;

/** Base exception for all ip-api.io client failures. */
public class IpApiException extends RuntimeException {
    private final Integer statusCode;
    private final String body;

    public IpApiException(String message, Integer statusCode, String body) {
        super(message);
        this.statusCode = statusCode;
        this.body = body;
    }

    public IpApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
        this.body = null;
    }

    /** HTTP status code, or null for transport-level failures. */
    public Integer getStatusCode() {
        return statusCode;
    }

    /** Raw response body, when available. */
    public String getBody() {
        return body;
    }
}
