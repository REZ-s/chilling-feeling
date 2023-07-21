package com.joolove.core.utils.filter;

import com.joolove.core.utils.RedisUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 연속되는 중복 호출 방지
 * (1) 1분에 60번 이내로 호출 제한 -> 레디스 캐시 소멸시간을 60초로 설정하고, count 를 올려가면서 판단
 * (2) TOO_MANY_REQUESTS(429) 이면, 10분 동안 해당 요청 lock
*/
public class ValidationFilter extends OncePerRequestFilter {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String clientIP = getRealClientIP(request);
        String key = clientIP + request.getRequestURI();
        ClientRequestInformation clientRequestInformation = null;

        Object o = redisUtils.get(key, ClientRequestInformation.class);
        if (o != null) {
            clientRequestInformation = (ClientRequestInformation) o;

            if (!clientRequestInformation.isFreeOnRequest()) {
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                        "Too many requests from this client (Locked on previous request).");
                return;
            }

            if (!clientRequestInformation.isValidRequest()) {
                clientRequestInformation.lockRequest();
                redisUtils.add(key, clientRequestInformation, 600, TimeUnit.SECONDS);
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                        "Too many requests from this client.");
                return;
            }

            clientRequestInformation.updateLastUpdatedTimeMs();
            clientRequestInformation.incrementCount();
        } else {
            clientRequestInformation = ClientRequestInformation.builder()
                    .requestURI(request.getRequestURI())
                    .build();
        }

        redisUtils.add(key, clientRequestInformation, 60, TimeUnit.SECONDS);

        filterChain.doFilter(request, response);
    }

    public String getRealClientIP(HttpServletRequest request) {
        String[] headersToCheck = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headersToCheck) {
            String ip = request.getHeader(header);
            if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
                continue;
            }

            return ip;
        }

        return request.getRemoteAddr();
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class ClientRequestInformation {
        private static final long HOLD_TIME_MILLI_SECOND = 600000;
        private String requestURI;
        private short count;
        private long createdTimeMs;
        private long lastUpdatedTimeMs;
        private long captureTimeMs;

        @Builder
        public ClientRequestInformation(String requestURI) {
            this.requestURI = requestURI;
            this.count = 1;
            this.createdTimeMs = System.currentTimeMillis();
            this.lastUpdatedTimeMs = System.currentTimeMillis();
            this.captureTimeMs = 0;
        }

        public boolean isValidRequest() {
            return count < 60;
        }

        public void incrementCount() {
            ++count;
        }

        public void updateLastUpdatedTimeMs() {
            lastUpdatedTimeMs = System.currentTimeMillis();
        }

        public void lockRequest() {
            captureTimeMs = System.currentTimeMillis() + HOLD_TIME_MILLI_SECOND;
        }

        public boolean isFreeOnRequest() {
            return System.currentTimeMillis() > captureTimeMs;
        }
    }
}
