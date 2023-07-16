package com.joolove.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Profile("local")
@Configuration
public class EmbeddedRedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedRedisConfig.class);
    private static RedisServer redisServer = null;

    @PostConstruct
    public void startRedis() {
        int embeddedRedisPort = 6378;

        if (redisServer == null || !redisServer.isActive()) {
            redisServer = RedisServer.builder()
                    .port(embeddedRedisPort)
                    .setting("maxmemory 128M")
                    .build();

            try {
                redisServer.start();
            } catch (Exception e) {
                logger.error("embedded redis start failed ", e);
            }
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}