package com.cdg.mtsbank.report;


public class BasicInfo {

	private String localPath; // �����ı����ļ����ش��·��
	private String userName; // Զ�̼�����û���
	private String password; // Զ�̼��������
	private String serverIP; // Զ�̼����IP��ַ��������Ŀ¼
	private String bankNO; // ���к�
	private String address; //webservice��ַ

	public String getBankNO() {
		return bankNO;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public void setBankNO(String bankNO) {
		this.bankNO = bankNO;
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
