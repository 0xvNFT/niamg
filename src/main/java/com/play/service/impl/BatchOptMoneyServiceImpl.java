package com.play.service.impl;

import java.math.BigDecimal;

import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.play.common.utils.BigDecimalUtil;
import com.play.core.MoneyRecordType;
import com.play.model.SysUser;
import com.play.service.BatchOptMoneyService;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserService;
import com.play.web.exception.BaseException;
import com.play.web.var.GuestTool;

@Service
public class BatchOptMoneyServiceImpl implements BatchOptMoneyService {
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserMoneyService userMoneyService;

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void batchAddMoney(Long stationId, Integer type, BigDecimal money, BigDecimal giftMoney, String usernames,
			BigDecimal betNumMultiple, BigDecimal giftBetNumMultiple, String remark, String bgRemark) {
		if (StringUtils.isEmpty(usernames)) {
			throw new ParamException(BaseI18nCode.stationInputVipName);
		}
		String errors = null;
		if (type == 1) {
			errors = batchAddMoneyCus(stationId, usernames, betNumMultiple, giftBetNumMultiple, remark, bgRemark);
		} else if (type == 2) {
			errors = batchAddMoneyFixed(stationId, usernames, money, giftMoney, betNumMultiple, giftBetNumMultiple,
					remark, bgRemark);
		} else if (type == 3) {
			errors = batchAddMoneyOther(stationId, usernames, betNumMultiple, remark, bgRemark);
		}
		if (errors.length() > 0) {
			errors.substring(0, errors.length() - 1);
			throw new BaseException(BaseI18nCode.stationErrorMessage, new Object[] { errors.toString() });
		}
	}

	private String batchAddMoneyFixed(Long stationId, String usernames, BigDecimal money, BigDecimal giftMoney,
			BigDecimal betNumMultiple, BigDecimal giftBetNumMultiple, String remark, String bgRemark) {
		if (money == null || giftMoney == null) {
			throw new ParamException(BaseI18nCode.stationInputMoneyOrCash);
		}
		if (money != null && money.compareTo(BigDecimal.ZERO) < 0) {
			throw new ParamException(BaseI18nCode.moneyFormatError);
		}
		if (giftMoney != null && giftMoney.compareTo(BigDecimal.ZERO) < 0) {
			throw new ParamException(BaseI18nCode.cashFormatError);
		}
		StringBuilder errors = new StringBuilder();
		String[] uns = usernames.split(" |,|\n");
		SysUser user = null;
		for (String un : uns) {
			un = StringUtils.trim(un);
			if (StringUtils.isEmpty(un)) {
				continue;
			}
			user = userService.findOneByUsername(un.toLowerCase(), stationId, null);
			if (user == null || GuestTool.isGuest(user)) {
				errors.append(un).append(":"+BaseI18nCode.userNotExist.getMessage()+ ",");
				continue;
			}
			try {
				userMoneyService.moneyOpe(user.getId(), stationId, MoneyRecordType.DEPOSIT_ARTIFICIAL, money,
						betNumMultiple, remark, giftMoney, giftBetNumMultiple, null, false, bgRemark);
			} catch (Exception e) {
				errors.append(un).append(":").append(e);
			}
		}
		return errors.toString();

	}

