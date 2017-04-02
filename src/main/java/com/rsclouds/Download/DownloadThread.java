/*
 * 下载线程
 * 2014-6-27 made by chenshangshang,zyk
 */

package com.rsclouds.Download;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class DownloadThread extends Thread {
	// HTTP的信息
	private String sourceUrl;// 资源路径
	private long startPos;// 下载开始位置
	private long endPos;// 下载结束位置
	private int threadId;// 线程ID
	private static int BUFF_LENGTH = 1024 * 8;// 缓冲区大小 8k
	private boolean downloadOver = false;// 该线程是否下载完毕
	private RandomAccessFile saveFile;// 保存文件对象

	// FTP的信息
	String ip; // 下载文件的地址
	int port; // 地址端口号
	String username; // 用户名
	String password; // 密码
	String remoteFile; // 远程文件路径+文件名称

	public DownloadThread(String sourceUrl, String fullName, long startPos,
			long endPos, int threadId) throws IOException {
		this.sourceUrl = sourceUrl;
		this.startPos = startPos;
		this.endPos = endPos;
		this.threadId = threadId;

		saveFile = new RandomAccessFile(fullName, "rw");
		// 定位写入文件的位置
		saveFile.seek(startPos);
	}

	// FTP的信息
	public FTPClient ftpClient = new FTPClient();

	public DownloadThread(String ip, int port, String username,
			String password, String remoteFile, String fullName, long startPos,
			long endPos, int threadId) throws IOException {
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
		this.remoteFile = remoteFile;
		this.startPos = startPos;
		this.endPos = endPos;
		this.threadId = threadId;

		saveFile = new RandomAccessFile(fullName, "rw");
		saveFile.seek(startPos);

	}

	// 连接到FTP服务器
	public boolean connect(String hostname, int port, String username,
			String password) throws IOException {
		ftpClient.connect(hostname, port);
		ftpClient.setControlEncoding("GBK");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password)) {
				return true;
			}
		}
		disconnect();
		return false;
	}

	// 断开与远程服务器的连接
	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	public void run() {

		if (StartDownload.HTTPORFTP) {
			BufferedInputStream inputStream = null;
			try {
				URL url = new URL(this.sourceUrl);// 创建URL对象

				// 默认的会处理302请求
				HttpURLConnection.setFollowRedirects(false);
				HttpURLConnection httpConnection = null;
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestProperty("RANGE", "bytes=" + startPos
						+ "-");
				int responseCode = httpConnection.getResponseCode();

				if (responseCode < 200 || responseCode >= 400) {
					throw new IOException("服务器返回无效信息:" + responseCode);
				}
				inputStream = new BufferedInputStream(
						httpConnection.getInputStream()); // 获取文件输入流，读取文件内容

				byte[] buff = new byte[BUFF_LENGTH];// 创建缓冲区
				int length = -1;
				while ((length = inputStream.read(buff)) > 0
						&& startPos < endPos && !downloadOver) {
					saveFile.write(buff, 0, length); // 写入文件内容
					// 写入已经下载的文件。
					StartDownload.downloaded = StartDownload.downloaded
							+ length;
					StartDownload.downloadednow = StartDownload.downloadednow
							+ length;
					startPos = startPos + length;
				}
				System.out.println("线程" + threadId + "执行结束");
				this.downloadOver = true;
				httpConnection.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					// 关闭流
					if (inputStream != null)
						inputStream.close();
					// 关闭打开的文件
					saveFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			BufferedInputStream inputStream = null;
			try {

				connect(ip, port, username, password);

				// 设置被动模式
				ftpClient.enterLocalPassiveMode();
				// 设置以二进制方式传输
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.rest(String.valueOf(startPos));// 设置开始点偏移量
				inputStream = new BufferedInputStream(
						ftpClient.retrieveFileStream(new String(remoteFile
								.getBytes("GBK"), "iso-8859-1"))); // 获取文件输入流，读取文件内容

				byte[] buff = new byte[BUFF_LENGTH];// 创建缓冲区
				int length = -1;
				while ((length = inputStream.read(buff)) > 0
						&& startPos < endPos && !downloadOver) {
					saveFile.write(buff, 0, length); // 写入文件内容
					// 写入已经下载的文件。
					StartDownload.downloaded = StartDownload.downloaded
							+ length;
					StartDownload.downloadednow = StartDownload.downloadednow
							+ length;
					startPos = startPos + length;
				}
				System.out.println("线程" + threadId + "执行结束");
				this.downloadOver = true;
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					// 关闭流
					if (inputStream != null)
						inputStream.close();
					// 关闭打开的文件
					saveFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isDownloadOver()// 返回该线程下载是否完成的标志
	{
		return downloadOver;
	}

	public long getStartPos()// 获取当前开始位置
	{
		return startPos;
	}

	public long getEndPos()// 获取结束位置
	{
		return endPos;
	}
}
