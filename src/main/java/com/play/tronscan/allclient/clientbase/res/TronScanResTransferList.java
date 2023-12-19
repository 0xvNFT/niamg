package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class TronScanResTransferList implements Serializable {
	public Long total;
	public Long rangeTotal;
	public List<TronScanResTransfer> data;
	public Map<String, Boolean> contractMap;
	public Map<String, TronScanResContractInfo> contractInfo;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getRangeTotal() {
		return rangeTotal;
	}

	public void setRangeTotal(Long rangeTotal) {
		this.rangeTotal = rangeTotal;
	}

	public List<TronScanResTransfer> getData() {
		return data;
	}

	public void setData(List<TronScanResTransfer> data) {
		this.data = data;
	}

	public Map<String, Boolean> getContractMap() {
		return contractMap;
	}

	public void setContractMap(Map<String, Boolean> contractMap) {
		this.contractMap = contractMap;
	}

	public Map<String, TronScanResContractInfo> getContractInfo() {
		return contractInfo;
	}

	public void setContractInfo(Map<String, TronScanResContractInfo> contractInfo) {
		this.contractInfo = contractInfo;
	}
}
