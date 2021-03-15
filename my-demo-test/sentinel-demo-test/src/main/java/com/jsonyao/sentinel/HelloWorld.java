package com.jsonyao.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;

/**
 * Sentinel快速入门
 */
public class HelloWorld {

    /**
     * 用于流控测试的资源名称
     */
    public static final String RESOURCE_NAME = "helloworld";

    /**
     * 初始化流控规则
     */
    private static void initFlowRules() {
        ArrayList<FlowRule> flowRules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();

        // 注意, 记得把规则和资源绑定起来, 一般一个规则对应一个资源, 但也可以一个规则对应多个资源
        flowRule.setResource(RESOURCE_NAME);

        // 设置QPS维度的流控规则
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);

        // 设置QPS为20个
        flowRule.setCount(20);

        // 交由流控管理器管理
        flowRules.add(flowRule);
        FlowRuleManager.loadRules(flowRules);
    }

    /**
     * 	对主流的5种流控策略做了 底层的抽象和资源的封装
     *
     * 	对于规则： FlowRule 、DegradeRule、ParamFlowRule、SystemRule、AuthorityRule
     * 	对于管理器：FlowRuleManager、DegradeRuleManager、ParamFlowRuleManager、SystemRuleManager、AuthorityRuleManager
     *  对于异常：FlowException、DegradeException、ParamFlowException、SystemBlockException、AuthorityException
     */
    public static void main(String[] args) throws InterruptedException {
        // 0. 引入Maven依赖
        /**
         *         <dependency>
         *             <groupId>com.alibaba.csp</groupId>
         *             <artifactId>sentinel-core</artifactId>
         *             <version>1.8.2-SNAPSHOT</version>
         *         </dependency>
         */

        // 1. 定义规则
        initFlowRules();

        // 2. 定义资源
        while (true) {
            // 流控的Entry
            Entry entry = null;
            try {
                // 2.1. 定义资源名称
                entry = SphU.entry(RESOURCE_NAME);

                // 2.2. 执行业务代码
                System.out.println("执行业务代码...");
                Thread.sleep(20);
            } catch (BlockException e) {
                // 2.3. 抛出异常, 表示被流控住了, 执行流控逻辑
                System.err.println("要访问的资源被流控了, 执行流控逻辑!");
            } finally {
                if(entry != null){
                    // 2.4. 关闭资源
                    entry.exit();
                }
            }
        }

        // 3. 查看结果

        // 4. 配置控制台
    }
}
