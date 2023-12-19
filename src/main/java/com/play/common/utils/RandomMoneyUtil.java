package com.play.common.utils;

import java.math.BigDecimal;

/**
 * 
 * @author Owner
 *随机金额
 */
public class RandomMoneyUtil {
	
	/**
	 * 最小金额 min
	 * 最大金额 max
	 * 小数位数 point
	 */
	public static Double generateNum(double min ,  double max , int point) {
	    int pow = (int) Math.pow(10, point); 
	    return Math.floor((Math.random() * (max - min) + min) * pow) / pow;
	}
	
	public static BigDecimal generateNum(BigDecimal mina ,  BigDecimal maxa , int point) {
		double min = Double.parseDouble(mina.toString());
		double max = Double.parseDouble(maxa.toString());
		int pow = (int) Math.pow(10, point); 
		return BigDecimalUtil.toBigDecimal(String.valueOf(Math.floor((Math.random() * (max - min) + min) * pow) / pow));
	}
	
}
