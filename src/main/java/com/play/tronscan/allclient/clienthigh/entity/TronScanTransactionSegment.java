package com.play.tronscan.allclient.clienthigh.entity;

import java.util.ArrayList;
import java.util.List;

import com.play.tronscan.allclient.clientbase.res.TronScanResTransaction;

public class TronScanTransactionSegment {
	/** 起始时刻（包含） */
	public Long fromTimestamp;
	/** 结束时刻（包含） */
	public Long toTimestamp;
	/** 两次之间的交易列表 */
	public final List<TronScanResTransaction> data = new ArrayList<>();

	public TronScanTransactionSegment(Long fromTimestamp, Long toTimestamp) {
		this.fromTimestamp = fromTimestamp;
		this.toTimestamp = toTimestamp;
	}
}
