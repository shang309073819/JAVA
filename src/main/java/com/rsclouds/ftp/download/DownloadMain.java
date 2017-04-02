/*
 * 主程序，该程序支持Ftp多线程分块断点续传下载
 * @author zyk
 */

package com.rsclouds.ftp.download;

import java.io.IOException;

public class DownloadMain {
	public static void main(String args[]) throws IOException {
		/*
		 * String sourceUrl =
		 * "http://dldir1.qq.com/qqfile/qq/QQ6.0/11754/QQ6.0.exe";// 资源路径 String
		 * saveFilename = "QQ.exe";// 保存的文件名 String savePath = "D://";// 保存的路径
		 * int threadNum = 8;// 开启的线程数
		 * 
		 * // 封装下载相关参数信息 DownloadBean bean = new DownloadBean();
		 * bean.setSourceUrl(sourceUrl); bean.setSaveFilename(saveFilename);
		 * bean.setSavePath(savePath); bean.setThreadNum(threadNum);
		 */

		// DownloadBean bean = new DownloadBean("192.168.106.250", 21, "chenss",
		// "abcd@1234", "/PPT/PPT/%B2%FA%C6%B7%B0%D7%C6%A4%CA%E9/",
		// "地理信息网络及会商系统应用白皮书-王永强-_20140521.doc",
		// "/Users/chenshang/Downloads",
		// "地理信息网络及会商系统应用白皮书-王永强-_20140521.doc", 1);

		// DownloadBean bean = new DownloadBean("192.168.106.250", 21, "wugq",
		// "abcd@1234", "/PPT/PPT/", "PPT目录一览表2014.7.7.xls",
		// "/Users/chenshang/Downloads", "PPT目录一览表2014.7.7.xls", 2);

		// DownloadBean bean = new DownloadBean("ftp.pku.edu.cn", 21, null,
		// null,
		// "/pub/Adobe/", "AdbeRdr11000_zh_CN.exe",
		// "/Users/chenshang/Downloads", "AdbeRdr11000_zh_CN.exe", 2);

		/*
		 * DownloadBean bean = new DownloadBean("192.168.101.21", 21, "nanlin",
		 * "123456", "/TIF/",
		 * "CA_eMTH_NDVI.2014.111-120.QKM.COMPRES.005.2014127014444.zip",
		 * "D://", "test.zip", 10);
		 */

		DownloadBean bean = new DownloadBean("192.168.2.4", 21, "ftp",
				"rsclouds@456", "/cutting-src/", "conghuashi_20111111_georef_WGS84.img",
				"/Users/chenshang/Downloads", "conghuashi_20111111_georef_WGS84.img", 4);

		StartDownload sd = new StartDownload(bean);// 创建开始下载对象
		sd.startDownload();// 开始下载
	}
}
