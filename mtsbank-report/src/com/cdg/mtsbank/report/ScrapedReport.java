package com.cdg.mtsbank.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.cdg.mtsbank.util.DateUtil;
import com.cdg.mtsbank.util.ExcelConfBean;
import com.cdg.mtsbank.util.ExcelFactory;
import com.cdg.mtsbank.util.FileUtil;

public class ScrapedReport {

private static Logger log = Logger.getLogger(ScrapedReport.class);

	private BasicInfo basicInfo;

	public BasicInfo getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(BasicInfo basicInfo) {
		this.basicInfo = basicInfo;
	}

	public void getScrapedReport() throws AxisFault {

		String jobID = DateUtil.getDateToStringThree(Calendar.getInstance()
				.getTime());
		log.info("�ƶ��ն˷���ϵͳ3.0-" + basicInfo.getBankNO() + "-�ϼ�����-��ʼ,��ʱ������" + jobID);

		// �����ļ����� YYYYMMDDHHMMSS
		String fileStamp = DateUtil.getDateToStringThree(Calendar.getInstance()
				.getTime());
		
		String startDate = DateUtil.getTodayNextYYYY_MM_DD(-1); //eg.20120821
//		String startDate = "2014-05-04";

		String smbFilePath = "smb://".concat(basicInfo.getUserName()).concat(":").concat(
				basicInfo.getPassword()).concat("@").concat(basicInfo.getServerIP());

		String[] flowTimeStrArray = null;
		flowTimeStrArray = getFlowTimeWebService(startDate);

		List<Map<String, String>> flowTimeList = null;


		System.out.println("���ݳ���Ϊ��" + flowTimeStrArray.length);
		List<String> dataIndexList = new ArrayList<String>();
		dataIndexList.add("OrderNo");
		dataIndexList.add("scrapName");
		dataIndexList.add("station");
		dataIndexList.add("tgCode");
		dataIndexList.add("tgName");
		dataIndexList.add("scrapType");
		dataIndexList.add("scrapDate");

		
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("OrderNo", "������");
		headerMap.put("scrapName", "����");
		headerMap.put("station", "��������");
		headerMap.put("tgCode", "Ӫ����Ա���");
		headerMap.put("tgName", "Ӫ����Ա����");
		headerMap.put("scrapType", "��������");
		headerMap.put("scrapDate", "��������");
		
		if (flowTimeStrArray != null && flowTimeStrArray.length > 0) {
			flowTimeList = fixFlowTimeList(flowTimeStrArray);

			ExcelConfBean confBean = new ExcelConfBean();
			confBean.setDataIndex(dataIndexList);
			confBean.setHeaderIndex(headerMap);
			String fname = basicInfo.getLocalPath() + "\\" + basicInfo.getBankNO() + "�ϼ�����_" + fileStamp + ".xls";
			confBean.setFileName(fname);
			confBean.setSheetName("hello");
			if (ExcelFactory.getExcelReport(confBean, flowTimeList)) {
				FileUtil.uploadPackage(smbFilePath, fname);
			}

		}

		log.info("�ƶ��ն˷���3.0-" + basicInfo.getBankNO() + " �ϼ�����-����,��ʱ������" + jobID);
	}

	public String[] getFlowTimeWebService(String startDate) throws AxisFault {
		String[] orderAmountStrArray = null;
		Object[] opAddEntryArgs = new Object[] { startDate, basicInfo.getBankNO() };
		orderAmountStrArray = new WebServiceDataFactory().getWebServiceData(
				basicInfo.getAddress(), opAddEntryArgs, "getReportScrap");
		return orderAmountStrArray;
	}

	public List<Map<String, String>> fixFlowTimeList(String[] flowTimeStrArray) {
		List<Map<String, String>> flowTimeList = null;
		final String S = "@";
		if (flowTimeStrArray != null && flowTimeStrArray.length > 0) {
			flowTimeList = new ArrayList<Map<String, String>>();
			Map<String, String> recordMap = null;
			for (String record : flowTimeStrArray) {
				String[] columns = record.split(S);
				recordMap = new HashMap<String, String>();
				recordMap.put("OrderNo", columns[0]);
				recordMap.put("scrapName", columns[1]);
				recordMap.put("station", columns[2]);
				recordMap.put("tgCode", columns[3]);
				recordMap.put("tgName", columns[4]);
				recordMap.put("scrapType", columns[5]);
				recordMap.put("scrapDate", columns[6]);
				flowTimeList.add(recordMap);
			}
		}

		return flowTimeList;
	}

	
	
}
