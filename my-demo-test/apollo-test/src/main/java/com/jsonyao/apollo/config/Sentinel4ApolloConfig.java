package com.jsonyao.apollo.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.jsonyao.apollo.listener.ApolloDataSourceListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sentinel DashBoard整合Apollo: Sentinel客户端配置类
 */
@Configuration
public class Sentinel4ApolloConfig {

    @Value("${spring.application.name}")
    private String applicationName = "";

    /**
     * SentinelResource切面配置类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public SentinelResourceAspect sentinelResourceAspect(){
        return new SentinelResourceAspect();
    }

    /**
     * 自定义SentinelResource配置监听类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ApolloDataSourceListener apolloDataSourceListener(){
        return new ApolloDataSourceListener(applicationName);
    }
}
