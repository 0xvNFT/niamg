package com.play.tronscan.allclient.clientbase.req;

public class TronScanReqTransferWithAddress extends TronScanReqCommon {
	/** '_' shows only TRX transfers */
	private String token;
	/** an account */
	private String address;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
