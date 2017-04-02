package com.shangshang.matrix;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainRun {

	public static final String HDFS = "hdfs://192.168.2.3:8020";
	public static final Pattern DELIMITER = Pattern.compile("[\t,]");

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Please start a task:");
		} else {
			doAction(Integer.parseInt(args[0]));
		}
	}

	public static void doAction(int task) {
		switch (task) {
		case 1:
			martrixMultiply();
			break;
		case 2:
			sparseMartrixMultiply();
			break;
		}
	}

	public static void martrixMultiply() {
		Map<String, String> path = new HashMap<String, String>();

		path.put("m1", "m1.csv");
		path.put("m2", "m2.csv");
		path.put("input", HDFS + "/chenshang/matrix");
		path.put("input1", HDFS + "/chenshang/matrix/m1");
		path.put("input2", HDFS + "/chenshang/matrix/m2");
		path.put("output", HDFS + "/chenshang/matrix/output");

		try {
			MartrixMultiply.run(path);// 启动程序
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void sparseMartrixMultiply() {
		Map<String, String> path = new HashMap<String, String>();
		path.put("m1", "logfile/matrix/sm1.csv");// 本地的数据文件
		path.put("m2", "logfile/matrix/sm2.csv");
		path.put("input", HDFS + "/chenshang/sparseMatrix");// HDFS的目录
		path.put("input1", HDFS + "/chenshang/sparseMatrix/m1");
		path.put("input2", HDFS + "/chenshang/sparseMatrix/m2");
		path.put("output", HDFS + "/chenshang/sparseMatrix/output");

		try {
			SparseMartrixMultiply.run(path);// 启动程序
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
