package com.joolove.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Bean(name = "cfAsync")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());     // 가득차면 큐에 가장 오래된 요청을 버리는 정책
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);     // shutdown 시 남아있는 요청을 처리하고 종료
        taskExecutor.setAwaitTerminationSeconds(60);                // 종료까지 기다리는 시간
        taskExecutor.setKeepAliveSeconds(30);
        return taskExecutor;
    }
}
