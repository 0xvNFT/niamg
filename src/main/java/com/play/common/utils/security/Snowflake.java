package com.play.common.utils.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;

public class Snowflake {
	private long workerId;
	private long datacenterId;
	private long sequence = 0L;

	private long twepoch = 1288834974657L;

	private long workerIdBits = 5L;
	private long datacenterIdBits = 5L;
	private long maxWorkerId = -1L ^ (-1L << this.workerIdBits);
	private long maxDatacenterId = -1L ^ (-1L << this.datacenterIdBits);
	private long sequenceBits = 12L;

	private long workerIdShift = this.sequenceBits;
	private long datacenterIdShift = this.sequenceBits + this.workerIdBits;
	private long timestampLeftShift = this.sequenceBits + this.workerIdBits + this.datacenterIdBits;
	private long sequenceMask = -1L ^ (-1L << this.sequenceBits);

	private long lastTimestamp = -1L;
	private static Snowflake idWorker = new Snowflake(RandomUtils.nextInt(0, 30), RandomUtils.nextInt(0, 30));

	public Snowflake(long workerId, long datacenterId) {
		// sanity check for workerId
		if (workerId > this.maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
		}
		if (datacenterId > this.maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(
					String.format("datacenter Id can't be greater than %d or less than 0", this.maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public synchronized long nextId() {
		long timestamp = this.timeGen();

		if (timestamp < this.lastTimestamp) {
			throw new RuntimeException(
					String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
							this.lastTimestamp - timestamp));
		}

		if (this.lastTimestamp == timestamp) {
			this.sequence = (this.sequence + 1) & this.sequenceMask;
			if (this.sequence == 0) {
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			this.sequence = 0L;
		}

		this.lastTimestamp = timestamp;

		return ((timestamp - this.twepoch) << this.timestampLeftShift) | (this.datacenterId << this.datacenterIdShift)
				| (this.workerId << this.workerIdShift) | this.sequence;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}

	public static String getOrderNo() {
		return String.valueOf(idWorker.nextId());
	}

	public static void main(String[] args) {
		List<String> orders = new ArrayList<>();
		Map<String, String> orderMap = new HashMap<>();
		int exsit = 0;
		System.out.println("循环次数:" + 10000);
		for (int i = 0; i < 10000; i++) {
			String orNo = getOrderNo();
			if (orderMap.containsKey(orNo)) {
				exsit++;
			}
			orders.add(orNo);
			orderMap.put(orNo, orNo);
		}
		System.out.println("重复次数：" + exsit);
		System.out.println(getOrderNo());
	}
}