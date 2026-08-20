package com.tmw.tracking.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory brute-force guard for the login endpoint: after too many failed attempts for a
 * given credential (phone/email), further attempts are rejected for a lockout window even if
 * the password is correct. State is per-JVM and resets on restart — acceptable for a
 * single-instance deployment; a multi-instance deployment would need this backed by a shared
 * store (e.g. the database or a cache) instead.
 */
public final class LoginAttemptTracker {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCKOUT_MILLIS = 15 * 60 * 1000L;

    private static final ConcurrentHashMap<String, Attempt> ATTEMPTS = new ConcurrentHashMap<String, Attempt>();

    private LoginAttemptTracker() {
        throw new UnsupportedOperationException("Empty constructor is not supported.");
    }

    public static boolean isLocked(final String credentials) {
        final Attempt attempt = ATTEMPTS.get(normalize(credentials));
        return attempt != null && attempt.lockedUntil > System.currentTimeMillis();
    }

    /**
     * @return remaining lockout time in whole minutes (rounded up), or 0 if not locked.
     */
    public static long lockedForMinutes(final String credentials) {
        final Attempt attempt = ATTEMPTS.get(normalize(credentials));
        if (attempt == null) {
            return 0;
        }
        final long remainingMillis = attempt.lockedUntil - System.currentTimeMillis();
        return remainingMillis > 0 ? (remainingMillis + 59_999) / 60_000 : 0;
    }

    public static void recordFailure(final String credentials) {
        final String key = normalize(credentials);
        ATTEMPTS.compute(key, (k, existing) -> {
            final Attempt attempt = (existing != null && existing.lockedUntil <= System.currentTimeMillis())
                    ? new Attempt() : (existing != null ? existing : new Attempt());
            attempt.count++;
            if (attempt.count >= MAX_ATTEMPTS) {
                attempt.lockedUntil = System.currentTimeMillis() + LOCKOUT_MILLIS;
            }
            return attempt;
        });
    }

    public static void recordSuccess(final String credentials) {
        ATTEMPTS.remove(normalize(credentials));
    }

    private static String normalize(final String credentials) {
        return credentials == null ? "" : credentials.trim().toLowerCase();
    }

    private static final class Attempt {
        int count;
        long lockedUntil;
    }
}
