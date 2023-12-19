package com.play.tronscan.allclient.alltool;

public enum EnumTronTransactionType {
	/** 1TRX */
	TRX(1),
	/** 2USDT */
	USDT(2);

	public final int code;

	EnumTronTransactionType(int code) {
		this.code = code;
	}
}
