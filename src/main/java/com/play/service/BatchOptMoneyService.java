package com.play.service;

import com.play.core.MoneyRecordType;

import java.math.BigDecimal;

public interface BatchOptMoneyService {
	/**
	 * 手动批量添加金额
	 * 
	 * @param stationId
	 * @param type               加款方式 1自定义加款 2固定金额加款 3自定义其他金额加款
	 * @param money
	 * @param giftMoney
	 * @param usernames
	 * @param betNumMultiple
	 * @param giftBetNumMultiple
	 * @param remark
	 * @param backDesc
	 */
	void batchAddMoney(Long stationId, Integer type, BigDecimal money, BigDecimal giftMoney, String usernames,
			BigDecimal betNumMultiple, BigDecimal giftBetNumMultiple, String remark, String bgRemark);

	void batchSubMoney(Long stationId, String usernames, BigDecimal money, String remark, String bgRemark,
			MoneyRecordType type);
}
