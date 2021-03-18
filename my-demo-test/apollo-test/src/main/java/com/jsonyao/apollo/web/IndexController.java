package com.jsonyao.apollo.web;

import com.jsonyao.apollo.config.JavaConfigBean;
import com.jsonyao.apollo.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Apollo测试应用: 前端控制器
 */
@RestController
public class IndexController {

    @Autowired
    private JavaConfigBean javaConfigBean;

    @Autowired
    private FlowService flowService;

    /**
     * 测试Apollo客户端
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        System.err.println("timeout: " + javaConfigBean.getTimeout());
        System.err.println("newkey: " + javaConfigBean.getNewkey());
        return "index";
    }

    /**
     * 测试Sentinel DashBoard整合Apollo
     * @return
     */
    @RequestMapping("/test")
    public String test(){
        return flowService.test();
    }
}
