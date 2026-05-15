package com.tmw.tracking.web.aop;

import com.tmw.tracking.domain.PermissionType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
public @interface MethodCall {
    PermissionType requiredPermission() default PermissionType.LOGIN_APP;
}
