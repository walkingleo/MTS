package com.cdg.mtsbank.report;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.log4j.Logger;
/**
 * @author wangtongmin
 */
public class WebServiceDataFactory {
	private static Logger log = Logger.getLogger(WebServiceDataFactory.class);
/**
 * 
 * @param address  eg:http://192.168.100.239:7041/mts-web/services/ReportFlowTimeService
 * @param opAddEntryArgs 调用接口需要数据的参数
 * @param funcName 接口名称
 * @return
 * @throws AxisFault
 */
	@SuppressWarnings("unchecked")
	public  String[] getWebServiceData(String address, Object[] opAddEntryArgs,
			String funcName) throws AxisFault {
		
		log.info("coming address is :"+address);
		
		String[] orderAmountStrArray = null;
		
		RPCServiceClient serviceClient = new RPCServiceClient();
		Options options = serviceClient.getOptions();		
		EndpointReference targetEPR = new EndpointReference(address);
		options.setTo(targetEPR);
		
		QName opAddEntry = new QName("http://report.webservice.imp.com",funcName);

		Class[] classes = new Class[] { String[].class };
		orderAmountStrArray = (String[]) serviceClient.invokeBlocking(opAddEntry, opAddEntryArgs, classes)[0];

		return orderAmountStrArray;

	}

}
