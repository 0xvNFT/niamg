package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;

/**
 * 【0-8】rawSignature
 * </p>
 * 【8,72】Address
 * </p>
 * 【72, 136】amount
 */
@SuppressWarnings("serial")
public class TronScanResTriggerInfo implements Serializable {
	public String method;
	public String data;
	public Parameter parameter;
	public String methodName;
	public String contract_address;
	public Long call_value;

	public static class Parameter implements Serializable {
		public String _value;
		public String _to;
	}
}
