package com.shangshang.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseBase {

	private static Configuration conf = null;

	/**
	 * 静态代码块，随着类的加载而执行，只执行一次。
	 */
	static {
		conf = HBaseConfiguration.create();
	}

	/**
	 * 创建表
	 */
	public static boolean createTable(String tablename, String[] cfs)
			throws IOException {
		@SuppressWarnings("resource")
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(tablename)) {
			System.out.println("table is already exist : " + tablename);
			return false;
		} else {
			TableName tm = TableName.valueOf(tablename);
			HTableDescriptor tableDesc = new HTableDescriptor(tm);
			for (int i = 0; i < cfs.length; i++) {
				tableDesc.addFamily(new HColumnDescriptor(cfs[i]));
			}
			admin.createTable(tableDesc);
			System.out.println("createTable success : " + tablename);
			return true;
		}
	}

	/**
	 * 删除表
	 */
	public static boolean deleteTable(String tablename) throws IOException {
		try {
			@SuppressWarnings("resource")
			HBaseAdmin admin = new HBaseAdmin(conf);
			admin.disableTable(tablename);
			admin.deleteTable(tablename);
			System.out.println("deleteTable success : " + tablename);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
			throw e;
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}

	/**
	 * 列出所有table
	 */
	public static List<String> listTable() throws IOException {
		List<String> result = new ArrayList<String>();
		@SuppressWarnings("resource")
		HBaseAdmin admin = new HBaseAdmin(conf);
		for (TableName name : admin.listTableNames()) {
			result.add(name.getNameAsString());
			System.out.println(name.getNameAsString());
		}
		return result;
	}

	/**
	 * 插入一行
	 */
	public static void writeRow(String tablename, String rowkey, String family,
			String qualifier, String value) throws IOException {
		HTable table = new HTable(conf, tablename);
		Put put = new Put(Bytes.toBytes(rowkey));
		put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
				Bytes.toBytes(value));
		table.put(put);
		table.close();
	}

	/**
	 * 插入多行
	 */
	public static void writeRows(String tablename, String rowkey,
			String family, Map<String, String> qv) throws IOException {
		HTable table = new HTable(conf, tablename);
		Put put = new Put(Bytes.toBytes(rowkey));
		for (String qualifier : qv.keySet()) {
			String value = qv.get(qualifier);
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
					Bytes.toBytes(value));
		}
		table.put(put);
		table.close();
	}

	/**
	 * 删除一行或者多行
	 */
	public static boolean deleteRows(String tablename, String... rowkey)
			throws IOException {
		HTable table = new HTable(conf, tablename);
		List<Delete> list = new ArrayList<Delete>();
		for (String rowString : rowkey) {
			Delete d = new Delete(Bytes.toBytes(rowString));
			list.add(d);
		}
		table.delete(list);
		table.close();
		return true;
	}

	/**
	 * 查询一行或者多行
	 */
	public static Result[] selectRows(String tablename, String... rowKey)
			throws IOException {
		HTable table = new HTable(conf, tablename);
		List<Get> gets = new ArrayList<Get>();
		for (String rowString : rowKey) {
			Get g = new Get(Bytes.toBytes(rowString));
			gets.add(g);
		}
		Result[] rs = table.get(gets);
		table.close();
		return rs;
	}

	/**
	 * 根据rowkey的关键字搜索一条记录
	 */
	public static List<Result> selectByRowFilter(String tablename,
			String keyWord) throws IOException {
		List<Result> results = new ArrayList<Result>();
		HTable table = new HTable(conf, tablename);
		Scan scan = new Scan();
		Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,
				new SubstringComparator(keyWord));
		scan.setFilter(filter);
		ResultScanner rs = table.getScanner(scan);
		for (Result res : rs) {
			results.add(res);
		}
		rs.close();
		table.close();
		return results;
	}

	/**
	 * 多个过滤条件，各个条件之间是“与”的关系
	 */
	public static List<Result> selectByFilter(String tablename, List<String> arr)
			throws IOException {
		List<Result> results = new ArrayList<Result>();
		HTable table = new HTable(conf, tablename);
		FilterList filterList = new FilterList();
		Scan scan = new Scan();
		for (String v : arr) {
			String[] s = v.split(",");
			filterList.addFilter(new SingleColumnValueFilter(Bytes
					.toBytes(s[0]), Bytes.toBytes(s[1]), CompareOp.EQUAL, Bytes
					.toBytes(s[2])));
		}
		scan.setFilter(filterList);
		ResultScanner rs = table.getScanner(scan);
		for (Result res : rs) {
			results.add(res);
		}
		rs.close();
		table.close();
		return results;
	}

	/**
	 * 根据范围搜索相关记录
	 */
	public static List<Result> selectByRegions(String tablename,
			String startRow, String stopRow) throws IOException {
		List<Result> results = new ArrayList<Result>();
		HTable table = new HTable(conf, tablename);
		Scan scan = new Scan();
		if (startRow != null)
			scan.setStartRow(Bytes.toBytes(startRow));
		if (stopRow != null)
			scan.setStopRow(Bytes.toBytes(stopRow));
		ResultScanner rs = table.getScanner(scan);
		for (Result res : rs) {
			results.add(res);
		}
		rs.close();
		table.close();
		return results;
	}

	public static List<Result> Scan(String tablename, int size,
			String startRow, String stopRow, String keyWord, List<String> arr)
			throws IOException {
		List<Result> results = new ArrayList<Result>();
		HTable table = new HTable(conf, tablename);
		Scan scan = new Scan();
		scan.setMaxVersions();
		if (size < 0)
			size = 50;
		if (startRow != null)
			scan.setStartRow(Bytes.toBytes(startRow));
		if (stopRow != null)
			scan.setStopRow(Bytes.toBytes(stopRow));
		FilterList filterList = new FilterList();
		if (keyWord != null)
			filterList.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL,
					new SubstringComparator(keyWord)));
		if (arr != null) {
			for (String v : arr) { // 各个条件之间是“与”的关系
				String[] s = v.split(",");
				filterList.addFilter(new SingleColumnValueFilter(Bytes
						.toBytes(s[0]), Bytes.toBytes(s[1]), CompareOp.EQUAL,
						Bytes.toBytes(s[2])));
				// 添加下面这一行后，则只返回指定的cell，同一行中的其他cell不返回
				// s1.addColumn(Bytes.toBytes(s[0]), Bytes.toBytes(s[1]));
			}
		}
		if (keyWord != null || arr != null)
			scan.setFilter(filterList);
		ResultScanner rs = table.getScanner(scan);
		if (startRow != null || stopRow == null) {
			int count = 0;
			for (Result res : rs) {
				results.add(res);
				count++;
				if (size != 0 && count == size) {
					break;
				}
			}
		} else if (stopRow != null) {
			for (Result res : rs) {
				results.add(res);
			}
			int length = results.size();
			if (size != 0 && length - size > 0) {
				results.subList(length - size, length - 1);
			}
		}
		rs.close();
		table.close();
		return results;
	}

	public static void main(String[] args) throws IOException {

		listTable();
		// for (Result rs : selectByRegions(HbaseConfig.METADATA_TABLE,
		// "resource/XTYY/public/china_cia/Layers/_allLayers/",
		// "resource/XTYY/public/china_cia/Layers/_allLayers//L02")) {
		// System.out.println(rs);
		// }
		// System.out.println(selectRows(HbaseConfig.METADATA_TABLE,
		// "resource/XTYY/public/china_cia/Layers/_allLayers//L00"));
	}

}
