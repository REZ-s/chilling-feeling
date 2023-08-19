package com.joolove.core.utils.aop;

import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.service.UserActivityLogService;
import lombok.RequiredArgsConstructor;
import org.aopalliance.aop.AspectException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@RequiredArgsConstructor
public class ClickLogAspect {
    private final UserActivityLogService userActivityLogService;

    @Around("@annotation(com.joolove.core.utils.aop.ClickLog)")
    public Object logToClick(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String url = request.getRequestURI();
        String query = (String) joinPoint.getArgs()[1];
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            username = "anonymousUser";
        } else {
            username = authentication.getPrincipal().toString();
        }

        UserActivityLog userActivityLog = UserActivityLog.builder()
                .username(username)
                .targetName(query)
                .targetCode(UserActivityLog.ETargetCode.GOODS)
                .activityCode(UserActivityLog.EActivityCode.CLICK)
                .activityDescription(url)
                .build();

        userActivityLogService.addUserActivityLog(userActivityLog);

        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            throw new AspectException(t.getMessage());
        }
    }
}

