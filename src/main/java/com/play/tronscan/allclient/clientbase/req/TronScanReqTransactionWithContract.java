package com.play.tronscan.allclient.clientbase.req;

public class TronScanReqTransactionWithContract extends TronScanReqCommon {
	/** contract address */
	private String contract;

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}
}
