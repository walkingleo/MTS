package com.cdg.mtsbank.util;

import java.util.List;
import java.util.Map;
/**
 * 
 * @author wangtongmin
 * ����EXCEL��Ҫ�����������Ϣ
 *
 */
public class ExcelConfBean {
	/**�ļ���*/
	private String fileName;
	/**sheet��**/
	private String sheetName;
	
	/***
	 * map.put("headerName","����") //��ͷ��ʾ����
	 * map.put("name","name")      //ƥ���ȡ��������
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
