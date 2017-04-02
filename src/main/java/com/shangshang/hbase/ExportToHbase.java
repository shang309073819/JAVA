package com.shangshang.hbase;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

/**
 * MapReduce 作业从一个文件中读取数据并写入HBase表
 * 
 * 1、需要创建shedule表
 * 
 * create 'shedule', 'family'
 * 
 * 2、需要准备数据
 * 
 * D11,12:20:22,13:22:29,100
 * 
 * G22,12:22:11,23:00:00,230
 * 
 * @author chenshang
 *
 */
public class ExportToHbase {
	static class Hmap extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			context.write(new Text(key.toString()), new Text(value));
		}
	}

	static class Hreduce extends
			TableReducer<Text, Text, ImmutableBytesWritable> {

		@Override
		protected void reduce(
				Text key,
				Iterable<Text> value,
				Reducer<Text, Text, ImmutableBytesWritable, Mutation>.Context context)
				throws IOException, InterruptedException {
			String k = key.toString();
			String[] qual = { "name", "starttime", "endtime", "price" };
			Put putrow = new Put(Bytes.toBytes(k));
			for (Text t : value) {
				String[] v2 = t.toString().split(",");

				for (int i = 0; i < v2.length; i++) {
					// 指定列簇family
					putrow.add(Bytes.toBytes("family"), Bytes.toBytes(qual[i]),
							Bytes.toBytes(v2[i]));
				}
			}
			context.write(new ImmutableBytesWritable(Bytes.toBytes(k)), putrow);

		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = HBaseConfiguration.create();
		// 指定输出到哪个表
		conf.set(TableOutputFormat.OUTPUT_TABLE, "shedule");

		Job job = Job.getInstance(conf, "hmap");
		job.setJarByClass(ExportToHbase.class);

		Path in = new Path("/tmp/in");
		FileInputFormat.addInputPath(job, in);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TableOutputFormat.class);
		job.setMapperClass(Hmap.class);
		job.setReducerClass(Hreduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.waitForCompletion(true);
	}
}
