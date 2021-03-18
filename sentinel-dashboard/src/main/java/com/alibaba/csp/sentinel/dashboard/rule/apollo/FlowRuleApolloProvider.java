package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Sentinel DashBoard整合Apollo: 流控规则服务获取
 */
@Component("flowRuleApolloProvider")
public class FlowRuleApolloProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {

    // FlowRuleEntity解码器
    @Autowired
    private Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder;

    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        ApolloOpenApiClient client = ApolloConfigUtil.createApolloOpenApiClient(appName);
        if(client == null){
            return Collections.emptyList();
        }

        // 格式化appName为流控规则格式: apollo-test => apollo-test-flow-rules
        String flowDataId = ApolloConfigUtil.getFlowDataId(appName);

        // 根据appId获取appName
        String appId = ApolloConfigUtil.getAppIdWithAppName(appName);
        OpenNamespaceDTO openNamespaceDTO = client.getNamespace(appId, ApolloConfig.ENV, ApolloConfig.CLUSTER_NAME, ApolloConfig.NAMESPACE);
        String rules = openNamespaceDTO.getItems().stream().filter(p -> flowDataId.equals(p.getKey())).map(OpenItemDTO::getValue).findFirst().orElse("");
        return flowRuleEntityDecoder.convert(rules);
    }
}