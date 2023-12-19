package com.play.tronscan.allclient.clientbase.req;

public class TronScanReqCommon {
	/** define the sequence of the records return */
	private String sort;
	/** page size for pagination */
	private Integer limit;
	/** query index for pagination */
	private Integer start;
	/** total number of records */
	private String count = "true";
	/** query date range - milliseconds */
	private Long start_timestamp;
	/** query date range - milliseconds */
	private Long end_timestamp;

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public Long getStart_timestamp() {
		return start_timestamp;
	}

	public void setStart_timestamp(Long start_timestamp) {
		this.start_timestamp = start_timestamp;
	}

	public Long getEnd_timestamp() {
		return end_timestamp;
	}

	public void setEnd_timestamp(Long end_timestamp) {
		this.end_timestamp = end_timestamp;
	}
}
