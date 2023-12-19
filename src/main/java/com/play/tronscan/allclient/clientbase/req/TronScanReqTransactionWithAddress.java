package com.play.tronscan.allclient.clientbase.req;

public class TronScanReqTransactionWithAddress extends TronScanReqCommon {
	/** an account */
	private String address;
	/** 区块高度 */
	private Long block;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getBlock() {
		return block;
	}

	public void setBlock(Long block) {
		this.block = block;
	}
}
