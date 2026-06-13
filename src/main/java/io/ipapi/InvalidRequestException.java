package io.ipapi;

/** HTTP 400/404/422 — malformed input or unknown resource. */
public class InvalidRequestException extends IpApiException {
    public InvalidRequestException(String message, int statusCode, String body) {
        super(message, statusCode, body);
    }
}
