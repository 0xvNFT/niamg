package com.play.tronscan.allclient.clienthigh;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.play.tronscan.allclient.alltool.SysPreDefineTron;
import com.play.tronscan.allclient.clientbase.TronScanBaseClient;
import com.play.tronscan.allclient.clientbase.res.TronScanResBlock;
import com.play.tronscan.allclient.clientbase.res.TronScanResBlockList;
import com.play.tronscan.allclient.clientbase.res.TronScanResSystemStauts;
import com.play.tronscan.allclient.clientbase.res.TronScanResTRC20Transfer;
import com.play.tronscan.allclient.clientbase.res.TronScanResTRC20TransferList;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransaction;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransactionList;
import com.play.tronscan.allclient.clienthigh.entity.TronScanBlockInfo;

public class TronScanHighClient {
	/** log */
	static Logger log = LoggerFactory.getLogger(TronScanHighClient.class);

	public static Long getCurrentBlock() {
		return TronScanBaseClient.sysStatus().full.block;
	}

	public static TronScanResBlock getBlock(Long block) {
		TronScanResSystemStauts stauts = TronScanBaseClient.sysStatus();
		/** 还没有生成此【block】 */
		if (stauts.full.block.compareTo(block) < 0) {
			log.error("stauts.full.block.compareTo(block) < 0");
			return null;
		}
		TronScanResBlockList blockList = TronScanBaseClient.block(block);
		/** 可以取出该【block】有且只有一个 */
		if (blockList.data.size() != 1) {
			log.error("blockList.data.size() != 1");
			return null;
		}
		return blockList.data.get(0);
	}

	public static List<TronScanResTransaction> getTrc10TransactionRelatedAddress(Long fromTimestamp, Long toTimestamp, String address) {
		List<TronScanResTransaction> result = new ArrayList<>();
		/**
		 * 波场最多只会返回【10000】条数据
		 * </p>
		 * 这里不用额外控制了，没有问题
		 */
		do {
			TronScanResTransactionList transactionList = TronScanBaseClient.transaction(SysPreDefineTron.QUERY_SORT_TIME, SysPreDefineTron.QUERY_LIMIT_MAX, result.size(), SysPreDefineTron.QUERY_COUNT, fromTimestamp, toTimestamp, address, null);
			/**
			 * 这里返回结果 不太稳定
			 * </p>
			 * 有时候是空， 有时候是null
			 * </p>
			 * 如果返回null 理解成波场错误 防止漏掉注单
			 */
			if (transactionList.data == null)
				throw new RuntimeException("transactionList.data == null, this is an error...");
			result.addAll(transactionList.data);
			if (transactionList.data.size() < SysPreDefineTron.QUERY_LIMIT_MAX)
				return result;
		} while (true);
	}

	public static TronScanBlockInfo getFullBlock(Long block) {
		TronScanBlockInfo result = new TronScanBlockInfo();
		TronScanResBlockList blockList = TronScanBaseClient.block(block);
		if (blockList.data.size() != 1) {
			log.error("blockList.data.size() != 1");
			return null;
		}
		TronScanResBlock tronScanResBlock = blockList.data.get(0);
		if (tronScanResBlock.number.equals(block) == false) {
			log.error("tronScanResBlock.number.equals(block) == false");
			return null;
		}
		result.block = tronScanResBlock;
		int startPos = 0;
		/**
		 * 波场最多只会返回【10000】条数据
		 * </p>
		 * 这里不用额外控制了，没有问题
		 */
		while (result.fullLoadTransaction() == false) {
			TronScanResTransactionList transactionList = TronScanBaseClient.transaction(SysPreDefineTron.QUERY_SORT_TIME, SysPreDefineTron.QUERY_LIMIT_MAX, startPos, SysPreDefineTron.QUERY_COUNT, null, null, null, block);
			/**
			 * 这里返回结果 不太稳定
			 * </p>
			 * 有时候是空， 有时候是null
			 * </p>
			 * 如果返回null 理解成波场错误 防止漏掉注单
			 */
			if (transactionList.data == null)
				throw new RuntimeException("transactionList.data == null, this is an error...");
			result.transactionList.addAll(transactionList.data);
			startPos += transactionList.data.size();
		}
		return result;
	}

