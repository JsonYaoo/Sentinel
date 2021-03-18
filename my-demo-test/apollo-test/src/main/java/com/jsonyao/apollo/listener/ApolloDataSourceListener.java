package com.jsonyao.apollo.listener;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * Sentinel DashBoard整合Apollo: 自定义SentinelResource配置监听类, 已在配置类交由Spring管理
 */
public class ApolloDataSourceListener implements InitializingBean {

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
     * Apollo默认Namespace工作空间
     */
    private static final String DEFAULT_NAMESPACE = "application";

    /**
     * 监听Apollo默认属性值
     */
    private static final String DEFAULT_LISTEN_VALUE = "[]";

    /**
     * 应用名
     */
    private String applicationName;

    public ApolloDataSourceListener(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initFlowRules();
    }

    /**
     * 初始化FlowRules
     */
    private void initFlowRules() {
        // apollo-test-flow-rules
        String flowRuleKey = applicationName + FLOW_DATA_ID_POSTFIX;

        // 创建Sentinel监听Apollo数据源, FlowRuleEntity是Sentinel DashBoard的, 客户端的FlowRuleManager接受的是FlowRule
        ReadableDataSource<String, List<FlowRule>> flowDataSource = new ApolloDataSource<>(
                DEFAULT_NAMESPACE, flowRuleKey, DEFAULT_LISTEN_VALUE, source ->
                    JSON.parseObject(source, new TypeReference<List<FlowRule>>(){

                    })
        );

        // 把监听到的流控规则交由FlowRuleManager管理 -> 刷新客户端内存中的流控规则
        FlowRuleManager.register2Property(flowDataSource.getProperty());
    }
}
