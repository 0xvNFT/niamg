package com.play.web.utils.redpacket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedPacketUtil {

	static public List<BigDecimal> splitRedPackets(BigDecimal allMoney, int count, BigDecimal min) {
		BigDecimal minNeed = min.multiply(new BigDecimal(count));
		List<BigDecimal> rlist = new ArrayList<>();
		if (minNeed.compareTo(allMoney) == 0) {
			for (int i = 0; i < count; i++) {
				rlist.add(min);
			}
			return rlist;
		}
		List<Integer> perList = new ArrayList<>();
		int all = 0;
		for (int i = 0; i < count; i++) {
			int c = getRandomInt();
			perList.add(c);
			all += c;
		}
		BigDecimal allPer = new BigDecimal(all);
		BigDecimal remain = allMoney.subtract(minNeed);// 350
		BigDecimal send = BigDecimal.ZERO;
		BigDecimal cur = null;
		for (int i = 0; i < count - 1; i++) {
			cur = remain.multiply(new BigDecimal(perList.get(i))).divide(allPer, 2, RoundingMode.DOWN);
			rlist.add(min.add(cur));
			send = send.add(cur);
		}
		rlist.add(min.add(remain).subtract(send));
		return rlist;
	}

	static private int getRandomInt() {
		Random random = new Random();
		return random.nextInt(100);
	}

	private static final float TIMES = 2.1f;

	/**
	 * 
	 * splitRedPacketsNew 分配红包
	 * 
	 * @param totalMoney
	 * @param totalCount
	 * @return
	 */
	public static List<BigDecimal> splitRedPacketsNew(double totalMoney, int totalCount, double minMoney,
			double maxMoney) {
		BigDecimal alll = BigDecimal.ZERO;
		BigDecimal maxIndex = BigDecimal.ZERO;
		double totalMoneyBank = totalMoney;
		if (maxMoney < (totalMoney / totalCount)) {
			maxMoney = totalMoney * TIMES / totalCount;
		}
		if (!isFinished(totalMoney, totalCount, minMoney, maxMoney)) {
			return null;
		}
		List<BigDecimal> distributionMoneyList = new ArrayList<>();
		// 如果随机生成的红包大于指定最大值，则默认
		double max = (double) totalMoney * TIMES / totalCount > maxMoney ? maxMoney
				: (double) totalMoney * TIMES / totalCount;
		// 将获得的红包放入集合
		int ii = 0;
		for (int i = 0; i < totalCount; i++) {
			double distributionMoney = randomRedPacket(totalMoney, totalCount - i, minMoney, max, minMoney, maxMoney);
			distributionMoneyList.add(new BigDecimal(distributionMoney).setScale(2, RoundingMode.HALF_UP));
			totalMoney -= distributionMoney;
			alll = alll.add(new BigDecimal(distributionMoney).setScale(2, RoundingMode.HALF_UP));
			if (maxIndex.compareTo(new BigDecimal(distributionMoney).setScale(2, RoundingMode.HALF_UP)) < 0) {
				maxIndex = new BigDecimal(distributionMoney).setScale(2, RoundingMode.HALF_UP);
				ii = i;
			}
		}
		if (alll.compareTo(new BigDecimal(totalMoneyBank)) > 0) {
			distributionMoneyList
					.add(distributionMoneyList.get(ii).subtract(alll.subtract(new BigDecimal(totalMoneyBank))));
			distributionMoneyList.remove(ii);
		}

		return distributionMoneyList;
	}

	/**
	 * 
	 * randomRedPacket 核心随机生成红包算法
	 * 
	 * @param totalMoney 红包金额
	 * @param totalCount 红包数量
	 * @param minMoney   最小红包
	 * @param maxMoney   最大红包
	 * @return
	 */
	public static double randomRedPacket(double totalMoney, int totalCount, double minMoney, double maxMoney,
			double minMoney2, double maxMoney2) {
		// 如果是单发红包，则对方全领
		if (totalCount == 1) {
			return (double) Math.round(totalMoney * 100) / 100;
		}
		// 如果最小红包和最大红包相等，默认最小红包
		if (minMoney == maxMoney) {
			return minMoney;
		}
		// 设置最大值，防止红包溢出
		double maxMoney_ = maxMoney > totalMoney ? totalMoney : maxMoney;
		// 随机分配红包(生成随机红包范围：minMoney < distributionMoney < maxMoney_)
		double distributionMoney = (double) Math
				.round(((double) Math.random() * (maxMoney_ - minMoney) + minMoney) * 100) / 100;
		// 得到剩余红包
		double remainMoney = totalMoney - distributionMoney;
		// 红包是否分完
		if (isFinished(remainMoney, totalCount - 1, minMoney2, maxMoney2)) {
			return distributionMoney;
		} else { // 剩余红包随机分配
			double avg = remainMoney / (totalCount - 1);
			if (avg < minMoney2) {
				return randomRedPacket(totalMoney, totalCount, minMoney, distributionMoney, minMoney2, maxMoney2);
			} else if (avg > maxMoney2) {
				return randomRedPacket(totalMoney, totalCount, distributionMoney, maxMoney, minMoney2, maxMoney2);
			}
		}
		return distributionMoney;
	}

	/**
	 * 
	 * isFinished 判断红包是否分完 根据平均金额判断红包内的金钱是否发放完
	 * 
	 * @param currentMoney 当前金额
	 * @param currentCount 当前领取红包数
	 * @return
	 */
	public static boolean isFinished(double currentMoney, int currentCount, double minMoney, double maxMoney) {
		double avg = currentMoney / currentCount;
		if (avg < minMoney) {
			return false;
		} else if (avg > maxMoney) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		List<BigDecimal> ll = splitRedPacketsNew(new BigDecimal(1000).doubleValue(), 100,
				new BigDecimal(3.33).doubleValue(), new BigDecimal(13.88).doubleValue());
		BigDecimal maxIndex = ll.get(0);
		BigDecimal minIndex = ll.get(0);
		BigDecimal alll = BigDecimal.ZERO;
		for (BigDecimal b1 : ll) {
			System.out.println(b1);
			alll = alll.add(b1);
			if (maxIndex.compareTo(b1) < 0) {
				maxIndex = b1;
			}
			if (minIndex.compareTo(b1) > 0) {
				minIndex = b1;
			}
		}
		System.out.println(maxIndex);
		System.out.println(minIndex);
		System.out.println(alll);
	}
}
