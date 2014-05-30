package com.cdg.mtsbank.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.cdg.mtsbank.util.DateUtil;
import com.cdg.mtsbank.util.ExcelConfBean;
import com.cdg.mtsbank.util.ExcelFactory;
import com.cdg.mtsbank.util.FileUtil;

public class FlowTimeReport{

	private static Logger log = Logger.getLogger(FlowTimeReport.class);

	private BasicInfo basicInfo;
	

	public BasicInfo getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(BasicInfo basicInfo) {
		this.basicInfo = basicInfo;
	}

	

	public void getFlowTimeReport() throws AxisFault {

		String jobID = DateUtil.getDateToStringThree(Calendar.getInstance()
				.getTime());
		log.info("移动终端服务系统3.0-" + basicInfo.getBankNO() + "-时效报表-开始,定时任务标记" + jobID);

		// 报表文件名称 YYYYMMDDHHMMSS
		String fileStamp = DateUtil.getDateToStringThree(Calendar.getInstance()
				.getTime());
		
		//
//		 String startDate = DateUtil.getTodayNextYYYY_MM_DD(-1); //eg.20120821
		String startDate = "2014-04-29";

		String smbFilePath = "smb://".concat(basicInfo.getUserName()).concat(":").concat(
				basicInfo.getPassword()).concat("@").concat(basicInfo.getServerIP());
//		String reportFilePath = localPath + "\\" + this.getBankNO() + "时效报表_"
//				+ fileStamp + ".xls";

		String[] flowTimeStrArray = null;
		flowTimeStrArray = getFlowTimeWebService(startDate);

		List<Map<String, String>> flowTimeList = null;


		System.out.println("数据长度为：" + flowTimeStrArray.length);
		List<String> dataIndexList = new ArrayList<String>();
		dataIndexList.add("OrderNo");
		dataIndexList.add("ImNum");
		dataIndexList.add("UploadDate");
		dataIndexList.add("RecieveDate");
		dataIndexList.add("CsBackDate");
		dataIndexList.add("CsFinishDate");
		dataIndexList.add("TimePerform");
		dataIndexList.add("CsRen");
		dataIndexList.add("TgRen");
		
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("OrderNo", "申请编号");
		headerMap.put("ImNum", "份数");
		headerMap.put("UploadDate", "上传时间");
		headerMap.put("RecieveDate", "初审接收时间");
		headerMap.put("CsBackDate", "初审退回时间");
		headerMap.put("CsFinishDate", "初审回传时间");
		headerMap.put("TimePerform", "初审时效（分钟）");
		headerMap.put("CsRen", "初审人");
		headerMap.put("TgRen", "推广人");
		
		if (flowTimeStrArray != null && flowTimeStrArray.length > 0) {
			flowTimeList = fixFlowTimeList(flowTimeStrArray);

			ExcelConfBean confBean = new ExcelConfBean();
			confBean.setDataIndex(dataIndexList);
			confBean.setHeaderIndex(headerMap);
			String fname = basicInfo.getLocalPath() + "\\" + basicInfo.getBankNO() + "初审时效报表_" + fileStamp + ".xls";
			confBean.setFileName(fname);
			confBean.setSheetName("初审时效报表");
			if (ExcelFactory.getExcelReport(confBean, flowTimeList)) {
//				FileUtil.uploadPackage(smbFilePath, fname);
			}

		}

		log.info("移动终端服务3.0-" + basicInfo.getBankNO() + " 初审时效报表-结束,定时任务标记" + jobID);
	}

	public String[] getFlowTimeWebService(String startDate) throws AxisFault {
		String[] orderAmountStrArray = null;

		Object[] opAddEntryArgs = new Object[] { startDate, basicInfo.getBankNO() };
		orderAmountStrArray = new WebServiceDataFactory().getWebServiceData(
				basicInfo.getAddress(), opAddEntryArgs, "getReportFlowTime");

		return orderAmountStrArray;
	}

	public List<Map<String, String>> fixFlowTimeList(String[] flowTimeStrArray) {
		List<Map<String, String>> flowTimeList = null;
		final String STR_DELIMITER = "@";
		if (flowTimeStrArray != null && flowTimeStrArray.length > 0) {
			flowTimeList = new ArrayList<Map<String, String>>();
			String _orderNo = "";
			String _orderStatus = "";
			Map<String, String> recordMap = null;
			for (String record : flowTimeStrArray) {
				String[] columns = record.split(STR_DELIMITER);
				if (!_orderNo.equals(columns[0])) {
					if (null != recordMap) {
						flowTimeList.add(recordMap);
					}
					recordMap = new TreeMap<String, String>();
				} else if ("03".equals(_orderStatus)) {
					flowTimeList.add(recordMap);
					recordMap = new TreeMap<String, String>();
				}
				if ("01".equals(columns[2])) {
					recordMap.put("OrderNo", columns[0]);
					recordMap.put("ImNum", columns[1]);
					recordMap.put("TgRen", columns[5]);
					if (null != columns[3] && columns[3].length() > 0) {
						Date date16 = DateUtil.getStrToDate(columns[3]);
						date16.setTime((date16.getTime() + (1000 * 10)));
						recordMap.put("RecieveDate", DateUtil.getDateTostr(date16));
					}
					recordMap.put("UploadDate", columns[3]);
				}
				if ("02".equals(columns[2])) {// 初审完成
					recordMap.put("CsFinishDate", columns[3]);
					String recieveDate = recordMap.get("RecieveDate");
					
					recordMap.put("CsRen", columns[4]);
					String timePerform = "";
					try {
						timePerform = DateUtil.getIntervalMinutes(columns[3], recieveDate);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.error("Calculate timePerform error with :"+columns[3] + " and "+recieveDate);
					}
					recordMap.put("TimePerform", timePerform);
				}

				if ("03".equals(columns[2])) {// 初审退回
					recordMap.put("CsBackDate", columns[3]);
					recordMap.put("CsRen", columns[4]);
					String timePerform = "";
					String recieveDate = recordMap.get("RecieveDate");
					try {
						timePerform = DateUtil.getIntervalMinutes(columns[3], recieveDate);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					recordMap.put("TimePerform", timePerform);
				}
				_orderStatus = columns[2];
				_orderNo = columns[0];
			}
			if (null != recordMap) {
				flowTimeList.add(recordMap);
			}
		}

		return flowTimeList;
	}


}
