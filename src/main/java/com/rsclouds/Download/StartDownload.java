/*
 * 开始下载文件
 * 2014-6-27 made by chenshangshang,zyk
 */
package com.rsclouds.Download;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


public class StartDownload {
	public static boolean HTTPORFTP = true; // 定义一个开关，判断是FTP还是HTTP
	private DownloadBean bean;// 封装下载参数信息实体
	private File positionFile;// 记录下载位置文件
	private long[] startPos;// 开始位置数组
	private long[] endPos;// 结束位置数组
	private static int sleepTime = 2000;// 每隔0.5秒向记录下载位置的缓存文件写入各个下载线程当前的下载位置
	private DownloadThread[] downloadThread;// 用于下载文件的线程数组
	private long fileLength;// 总的字节数
	@SuppressWarnings("unused")
	private boolean first = true;// 是否刚开始下载，如果不是刚开始下载则为false(即已经开始下载且暂停过)
	private boolean stop = false;// 下载结束标志
	public FTPClient ftpClient = new FTPClient();// FTP服务端对象

	// =======下载进度信息
	private long beginTime;
	private File saveFile;// 保存文件对象
	public static long downloaded = 0; // 已经下载的字节数(全局)
	public static long downloadednow = 0;// 断点下载的已下载字节数（局部）为了计算当前下载速度

	private final Log log = LogFactory.getLog(getClass().getName());

	// 构造方法
	public StartDownload(DownloadBean bean) {
		this.bean = bean;
		// 创建saveFile
		saveFile = new File(new File(this.bean.getSavePath()),
				bean.getSaveFilename());
		// 创建saveFile.lck，用于记录下载位置
		positionFile = new File(new File(this.bean.getSavePath()),
				this.bean.getSaveFilename() + ".lck");
	}

	// 连接FTP服务端
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

