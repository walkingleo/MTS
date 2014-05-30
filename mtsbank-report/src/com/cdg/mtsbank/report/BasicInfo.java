package com.cdg.mtsbank.report;


public class BasicInfo {

	private String localPath; // 产生的报表文件本地存放路径
	private String userName; // 远程计算机用户名
	private String password; // 远程计算机密码
	private String serverIP; // 远程计算机IP地址及报表存放目录
	private String bankNO; // 银行号
	private String address; //webservice地址

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
