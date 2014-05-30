package com.cdg.mtsbank.report;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JobStart {

	private static Logger log = Logger.getLogger(JobStart.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext txt = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		AllFlowTimeReport allFlowTimeReport = (AllFlowTimeReport)txt.getBean("allFlowTimeReport");
		try {
			allFlowTimeReport.getAllFlowTimeReport();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("ȫ���̱����쳣��");
		}
		
//		BackReasonReport backReasonReport = (BackReasonReport)txt.getBean("backReasonReport");
//		try {
//			backReasonReport.getBackReasonReport();
//		} catch (AxisFault e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.error("����ԭ�򱨱��쳣��");
//		}
//		
//		DailyAmountReport dailyAmountReport = (DailyAmountReport)txt.getBean("dailyAmountReport");
//		try {
//			dailyAmountReport.getDailyAmountReport();
//		} catch (AxisFault e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.error("�û�������������쳣��");
//		}
		
		FlowTimeReport flowTimeReport = (FlowTimeReport)txt.getBean("flowTimeReport");
		try {
			flowTimeReport.getFlowTimeReport();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("����ʱЧ�����쳣��");
		}
		
//		ScrapedReport scrapedReport = (ScrapedReport)txt.getBean("scrapedReport");
//		try {
//			scrapedReport.getScrapedReport();
//		} catch (AxisFault e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.error("�ϼ������쳣��");
//		}
		log.info("MTS3.0 report system loading over!");
	}

}
