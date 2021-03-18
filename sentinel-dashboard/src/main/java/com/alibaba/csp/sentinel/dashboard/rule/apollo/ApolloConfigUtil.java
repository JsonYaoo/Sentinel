package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Sentinel DashBoard整合Apollo配置工具类
 */
public final class ApolloConfigUtil {

    /**
     * 流控规则类型
     */
    private static final String FLOW_RULE_TYPE = "flow";

    /**
     * 降级规则类型
     */
    private static final String DEGRADE_RULE_TYPE = "degrade";

    /**
     * 流控规则后缀 => -flow-rules
     */
    private static final String FLOW_DATA_ID_POSTFIX = "-" + FLOW_RULE_TYPE + "-rules";

    /**
     * 降级规则后缀 => -degrade-rules
     */
    private static final String DEGRADE_DATA_ID_POSTFIX = "-" + DEGRADE_RULE_TYPE + "-rules";

    /**
     * appId - ApolloOpenApiClient
     */
    private static ConcurrentHashMap<String, ApolloOpenApiClient> apolloOpenApiClientMap = new ConcurrentHashMap<>();

    /**
     * 格式化appName为流控规则格式: apollo-test => apollo-test-flow-rules
     * @param appName
     * @return
     */
    public static String getFlowDataId(String appName){
        return String.format("%s%s", appName, FLOW_DATA_ID_POSTFIX);
    }

    /**
     * 格式化appName为降级规则格式: apollo-test => apollo-test-degrade-rules
     * @param appName
     * @return
     */
    public static String getDegradeDataId(String appName){
        return String.format("%s%s", appName, DEGRADE_DATA_ID_POSTFIX);
    }

    /**
     * 根据appName构建ApolloOpenApiClient
     * @param appName
     */
    public static ApolloOpenApiClient createApolloOpenApiClient(String appName){
        // 获取缓存的ApolloOpenApiClient
        ApolloOpenApiClient client = apolloOpenApiClientMap.get(appName);
        if(client != null){
            return client;
        }

        String token = ApolloConfig.tokenMap.get(appName);
        if(StringUtils.isBlank(token)){
            System.err.println("创建Client失败: 根据指定的appName: " + appName + ", 找不到对应的token!");
            return null;
        }

        // 构建ApolloOpenApiClient
        client = ApolloOpenApiClient.newBuilder()
                    .withPortalUrl(ApolloConfig.URL)
                    .withToken(token)
                    .build();
        apolloOpenApiClientMap.putIfAbsent(appName, client);
        return client;
    }
}
