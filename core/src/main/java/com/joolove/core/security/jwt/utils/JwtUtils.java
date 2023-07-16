package com.joolove.core.security.jwt.utils;

import com.joolove.core.domain.member.User;
import com.joolove.core.security.service.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${joolove.app.jwtSecret}")
    private String jwtSecret;
    @Value("${joolove.app.jwtExpirationSecond}")
    private long jwtExpirationSecond;
    @Value("${joolove.app.jwtRefreshExpirationSecond}")
    private long jwtRefreshExpirationSecond;
    @Value("${joolove.app.jwtCookieName}")
    private String jwtCookie;
    @Value("${joolove.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    public String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        String cookieValue = name + "=" + value + "; Path=/; HttpOnly; Max-Age=" + maxAge;
        response.setHeader("Set-Cookie", cookieValue);
    }

    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public void deleteJwtCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(jwtCookie) || cookie.getName().equals(jwtRefreshCookie)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, "")
                .path("/")
                .build();
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        return ResponseCookie.from(jwtRefreshCookie, "")
                .path("/")
                .build();
    }

    private ResponseCookie generateCookie(String name, String value, String path, long millisecond) {
        return ResponseCookie.from(name, value)
                .path(path)
                .maxAge(millisecond / 1000)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie generateJwtCookie(UserPrincipal userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUser().getUsername());
        return generateCookie(jwtCookie, jwt, "/", jwtExpirationSecond);
    }

    public ResponseCookie generateJwtCookie(User user) {
        String jwt = generateTokenFromUsername(user.getUsername());
        return generateCookie(jwtCookie, jwt, "/", jwtExpirationSecond);
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(jwtRefreshCookie, refreshToken, "/", jwtRefreshExpirationSecond);
    }

    public Optional<Cookie> getCookieByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null)
            return Optional.of(cookie);

        return Optional.empty();
    }

    public String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null)
            return cookie.getValue();

        return null;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookie);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateTokenFromUsername(String username) {
        long startTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        long endTime = startTime + jwtExpirationSecond;

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(startTime))
                .setExpiration(new Date(endTime))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}