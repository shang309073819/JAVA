/*
 * 封装下载的相关参数信息
 * 2014-6-27 made by chenshangshang, zyk
 */
package com.rsclouds.Download;

public class DownloadBean {

	// HTTP的相关信息
	private String sourceUrl;// 资源路径
	private String saveFilename;// 保存文件名
	private String savePath;// 保存路径
	private int threadNum;// 线程数

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSaveFilename() {
		return saveFilename;
	}

	public void setSaveFilename(String saveFilename) {
		this.saveFilename = saveFilename;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	// FTP的相关信息
	private String ip; // ftp地址
	private int port; // ftp端口号
	private String username; // 用户名
	private String password; // 密码
	private String remotePath; // ftp文件存放路径
	private String remoteFileName; // ftp文件名字

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getRemoteFileName() {
		return remoteFileName;
	}

	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * 默认初始化
	 */
	public DownloadBean() {
		// default 5
		this("", 21, "", "", "", "", "", "", 5);
	}

	/**
	 * 下载文件信息初始化
	 */
	public DownloadBean(String ip, int port, String username, String password,
			String remotePath, String remoteFileName, String localPath,
			String localFileName, int nSplitter) {
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
		this.remotePath = remotePath;
		this.remoteFileName = remoteFileName;
		this.savePath = localPath;
		this.saveFilename = localFileName;
		this.threadNum = nSplitter;
	}
}
