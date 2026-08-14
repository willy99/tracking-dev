package com.tmw.tracking.service.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.web.service.exception.ServiceException;
import com.tmw.tracking.web.service.util.error.ErrorCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks failed login attempts per credentials (username/phone/email) and locks
 * the credentials out for a fixed duration after too many consecutive failures.
 * In-memory only: state is per-instance and resets on restart. Fine for the
 * current single-instance deployment; revisit with a shared store if the app
 * is ever scaled to multiple instances behind a load balancer.
 */
@Singleton
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_DURATION_MILLIS = 15 * 60 * 1000L;

    private static class Attempt {
        int failCount;
        long lockedUntil;
    }

    private final Map<String, Attempt> attemptsByCredentials = new ConcurrentHashMap<>();

    public void checkNotLocked(final String credentials) {
        final Attempt attempt = attemptsByCredentials.get(normalize(credentials));
        if (attempt == null) {
            return;
        }
        final long remainingMillis = attempt.lockedUntil - System.currentTimeMillis();
        if (remainingMillis > 0) {
            final long minutesLeft = remainingMillis / 60000 + 1;
            throw new ServiceException(
                    "Too many failed login attempts. Try again in " + minutesLeft + " minute(s).",
                    ErrorCode.AUTH_ERROR_USER_IS_BLOCKED);
        }
    }

    public void recordFailure(final String credentials) {
        final String key = normalize(credentials);
        final Attempt attempt = attemptsByCredentials.computeIfAbsent(key, k -> new Attempt());
        synchronized (attempt) {
            if (attempt.lockedUntil > System.currentTimeMillis()) {
                return;
            }
            attempt.failCount++;
            if (attempt.failCount >= MAX_ATTEMPTS) {
                attempt.lockedUntil = System.currentTimeMillis() + LOCK_DURATION_MILLIS;
                attempt.failCount = 0;
            }
        }
    }

    public void recordSuccess(final String credentials) {
        attemptsByCredentials.remove(normalize(credentials));
    }

    private String normalize(final String credentials) {
        return credentials == null ? "" : credentials.trim().toUpperCase();
    }
}
