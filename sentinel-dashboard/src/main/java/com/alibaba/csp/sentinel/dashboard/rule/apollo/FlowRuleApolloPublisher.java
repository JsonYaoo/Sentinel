package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Sentinel DashBoard整合Apollo: 流控规则服务推送
 */
@Component("flowRuleApolloPublisher")
public class FlowRuleApolloPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {

    private static FastDateFormat FAST_DATE_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");

    // FlowRuleEntity编码器
    @Autowired
    private Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder;

    @Override
    public void publish(String appName, List<FlowRuleEntity> rules) throws Exception {
        ApolloOpenApiClient client = ApolloConfigUtil.createApolloOpenApiClient(appName);
        if(client == null){
            System.err.println("client is null, publish failed!");
            return;
        }

        // 格式化appName为流控规则格式: apollo-test => apollo-test-flow-rules
        String flowDataId = ApolloConfigUtil.getFlowDataId(appName);

        // 根据appId获取appName
        String appId = ApolloConfigUtil.getAppIdWithAppName(appName);

        // 1. Apollo预发布(修改)规则
        Date now = new Date();
        String dataFormat = FAST_DATE_FORMAT.format(now);
        OpenItemDTO openItemDTO = new OpenItemDTO();
        openItemDTO.setKey(flowDataId);
        openItemDTO.setValue(flowRuleEntityEncoder.convert(rules));
        openItemDTO.setComment("modify: " + dataFormat);
        openItemDTO.setDataChangeCreatedBy(ApolloConfig.USER_ID);
        openItemDTO.setDataChangeCreatedTime(now);
        openItemDTO.setDataChangeLastModifiedBy(ApolloConfig.USER_ID);
        openItemDTO.setDataChangeLastModifiedTime(now);
        client.createOrUpdateItem(appId, ApolloConfig.ENV, ApolloConfig.CLUSTER_NAME, ApolloConfig.NAMESPACE, openItemDTO);

        // 2. Apollo真正的发布
        NamespaceReleaseDTO namespaceReleaseDTO = new NamespaceReleaseDTO();
        namespaceReleaseDTO.setEmergencyPublish(true);
        namespaceReleaseDTO.setReleaseComment("release: " + dataFormat);
        namespaceReleaseDTO.setReleaseTitle("release new properties: " + dataFormat);
        namespaceReleaseDTO.setReleasedBy(ApolloConfig.USER_ID);
        client.publishNamespace(appId, ApolloConfig.ENV, ApolloConfig.CLUSTER_NAME, ApolloConfig.NAMESPACE, namespaceReleaseDTO);
    }
}
