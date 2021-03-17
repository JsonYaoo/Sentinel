package com.jsonyao.sentinel.controller;

import com.jsonyao.sentinel.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SentinelAnnotationController {

	@Autowired
	private FlowService flowService;

//	@Autowired
//	private DegradeService degradeService;

	/**
	 * 测试注解设置流控
	 * @return
	 */
	@RequestMapping("/flow-test")
	public String flowTest() {
		return flowService.flow();
	}

//	@RequestMapping("/degrade-test")
//	public String degradeTest() {
//		return degradeService.degrade();
//	}
}
