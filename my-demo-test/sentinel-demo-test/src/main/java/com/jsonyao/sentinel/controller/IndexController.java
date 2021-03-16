package com.jsonyao.sentinel.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@RestController
public class IndexController {

    /**
     * 用于流控测试的资源名称
     */
    public static final String RESOURCE_NAME = "helloworld";

    /**
     * 控制台流控测试
     * @return
     */
    @RequestMapping("/flow")
    public String flow(){
        // 1. 定义规则 => main方法中加载
//        initFlowRules();

        // 2. 定义资源
        Entry entry = null;// 流控的Entry
        try {
            // 2.1. 定义资源名称
            entry = SphU.entry(RESOURCE_NAME);

            // 2.2. 执行业务代码
            System.out.println("执行业务代码...");
            Thread.sleep(20);
        } catch (BlockException e) {
            // 2.3. 抛出异常, 表示被流控住了, 执行流控逻辑
            System.err.println("要访问的资源被流控了, 执行流控逻辑!");
        } catch (InterruptedException e) {

        } finally {
            if(entry != null){
                // 2.4. 关闭资源
                entry.exit();
            }
        }

        return "flow";
    }

    /**
     * 控制台降级测试
     * @return
     */
    public AtomicInteger counts = new AtomicInteger(0);
    @RequestMapping("/degrade")
    public String degrade() {
        Entry entry = null;// 流控的Entry
        try {
            String resourceName = "com.jsonyao.sentinel.controller.IndexController:degrade:test";
            entry = SphU.entry(resourceName);

            System.out.println("执行业务代码...");
            if(counts.getAndIncrement() % 2 == 0){
                Thread.sleep(100);
            }

            Thread.sleep(20);
        } catch (BlockException e) {
            System.err.println("要访问的资源被流控了, 执行流控逻辑!");
        } catch (InterruptedException e) {

        } finally {
            if(entry != null){
                entry.exit();
            }
        }

        return "degrade";
    }
}
