package com.tmw.tracking.web.aop;

import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.domain.events.dao.EventLogDao;
import com.tmw.tracking.domain.events.entities.EventLog;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.utils.DomainUtils;
import com.tmw.tracking.utils.GuiceInstanceHolder;
import com.tmw.tracking.web.service.exception.PermissionException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import java.util.Date;

public class MethodCallInterceptor implements MethodInterceptor {

    protected final static Logger logger = LoggerFactory.getLogger(MethodCallInterceptor.class);

    /**
     * {@inheritDoc}
     *
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        MethodCall methodCall = methodInvocation.getMethod().getAnnotation(MethodCall.class);
        if (methodCall == null) {
            return methodInvocation.proceed();
        }
        PermissionType requiredPermission = methodCall.requiredPermission();
        logger.info("called method where required permission = " + requiredPermission);
        if (!SecurityUtils.getSubject().isPermitted(requiredPermission.name())) {
            throw new PermissionException("Permission Denied on the resourse for user!");
        }

        // GET calls are reads — audited only when they mutate state (see below).
        if (methodInvocation.getMethod().isAnnotationPresent(GET.class)) {
            return methodInvocation.proceed();
        }

        Throwable failure = null;
        try {
            return methodInvocation.proceed();
        } catch (Throwable e) {
            failure = e;
            throw e;
        } finally {
            recordEvent(methodInvocation, requiredPermission, failure);
        }
    }

    /**
     * Best-effort audit trail for every mutating {@link MethodCall}. Never lets a logging
     * failure affect the wrapped business call — failures are only logged, never thrown.
     */
    private void recordEvent(final MethodInvocation methodInvocation, final PermissionType requiredPermission,
                              final Throwable failure) {
        try {
            User currentUser = DomainUtils.getCurrentUser();
            if (currentUser == null) {
                return;
            }
            EventLog event = new EventLog();
            event.setTenant(currentUser.getTenant());
            event.setUser(currentUser);
            event.setEventDate(new Date());
            event.setAction(methodInvocation.getThis().getClass().getSuperclass().getSimpleName() + "."
                    + methodInvocation.getMethod().getName());
            event.setPermission(requiredPermission.name());
            event.setHttpMethod(resolveHttpMethod(methodInvocation));
            event.setSuccess(failure == null);
            if (failure != null) {
                String message = failure.getMessage();
                event.setErrorMessage(message != null && message.length() > 500 ? message.substring(0, 500) : message);
            }
            GuiceInstanceHolder.getInjector().getInstance(EventLogDao.class).log(event);
        } catch (Exception e) {
            logger.error("Failed to record event log entry", e);
        }
    }

    private String resolveHttpMethod(final MethodInvocation methodInvocation) {
        if (methodInvocation.getMethod().isAnnotationPresent(javax.ws.rs.POST.class)) {
            return "POST";
        }
        if (methodInvocation.getMethod().isAnnotationPresent(javax.ws.rs.PUT.class)) {
            return "PUT";
        }
        if (methodInvocation.getMethod().isAnnotationPresent(javax.ws.rs.DELETE.class)) {
            return "DELETE";
        }
        return null;
    }

}
