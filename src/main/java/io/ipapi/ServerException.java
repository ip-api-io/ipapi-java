package io.ipapi;

/** HTTP 5xx — ip-api.io server-side failure. */
public class ServerException extends IpApiException {
    public ServerException(String message, int statusCode, String body) {
        super(message, statusCode, body);
    }
}
