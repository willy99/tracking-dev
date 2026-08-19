package com.tmw.tracking.filter;

import com.tmw.tracking.domain.events.dao.EventLogDao;
import com.tmw.tracking.domain.events.entities.EventLog;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.utils.GuiceInstanceHolder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Date;

/**
 * Same as the stock Shiro {@link LogoutFilter}, but records an audit event for the browser
 * "Log Out" link — the only logout path that never goes through {@code AuthenticationService}.
 */
public class TrackingLogoutFilter extends LogoutFilter {

    private static final Logger logger = LoggerFactory.getLogger(TrackingLogoutFilter.class);

    @Override
    protected boolean preHandle(final ServletRequest request, final ServletResponse response) throws Exception {
        final Subject subject = SecurityUtils.getSubject();
        final Object principal = subject != null ? subject.getPrincipal() : null;
        try {
            return super.preHandle(request, response);
        } finally {
            if (principal instanceof User) {
                logLogoutEvent((User) principal);
            }
        }
    }

    private void logLogoutEvent(final User user) {
        try {
            if (user.getTenant() == null) {
                return;
            }
            EventLog event = new EventLog();
            event.setTenant(user.getTenant());
            event.setUser(user);
            event.setEventDate(new Date());
            event.setAction("Logout");
            event.setSuccess(true);
            GuiceInstanceHolder.getInjector().getInstance(EventLogDao.class).log(event);
        } catch (Exception e) {
            logger.error("Failed to record Logout event log entry", e);
        }
    }
}
