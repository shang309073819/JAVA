/*
 * 下载线程
 * @author zyk
 */

package com.rsclouds.ftp.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class DownloadThread extends Thread {
	String ip; // 下载文件的地址
	int port; // 地址端口号
	String username; // 用户名
	String password; // 密码
	String remoteFile; // 远程文件路径+文件名称
	private long startPos;// 下载开始位置
	private long endPos;// 下载结束位置
	private int threadId;// 线程ID
	private static int BUFF_LENGTH = 1024 * 8;// 缓冲区大小 8k
	private boolean downloadOver = false;// 该线程是否下载完毕
	private RandomAccessFile saveFile;// 保存文件对象
	private Object object;

	public FTPClient ftpClient = new FTPClient();

	public DownloadThread(String ip, int port, String username,
			String password, String remoteFile, String fullName, long startPos,
			long endPos, int threadId, Object object) throws IOException {
		// this.sourceUrl = sourceUrl;
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
		this.remoteFile = remoteFile;
		this.startPos = startPos;
		this.endPos = endPos;
		this.threadId = threadId;
		this.object = object;

		saveFile = new RandomAccessFile(fullName, "rw");
		saveFile.seek(startPos);

	}

	/**
	 * 连接到FTP服务器
	 * 
	 * @param hostname
	 *            主机名
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 是否连接成功
	 * @throws IOException
	 */
	public boolean connect(String hostname, int port, String username,
			String password) throws IOException {
		ftpClient.connect(hostname, port);
		ftpClient.setControlEncoding(System.getProperty("file.encoding"));
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password)) {
				return true;
			}
		}
		disconnect();
		return false;
	}

	/**
	 * 断开与远程服务器的连接
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	public void run() {
		try {
			System.out.println("Thread " + threadId + " url start >> "
					+ startPos + "------end >> " + endPos);
			connect(ip, port, username, password);

			// 设置被动模式
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			int num = ftpClient.rest(String.valueOf(startPos));// 设置开始点偏移量
			//System.out.println("num is:" + num);

			InputStream is = ftpClient.retrieveFileStream(new String(remoteFile
					.getBytes("UTF-8"), "iso-8859-1"));

			byte[] buff = new byte[BUFF_LENGTH];// 创建缓冲区
			int length = -1;
			boolean flag = true;
			while ((length = is.read(buff)) > 0 && startPos < endPos
					&& !downloadOver && flag) {
				// System.out.println(Thread.currentThread().getName()
				// + ":length is " + length);
				if ((startPos + length) >= endPos) {
					saveFile.write(buff, 0, (int) (endPos - startPos));
					// synchronized (object) {
					// saveFile.write(buff, 0, (int) (endPos - startPos));
					// }
					flag = false;
				} else {
					saveFile.write(buff, 0, length); // 写入文件内容
					// synchronized (object) {
					// saveFile.write(buff, 0, length); // 写入文件内容
					// }
				}
				startPos = startPos + length;
			}
			System.out.println("线程" + threadId + "执行结束");
			this.downloadOver = true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭打开的文件
				saveFile.close();
			} catch (IOException e) {
				e.printStackTrace();
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
