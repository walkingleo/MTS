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

public class BackReasonReport {
	private static Logger log = Logger.getLogger(BackReasonReport.class);
	private BasicInfo basicInfo;
	public BasicInfo getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(BasicInfo basicInfo) {
		this.basicInfo = basicInfo;
	}

	public void getBackReasonReport() throws AxisFault {
	
	String jobID = DateUtil.getDateToStringThree(Calendar.getInstance()
			.getTime());
	log.info("�ƶ��ն˷���ϵͳ3.0-" + basicInfo.getBankNO() + "-����ԭ�򱨱�-��ʼ,��ʱ������" + jobID);

	// �����ļ����� YYYYMMDDHHMMSS
	String fileStamp = DateUtil.getDateToStringThree(Calendar.getInstance()
			.getTime());
	
	 String startDate = DateUtil.getTodayNextYYYY_MM_DD(-1); //eg.20120821
	 String endDate = DateUtil.getTodayYYYY_MM_DD();//eg.20120822
	String smbFilePath = "smb://".concat(basicInfo.getUserName()).concat(":").concat(
			basicInfo.getPassword()).concat("@").concat(basicInfo.getServerIP());

	String[] BackReasonStrArray = null;
	BackReasonStrArray = getBackReasonWebService(startDate,endDate);

	List<Map<String, String>> BackReasonList = null;


	System.out.println("���ݳ���Ϊ��" + BackReasonStrArray.length);
	List<String> dataIndexList = new ArrayList<String>();

	dataIndexList.add("orderNo"); 
	dataIndexList.add("orgnizeName"); 
	dataIndexList.add("orgnizeId");
	dataIndexList.add("bankSprName");
	dataIndexList.add("creationDate");
	dataIndexList.add("description");

	
	Map<String, String> headerMap = new HashMap<String, String>();

	headerMap.put("orderNo","������"); 
	headerMap.put("orgnizeName","�ƹ����"); 
	headerMap.put("orgnizeId","�ƹ���");
	headerMap.put("bankSprName","����ʱ��");
	headerMap.put("creationDate","��������");
	headerMap.put("description","����ԭ��");

	if (BackReasonStrArray != null && BackReasonStrArray.length > 0) {
		BackReasonList = fixBackReasonList(BackReasonStrArray);

		ExcelConfBean confBean = new ExcelConfBean();
		confBean.setDataIndex(dataIndexList);
		confBean.setHeaderIndex(headerMap);
		String fname = basicInfo.getLocalPath() + "\\" + basicInfo.getBankNO() + "����ԭ�򱨱�_" + fileStamp + ".xls";
		confBean.setFileName(fname);
		confBean.setSheetName("����ԭ�򱨱�");
		if (ExcelFactory.getExcelReport(confBean, BackReasonList)) {
			FileUtil.uploadPackage(smbFilePath, fname);
		}

	}

	log.info("�ƶ��ն˷���3.0-" + basicInfo.getBankNO() + " ����ԭ�򱨱�-����,��ʱ������" + jobID);
}

public String[] getBackReasonWebService(String startDate,String endDate) throws AxisFault {
	String[] orderAmountStrArray = null;

	Object[] opAddEntryArgs = new Object[] { startDate,endDate, basicInfo.getBankNO() };
	orderAmountStrArray = new WebServiceDataFactory().getWebServiceData(
			basicInfo.getAddress(), opAddEntryArgs, "getReportBackReason");

	return orderAmountStrArray;
}

public List<Map<String, String>> fixBackReasonList(String[] BackReasonStrArray) {
	List<Map<String, String>> backReasonList = null;
	final String S = "@";
	if (BackReasonStrArray != null && BackReasonStrArray.length > 0) {
		backReasonList = new ArrayList<Map<String, String>>();
		for (String record : BackReasonStrArray) {
			Map<String, String> recordMap = new HashMap<String, String>();
			String [] columns = record.split(S); 
			 recordMap.put("orderNo", columns[0]); 
			 recordMap.put("orgnizeName", columns[1]); 
			 recordMap.put("orgnizeId", columns[2]);
			 recordMap.put("bankSprName", columns[3]);
			 recordMap.put("creationDate", columns[4]);
			 recordMap.put("description", columns[5]);
			 backReasonList.add(recordMap);
		}
			
	}

	return backReasonList;
}
}
