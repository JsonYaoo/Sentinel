package com.jsonyao.sentinel.service;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

/**
 * 测试注解设置流控
 */
@Service
public class FlowService {

    /**
     * 测试注解设置流控
     *
     * 	blockHandler: 流控降级的时候进入的兜底函数
     *  fallback: 抛出异常的时候进入的兜底函数
     *  (1.6.0 之前的版本 fallback 函数只针对降级异常（DegradeException）进行处理，不能针对业务异常进行处理)
     *
     * @return
     */
    @SentinelResource(
            value = "com.jsonyao.sentinel.service.FlowService:flow",
            // 资源调用的流量类型, 表示控制出口流量, 注意系统规则只会对入口流量生效
            entryType = EntryType.OUT,
            blockHandler = "flowBlockHandler"
//            ,
            // 如果同时配置了blockHanlder和fallback, 则只会生效blockHanlder
//            fallback = ""
    )
    public String flow() {
        System.err.println("----> 正常执行flow方法");
        return "flow";
    }

    /**
     * 触发流控
     * @param ex
     * @return
     */
    public String flowBlockHandler(BlockException ex) {
        System.err.println("----> 触发流控策略:" + ex);
        return "执行流控方法";
    }
}
