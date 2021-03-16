package com.jsonyao.sentinel;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.ArrayList;

/**
 * Sentienl Demo测试应用
 */
@SpringBootApplication
public class SentinelDemoTestApplication {

    /**
     * 初始化流控规则
     */
    private static void initFlowRules() {
        ArrayList<FlowRule> flowRules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();

        // 注意, 记得把规则和资源绑定起来, 一般一个规则对应一个资源, 但也可以一个规则对应多个资源
        flowRule.setResource("helloworld");

        // 设置QPS维度的流控规则
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);

        // 设置QPS为20个
        flowRule.setCount(20);

        // 交由流控管理器管理
        flowRules.add(flowRule);
        FlowRuleManager.loadRules(flowRules);
    }

    /**
     * 初始化降级规则
     */
    private static void initDradeRules() {
        ArrayList<DegradeRule> degradeRules = new ArrayList<>();
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource("com.jsonyao.sentinel.controller.IndexController:degrade:test");
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        degradeRule.setCount(2);
        degradeRules.add(degradeRule);
        DegradeRuleManager.loadRules(degradeRules);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SentinelDemoTestApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);

        // main方法中加载规则
        initFlowRules();
        initDradeRules();
        System.err.println("规则加载完毕!");
    }
}
