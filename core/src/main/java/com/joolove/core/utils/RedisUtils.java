package com.joolove.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final long DEFAULT_TIME_TO_LIVE = 60;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    public boolean addInf(String keyString, Object object) {
        return add(keyString, object, -1, TimeUnit.DAYS);
    }

    public boolean add(String keyString, Object object) {
        return add(keyString, object, DEFAULT_TIME_TO_LIVE, DEFAULT_TIME_UNIT);
    }

    public boolean add(String keyString, Object object, long timeToLive, TimeUnit timeUnit) {
        String key = generateKey(keyString, object);
        try {
            String value = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, value, timeToLive, timeUnit);
        } catch (JsonProcessingException e) {
            logger.error("[Error] Add - Object to Json failed : ", e);
            return false;
        }

        return true;
    }

    public boolean remove(String keyString, Class<?> clazz) {
        String className = clazz.getSimpleName();
        String key = className + ":" + keyString;
        Long size = redisTemplate.opsForValue().size(key);
        if (size == null || size == 0) {
            return false;
        }

        redisTemplate.delete(key);
        return true;
    }

    public Object get(String keyString, Class<?> clazz) {
        String className = clazz.getSimpleName();
        String key = className + ":" + keyString;
        Long size = redisTemplate.opsForValue().size(key);
        if (size == null || size == 0) {
            return null;
        }

        try {
            String value = (String) redisTemplate.opsForValue().get(key);
            return objectMapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            logger.error("[Error] Get - Json to Object failed : ", e);
        }

        return null;
    }

    private String generateKey(String keyString, Object object) {
        String className = object.getClass().getSimpleName();
        return className + ":" + keyString;
    }
}
