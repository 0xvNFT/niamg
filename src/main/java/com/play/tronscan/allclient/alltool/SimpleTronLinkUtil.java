package com.play.tronscan.allclient.alltool;

import java.math.BigDecimal;


import com.play.tronscan.allclient.clientbase.res.TronScanResTRC20Transfer;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransaction;

/**
 */
public final class SimpleTronLinkUtil {
	/** log */
	//static Logger log = LoggerFactory.getLogger(SimpleTronLinkUtil.class);

	/**
	 * 返回【addressShortText】
	 * </p>
	 * 
	 * @param address 输入
	 * @return String 输出
	 */
	public static String addressShortText(String address) {
		StringBuilder builder = new StringBuilder();
		builder.append(address.substring(0, 6) + "..." + address.substring(address.length() - 6, address.length()));
		return builder.toString();
	}

	/**
	 * 是否有效的【TRX交易】
	 * </p>
	 * 
	 * @param transaction 输入
	 * @param address     交易目标地址
	 * @param minMoney    交易最小值
	 * @return boolean 输出
	 */
	public static boolean isValidTRXTransaction(TronScanResTransaction transaction, String address, Long minMoney) {
		// 是否成功
		if (transaction.isSuccess() == false)
			return false;
		// 发起人是自己
		if (transaction.ownerAddress.equals(address))
			return false;
		// 不是TRX交易
		if (transaction.isTrxTransaction() == false)
			return false;
		// 交易太老 大于3天
		if (transaction.timestamp.compareTo(System.currentTimeMillis() - 3 * SysPreDefineTron.ONE_DAY_MILLE_SECONDES) < 0)
			return false;
		// TRX交易金额太小
		if (Long.valueOf(transaction.getTrxAmount()) < minMoney)
			return false;
		return true;
	}

	/**
	 * 是否有效的【USDT交易】
	 * </p>
	 * 请注意 这里不要使用 【stream】方式过滤
	 * </p>
	 * 特别是【金额】的判断需要放在最后，否则容易溢出（有些合约的小数位数很多）
	 * 
	 * @param transfer 输入
	 * @param address  交易目标地址
	 * @param minMoney 交易最小值
	 * @return boolean 输出
	 */
	public static boolean isValidUSDTTransaction(TronScanResTRC20Transfer transfer, String address, Long minMoney) {
		// 是否成功
		if (transfer.isContractSuccess() == false)
			return false;
		// 【TRC20合约】
		if (transfer.isTRC20Contract() == false)
			return false;
		// 是否转账类型
		if (transfer.isTransferEvent() == false)
			return false;
		// 发起人是自己
		if (transfer.getFromAddress().equals(address))
			return false;
		// 交易太老 大于3天
		if (transfer.block_ts.compareTo(System.currentTimeMillis() - 3 * SysPreDefineTron.ONE_DAY_MILLE_SECONDES) < 0)
			return false;
		// 不是USDT
		if (SysPreDefineTron.MAINNET_USDT_ADDRESS.equals(transfer.trigger_info.contract_address) == false)
			return false;
		// USDT交易金额太小
		if (Long.valueOf(transfer.getTrc20Amount()) < minMoney * SysPreDefineTron.ONE_USDT_VALUE)
			return false;
		return true;
	}

	/**
	 * 这里请求之间需要简单的控制下间隔
	 * </p>
	 * 否则容易被波场封IP....
	 */
	public static void sleepAwhile(Long milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	/**
	 * 金额转换
	 */
	public static BigDecimal getReadableValue(String money) {
		return BigDecimal.valueOf(Long.valueOf(money) * Math.pow(0.1, 6)).setScale(6, BigDecimal.ROUND_HALF_UP);
	}
}