package com.cdg.mtsbank.report;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class TestBean extends TestCase{

	public TestBean(String name){
		super(name);
	}
	
	public void test11() {
		ApplicationContext txt = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		FlowTimeReport backReasonReport = (FlowTimeReport)txt.getBean("flowTimeReport");
		try {
			backReasonReport.getFlowTimeReport();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
