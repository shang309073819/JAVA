package com.shangshang.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * MapReduce任务从Hbase表中读取数据，写入到HDFS中
 * 
 * @author chenshang
 *
 */
public class ImportFromHbase {

	static class MapperRead extends TableMapper<Text, Text> {

		@Override
		protected void map(
				ImmutableBytesWritable key,
				Result value,
				Mapper<ImmutableBytesWritable, Result, Text, Text>.Context context)
				throws IOException, InterruptedException {

			String name = new String(value.getValue(Bytes.toBytes("family"),
					Bytes.toBytes("name")));
			String starttime = new String(value.getValue(
					Bytes.toBytes("family"), Bytes.toBytes("starttime")));
			String endtime = new String(value.getValue(Bytes.toBytes("family"),
					Bytes.toBytes("endtime")));
			String price = new String(value.getValue(Bytes.toBytes("family"),
					Bytes.toBytes("price")));
			String rowkey = new String(value.getRow());
			String str = name + "," + starttime + "," + endtime + "," + price;

			context.write(new Text(rowkey), new Text(str));
		}
	}

	static class ReducerExport extends Reducer<Text, Text, Text, Text> {

		@Override
		protected void reduce(Text key, Iterable<Text> value,
				Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			for (Text text : value) {
				context.write(key, text);
			}
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = HBaseConfiguration.create();
		// 指定输入的表
		conf.set(TableInputFormat.INPUT_TABLE, "shedule");
		// 指定读取的起始行
		conf.set(TableInputFormat.SCAN_ROW_START, "17");
		// 指定读取的结束行
		conf.set(TableInputFormat.SCAN_ROW_STOP, "18");

		Job job = Job.getInstance(conf, "ImportFromHbase");
		job.setJarByClass(ImportFromHbase.class);
		job.setInputFormatClass(TableInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		// 指定写入HDFS中的文件目录地址
		FileOutputFormat.setOutputPath(job, new Path("/tmp/out/"));

		job.setInputFormatClass(TableInputFormat.class);
		job.setMapperClass(MapperRead.class);
		job.setReducerClass(ReducerExport.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.waitForCompletion(true);
	}
}