	// 获取FTP文件的长度
	public long getHTTPFileSize() {
		long size = 0;
		try {
			FTPFile[] files = ftpClient.listFiles(bean.getRemotePath());
			for (int i = 0; i < files.length; i++) {
				FTPFile file = files[i];
				String fileName = file.getName();
				if (fileName.equals(bean.getRemoteFileName())) {
					size = file.getSize();
					return size;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		}
		return size;
	}

	// 开始下载
	public void startDownload() throws IOException {
		if (HTTPORFTP) {
			// 建立连接请求
			HttpURLConnection conn = null;
			try {
				URL url = new URL(this.bean.getSourceUrl());// 获取资源路径
				conn = (HttpURLConnection) url.openConnection();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// 获取文件长度，单位是byte
			String contentLength = conn.getHeaderField("Content-Length");

			if (contentLength != null) {
				try {
					fileLength = Long.parseLong(contentLength);
				} catch (Exception e) {
				}
			}
		} else {
			// 建立连接请求
			connect(bean.getIp(), bean.getPort(), bean.getUsername(),
					bean.getPassword());

			fileLength = getHTTPFileSize();
		}

		log.info("下载文件的大小:" + getFileFormat(fileLength));

		if (fileLength <= 0) {
			// 不支持多线程下载,采用单线程下载
			log.info("服务器不能返回文件大小，采用单线程下载");
			bean.setThreadNum(1);
		}

		File parent = ((File) saveFile).getParentFile();
		if (fileLength > 0 && parent.getFreeSpace() < fileLength) {
			try {
				throw new Exception("磁盘空间不够");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		log.info("分配线程。线程数量=" + bean.getThreadNum() + ",总字节数=" + fileLength
				+ ",字节/线程=" + fileLength / bean.getThreadNum());

		// 如果缓存文件已经存在，表明之前已经下载过一部分
		if (positionFile.exists()) {
			first = false;
			// 读取缓存文件中的下载位置，即每个下载线程的开始位置和结束位置，
			// 将读取到的下载位置写入到开始数组和结束数组
			readDownloadPosition();
		} else {
			// 如果是刚开始下载
			getDownloadPosition();// 获取下载位置
		}

		if (!stop) {
			// 创建下载线程数组,每个下载线程负责各自的文件块下载
			downloadThread = new DownloadThread[bean.getThreadNum()];
			for (int i = 0; i < bean.getThreadNum(); i++) {
				if (HTTPORFTP) {
					downloadThread[i] = new DownloadThread(bean.getSourceUrl(),
							bean.getSavePath() + File.separator
									+ bean.getSaveFilename(), startPos[i],
							endPos[i], i);
				} else {
					downloadThread[i] = new DownloadThread(bean.getIp(),
							bean.getPort(), bean.getUsername(),
							bean.getPassword(), bean.getRemotePath()
									+ bean.getRemoteFileName(),
							bean.getSavePath() + File.separator
									+ bean.getSaveFilename(), startPos[i],
							endPos[i], i);
				}

				downloadThread[i].start();// 启动线程，开始下载
				beginTime = System.currentTimeMillis();
				System.out.println("线程" + i + "开始下载");
			}
			// 向缓存文件循环写入下载文件位置信息
			while (!stop) {
				try {
					writeDownloadPosition();// 更新下载位置信息
					Thread.sleep(sleepTime);// 每隔2秒更新一次下载位置信息
					// 获取下载信息，输出到控制台
					log.info(getDesc());
					stop = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (int i = 0; i < startPos.length; i++)// 判断是否所有下载线程都执行结束
				{
					if (!downloadThread[i].isDownloadOver()) {
						stop = false;// 只要有一个下载线程没有执行结束，则文件还没有下载完毕
						break;
					}
				}
			}
			// 删除下载位置缓存文件
			positionFile.delete();
			System.out.println("所有下载线程执行完毕,文件下载完成");
		}
	}

	// 更新下载位置缓存文件的下载位置
	public void writeDownloadPosition() {
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(
					positionFile));
			// 将一个 int 值以 4-byte 值形式写入基础输出流中，先写入高字节。
			// 如果没有抛出异常，则计数器 written 增加 4。
			dos.writeInt(bean.getThreadNum());
			for (int i = 0; i < bean.getThreadNum(); i++) {
				// 将一个 long 值以 8-byte 值形式写入基础输出流中，先写入高字节。
				// 如果没有抛出异常，则计数器 written增加 8。
				dos.writeLong(downloadThread[i].getStartPos());
				dos.writeLong(downloadThread[i].getEndPos());
			}
			dos.writeLong(downloaded);
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 读取已经存在的缓存文件中的下载位置
	private void readDownloadPosition() {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(
					positionFile));
			// 获取下载位置的数目，即有多少个开始位置，多少个结束位置
			int DownloadNum = dis.readInt();
			startPos = new long[DownloadNum];
			endPos = new long[DownloadNum];
			for (int i = 0; i < DownloadNum; i++)// 获取开始位置数组和结束位置数组
			{
				startPos[i] = dis.readLong();
				endPos[i] = dis.readLong();
			}
			downloaded = dis.readLong();
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取下载位置(刚开始下载的情况)的起始指针和结束指针，并存放到数组中
	private void getDownloadPosition() {
		startPos = new long[bean.getThreadNum()];// 创建开始位置数组
		endPos = new long[bean.getThreadNum()];// 创建结束位置数组
		if (fileLength == -1) {
			stop = true;
		} else if (fileLength == -2) {
			stop = true;
		} else if (fileLength > 0) {
			for (int i = 0, len = bean.getThreadNum(); i < len; i++) {
				long size = i * (fileLength / len);
				startPos[i] = size;
				// 设置最后一个结束点的位置
				if (i == len - 1) {
					endPos[i] = fileLength;
				} else {
					size = (i + 1) * (fileLength / len);
					endPos[i] = size;
				}
			}
		} else {
			stop = true;
		}
	}

	// 获取相关下载信息
	private String getDesc() {
		long downloadBytes = downloaded;
		long dwonloadednow = downloadednow;
		return String
				.format("已下载/总大小=%s/%s(%s),速度:%s,耗时:%s,剩余大小:%d",
						getFileFormat(downloadBytes),
						getFileFormat(fileLength),
						getProgress(fileLength, downloadBytes),
						getFileFormat(dwonloadednow
								/ ((System.currentTimeMillis() - beginTime) / 1000 + 1)),
						getTime((System.currentTimeMillis() - beginTime) / 1000),
						fileLength - downloadBytes);
	}

	// 格式化输出
	private String getFileFormat(long totals) {
		// 计算文件大小
		int i = 0;
		String j = "BKMGT";
		float s = totals;
		while (s > 1024) {
			s /= 1024;
			i++;
		}
		return String.format("%.2f", s) + j.charAt(i);
	}

	// 下载进度
	private String getProgress(long totals, long read) {
		if (totals == 0)
			return "0%";
		return String.format("%d", read * 100 / totals) + "%";
	}

	// 耗时
	private String getTime(long seconds) {
		int i = 0;
		String j = "秒分时天";
		long s = seconds;
		String result = "";
		while (s > 0) {
			if (s % 60 > 0) {
				result = String.valueOf(s % 60) + (char) j.charAt(i) + result;
			}
			s /= 60;
			i++;
		}
		return result;
	}

}
