package com.jsonyao.sentinel.service;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试注解降级流控
 */
@Service
public class DegradeService {

    /**
     * 用于测试注解降级流控
     */
    private AtomicInteger counts = new AtomicInteger(0);

    /**
     * 测试注解降级流控
     *
     * 	blockHandler: 流控降级异常的时候进入的兜底函数
     *  fallback: 抛出业务异常的时候进入的兜底函数
     *  (1.6.0 之前的版本 fallback 函数只针对降级异常（DegradeException）进行处理，不能针对业务异常进行处理)
     *
     * @return
     */
    @SentinelResource(
            value = "com.jsonyao.sentinel.service.DegradeService:degrade",
            // 资源调用的流量类型, 表示控制出口流量, 注意系统规则只会对入口流量生效
            entryType = EntryType.OUT,
            blockHandler = "degradeBlockHandler",
            fallback = "degradeFallback"
    )
    public String degrade() {
        System.err.println("----> 正常执行degrade方法");

        if(counts.incrementAndGet() % 2 == 0){
            throw new RuntimeException("抛出业务异常");
        }

        return "degrade";
    }

    /**
     * 触发降级流控
     * @param ex
     * @return
     */
    public String degradeBlockHandler(BlockException ex) {
        System.err.println("----> 触发降级流控策略:" + ex);
        return "执行降级流控方法";
    }

    /**
     * 触发业务异常降级
     * @param t
     * @return
     */
    public String degradeFallback(Throwable t) {
        System.err.println("----> 触发异常时的降级策略:" + t);
        return "执行异常降级方法";
    }
}
