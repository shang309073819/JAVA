/*
 * 评分矩阵
 */
package com.shangshang.recommend;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.shangshang.hdfs.HdfsDAO;

public class Step3 {

	public static class Step31_UserVectorSplitterMapper extends
			Mapper<LongWritable, Text, IntWritable, Text> {
		private final static IntWritable k = new IntWritable();
		private final static Text v = new Text();

		public void map(LongWritable key, Text values, Context context)
				throws IOException, InterruptedException {
			String[] tokens = Recommend.DELIMITER.split(values.toString());
			for (int i = 1; i < tokens.length; i++) {
				String[] vector = tokens[i].split(":");
				int itemID = Integer.parseInt(vector[0]);
				String pref = vector[1];

				k.set(itemID);
				v.set(tokens[0] + ":" + pref);
				context.write(k, v);
			}
		}
	}

	public static void run1(Map<String, String> path) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		String input = path.get("Step3Input1");
		String output = path.get("Step3Output1");

		HdfsDAO hdfs = new HdfsDAO(Recommend.HDFS, conf);
		hdfs.rmr(output);

		job.setJarByClass(Step3.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Step31_UserVectorSplitterMapper.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		job.waitForCompletion(true);
	}

	public static class Step32_CooccurrenceColumnWrapperMapper extends
			Mapper<LongWritable, Text, Text, IntWritable> {
		private final static Text k = new Text();
		private final static IntWritable v = new IntWritable();

		@Override
		public void map(LongWritable key, Text values, Context context)
				throws IOException, InterruptedException {
			String[] tokens = Recommend.DELIMITER.split(values.toString());
			k.set(tokens[0]);
			v.set(Integer.parseInt(tokens[1]));
			context.write(k, v);
		}
	}

	public static void run2(Map<String, String> path) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		String input = path.get("Step3Input2");
		String output = path.get("Step3Output2");

		HdfsDAO hdfs = new HdfsDAO(Recommend.HDFS, conf);
		hdfs.rmr(output);

		job.setJarByClass(Step3.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Step3.Step32_CooccurrenceColumnWrapperMapper.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		job.waitForCompletion(true);
	}

}
