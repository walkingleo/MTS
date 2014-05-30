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

public class AllFlowTimeReport {
	
	private static Logger log = Logger.getLogger(AllFlowTimeReport.class);
	private BasicInfo basicInfo;

	public BasicInfo getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(BasicInfo basicInfo) {
		this.basicInfo = basicInfo;
	}
	
	final String S = "@";
	
	public void getAllFlowTimeReport(){
		String jobID = DateUtil.getDateToStringThree(Calendar.getInstance()
				.getTime());
		log.info("移动终端服务系统3.0-" + this.basicInfo.getBankNO() + "-全流程报表-开始,定时任务标记" + jobID);

		// 报表文件名称 YYYYMMDDHHMMSS
		String fileStamp = DateUtil.getDateToStringThree(Calendar.getInstance().getTime());
//		String startDate = DateUtil.getTodayNextYYYY_MM_DD(-1); //eg.20120821
		String startDate = "2014-04-29";

		String smbFilePath = "smb://".concat(basicInfo.getUserName()).concat(":").concat(
				basicInfo.getPassword()).concat("@").concat(basicInfo.getServerIP());

		String[] flowTimeStrArray = null;
		try {
			flowTimeStrArray = getFlowTimeWebService(startDate);
		} catch (AxisFault e) {
			e.printStackTrace();
		}

		List<Map<String, String>> flowTimeList = null;


		System.out.println("数据长度为：" + flowTimeStrArray.length);
		List<String> dataIndexList = new ArrayList<String>();
		dataIndexList.add("cDate");
		dataIndexList.add("inComeNum");
		dataIndexList.add("backNum");
		dataIndexList.add("yetNum");
		dataIndexList.add("passNum");
		dataIndexList.add("thenPassNum");
		
		
		Map<String, String> headerMap = new HashMap<String, String>();

		headerMap.put("cDate", "业务日期");
		headerMap.put("inComeNum", "进件量");
		headerMap.put("backNum", "初审退回量");
		headerMap.put("yetNum", "初审未处理量");
		headerMap.put("passNum", "当天进件初审成功量");
		headerMap.put("thenPassNum", "非当天进件初审成功量");
		
		if (flowTimeStrArray != null && flowTimeStrArray.length > 0) {
			flowTimeList = fixFlowTimeList(startDate,flowTimeStrArray);

			ExcelConfBean confBean = new ExcelConfBean();
			confBean.setDataIndex(dataIndexList);
			confBean.setHeaderIndex(headerMap);
			String fname = this.basicInfo.getLocalPath() + "\\" + this.basicInfo.getBankNO() + "全流程报表_" + fileStamp + ".xls";
			confBean.setFileName(fname);
			confBean.setSheetName("全流程报表");
			if (ExcelFactory.getExcelReport(confBean, flowTimeList)) {
//				FileUtil.uploadPackage(smbFilePath, fname);
			}

		}

		log.info("移动终端服务3.0-" + this.basicInfo.getBankNO() + " 全流程报表-结束,定时任务标记" + jobID);
	}
	
	public String[] getFlowTimeWebService(String startDate) throws AxisFault {
		String[] orderAmountStrArray = null;

		Object[] opAddEntryArgs = new Object[] { startDate, this.basicInfo.getBankNO() };
		orderAmountStrArray = new WebServiceDataFactory().getWebServiceData(
				this.basicInfo.getAddress(), opAddEntryArgs, "getAllFlowTimeReport");

		return orderAmountStrArray;
	}
	public List<Map<String, String>> fixFlowTimeList(String startDate,String[] flowTimeStrArray) {
		List<Map<String, String>> flowTimeList = null;
		final String S = "@";

		if (flowTimeStrArray != null && flowTimeStrArray.length > 0) {
			flowTimeList = new ArrayList<Map<String, String>>();
			Map<String, String> recordMap = null;
			for (String record : flowTimeStrArray) {
				String[] columns = record.split(S);
				recordMap = new HashMap<String, String>();
				recordMap.put("cDate", startDate);
				recordMap.put("inComeNum", columns[0]);
				recordMap.put("backNum", columns[1]);
				recordMap.put("yetNum", columns[2]);
				recordMap.put("passNum", columns[3]);
				recordMap.put("thenPassNum", columns[4]);
				flowTimeList.add(recordMap);
			}
			
		}

		return flowTimeList;
	}

}
