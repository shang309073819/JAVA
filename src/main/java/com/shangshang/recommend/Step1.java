/*
 * Step1.java，按用户分组，计算所有电影出现的组合列表，得到用户对电影的评分矩阵
 */
package com.shangshang.recommend;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.shangshang.hdfs.HdfsDAO;

public class Step1 {

	public static class Step1_ToItemPreMapper extends
			Mapper<Object, Text, IntWritable, Text> {
		private final static IntWritable k = new IntWritable();
		private final static Text v = new Text();
		private Log log = LogFactory.getLog(getClass().getName());

		public void map(Object key, Text values, Context context)
				throws IOException, InterruptedException {
			String[] tokens = Recommend.DELIMITER.split(values.toString());
			int userID = Integer.parseInt(tokens[0]);
			String itemID = tokens[1];
			String pref = tokens[2];
			k.set(userID);
			v.set(itemID + ":" + pref);
			log.info("mapper:key----->value=" + k + "----->" + v);
			context.write(k, v);

		}
	}

	public static class Step1_ToUserVectorReducer extends
			Reducer<IntWritable, Text, IntWritable, Text> {

		private final static Text v = new Text();
		private Log log = LogFactory.getLog(getClass().getName());

		protected void reduce(IntWritable key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			StringBuilder sb = new StringBuilder();
			for (Text value : values) {
				sb.append("," + value);
			}
			v.set(sb.toString().replaceFirst(",", ""));
			log.info("reducer:key----->value=" + key + "----->" + v);
			context.write(key, v);
		}
	}

	public static void run(Map<String, String> path) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		String input = path.get("Step1Input");
		String output = path.get("Step1Output");

		HdfsDAO hdfs = new HdfsDAO(Recommend.HDFS, conf);
		hdfs.rmr(output);
		hdfs.rmr(input);
		hdfs.mkdirs(input);
		hdfs.copyFile(path.get("data"), input);

		job.setJarByClass(Step1.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Step1.Step1_ToItemPreMapper.class);
		job.setCombinerClass(Step1_ToUserVectorReducer.class);
		job.setReducerClass(Step1_ToUserVectorReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		job.waitForCompletion(true);
	}

}
