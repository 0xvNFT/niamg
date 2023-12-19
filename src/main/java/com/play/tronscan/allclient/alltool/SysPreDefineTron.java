package com.play.tronscan.allclient.alltool;

import java.util.regex.Pattern;

/**
 * @see SysPreDefineTron
 */
public final class SysPreDefineTron {
	/**
	 * 一天毫秒数
	 */
	public static final Long ONE_DAY_MILLE_SECONDES = 24 * 60 * 60 * 1000L;
	/**
	 * 一个USDT的金额
	 */
	public static final Long ONE_USDT_VALUE = (long) Math.pow(10, 6);
	/**
	 * tronscan 默认最小请求间隔：5秒（波场3秒出一个块）
	 */
	public final static int DEFAULT_MIN_INTERVAL_QUEST_MILLSECONDS = 5 * 1000;
	/**
	 * 每次执行最多 30分钟数据
	 */
	public final static int DEFAULT_MAX_COLLECTION_INTERVAL_MILLSECONDS = 30 * 60 * 1000;
	/**
	 * tronscan 默认冗余时间间隔：65秒（否则容易丢失注单）
	 */
	public final static int DEFAULT_REDUNDENCY_RECORD_MILLE_SECONDS = 65 * 1000;
	/**
	 * tronscan 默认有效的投注时间跨度：3天（只统计三天内的注单）
	 */
	public final static int DEFAULT_BET_VALID_DAYS = 3;
	/**
	 * tronscan 每次请求的排序：-time
	 */
	public final static String QUERY_SORT_TIME = "-time";
	/**
	 * tronscan 每次请求的限制大小（默认）：20
	 */
	public final static int QUERY_LIMIT_DEFAULT = 20;
	/**
	 * tronscan 每次请求的限制大小（最大）：50
	 */
	public final static int QUERY_LIMIT_MAX = 50;
	/**
	 * tronscan QUERY_COUNT
	 */
	public final static String QUERY_COUNT = "true";
	/**
	 * 波场主网USDT地址
	 */
	public final static String MAINNET_USDT_ADDRESS = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
	/**
	 * 波场主网【transaction查询】地址前缀
	 */
	public final static String MAINNET_HTTPS_TRANSACTION_INFO_PREFIX = "https://tronscan.org/#/transaction/";
	/**
	 * 波场主网【block查询】地址前缀
	 */
	public final static String MAINNET_HTTPS_BLOCK_INFO_PREFIX = "https://tronscan.org/#/block/";
	/**
	 * 波场主网【account查询】地址前缀
	 */
	public final static String MAINNET_HTTPS_ACCOUNT_INFO_PREFIX = "https://tronscan.org/#/address/";
	/**
	 * tronscan 哈希长度：64
	 */
	public final static int HASH_SIZE = 64;
	/**
	 * tronscan 哈希pattern
	 */
	public final static Pattern DEFAULT_HASH_PATTERN = Pattern.compile("^[0-9a-f]{64}$");
	/**
	 * tronscan 地址pattern
	 */
	public final static Pattern DEFAULT_BASE58_ADDRESS_PATTERN = Pattern.compile("^T[0-9a-zA-Z]{33}$");

	/**
	 * 判断是否合法哈希
	 * </p>
	 * 
	 * @param hash 输入
	 * @return boolean 输出 true合法 false不合法
	 */
	public static boolean isValidHash(String hash) {
		return DEFAULT_HASH_PATTERN.matcher(hash).matches();
	}

	/**
	 * 判断是否合法哈希
	 * </p>
	 * 
	 * @param hash 输入
	 * @return boolean 输出 true合法 false不合法
	 */
	public static boolean isValidBase58Address(String address) {
		return DEFAULT_BASE58_ADDRESS_PATTERN.matcher(address).matches();
	}
}
