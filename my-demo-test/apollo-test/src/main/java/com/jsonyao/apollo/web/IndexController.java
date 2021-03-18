package com.jsonyao.apollo.web;

import com.jsonyao.apollo.config.JavaConfigBean;
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

    @RequestMapping("/index")
    private String index(){
        System.err.println("timeout: " + javaConfigBean.getTimeout());
        System.err.println("newkey: " + javaConfigBean.getNewkey());
        return "index";
    }
}
