package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class TronScanResTransaction implements Serializable {
	public Long block;
	public String hash;
	public Long timestamp;
	public String ownerAddress;
	public List<String> toAddressList;
	public String toAddress;
	/** 这两个字段不一样 */
	public Integer contractType;
	public String contract_Type;
	public Boolean confirmed;
	public Boolean revert;
	public TronScanResContractData contractData;
	public String contractRet;
	public String result;
	/**
	 * 该字段只有 TRX时有意义
	 * </p>
	 * 请不要直接使用
	 * </p>
	 * 请调用方法 【getTrxAmount()】或者【getTrc20Amount】来获取值
	 */
	public String amount;
	public TronScanResCost cost;
	public TronScanResTokenInfo tokenInfo;
	public String tokenType;
	public TronScanResTriggerInfo trigger_info;

	public Boolean isSuccess() {
		if (result == null)
			return false;
		return result.equals("SUCCESS");
	}

	public Boolean isContractSuccess() {
		if (result == null && contractRet == null)
			return false;
		if (result == null)
			return contractRet.equals("SUCCESS");
		return result.equals("SUCCESS") && contractRet.equals("SUCCESS");
	}

	public Boolean isTrxTransaction() {
		if (contractType == null)
			return false;
		return contractType == 1;
	}

	public Boolean isTrc20Transaction() {
		if (contractType == null)
			return false;
		return contractType == 31;
	}

	/**
	 * 这里注意
	 * </p>
	 * 很多【TRC20】的合约 小数位数都特别多
	 * </p>
	 * 不要在此处转换成数字
	 * </p>
	 * 需要等到确定了是哪个合约的时候才可以
	 */
	public String getTrc20Amount() {
		if (trigger_info == null || trigger_info.parameter == null || trigger_info.parameter._value == null)
			return null;
		return trigger_info.parameter._value;
	}

	public String getTrxAmount() {
		if (amount == null)
			return null;
		return amount;
	}

	/**
	 * 请先判断是否是【TRC20】
	 */
	public String getContractAddress() {
		if (trigger_info == null)
			return null;
		return trigger_info.contract_address;
	}

	public Long getBlock() {
		return block;
	}

	public void setBlock(Long block) {
		this.block = block;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getOwnerAddress() {
		return ownerAddress;
	}

	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	public List<String> getToAddressList() {
		return toAddressList;
	}

	public void setToAddressList(List<String> toAddressList) {
		this.toAddressList = toAddressList;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public Integer getContractType() {
		return contractType;
	}

	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Boolean getRevert() {
		return revert;
	}

	public void setRevert(Boolean revert) {
		this.revert = revert;
	}

	public String getContract_Type() {
		return contract_Type;
	}

	public void setContract_Type(String contract_Type) {
		this.contract_Type = contract_Type;
	}

	public TronScanResContractData getContractData() {
		return contractData;
	}

	public void setContractData(TronScanResContractData contractData) {
		this.contractData = contractData;
	}

	public String getContractRet() {
		return contractRet;
	}

	public void setContractRet(String contractRet) {
		this.contractRet = contractRet;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public TronScanResCost getCost() {
		return cost;
	}

	public void setCost(TronScanResCost cost) {
		this.cost = cost;
	}

	public TronScanResTokenInfo getTokenInfo() {
		return tokenInfo;
	}

	public void setTokenInfo(TronScanResTokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public TronScanResTriggerInfo getTrigger_info() {
		return trigger_info;
	}

	public void setTrigger_info(TronScanResTriggerInfo trigger_info) {
		this.trigger_info = trigger_info;
	}
}
