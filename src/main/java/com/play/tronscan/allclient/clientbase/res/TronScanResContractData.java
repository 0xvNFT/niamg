package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;

/**
 * 【0-8】rawSignature
 * </p>
 * 【8,72】Address
 * </p>
 * 【72, 136】amount
 */
@SuppressWarnings({ "serial" })
public class TronScanResContractData implements Serializable {
	public String data;
	public String owner_address;
	public String contract_address;

	/**
	 * 建议直接从【TronScanResTriggerInfo】结构体中获得
	 * </p>
	 * TronScanResTriggerInfo->parameter->_value
	 */
	public Long amount() {
//		if (data == null)
//			return null;
//		Uint256 amount = (Uint256) FunctionReturnDecoder.decode(data.substring(72, 136), Utils.convert(Arrays.asList(new TypeReference<Uint256>() {
//		}))).get(0);
//		return amount.getValue().longValue();
		return null;
	}

	/**
	 * 建议直接从【TronScanResTriggerInfo】结构体中获得
	 * </p>
	 * TronScanResTriggerInfo->parameter->_to
	 */
	public String toAddress() {
//		if (data == null)
//			return null;
//		Address address = (Address) FunctionReturnDecoder.decode(data.substring(8, 72), Utils.convert(Arrays.asList(new TypeReference<Address>() {
//		}))).get(0);
//		return address.getValue();
		return null;
	}
}
