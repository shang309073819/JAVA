package com.shangshang.hbase;

public class HbaseConfig {

	/* gt-data */
	public static String METADATA_TABLE = "metadata";
	public static String RESOURCE_TABLE = "resource";
	public static String METADATA_ATTS = "atts";
	public static String METADATA_ATTS_URL = "url";
	public static String METADATA_ATTS_DFS = "dfs";
	public static String METADATA_ATTS_SIZE = "size";
	public static String METADATA_ATTS_TIME = "time";
	public static String RESOURCE_IMG = "img";
	public static String RESOURCE_IMG_DATA = "data";
	public static String RESOURCE_IMG_LINKS = "links";
	public static String HBASE_SEPARATOR = "/";

	/* job_meta */
	public static String JOB_TABLE = "job";
	public static String JOB_META = "meta";
	public static String JOB_META_PID = "pid";
	public static String JOB_META_OUT_PATH = "out_path";
	public static String JOB_META_NODE = "node";
	public static String JOB_META_STATE = "state";
	public static String JOB_META_TYPE = "type";
	public static String JOB_META_PROGRESS = "progress";// 进度
	public static String JOB_META_JID = "jid";// 记录任务在hadoop中执行的job号
	public static String JOB_META_START_TIME = "start_time";
	public static String JOB_META_END_TIME = "end_time";

	public static String[] LEVEL = { "_allLayers", "L00", "L01", "L02", "L03",
			"L04", "L05", "L06", "L07", "L08", "L09", "L0a", "L0b", "L0c",
			"L0d", "L0e", "L0f", "L10", "L11", "L12", "L13", "L14" };

}
