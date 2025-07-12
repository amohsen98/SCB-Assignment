package com.scb.application.annotation;

import com.scb.application.constants.RoleConstants;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize("hasAuthority('" + RoleConstants.ADMIN + "')")
public @interface AdminOnly {
}