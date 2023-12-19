package com.play.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.play.core.BankInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.play.dao.StationBankDao;
import com.play.model.StationBank;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationBankService;
import com.play.service.TronTransIndexAddressService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 站点银行信息表
 *
 * @author admin
 */
@Service
public class StationBankServiceImpl implements StationBankService {
	@Autowired
	private StationBankDao stationBankDao;

	@Override
	public Page<StationBank> adminPage(Long stationId, String bankName, String bankCode) {
		return stationBankDao.page(stationId, bankName, bankCode);
	}

	@Override
	public void adminAddBank(StationBank bank) {
		String name = "";
		String code = "";
		Integer sortNo = bank.getSortNo();
		if (StringUtils.isNotEmpty(bank.getBankCode()) &&
				BankInfo.OTHER.getBankCode().equalsIgnoreCase(bank.getBankCode())) {
			name = bank.getName().trim();
			code = bank.getCode().trim();
		}else{
			BankInfo info = BankInfo.getBankInfoByCode(bank.getBankCode());
			name = info.getBankName();
			code = info.getBankCode();
		}
		bank.setName(name);
		bank.setCode(code);
		// 校验合法性
		checkStationBank(name, code, sortNo, bank.getStationId());
		stationBankDao.save(bank);
	}

	@Override
	public void deleteStationBank(Long stationId, Long id) {
		StationBank stationBank = stationBankDao.findOneById(id);
		if (stationBank == null) {
			throw new ParamException(BaseI18nCode.bankStationIsNotExist);
		}
		if (!stationBank.getStationId().equals(stationId)) {
			throw new ParamException(BaseI18nCode.userNotBelongToSite);
		}
		stationBankDao.deleteById(id);
	}

	@Override
	public boolean modifySave(StationBank bank) {
		String name = "";
		String code = "";
		Integer sortNo = bank.getSortNo();
		if (StringUtils.isNotEmpty(bank.getBankCode()) &&
				BankInfo.OTHER.getBankCode().equalsIgnoreCase(bank.getBankCode())) {
			name = bank.getName().trim();
			code = bank.getCode().trim();
		}else{
			BankInfo info = BankInfo.getBankInfoByCode(bank.getBankCode());
			name = info.getBankName();
			code = info.getBankCode();
		}
		// 校验合法性
		checkModifySave(bank.getId(), name, code, sortNo, bank.getStationId());
		bank.setName(name);
		bank.setCode(code);
		return stationBankDao.modifySave(bank);
	}

	@Override
	public StationBank findOneById(Long id, Long stationId) {
		return stationBankDao.findOne(id, stationId);
	}

	private void checkStationBank(String name, String code, Integer sortNo, Long stationId) {
		if (StringUtils.isEmpty(name)) {
			throw new ParamException(BaseI18nCode.bankNameCanntEmpty);
		}
		if (StringUtils.isEmpty(code)) {
			throw new ParamException(BaseI18nCode.bankcodeCanntEmpty);
		}
		if (StringUtils.equals(code, StationBank.otherCode)) {
			throw new ParamException(BaseI18nCode.bankcodeCanntRepeat);
		}
		if (sortNo == null) {
			throw new ParamException(BaseI18nCode.bankSortNoCanntEmpty);
		}
		StationBank stationBank = stationBankDao.findStationBankByName(name, stationId);
		if (stationBank != null) {
			throw new ParamException(BaseI18nCode.bankNameCanntRepeat);
		}
		StationBank bankByCode = stationBankDao.findStationBankByCode(code, stationId);
		if (bankByCode != null) {
			throw new ParamException(BaseI18nCode.bankcodeCanntRepeat);
		}
	}

	private void checkModifySave(Long id, String name, String code, Integer sortNo, Long stationId) {
		if (StringUtils.isEmpty(name)) {
			throw new ParamException(BaseI18nCode.bankNameCanntEmpty);
		}
		if (StringUtils.isEmpty(code)) {
			throw new ParamException(BaseI18nCode.bankcodeCanntEmpty);
		}
		if (StringUtils.equals(code, StationBank.otherCode)) {
			throw new ParamException(BaseI18nCode.bankcodeCanntRepeat);
		}
		if (sortNo == null) {
			throw new ParamException(BaseI18nCode.bankSortNoCanntEmpty);
		}
		StationBank bankDaoOne = stationBankDao.findOne(id, stationId);
		if (bankDaoOne == null) {
			throw new ParamException(BaseI18nCode.stationUnExist);
		}
		StationBank stationBank = stationBankDao.findStationBankByName(name, stationId);
		if (stationBank != null && !stationBank.getName().equals(bankDaoOne.getName())) {
			throw new ParamException(BaseI18nCode.bankNameCanntRepeat);
		}
		StationBank bankByCode = stationBankDao.findStationBankByCode(code, stationId);
		if (bankByCode != null && !bankByCode.getCode().equals(bankDaoOne.getCode())) {
			throw new ParamException(BaseI18nCode.bankcodeCanntRepeat);
		}
	}

	@Override
	public List<StationBank> findAll(Long stationId) {
		List<StationBank> list = stationBankDao.findAll(stationId);
		if (list == null) {
			list = new ArrayList<>();
		}
		StationBank bank = new StationBank();
		bank.setName(I18nTool.getMessage("admin.other.thing"));
		bank.setCode(StationBank.otherCode);
		list.add(bank);
		return list;
	}

	@Override
	public String getNameByCode(String code, Long stationId) {
		StationBank bank = stationBankDao.findStationBankByCode(code, stationId);
		if (bank != null) {
			return bank.getName();
		}
		return "";
	}

	@Override
	public StationBank getStationBankByCode(String code, Long stationId) {
		StationBank stationBank = stationBankDao.findStationBankByCode(code, stationId);
		if (ObjectUtils.isNotEmpty(stationBank)) {
			return stationBankDao.findStationBankByCode(code, stationId);
		}
		return new StationBank();
	}
}
