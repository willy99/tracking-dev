package com.tmw.tracking.web.aop;

import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.web.service.exception.PermissionException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


        return methodInvocation.proceed();
        /*long start = System.currentTimeMillis();
        String target = methodInvocation.getThis().getClass().getSuperclass().getName() + "." + methodInvocation.getMethod().getName();
        String originalMethod = (String) MDC.get(Utils.MDC_METHOD);
        String originalRequestId = (String) MDC.get(Utils.MDC_REQUEST_ID);
        MDC.put(Utils.MDC_METHOD, target);

        if(StringUtils.isBlank(originalRequestId)) {
            MDC.put(Utils.MDC_REQUEST_ID, String.valueOf(UUID.randomUUID()));
        }
        logger.info("Call method: " + target);
        try {
            return methodInvocation.proceed();
        } catch (Throwable e) {
            logger.error("Error during execution " + this.getClass().getName() + " " + e.getMessage(), e);
            throw e;
        } finally {
            MDC.put(Utils.MDC_DURATION, String.valueOf(System.currentTimeMillis() - start));
            logger.info("Processed method: " + target);
            MDC.remove(Utils.MDC_DURATION);
            if(StringUtils.isNotBlank(originalMethod)) {
                MDC.put(Utils.MDC_METHOD, originalMethod);
            } else {
                MDC.remove(Utils.MDC_METHOD);
            }
            if(StringUtils.isBlank(originalRequestId)) {
                MDC.remove(Utils.MDC_REQUEST_ID);
            }
        }*/
    }

}
