package io.ipapi;

/**
 * HTTP 429 — quota exhausted. Exposes the x-ratelimit-* response headers;
 * {@link #getReset()} is the unix timestamp when the quota renews.
 * The client never retries.
 */
public class RateLimitException extends IpApiException {
    private final Long limit;
    private final Long remaining;
    private final Long reset;

    public RateLimitException(String message, String body, Long limit, Long remaining, Long reset) {
        super(message, 429, body);
        this.limit = limit;
        this.remaining = remaining;
        this.reset = reset;
    }

    public Long getLimit() {
        return limit;
    }

    public Long getRemaining() {
        return remaining;
    }

    public Long getReset() {
        return reset;
    }
}
