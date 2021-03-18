package com.jsonyao.apollo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Apollo配置类
 */
@Configuration
public class JavaConfigBean {

    @Value("${timeout:20}")
    private int timeout;

    @Value("${newkey:'hello'}")
    private String newkey;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getNewkey() {
        return newkey;
    }

    public void setNewkey(String newkey) {
        this.newkey = newkey;
    }
}
