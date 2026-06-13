package io.ipapi;

/** HTTP 401/403 — missing or invalid API key. */
public class AuthenticationException extends IpApiException {
    public AuthenticationException(String message, int statusCode, String body) {
        super(message, statusCode, body);
    }
}
