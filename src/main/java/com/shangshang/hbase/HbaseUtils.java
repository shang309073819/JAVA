package com.shangshang.hbase;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseUtils {

	public static List<Job> result2Jobs(Result... results) {
		List<Job> jobs = new ArrayList<Job>();
		for (Result rs : results) {
			Job job = new Job();
			job.setRowKey(Bytes.toString(rs.getRow()));
			job.setPid(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_PID))));
			job.setType(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_TYPE))));
			job.setNode(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_NODE))));
			job.setOutPath(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_OUT_PATH))));
			job.setProgress(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_PROGRESS))));
			job.setState(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_STATE))));
			job.setJid(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_JID))));
			jobs.add(job);
		}
		return jobs;
	}

	public static List<Job> result2Jobs(List<Result> results) {
		List<Job> jobs = new ArrayList<Job>();
		for (Result rs : results) {
			Job job = new Job();
			job.setRowKey(Bytes.toString(rs.getRow()));
			job.setPid(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_PID))));
			job.setType(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_TYPE))));
			job.setNode(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_NODE))));
			job.setOutPath(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_OUT_PATH))));
			job.setProgress(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_PROGRESS))));
			job.setState(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_STATE))));
			job.setJid(Bytes.toString(rs.getValue(
					Bytes.toBytes(HbaseConfig.JOB_META),
					Bytes.toBytes(HbaseConfig.JOB_META_JID))));
			jobs.add(job);
		}
		return jobs;
	}

	public static Job result2Job(Result result) {
		Job job = new Job();
		job.setRowKey(Bytes.toString(result.getRow()));
		job.setPid(Bytes.toString(result.getValue(
				Bytes.toBytes(HbaseConfig.JOB_META),
				Bytes.toBytes(HbaseConfig.JOB_META_PID))));
		job.setType(Bytes.toString(result.getValue(
				Bytes.toBytes(HbaseConfig.JOB_META),
				Bytes.toBytes(HbaseConfig.JOB_META_TYPE))));
		job.setNode(Bytes.toString(result.getValue(
				Bytes.toBytes(HbaseConfig.JOB_META),
				Bytes.toBytes(HbaseConfig.JOB_META_NODE))));
		job.setOutPath(Bytes.toString(result.getValue(
				Bytes.toBytes(HbaseConfig.JOB_META),
				Bytes.toBytes(HbaseConfig.JOB_META_OUT_PATH))));
		job.setProgress(Bytes.toString(result.getValue(
				Bytes.toBytes(HbaseConfig.JOB_META),
				Bytes.toBytes(HbaseConfig.JOB_META_PROGRESS))));
		job.setState(Bytes.toString(result.getValue(
				Bytes.toBytes(HbaseConfig.JOB_META),
				Bytes.toBytes(HbaseConfig.JOB_META_STATE))));
		job.setJid(Bytes.toString(result.getValue(
				Bytes.toBytes(HbaseConfig.JOB_META),
				Bytes.toBytes(HbaseConfig.JOB_META_JID))));
		return job;
	}

}