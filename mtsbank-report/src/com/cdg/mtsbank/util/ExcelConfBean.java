package com.cdg.mtsbank.util;

import java.util.List;
import java.util.Map;
/**
 * 
 * @author wangtongmin
 * 生成EXCEL需要的相关配置信息
 *
 */
public class ExcelConfBean {
	/**文件名*/
	private String fileName;
	/**sheet名**/
	private String sheetName;
	
	/***
	 * map.put("headerName","姓名") //表头显示名称
	 * map.put("name","name")      //匹配获取数据名称
	 * list.add(map)
	 */
	private Map<String,String> headerIndex;
	
	private List<String> dataIndex;

	public List<String> getDataIndex() {
		return dataIndex;
	}

	public void setDataIndex(List<String> dataIndex) {
		this.dataIndex = dataIndex;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Map<String,String> getHeaderIndex() {
		return headerIndex;
	}

	public void setHeaderIndex(Map<String,String> headerIndex) {
		this.headerIndex = headerIndex;
	} 

}
