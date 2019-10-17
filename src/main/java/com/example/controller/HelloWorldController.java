package com.example.controller;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {

	@Value("${testkey}")
	private String testkey;

	/**
	 * 1. 使用RequestMapping注解来映射请求的URL 2. 返回值会通过视图解析器解析为实际的物理视图,
	 * 对于InternalResourceViewResolver视图解析器，会做如下解析 通过prefix+returnVal+suffix
	 * 这样的方式得到实际的物理视图，然后会转发操作 "/WEB-INF/views/success.jsp"
	 * 
	 * @return
	 */
	@RequestMapping("/helloworld")
	public String hello() {
		// System.out.println("hello world");
		return "success";
	}

	@RequestMapping("/testkey")
	@ResponseBody
	public String testkey() {
		return "testkey.value:" + testkey;
	}
	
	@RequestMapping("/props")
	@ResponseBody
	public String props() {
		Properties properties = System.getProperties();
		for(Object key: properties.keySet()) {
			System.out.println(key+"="+properties.getProperty(key.toString()));
		}
		return System.getProperty("project.name")+"props";
	}
}
