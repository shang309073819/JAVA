/*
 * 对物品组合列表进行计数，建立物品的同现矩阵
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
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.shangshang.hdfs.HdfsDAO;

public class Step2 {
	public static class Step2_UserVectorToCooccurrenceMapper extends
			Mapper<LongWritable, Text, Text, IntWritable> {
		private final static Text k = new Text();
		private final static IntWritable v = new IntWritable(1);

		public void map(LongWritable key, Text values, Context context)
				throws IOException, InterruptedException {
			String[] tokens = Recommend.DELIMITER.split(values.toString());
			for (int i = 1; i < tokens.length; i++) {
				String itemID = tokens[i].split(":")[0];
				for (int j = 1; j < tokens.length; j++) {
					String itemID2 = tokens[j].split(":")[0];
					k.set(itemID + ":" + itemID2);
					context.write(k, v);
				}
			}
		}
	}

	public static class Step2_UserVectorToConoccurrenceReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {

		private IntWritable result = new IntWritable();

		protected void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void run(Map<String, String> path) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String input = path.get("Step2Input");
		String output = path.get("Step2Output");

		HdfsDAO hdfs = new HdfsDAO(Recommend.HDFS, conf);
		hdfs.rmr(output);

		Job job = Job.getInstance(conf);
		job.setJarByClass(Step2.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Step2.Step2_UserVectorToCooccurrenceMapper.class);
		job.setCombinerClass(Step2.Step2_UserVectorToConoccurrenceReducer.class);
		job.setReducerClass(Step2.Step2_UserVectorToConoccurrenceReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		job.waitForCompletion(true);
	}
}
