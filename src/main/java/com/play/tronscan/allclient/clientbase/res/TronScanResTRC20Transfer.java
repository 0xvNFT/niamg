package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
public class TronScanResTRC20Transfer implements Serializable {
	public String transaction_id;
	public String approval_amount;
	public String contractRet;
	public String to_address;
	public Boolean revert;
	public String contract_address;
	public String quant;
	public Boolean confirmed;
	public TronScanResTokenInfo tokenInfo;
	public String contract_Type;
	public Boolean toAddressIsContract;
	public Long block_ts;
	public String event_type;
	public TronScanResTriggerInfo trigger_info;
	public Boolean fromAddressIsContract;
	public Long block;
	public String from_address;
	public Integer status;
	public String finalResult;

	/** 交易是否已经被确认（超级节点） */
	public Boolean isConfirmed() {
		return confirmed;
	}

	/** 交易是否成功 */
	public Boolean isContractSuccess() {
		return contractRet.equals("SUCCESS") && finalResult.equals("SUCCESS");
	}

	/** 交易是【TRC20】合约 */
	public Boolean isTRC20Contract() {
		return StringUtils.compare(contract_Type, "trc20") == 0;
	}

	/** 是否是【转账类型】 */
	public boolean isTransferEvent() {
		return StringUtils.compare(event_type, "Transfer") == 0;
	}

	/** 获取转账金额 */
	public String getTrc20Amount() {
		return trigger_info.parameter._value;
	}

	public String getFromAddress() {
		return from_address;
	}

	public String getToAddress() {
		return trigger_info.parameter._to;
	}
}
