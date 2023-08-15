package com.joolove.core.utils.aop;

import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.domain.member.User;
import com.joolove.core.service.UserActivityLogService;
import com.joolove.core.service.UserService;
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

@Aspect
@Component
@RequiredArgsConstructor
public class SearchLogAspect {
    private Logger logger = LoggerFactory.getLogger(SearchLogAspect.class);
    private final UserActivityLogService userActivityLogService;

    @Around("@annotation(com.joolove.core.utils.aop.SearchLog)")
    public Object logToSearch(ProceedingJoinPoint joinPoint) {
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
                .activityCode(UserActivityLog.EActivityCode.SEARCH)
                .build();

        userActivityLogService.addUserActivityLog(userActivityLog);

        try {
//            logger.info("[1] method name : " + joinPoint.getSignature().getName() +
//                    "\n[2] username : " + username +
//                    "\n[3] queryString : " + query);
            return joinPoint.proceed();
        } catch (Throwable t) {
            throw new AspectException(t.getMessage());
        }
    }
}
