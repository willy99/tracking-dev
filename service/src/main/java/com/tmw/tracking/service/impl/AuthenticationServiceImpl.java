package com.tmw.tracking.service.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.AuthenticatedUserDao;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.domain.events.dao.EventLogDao;
import com.tmw.tracking.domain.events.entities.EventLog;
import com.tmw.tracking.entity.AuthenticatedUser;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.mail.MailSender;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.utils.LoginAttemptTracker;
import com.tmw.tracking.utils.PasswordGenerator;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.service.exception.NotFoundException;
import com.tmw.tracking.web.service.exception.PermissionException;
import com.tmw.tracking.web.service.exception.ServiceException;
import com.tmw.tracking.web.service.util.error.ErrorCode;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.MDC;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.UUID;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticatedUserDao authenticatedUserDao;
    private final UserDao userDao;
    private final Integer tokenExpirationMinutes;
    private final MailSender mailSender;
    private final EventLogDao eventLogDao;

    @Inject
    public AuthenticationServiceImpl(final AuthenticatedUserDao authenticatedUserDao,
                                     final UserDao userDao,
                                     final MailSender mailSender,
                                     final EventLogDao eventLogDao,
                                     @Named("tmw.auth.token.expiration")final Integer tokenExpirationMinutes
                                     ) {
        this.authenticatedUserDao = authenticatedUserDao;
        this.userDao = userDao;
        this.tokenExpirationMinutes = tokenExpirationMinutes;
        this.mailSender = mailSender;
        this.eventLogDao = eventLogDao;
    }

    @Override
    public AuthenticatedUser login(String credentials, final String password) {
        User user;
        if (credentials != null) {
            credentials = credentials.replaceAll(" ","");
            if (!credentials.startsWith("+38") && !credentials.contains("@") && credentials.length() == 10) {
                credentials = "+38" + credentials; // default is UKRAINE code
            } else if (!credentials.startsWith("+") && !credentials.contains("@")) {
                credentials = "+" + credentials;
            }

        }
        MDC.put(Utils.MDC_USER, credentials);
        if (LoginAttemptTracker.isLocked(credentials)) {
            throw new ServiceException("Too many failed login attempts. Try again in "
                    + LoginAttemptTracker.lockedForMinutes(credentials) + " minute(s).",
                    ErrorCode.AUTH_ERROR_USER_IS_BLOCKED);
        }
        user = userDao.getAnyUserByCredentials(credentials);
        if (user == null) {
            // no user resolved -> no tenant to attribute the event to, can't log
            LoginAttemptTracker.recordFailure(credentials);
            throw new NotFoundException("User ["+credentials+"] not recognized. Please provide password.");
        }
        if(!user.isActive() || !user.getTenant().isActive()){
            logLoginEvent(user, false, "Account disabled");
            throw new ServiceException("User ["+credentials+"] is not active, login denied", ErrorCode.AUTH_ERROR_ACCOUNT_DISABLED);
        }
        if (!PasswordGenerator.verifyPassword(password, user.getPassword())) {
            LoginAttemptTracker.recordFailure(credentials);
            logLoginEvent(user, false, "Invalid credentials");
            throw new ServiceException("The credentials are incorrect!", ErrorCode.AUTH_ERROR_USER_OR_PASSWORD_IS_INVALID);
        }
        LoginAttemptTracker.recordSuccess(credentials);
        if (PasswordGenerator.isLegacyHash(user.getPassword())) {
            // password matched under the old unsalted scheme -> migrate it now that we have the plaintext
            user.setPassword(PasswordGenerator.hashPassword(password));
            userDao.update(user);
        }
        final AuthenticatedUser authenticatedUser = loginUser(user);
        logLoginEvent(user, true, null);
        return authenticatedUser;
    }

    /**
     * Best-effort login/logout audit entry. Never lets a logging failure affect the actual
     * auth flow — failures are only logged, never thrown.
     */
    private void logLoginEvent(final User user, final boolean success, final String reason) {
        logAuthEvent(user, "Login", success, reason);
    }

    private void logLogoutEvent(final User user) {
        logAuthEvent(user, "Logout", true, null);
    }

    private void logAuthEvent(final User user, final String action, final boolean success, final String reason) {
        try {
            if (user == null || user.getTenant() == null) {
                return;
            }
            EventLog event = new EventLog();
            event.setTenant(user.getTenant());
            event.setUser(user);
            event.setEventDate(new Date());
            event.setAction(action);
            event.setSuccess(success);
            event.setErrorMessage(reason);
            eventLogDao.log(event);
        } catch (Exception e) {
            logger.error("Failed to record " + action + " event log entry", e);
        }
    }


    private AuthenticatedUser loginUser(User user) {
        return authenticatedUserDao.transactionallyAuthenticateUser(user, tokenExpirationMinutes);
    }


    /**
     * Validate user
     *
     * @param token the authentication token. Can be {@code null}
     * @return the authenticated {@code User}
     */
    @Override
    public User validateUser(final String token) {
        if (StringUtils.isBlank(token)) {
            throw new ServiceException("Token cannot be blank", ErrorCode.AUTH_ERROR_TOKEN_IS_INVALID);
        }
        final AuthenticatedUser authenticatedUser = authenticatedUserDao.getAuthenticatedUserByToken(token);
        if (authenticatedUser == null) {
            throw new ServiceException("Token is invalid. User is not logged.", ErrorCode.AUTH_ERROR_TOKEN_IS_INVALID);
        }
        if (authenticatedUser.getExpired().before(new Date())) {
            throw new ServiceException("Token is expired.", ErrorCode.AUTH_ERROR_TOKEN_IS_EXPIRED);
        }
        return authenticatedUser.getUser();
    }

    /**
     * Logout functionality
     *
     * @param token he authentication token. Can be {@code null}
     */
    @Override
    @Transaction
    public void logout(final String token) {
        if (StringUtils.isBlank(token)) {
            throw new ServiceException("Token cannot be blank", ErrorCode.AUTH_ERROR_TOKEN_IS_INVALID);
        }

        final AuthenticatedUser authenticatedUser = authenticatedUserDao.getAuthenticatedUserByToken(token);
        if (authenticatedUser != null) {
            authenticatedUserDao.delete(authenticatedUser);
            logLogoutEvent(authenticatedUser.getUser());
        }
    }

    /**
     * Logout functionality
     *
     * @param user the authenticated user. Cannot be {@code null}
     */
    @Override
    public void logout(final User user) {
        if (user == null) {
            return;
        }
        final AuthenticatedUser authenticatedUser = authenticatedUserDao.getAuthenticatedUser(user);
        if (authenticatedUser != null) {
            authenticatedUserDao.delete(authenticatedUser);
        }
        logLogoutEvent(user);
    }

    @Override
    public void deactivateCompany(Company company) {
        authenticatedUserDao.deactivateCompany(company);
    }


    @Override
    public void passwordWorkflowForNewUser(User user, Boolean generateNew) {

        //send email
        //TODO generate link
        if (generateNew) {
            String password = PasswordGenerator.generateSecurePassword();
            user.setPassword(PasswordGenerator.hashPassword(password));
        }

        user.setActivationHash(PasswordGenerator.generateHash(user.getPassword() + System.currentTimeMillis(), "SHA-256"));
        user.setActivationCreated(new Date());
        String link = Utils.getWebLink() + "/activate/" + user.getActivationHash();
        String body = "The temporary password was generated. Please follow the link <a href='" + link + "'>Link</a> to create new password and activate account!";

        mailSender.sendEmailMessage(user.getEmail(), "So-Tracking. Account activation link", body);
    }

    @Override
    public void passwordWorkflowForUpdateUser(User user, String newPassword) {
        user.setPassword(PasswordGenerator.hashPassword(newPassword));
        user.setActivationHash(null);
        String body = "Password for your account has been changed!";
        mailSender.sendEmailMessage(user.getEmail(), "So-Tracking. Account password changing", body);
    }

    @Override
    public User checkHash(String hash) {
        User user = userDao.getByActivationHash(hash);
        if (user == null) {
            throw new PermissionException("The activation link is incorrect or invalidated!");
        }
        return user;
    }


    // ------------------------------------------------------------------------

    /**
     * Save/Update {@link AuthenticatedUser authenticated user}
     *
     * @param authenticatedUser the {@code AuthenticatedUser}. Cannot be {@code null}
     */
    private void setAsAuthenticated(final AuthenticatedUser authenticatedUser) {
        final String token = UUID.randomUUID().toString();
        authenticatedUser.setToken(token);
        final Date expired = DateUtils.addMinutes(new Date(), tokenExpirationMinutes);
        authenticatedUser.setExpired(expired);
        if (authenticatedUser.getId() == null) {
            authenticatedUserDao.create(authenticatedUser);
        }
        else {
            authenticatedUserDao.update(authenticatedUser);
        }
    }

    public static User getAuthenticatedUser() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser;
        }
        throw new ServiceException("Token is invalid. User is not logged.", ErrorCode.AUTH_ERROR_TOKEN_IS_INVALID);
    }

    private static User getCurrentUser() {
        final Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() && subject.getPrincipal() != null) {
            return (User) subject.getPrincipal();
        } else {
            return null;
        }
    }

}
