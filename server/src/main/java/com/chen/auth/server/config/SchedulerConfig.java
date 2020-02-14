package com.chen.auth.server.config;/**
 * Created by Administrator on 2019/9/9.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 定时任务配置-线程池的方式配置
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/9 23:05
 **/
@Configuration
public class SchedulerConfig {

    @Bean("taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(4);
        //最大核心线程数
        executor.setMaxPoolSize(10);
        //设置队列中等待被调度处理的任务的数量
        executor.setQueueCapacity(8);
        executor.initialize();
        return executor;
    }

}