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
	 * 1. ʹ��RequestMappingע����ӳ�������URL 2. ����ֵ��ͨ����ͼ����������Ϊʵ�ʵ�������ͼ,
	 * ����InternalResourceViewResolver��ͼ���������������½��� ͨ��prefix+returnVal+suffix
	 * �����ķ�ʽ�õ�ʵ�ʵ�������ͼ��Ȼ���ת������ "/WEB-INF/views/success.jsp"
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
