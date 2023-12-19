package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class TronScanResBlockList implements Serializable {
	public Long total;
	public Long rangeTotal;
	public List<TronScanResBlock> data;

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

	public List<TronScanResBlock> getData() {
		return data;
	}

	public void setData(List<TronScanResBlock> data) {
		this.data = data;
	}
}
