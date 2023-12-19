package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TronScanResTransfer implements Serializable {
	public Long block;
	public String transactionHash;
	public Long timestamp;
	public String transferFromAddress;
	public String transferToAddress;
	public Long amount;
	public String tokenName;
	public Boolean confirmed;
	public String contractRet;
	public Boolean revert;
	public TronScanResTokenInfo tokenInfo;

	public Long getBlock() {
		return block;
	}

	public void setBlock(Long block) {
		this.block = block;
	}

	public String getTransactionHash() {
		return transactionHash;
	}

	public void setTransactionHash(String transactionHash) {
		this.transactionHash = transactionHash;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTransferFromAddress() {
		return transferFromAddress;
	}

	public void setTransferFromAddress(String transferFromAddress) {
		this.transferFromAddress = transferFromAddress;
	}

	public String getTransferToAddress() {
		return transferToAddress;
	}

	public void setTransferToAddress(String transferToAddress) {
		this.transferToAddress = transferToAddress;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getContractRet() {
		return contractRet;
	}

	public void setContractRet(String contractRet) {
		this.contractRet = contractRet;
	}

	public Boolean getRevert() {
		return revert;
	}

	public void setRevert(Boolean revert) {
		this.revert = revert;
	}

	public TronScanResTokenInfo getTokenInfo() {
		return tokenInfo;
	}

	public void setTokenInfo(TronScanResTokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}
}
