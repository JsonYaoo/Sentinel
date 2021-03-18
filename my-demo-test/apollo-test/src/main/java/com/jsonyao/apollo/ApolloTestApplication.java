package com.jsonyao.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Apollo测试应用
 */
@SpringBootApplication
@EnableApolloConfig
public class ApolloTestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApolloTestApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
