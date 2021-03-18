package com.jsonyao.apollo.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

/**
 * Sentinel DashBoard整合Apollo: 流控测试类
 */
@Service
public class FlowService {

    @SentinelResource(
            value = "com.jsonyao.apollo.service.FlowService:test",
            blockHandler = "testBlockHandler"
    )
    public String test(){
        System.err.println("正常执行");
        return "正常执行";
    }

    /**
     * 流控降级逻辑
     * @param ex
     * @return
     */
    public String testBlockHandler(BlockException ex){
        System.err.println("流控执行, " + ex);
        return "流控执行";
    }
}
