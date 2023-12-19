package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class TronScanResTRC20TransferList implements Serializable {
	public Long total;
	public Long rangeTotal;
	public Map<String, TronScanResContractInfo> contractInfo;
	public List<TronScanResTRC20Transfer> token_transfers;
	public String from;
}
