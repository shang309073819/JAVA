package com.shangshang.hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.hadoop.hbase.client.Result;

public class HbaseExample {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String jobid = UUID.randomUUID().toString();
		System.out.println(jobid);
		// 新建一个table
		HbaseBase.createTable(HbaseConfig.JOB_TABLE,
				new String[] { HbaseConfig.JOB_META });
		String rowkey = jobid + "_" + "http://www.sina.com/" + Math.random();
		Map<String, String> map = new HashMap<String, String>();
		map.put(HbaseConfig.JOB_META_PID, "14523");
		map.put(HbaseConfig.JOB_META_TYPE, "cutting");
		map.put(HbaseConfig.JOB_META_NODE, "192.168.2.14");
		map.put(HbaseConfig.JOB_META_OUT_PATH,
				"resource/XTYY/public/china_cia/Layers/_allLayers//L00");
		map.put(HbaseConfig.JOB_META_PROGRESS, "55");
		map.put(HbaseConfig.JOB_META_STATE, "pasue");
		map.put(HbaseConfig.JOB_META_JID, "job_276123121787");
		// 插入一条模拟记录
		HbaseBase.writeRows(HbaseConfig.JOB_TABLE, rowkey,
				HbaseConfig.JOB_META, map);
		// 搜索指定范围的记录
		System.out.println("搜索指定范围的记录:");
		for (Result rs : HbaseBase.selectByRegions(HbaseConfig.JOB_TABLE, null,
				null)) {// 设为null表示所有范围
			System.out.println("==================");
			System.out.println(HbaseUtils.result2Job(rs).toString());
		}
		// 搜索rowkey包含指定关键字的记录
		System.out.println("搜索rowkey包含指定关键字的记录:");
		for (Result rs : HbaseBase.selectByRowFilter(HbaseConfig.JOB_TABLE,
				"www.sina.com")) {
			System.out.println("==================");
			System.out.println(HbaseUtils.result2Job(rs).toString());
		}
	}
}
