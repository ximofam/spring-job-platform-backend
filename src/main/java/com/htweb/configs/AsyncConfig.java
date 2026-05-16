package com.htweb.configs;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
@PropertySource("classpath:config.properties")
public class AsyncConfig implements AsyncConfigurer, SchedulingConfigurer {
    @Autowired
    private Environment env;

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(env.getProperty("async.core-pool-size", Integer.class, 5));
        executor.setMaxPoolSize(env.getProperty("async.max-pool-size", Integer.class, 20));
        executor.setQueueCapacity(env.getProperty("async.queue-capacity", Integer.class, 500));
        executor.setThreadNamePrefix(env.getProperty("async.thread-name-prefix", "AsyncThread-"));

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(env.getProperty("task.await-termination-seconds", Integer.class, 60));
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            System.err.println("Lỗi @Async tại hàm: " + method.getName());
            System.err.println("Thông báo lỗi: " + ex.getMessage());
        };
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(env.getProperty("scheduled.pool-size", Integer.class, 2));
        scheduler.setThreadNamePrefix(env.getProperty("scheduled.thread-name-prefix", "ScheduledThread-"));

        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(env.getProperty("task.await-termination-seconds", Integer.class, 60));
        scheduler.initialize();
        return scheduler;
    }
}