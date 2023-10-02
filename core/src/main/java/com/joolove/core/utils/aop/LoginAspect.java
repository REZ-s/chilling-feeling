package com.joolove.core.utils.aop;

import com.joolove.core.model.RestAPIResponse;
import org.aopalliance.aop.AspectException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginAspect {
    @Around("@annotation(com.joolove.core.utils.aop.LoginState)")
    public Object redirectToLoginState(ProceedingJoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            throw new AspectException(t.getMessage());
        }
    }

    @Around("@annotation(com.joolove.core.utils.aop.LogoutState)")
    public Object redirectToLogoutState(ProceedingJoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }

        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            throw new AspectException(t.getMessage());
        }
    }

    @Around("@annotation(com.joolove.core.utils.aop.APILoginState)")
    public Object redirectToAPILoginState(ProceedingJoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.ok().body(RestAPIResponse.success("invalid user"));
        }

        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            throw new AspectException(t.getMessage());
        }
    }
}