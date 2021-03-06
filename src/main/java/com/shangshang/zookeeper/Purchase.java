package com.shangshang.zookeeper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.shangshang.hdfs.HdfsDAO;

public class Purchase {

	public static final String HDFS = "hdfs://192.168.2.3:8020";
	public static final Pattern DELIMITER = Pattern.compile("[\t,]");

	public static class PurchaseMapper extends
			Mapper<LongWritable, Text, Text, IntWritable> {

		private String month = "2013-01";
		private Text k = new Text(month);
		private IntWritable v = new IntWritable();
		private int money = 0;

		public void map(LongWritable key, Text values, Context context)
				throws IOException, InterruptedException {
			String[] tokens = DELIMITER.split(values.toString());
			if (tokens[3].startsWith(month)) {// 1月的数据
				money = Integer.parseInt(tokens[1])
						* Integer.parseInt(tokens[2]);// 单价*数量
				v.set(money);
				context.write(k, v);
			}
		}
	}

	public static class PurchaseReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable v = new IntWritable();
		private int money = 0;

		@Override
		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			for (IntWritable line : values) {
				money += line.get();
			}
			v.set(money);
			context.write(null, v);
		}

	}

	public static void run(Map<String, String> path) throws IOException,
			InterruptedException, ClassNotFoundException {
		JobConf conf = config();
		String local_data = path.get("purchase");
		String input = path.get("input");
		String output = path.get("output");

		// 初始化purchase
		HdfsDAO hdfs = new HdfsDAO(HDFS, conf);
		hdfs.rmr(input);
		hdfs.mkdirs(input);
		hdfs.copyFile(local_data, input);
		hdfs.rmr(output);

		Job job = Job.getInstance(conf);
		job.setJarByClass(Purchase.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(PurchaseMapper.class);
		job.setReducerClass(PurchaseReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		job.waitForCompletion(true);
	}

	public static JobConf config() {// Hadoop集群的远程配置信息
		JobConf conf = new JobConf(Purchase.class);
		conf.setJobName("purchase");
		return conf;
	}

	public static Map<String, String> path() {
		Map<String, String> path = new HashMap<String, String>();
		path.put("purchase", "purchase.csv");// 本地的数据文件
		path.put("input", HDFS + "/chenshang/purchase/input");// HDFS的目录
		path.put("output", HDFS + "/chenshang/purchase/output"); // 输出目录
		return path;
	}

	public static void main(String[] args) throws Exception {
		run(path());
	}

}
