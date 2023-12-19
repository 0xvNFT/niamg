package com.play.tronscan.allclient;

import java.util.List;

import com.play.tronscan.allclient.clientbase.res.TronScanResBlock;
import com.play.tronscan.allclient.clientbase.res.TronScanResTRC20Transfer;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransaction;
import com.play.tronscan.allclient.clienthigh.TronScanHighClient;
import com.play.tronscan.allclient.clienthigh.entity.TronScanBlockInfo;

/**
 * 波场【tronscan】客户端
 * </p>
 * 请不要直接使用【TronScanBaseClient】或者【TronScanHighClient】
 */
public class TronScanClient {
	/**
	 * 获得当前波场block最新高度
	 */
	public static Long getCurrentBlock() {
		return TronScanHighClient.getCurrentBlock();
	}

	/**
	 * 获得指定block信息
	 * </p>
	 * 
	 * @param block block高度
	 * @return TronScanResBlock 返回
	 */
	public static TronScanResBlock getBlock(Long block) {
		return TronScanHighClient.getBlock(block);
	}

	/**
	 * 获得block完整信息
	 * </p>
	 * 
	 * @return TronScanBlockInfo 返回
	 */
	public static TronScanBlockInfo getFullBlock(Long block) {
		return TronScanHighClient.getFullBlock(block);
	}

	/**
	 * 获得指定地址的【TRC10】交易信息
	 * </p>
	 * 这里注意一下，波场接口 有一些排序控制字段是无效的
	 * </p>
	 * 【transaction】接口就是其中之一
	 * </p>
	 * 导致每次都只能从最新的记录开始获得 给代码解析带来了不少麻烦
	 * 
	 * @param address       【base58check地址】
	 * @param fromTimestamp 请求开始时刻（毫秒 包含该时刻）
	 * @param toTimestamp   请求结束时刻（毫秒 包含该时刻）
	 * @return List<TronScanResTransaction> 返回
	 */
	public static List<TronScanResTransaction> getTrc10TransactionRelatedAddress(Long fromTimestamp, Long toTimestamp, String address) {
		return TronScanHighClient.getTrc10TransactionRelatedAddress(fromTimestamp, toTimestamp, address);
	}

	/**
	 * 获得【指定地址的】【TRC20】交易记录
	 * </p>
	 * 波场允许查询的数据量最大是【10000】条，如果大于这个数量，将丢失数据
	 * </p>
	 *
	 * @param startTimestamp - 请求开始时刻（毫秒 包含该时刻）
	 * @param startTimestamp - 请求结束时刻（毫秒 包含该时刻）
	 * @param relatedAddress - 相关地址
	 * @return List<TronScanResTRC20Transfer> 返回
	 */
	public static List<TronScanResTRC20Transfer> getTrc20TransfersRelatedAddress(Long startTimestamp, Long toTimestamp, String relatedAddress) {
		return TronScanHighClient.getTrc20TransfersRelatedAddress(startTimestamp, toTimestamp, relatedAddress);
	}

	/**
	 * 获得到【toTimestamp】时刻为止的所有USDT交易记录（遇到【finishTransactionID】时，不再接着查找）
	 * </p>
	 * 特别注意：两次查询的【toTimestamp】不要间隔太久（比如两三天）
	 * </p>
	 * 建议时间控制在【10秒以内】
	 * </p>
	 * 波场允许查询的数据量最大是【10000】条，如果大于这个数量，将丢失数据
	 * </p>
	 *
	 * @param toTimestamp         可以为空 - 请求结束时刻（毫秒 包含该时刻）
	 * @param finishTransactionID 可以为空 - 遇到该交易ID终止查询（如果未设置 只会拿取前50条交易记录）
	 * @return List<TronScanResTRC20Transfer> 返回
	 */
	public static List<TronScanResTRC20Transfer> getUSDTTransfers(Long toTimestamp, String finishTransactionID) {
		return TronScanHighClient.getUSDTTransfers(toTimestamp, finishTransactionID);
	}

	/**
	 * 获得到【toTimestamp】时刻为止startPos开始的50条（一次请求最多返回50条记录）USDT交易记录
	 * </p>
	 * 方便使用多线程调度
	 * </p>
	 *
	 * @param toTimestamp 可以为空 - 请求结束时刻（毫秒 包含该时刻）
	 * @param startPos    可以为空 - 记录起始位置
	 * @return List<TronScanResTRC20Transfer> 返回
	 */
	public static List<TronScanResTRC20Transfer> getUSDTTransfersLimit50(Long toTimestamp, Integer startPos) {
		return TronScanHighClient.getUSDTTransfersLimit50(toTimestamp, startPos);
	}
}
