package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.play.tronscan.allclient.alltool.SysPreDefineTron;

@SuppressWarnings("serial")
public class TronScanResAccount implements Serializable {
	public Long transactions_out;
	public Long acquiredDelegateFrozenForBandWidth;
	public Long rewardNum;
	// 总质押
	public Long totalFrozen;
	// 为自己质押的【能量】和【带宽】
	public Long frozenForEnergy;
	public Long frozenForBandWidth;
	// 为别人质押的【能量】和【带宽】
	public Long delegateFrozenForEnergy;
	public Long delegateFrozenForBandWidth;
	public Long balance;
	public Long voteTotal;
	public Long transactions_in;
	public Long totalTransactionCount;
	public Long reward;
	public String addressTagLogo;
	public String address;
	public Long date_created;
	public Long accountType;
	public String addressTag;
	public Long transactions;
	public Long acquiredDelegateFrozenForEnergy;
	public String name;
	public List<TronScanResTRC20TokenBalance> trc20token_balances;

	public Long getAvailableTRXBalance() {
		return balance - frozenForEnergy - frozenForBandWidth;
	}

	public Long getUSDTBalance() {
		if (trc20token_balances == null)
			return 0L;
		for (TronScanResTRC20TokenBalance item : trc20token_balances) {
			if (StringUtils.compare(item.tokenId, SysPreDefineTron.MAINNET_USDT_ADDRESS) == 0)
				return Long.valueOf(item.balance);
		}
		return 0L;
	}
}
