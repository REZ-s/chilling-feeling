package com.joolove.core.utils.filter;

import com.joolove.core.utils.RedisUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 연속되는 중복 호출 방지
 * (1) 1분에 20번 이내로 호출 제한 -> 레디스 캐시 소멸시간을 60초로 설정하고, count 를 올려가면서 판단
 * (2) 0.2초에 1번으로 호출 제한 -> 캐시가 살아있으면, 마지막 업데이트 시간과 현재 시간 비교
*/
public class ValidationFilter extends OncePerRequestFilter {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String clientIP = getRealClientIP(request);
        String key = clientIP + "/" + request.getRequestURI();
        ClientRequestInformation clientRequestInformation = null;

        Object o = redisUtils.get(key, ClientRequestInformation.class);
        if (o != null) {
            clientRequestInformation = (ClientRequestInformation) o;

            if (clientRequestInformation.isValidRequest()) {
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too many requests from this client.");
                return;
            }

            clientRequestInformation.updateLastUpdatedTimeMs(System.currentTimeMillis());
            clientRequestInformation.incrementCount();
        } else {
            clientRequestInformation = ClientRequestInformation.builder()
                    .requestURI(request.getRequestURI())
                    .count((short) 1)
                    .lastUpdatedTimeMs(System.currentTimeMillis())
                    .build();
        }

        redisUtils.add(clientIP, clientRequestInformation);

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

    @Getter
    @Builder
    public static class ClientRequestInformation {
        String requestURI;
        short count;
        long lastUpdatedTimeMs;

        public boolean isValidRequest() {
            return System.currentTimeMillis() - lastUpdatedTimeMs >= 200 && count < 20;
        }

        public void incrementCount() {
            ++count;
        }

        public void updateLastUpdatedTimeMs(long currentTimeMs) {
            lastUpdatedTimeMs = currentTimeMs;
        }
    }
}
