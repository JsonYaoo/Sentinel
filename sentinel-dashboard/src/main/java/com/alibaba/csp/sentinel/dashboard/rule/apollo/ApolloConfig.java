package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sentinel DashBoard整合Apollo配置类
 */
@Configuration
@ConfigurationProperties(prefix = "apollo.portal")
@ComponentScan("com.alibaba.csp.sentinel.dashboard.rule.apollo.*")
public class ApolloConfig implements InitializingBean {

    // 静态常量 => 用于访问
    public static String URL = "";
    public static String ENV = "DEV";
    public static String USER_ID = "apollo";
    public static String CLUSTER_NAME = "default";
    public static String NAMESPACE = "application";

    /**
     * Apollo Portal地址
     */
    private String url;

    /**
     * 服务列表, 格式: Sentinel服务名称:apollo token:apollo appId:apollo thirdId(第三方服务ID)
     */
    private List<String> appNameConfigList = new ArrayList<>();

    /**
     * 所属环境, 默认为DEV
     */
    private String env = ENV;

    /**
     * 管理使用的用户ID
     */
    private String userId = USER_ID;

    /**
     * 所属集群的名称
     */
    private String clusterName = CLUSTER_NAME;

    /**
     * 所属的Namespace工作空间
     */
    private String nameSpace = NAMESPACE;

    /**
     * appId - thirdId
     */
    public static volatile ConcurrentHashMap<String, String> THIRD_ID_MAP = new ConcurrentHashMap<>();

    /**
     * applicationName(Sentinel服务名称) - appId
     */
    public static volatile ConcurrentHashMap<String, String> APP_ID_MAP = new ConcurrentHashMap<>();

    /**
     * applicationName(Sentinel服务名称) - token
     */
    public static volatile ConcurrentHashMap<String, String> TOKEN_MAP = new ConcurrentHashMap<>();

    /**
     * FlowRuleEntity编码器
     * @return
     */
    @Bean
    public Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder(){
        return JSON::toJSONString;
    }

    /**
     * FlowRuleEntity解码器
     * @return
     */
    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder(){
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    /**
     * DegradeRuleEntity编码器
     * @return
     */
    @Bean
    public Converter<List<DegradeRuleEntity>, String> degradeRuleEntityEncoder(){
        return JSON::toJSONString;
    }

    /**
     * DegradeRuleEntity解码器
     * @return
     */
    @Bean
    public Converter<String, List<DegradeRuleEntity>> degradeRuleEntityDecoder(){
        return s -> JSON.parseArray(s, DegradeRuleEntity.class);
    }

    /**
     * Properties属性注入后
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 设置静态常量 -> 用于访问
        ApolloConfig.URL = url;
        ApolloConfig.ENV = env;
        ApolloConfig.USER_ID = userId;
        ApolloConfig.CLUSTER_NAME = clusterName;
        ApolloConfig.NAMESPACE = nameSpace;

        // 设置服务列表 -> 格式: Sentinel服务名称:apollo token:apollo appId:apollo thirdId(第三方服务ID)
        this.appNameConfigList.forEach(item -> {
            String[] items = item.split(",");
            if(item.length() == 4){
                String applicationName = items[0];
                String token = items[1];
                String appId = items[2];
                String thirdId = items[3];
                THIRD_ID_MAP.putIfAbsent(appId, thirdId);
                APP_ID_MAP.putIfAbsent(applicationName, appId);
                TOKEN_MAP.putIfAbsent(applicationName, token);
            }
        });
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getAppNameConfigList() {
        return appNameConfigList;
    }

    public void setAppNameConfigList(List<String> appNameConfigList) {
        this.appNameConfigList = appNameConfigList;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }
}
