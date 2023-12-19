package com.play.web.utils.mlangIpCommons;

/**
 * ip记录
 * 
 * @author macair
 *
 */
public class ALLIpRecord {
	/**
	 * 起始IP
	 */
	public long start;
	/**
	 * start之后往后多少都是该范围
	 */
	public int count;

	public String countryCode;

	public ALLIpRecord(long start, int count, String countryCode) {
		this.start = start;
		this.count = count;
		this.countryCode = countryCode;
	}

	/**
	 * 判断ipValue是否在范围内
	 * 
	 * @param ipValue
	 * @return
	 */
	public boolean contains(long ipValue) {
		return ipValue >= start && ipValue < start + count;
	}
}
