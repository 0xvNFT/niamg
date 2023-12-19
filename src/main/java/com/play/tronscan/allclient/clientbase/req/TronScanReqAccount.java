package com.play.tronscan.allclient.clientbase.req;

public class TronScanReqAccount {
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public TronScanReqAccount(String address) {
		this.address = address;
	}
}