	public static List<TronScanResTRC20Transfer> getUSDTTransfers(Long toTimestamp, String finishTransactionID) {
		List<TronScanResTRC20Transfer> result = new ArrayList<>();
		int startPos = 0;
		/**
		 * 波场最多只会返回【10000】条数据
		 * </p>
		 * 这里不用额外控制了，没有问题
		 */
		while (true) {
			TronScanResTRC20TransferList transferList = TronScanBaseClient.trc20Transfers(SysPreDefineTron.QUERY_LIMIT_MAX, startPos, SysPreDefineTron.MAINNET_USDT_ADDRESS, null, toTimestamp, null);
			/**
			 * 这里返回结果 不太稳定
			 * </p>
			 * 有时候是空， 有时候是null
			 * </p>
			 * 如果返回null 理解成波场错误 防止漏掉注单
			 */
			if (transferList.token_transfers == null)
				throw new RuntimeException("transferList.token_transfers == null, this is an error...");
			/**
			 * 如果未指定【finishTransactionID】
			 * </p>
			 * 只取第一次返回的结果
			 */
			if (StringUtils.isBlank(finishTransactionID)) {
				result.addAll(transferList.token_transfers);
				return result;
			}
			for (TronScanResTRC20Transfer item : transferList.token_transfers) {
				if (StringUtils.compare(finishTransactionID, item.transaction_id) == 0)
					return result;
				result.add(item);
			}
			startPos += transferList.token_transfers.size();
		}
	}

	public static List<TronScanResTRC20Transfer> getTrc20TransfersRelatedAddress(Long startTimestamp, Long toTimestamp, String relatedAddress) {
		List<TronScanResTRC20Transfer> result = new ArrayList<>();
		int startPos = 0;
		/**
		 * 波场最多只会返回【10000】条数据
		 * </p>
		 * 这里不用额外控制了，没有问题
		 */
		while (true) {
			TronScanResTRC20TransferList transactionList = TronScanBaseClient.trc20TransfersRelatedAddress(SysPreDefineTron.QUERY_LIMIT_MAX, startPos, "0", relatedAddress, startTimestamp, toTimestamp, null);
			/**
			 * 这里返回结果 不太稳定
			 * </p>
			 * 有时候是空， 有时候是null
			 * </p>
			 * 如果返回null 理解成波场错误 防止漏掉注单
			 */
			if (transactionList.token_transfers == null)
				throw new RuntimeException("transactionList.token_transfers == null, this is an error...");
			result.addAll(transactionList.token_transfers);
			if (transactionList.token_transfers.size() < SysPreDefineTron.QUERY_LIMIT_MAX)
				return result;
			startPos += transactionList.token_transfers.size();
		}
	}

	public static List<TronScanResTRC20Transfer> getUSDTTransfersLimit50(Long toTimestamp, Integer startPos) {
		List<TronScanResTRC20Transfer> result = new ArrayList<>();
		TronScanResTRC20TransferList transferList = TronScanBaseClient.trc20Transfers(SysPreDefineTron.QUERY_LIMIT_MAX, startPos, SysPreDefineTron.MAINNET_USDT_ADDRESS, null, toTimestamp, null);
		/**
		 * 这里返回结果 不太稳定
		 * </p>
		 * 有时候是空， 有时候是null
		 * </p>
		 * 如果返回null 理解成波场错误 防止漏掉注单
		 */
		if (transferList.token_transfers == null)
			throw new RuntimeException("transferList.token_transfers == null, this is an error...");
		result.addAll(transferList.token_transfers);
		return result;
	}
}