	private String batchAddMoneyCus(Long stationId, String usernames, BigDecimal betNumMultiple,
			BigDecimal giftBetNumMultiple, String remark, String bgRemark) {
		String[] uns = usernames.split("\n");
		SysUser user = null;
		String[] ais = null;
		BigDecimal money = null;
		BigDecimal giftMoney = null;
		StringBuilder errors = new StringBuilder();
		for (String un : uns) {
			un = StringUtils.trim(un);
			if (StringUtils.isEmpty(un)) {
				continue;
			}
			un = un.replaceAll("( +)|(,+)", " ");
			ais = un.split("\t| ");
			if (ais.length < 2) {
				errors.append(un).append(BaseI18nCode.stationDataFormatError.getMessage()+",");
				continue;
			}
			user = userService.findOneByUsername(ais[0].toLowerCase(), stationId, null);
			if (user == null || GuestTool.isGuest(user)) {
				errors.append(un).append(":"+BaseI18nCode.userNotExist.getMessage()+ ",");
				continue;
			}
			money = BigDecimalUtil.toBigDecimal(ais[1]);
			if (money == null || money.compareTo(BigDecimal.ZERO) < 0) {
				errors.append(un).append(":"+BaseI18nCode.moneyFormatError.getMessage()+ ",");
				continue;
			}
			giftMoney = new BigDecimal(0);
			if (ais.length > 2) {
				giftMoney = BigDecimalUtil.toBigDecimal(ais[2]);
			}
			try {
				userMoneyService.moneyOpe(user.getId(), stationId, MoneyRecordType.DEPOSIT_ARTIFICIAL, money,
						betNumMultiple, remark, giftMoney, giftBetNumMultiple, null, false, bgRemark);
			} catch (Exception e) {
				errors.append(un).append(":").append(e);
			}
		}
		return errors.toString();
	}

	private String batchAddMoneyOther(Long stationId, String usernames, BigDecimal betNumMultiple, String remark,
			String bgRemark) {
		String[] uns = usernames.split("\n");
		SysUser user = null;
		String[] ais = null;
		BigDecimal money = null;
		StringBuilder errors = new StringBuilder();
		for (String un : uns) {
			un = StringUtils.trim(un);
			if (StringUtils.isEmpty(un)) {
				continue;
			}
			un = un.replaceAll("( +)|(,+)", " ");
			ais = un.split("\t| ");
			if (ais.length < 2) {
				errors.append(un).append(BaseI18nCode.stationDataFormatError.getMessage()+",");
				continue;
			}
			user = userService.findOneByUsername(ais[0].toLowerCase(), stationId, null);
			if (user == null || GuestTool.isGuest(user)) {
				errors.append(un).append(":"+BaseI18nCode.userNotExist.getMessage()+ ",");
				continue;
			}
			money = BigDecimalUtil.toBigDecimal(ais[1]);
			if (money == null || money.compareTo(BigDecimal.ZERO) < 0) {
				errors.append(un).append(":"+BaseI18nCode.moneyFormatError.getMessage()+ ",");
				continue;
			}
			try {
				userMoneyService.moneyOpe(user.getId(), stationId, MoneyRecordType.GIFT_OTHER, money, betNumMultiple,
						remark, null, null, null, false, bgRemark);
			} catch (Exception e) {
				errors.append(un).append(":").append(e);
			}
		}
		return errors.toString();
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void batchSubMoney(Long stationId, String usernames, BigDecimal money, String remark, String bgRemark,
			MoneyRecordType type) {
		if (StringUtils.isEmpty(usernames)) {
			throw new ParamException(BaseI18nCode.stationInputVipName);
		}
		if (money == null || money.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ParamException(BaseI18nCode.moneyFormatError);
		}
		String[] uns = usernames.split(" |,|\n");
		StringBuilder errors = new StringBuilder();
		SysUser user = null;
		for (String un : uns) {
			un = StringUtils.trim(un);
			if (StringUtils.isEmpty(un)) {
				continue;
			}
			user = userService.findOneByUsername(un.toLowerCase(), stationId, null);
			if (user == null || GuestTool.isGuest(user)) {
				errors.append(un).append(":"+BaseI18nCode.userNotExist.getMessage()+ ",");
				continue;
			}
			try {
				userMoneyService.moneyOpe(user.getId(), stationId, type, money, null, remark, null, null, null, false,
						bgRemark);
			} catch (Exception e) {
				errors.append(un).append(":").append(e);
			}
		}
		if (errors.length() > 0) {
			errors.deleteCharAt(errors.length() - 1);
			throw new BaseException(BaseI18nCode.stationErrorMessage, new Object[]{ errors } );
		}
	}
}
