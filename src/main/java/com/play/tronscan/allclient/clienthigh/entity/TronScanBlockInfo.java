package com.play.tronscan.allclient.clienthigh.entity;

import java.util.ArrayList;
import java.util.List;

import com.play.tronscan.allclient.clientbase.res.TronScanResBlock;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransaction;

public class TronScanBlockInfo {
	/** block */
	public TronScanResBlock block;
	/** transactionList */
	public final List<TronScanResTransaction> transactionList = new ArrayList<>();

	/** 是否下载完成了本区块内的所有交易 */
	public boolean fullLoadTransaction() {
		return transactionList.size() >= block.nrOfTrx ? true : false;
	}
}
