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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class SearchLogAspect {
    private final UserActivityLogService userActivityLogService;

    // 검색 결과 컨트롤러에 적용
    @Around("@annotation(com.joolove.core.utils.aop.SearchLog)")
    public Object logToSearch(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String url = request.getRequestURI();

        String query = (String) joinPoint.getArgs()[1];
        UUID deviceId = (UUID) joinPoint.getArgs()[2];

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
                .activityDescription(url)
                .deviceId(deviceId)
                .build();

        userActivityLogService.addUserActivityLog(userActivityLog);

        // 1. js 에서 deviceID(UUID) 를 localStorage 에 발급
        // 2. deviceID 를 log 와 함께 저장
        // 3. '최근 검색어' 목록을 불러올 때, 클라이언트의 deviceID 를 기준으로 조회하여 불러온다.
        // 4. 클라이언트가 쿠키 초기화 등을 하여 deviceID 가 없거나 보내지 않는다면, 최근 검색어는 비어있다.

        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            throw new AspectException(t.getMessage());
        }
    }
}
