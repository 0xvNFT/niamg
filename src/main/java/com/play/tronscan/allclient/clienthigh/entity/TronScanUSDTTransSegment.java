package com.play.tronscan.allclient.clienthigh.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.play.tronscan.allclient.clientbase.res.TronScanResTRC20Transfer;

public class TronScanUSDTTransSegment {
	/** 结束时刻（包含） */
	public Long toTimestamp;
	/** 遇到该【交易ID】终止查询（如果未设置 只会拿取前50条交易记录） */
	public String finishTransactionID;
	/** 交易列表 */
	public List<TronScanResTRC20Transfer> data = new ArrayList<>();

	public boolean containsFinishTransactionID() {
		for (TronScanResTRC20Transfer item : data) {
			if (StringUtils.compare(finishTransactionID, item.transaction_id) == 0)
				return true;
		}
		return false;
	}

	public void dropUselessTransactions() {
		List<TronScanResTRC20Transfer> newDataList = new ArrayList<>();
		for (TronScanResTRC20Transfer item : data) {
			if (StringUtils.compare(finishTransactionID, item.transaction_id) == 0)
				break;
			newDataList.add(item);
		}
		this.data = newDataList;
	}
}
