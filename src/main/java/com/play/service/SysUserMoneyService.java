package com.play.service;

import java.math.BigDecimal;

import com.play.core.MoneyRecordType;
import com.play.model.SysUserMoneyHistory;
import com.play.model.bo.MnyMoneyBo;

/**
 * 会员余额信息表
 *
 * @author admin
 *
 */
public interface SysUserMoneyService {
	/**
	 * 初始化
	 * 
	 * @param userId
	 * @param money
	 */
	void init(Long userId, BigDecimal money);

	/**
	 * 修改金额并记录帐变记录，试玩账号不记录账变
	 * 
	 * @param moneyBo
	 * @return
	 */
	SysUserMoneyHistory updMnyAndRecord(MnyMoneyBo moneyBo);

	BigDecimal getMoney(Long userId);

	void moneyOpe(Long userId, Long stationId, MoneyRecordType type, BigDecimal money, BigDecimal betNumMultiple,
			String remark, BigDecimal giftMoney, BigDecimal giftBetNumMultiple, Long score, boolean useStrategy,
			String bgRemark);

}