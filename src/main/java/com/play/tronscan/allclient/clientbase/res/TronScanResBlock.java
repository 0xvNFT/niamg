package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TronScanResBlock implements Serializable {
	public String hash;
	public Boolean confirmed;
	public Long number;
	public Long size;
	public Long timestamp;
	public String parentHash;
	public String witnessAddress;
	public String witnessName;
	public String version;
	public Double fee;
	/** 交易数量 */
	public Long nrOfTrx;
	/** 交易失败数量 */
	public Long failTxCount;
	/** 转账数量 */
	public Long transferCount;
	public String txTrieRoot;
	public Long witnessId;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getParentHash() {
		return parentHash;
	}

	public void setParentHash(String parentHash) {
		this.parentHash = parentHash;
	}

	public String getWitnessAddress() {
		return witnessAddress;
	}

	public void setWitnessAddress(String witnessAddress) {
		this.witnessAddress = witnessAddress;
	}

	public String getWitnessName() {
		return witnessName;
	}

	public void setWitnessName(String witnessName) {
		this.witnessName = witnessName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Long getNrOfTrx() {
		return nrOfTrx;
	}

	public void setNrOfTrx(Long nrOfTrx) {
		this.nrOfTrx = nrOfTrx;
	}

	public Long getFailTxCount() {
		return failTxCount;
	}

	public void setFailTxCount(Long failTxCount) {
		this.failTxCount = failTxCount;
	}

	public Long getTransferCount() {
		return transferCount;
	}

	public void setTransferCount(Long transferCount) {
		this.transferCount = transferCount;
	}

	public String getTxTrieRoot() {
		return txTrieRoot;
	}

	public void setTxTrieRoot(String txTrieRoot) {
		this.txTrieRoot = txTrieRoot;
	}

	public Long getWitnessId() {
		return witnessId;
	}

	public void setWitnessId(Long witnessId) {
		this.witnessId = witnessId;
	}
}
