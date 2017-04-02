/*
  主程序，该程序支持HTTP多线程断点续传下载
  2014-6-27 made by chenshangshang
 */

package com.rsclouds.Download;

import java.io.IOException;

public class DownloadMain {

	public static void main(String args[]) throws IOException {

		String sourceUrl = args[0];
		String urlString = sourceUrl.substring(0, 3);
		if (urlString.equalsIgnoreCase("htt")) {
			// 使用HTTP下载
			StartDownload.HTTPORFTP = true;
			sourceUrl = args[0];// 资源路径

			String saveFilename = getFileNameFromUrl(sourceUrl);// 保存的文件名
			String savePath = "/root";// 保存的路径
			int threadNum = 8;// 开启的线程数

			// 封装下载相关参数信息
			DownloadBean bean = new DownloadBean();
			bean.setSourceUrl(sourceUrl);
			bean.setSaveFilename(saveFilename);
			bean.setSavePath(savePath);
			bean.setThreadNum(threadNum);

			StartDownload sd = new StartDownload(bean);// 创建开始下载对象
			sd.startDownload();// 开始下载
		} else if (urlString.equalsIgnoreCase("ftp")) {
			// 使用FTP下载
			StartDownload.HTTPORFTP = false;

			int index_1 = sourceUrl.lastIndexOf("?");
			String url_1 = sourceUrl.substring(0, index_1);
			int index_1_1 = url_1.indexOf("/", 6);
			int index_1_2 = url_1.lastIndexOf("/");
			String ip = url_1.substring(6, index_1_1);
			String remoteFileName = getFileNameFromUrl(url_1);
			String remotePath = url_1.substring(index_1_1, index_1_2 + 1);

			String url_2 = sourceUrl.substring(index_1 + 1);
			int index_2 = url_2.lastIndexOf("&");

			String url_3 = url_2.substring(0, index_2);
			String url_4 = url_2.substring(index_2 + 1);

			int index_3 = url_3.lastIndexOf("=");
			String username = url_3.substring(index_3 + 1);

			int index_4 = url_4.lastIndexOf("=");
			String password = url_4.substring(index_4 + 1);

			DownloadBean bean = new DownloadBean(ip, 21, username, password,
					remotePath, remoteFileName, "/root", remoteFileName, 8);
			StartDownload sd = new StartDownload(bean);// 创建开始下载对象
			sd.startDownload();// 开始下载

		} else {
			System.out.println("please use <http> or <ftp>");
		}
	}

	public static String getFileNameFromUrl(String url) {
		String name = new Long(System.currentTimeMillis()).toString() + ".X";
		int index_1 = url.lastIndexOf("/");

		if (index_1 > 0) {
			name = url.substring(index_1 + 1);
			int index_2 = name.lastIndexOf("?");
			if (index_2 != -1) {
				name = name.substring(0, index_2);
			}
			if (name.trim().length() > 0) {
				return name;
			}
		}
		return name;
	}
}
