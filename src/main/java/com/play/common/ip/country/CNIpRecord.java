package com.play.common.ip.country;

/**
 * 大陆ip记录
 * 
 * @author macair
 *
 */
public class CNIpRecord {
	/**
	 * 起始IP
	 */
	public long start;
	/**
	 * start之后往后多少都是该范围
	 */
	public int count;

	public CNIpRecord(long start, int count) {
		this.start = start;
		this.count = count;
	}

	/**
	 * 判断ipValue是否在范围内
	 * 
	 * @param ipValue
	 * @return
	 */
	public boolean contains(long ipValue) {
		return ipValue >= start && ipValue <= start + count;
	}
}
