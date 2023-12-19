package com.play.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.play.common.Constants;
import com.play.core.BankInfo;
import com.play.core.StationConfigEnum;
import com.play.model.dto.ExchangeUSDTDto;
import com.play.web.exception.BaseException;
import com.play.web.utils.StationConfigUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.dao.StationDepositBankDao;
import com.play.model.StationBank;
import com.play.model.StationDepositBank;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationBankService;
import com.play.service.StationDepositBankService;
import com.play.service.TronTransIndexAddressService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.HidePartUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;
import org.springframework.util.CollectionUtils;

/**
 * @author admin
 */
@Service
public class StationDepositBankServiceImpl implements StationDepositBankService {
	@Autowired
	private TronTransIndexAddressService tronTransIndexAddressService;
	@Autowired
	private StationDepositBankDao stationDepositBankDao;
	@Autowired
	private StationBankService bankService;

	@Override
	public Page<StationDepositBank> getPage(String name, Integer status, Long stationId) {
		Page<StationDepositBank> page = stationDepositBankDao.getPage(name, status, stationId);
		return page;
	}

	@Override
	public void addSave(StationDepositBank bank) {
		if (bank == null) {
			throw new ParamException(BaseI18nCode.dataError);
		}
		if (bank.getBankCard() == null) {
			throw new ParamException(BaseI18nCode.cardNoFormatError);
		}
		Long stationId = SystemUtil.getStationId();
		StationBank stationBank = null;
		if (!StringUtils.equals(StationBank.otherCode, bank.getPayPlatformCode())) {
			stationBank = bankService.getStationBankByCode(bank.getPayPlatformCode(), stationId);
			bank.setBankName(stationBank.getName());
		}

		if(BankInfo.USDT.getBankName().equals(bank.getBankName())) {
			List<StationDepositBank> list = this.getStationDepositBankList(bank.getStationId(), BankInfo.USDT.getBankName(), null, null);
			boolean isExist = list.stream().filter(e -> e.getStatus() == 2 || e.getBankCard().equals(bank.getBankCard())).findFirst().isPresent();
			if (isExist) {
				throw new BaseException(BaseI18nCode.tronLinkAddrEnabled);
			}
		}

		// 新增后禁用
		bank.setCreateUser(LoginAdminUtil.getUserId());
		bank.setStationId(stationId);
		LogUtils.addLog("新增银行入款,卡号：" + bank.getBankCard() + "  持卡人：" + bank.getRealName());
		stationDepositBankDao.save(bank);
	}

	@Override
	public void modifySave(StationDepositBank bank) {
		if (bank == null) {
			throw new ParamException(BaseI18nCode.dataError);
		}
		if (bank.getBankCard() == null) {
			throw new ParamException(BaseI18nCode.cardNoFormatError);
		}
		if (!StringUtils.equals(StationBank.otherCode, bank.getPayPlatformCode())) {
			bank.setBankName(bankService.getNameByCode(bank.getPayPlatformCode(), bank.getStationId()));
		}
		StationDepositBank old = stationDepositBankDao.findOne(bank.getId(), bank.getStationId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.dataError);
		}
		old.setBankName(bank.getBankName());
		old.setBankAddress(bank.getBankAddress());
		old.setBankCard(bank.getBankCard());
		old.setRealName(bank.getRealName());
		old.setMin(bank.getMin());
		old.setMax(bank.getMax());
		old.setSortNo(bank.getSortNo());
		// 修改后禁用
		old.setStatus(bank.getStatus());
		old.setDepositRate(bank.getDepositRate());
		old.setWithdrawRate(bank.getWithdrawRate());
		old.setPayPlatformCode(bank.getPayPlatformCode());
		old.setIcon(bank.getIcon());
		// 修改是重新保存创建人
		old.setCreateUser(LoginAdminUtil.getUserId());
		old.setOpenUser(null);
		old.setQrCodeImg(bank.getQrCodeImg());
		old.setAliQrcodeStatus(bank.getAliQrcodeStatus());
		old.setCardIndex(bank.getCardIndex());
		old.setRemark(bank.getRemark());
		old.setBgRemark(bank.getBgRemark());
		old.setDegreeIds(bank.getDegreeIds());
		old.setGroupIds(bank.getGroupIds());
		LogUtils.modifyLog("修改银行入款,卡号：" + old.getBankCard() + "  持卡人：" + old.getRealName());
		stationDepositBankDao.update(old, false);
	}

	@Override
	public void delBank(long id) {
		StationDepositBank bank = stationDepositBankDao.findOneById(id);
		if (bank == null) {
			throw new ParamException(BaseI18nCode.depositMethodNotExist);
		}
		// 如果删除USDT，需要同时删除tron_trans_index_address相应数据
		if(BankInfo.USDT.getBankName().equals(bank.getBankName())) {
			tronTransIndexAddressService.deleteByAddress(bank.getBankCard());
		}
		stationDepositBankDao.deleteById(id);
		LogUtils.delLog("删除银行入款，卡号：" + bank.getBankCard());
	}

    @Override
    public StationDepositBank getOne(Long id, Long stationId, Long groupId, Long degreeId, String bankName) {
        StationDepositBank ma = null;
        if (id != null) {
            ma = stationDepositBankDao.findOneById(id);
            if (ma != null && (stationId == null || ma.getStationId().equals(stationId))) {
                if (StringUtils.isEmpty(ma.getBankName())) {
                    ma.setBankName(bankService.getNameByCode(ma.getPayPlatformCode(), stationId));
                }
            }
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("bankName", bankName);
            map.put("stationId", stationId);
            map.put("groupId", groupId);
            map.put("degreeId", degreeId);
            ma = stationDepositBankDao.findOne("select * from station_deposit_bank where and station_id=:stationId  group_id=:groupIdand  and degree_id=:degreeId bank_name=:bankName", map);
        }
        return ma;
    }


    @Override
	public StationDepositBank getOne(Long id, Long stationId) {
		if (id != null) {
			StationDepositBank ma = stationDepositBankDao.findOneById(id);
			if (ma != null && (stationId == null || ma.getStationId().equals(stationId))) {
				if (StringUtils.isEmpty(ma.getBankName())) {
					ma.setBankName(bankService.getNameByCode(ma.getPayPlatformCode(), stationId));
				}
				return ma;
			}
		}
		return null;
	}

	@Override
	public void updateStatus(Integer status, Long id, Long stationId) {
		if (id == null || id <= 0 || status == null || stationId == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		Long loginUser = LoginAdminUtil.getUserId();
		StationDepositBank bank = stationDepositBankDao.findOneById(id);
		if (bank == null) {
			throw new ParamException(BaseI18nCode.depositMethodNotExist);
		}

		// 如果启用USDT，需验证是否已存在启用的记录，同一站点只能配置一个启用的Tron链地址
		if(BankInfo.USDT.getBankName().equals(bank.getBankName()) && status == Constants.STATUS_ENABLE) {
			List<StationDepositBank> list = this.getStationDepositBankList(bank.getStationId(), BankInfo.USDT.getBankName(), null, Constants.STATUS_ENABLE);
			if(!CollectionUtils.isEmpty(list)) {
				throw new ParamException(BaseI18nCode.tronLinkAddrEnabled);
			}
		}

		// 如果禁用USDT，需要同时删除tron_trans_index_address相应数据（该表last_timestamp通过定时任务刷新时间，会影响从tronScan取数据），删除后，下次启用定时任务会创建最新时间的记录
		if(BankInfo.USDT.getBankName().equals(bank.getBankName()) && status == Constants.STATUS_DISABLE) {
			tronTransIndexAddressService.deleteByAddress(bank.getBankCard());
		}

		stationDepositBankDao.updateStatus(status, id, stationId, loginUser);
		LogUtils.modifyStatusLog("修改银行入款设置：" + bank.getBankName() + "  修改网站入款设置状态："
				+ ((status.equals(1)) ? "禁用" : "启用") + "    站点：" + stationId);
	}

	@Override
	public List<StationDepositBank> getStationBankList(Long stationId, Long degreeId, Long groupId, Integer status,String bankName) {
		List<StationDepositBank> bankList = stationDepositBankDao.getStationBankList(stationId, degreeId, groupId,
				status,bankName);
		if (bankList != null && !bankList.isEmpty()) {
			bankList.forEach(x -> {
				if (StringUtils.isEmpty(x.getBankName())) {
					x.setBankName(bankService.getNameByCode(x.getPayPlatformCode(), stationId));
				}
				if (StringUtils.isNotEmpty(x.getCardIndex()) && StringUtils.isNotEmpty(x.getBankCard())) {
					x.setBankCard(HidePartUtil.removePart(x.getBankCard()));
				}
			});
		}
		return bankList;
	}


	@Override
	public List<StationDepositBank> getBankListByStation(Long stationId) {
		return stationDepositBankDao.getBankListByStation(stationId);
	}

	@Override
	public void remarkModify(Long id, String remark) {
		Long stationId = SystemUtil.getStationId();
		StationDepositBank ado = stationDepositBankDao.findOne(id, SystemUtil.getStationId());
		if (ado == null) {
			throw new ParamException(BaseI18nCode.depositMethodNotExist);
		}
		stationDepositBankDao.changeRemark(id, stationId, remark);
		LogUtils.modifyLog("用户:" + ado.getCreateUser() + " 的备注从 " + ado.getBgRemark() + " 修改成 " + remark);
	}

	/**
	 * 获取单个
	 *
	 * @param  bankCard
	 * @param stationId
	 * @param bankName
	 * @return
	 */
	public StationDepositBank getUsdtBank(Long stationId,String bankName,String bankCard){
		if (Objects.nonNull(bankCard)) {
			StationDepositBank stationDepositBank = stationDepositBankDao.findUsdtOne(stationId,bankName,bankCard);
			return stationDepositBank;
		}
		return null;
	}

	@Override
	public List<StationDepositBank> getAllUsdtBank() {
		return stationDepositBankDao.findUsdtAll();
	}

	@Override
	public List<StationDepositBank> getStationDepositBankList(Long stationId, String bankName, String bankCard, Integer status) {
		return stationDepositBankDao.getStationDepositBankList(stationId, bankName, bankCard, status);
	}

	@Override
	public ExchangeUSDTDto getUsdtRate(Long stationId) {
		// 后台站点只能配置一个启用状态的tron链地址（出于TronScan单日请求数量限制，及获取站点取款汇率等原因）
        List<StationDepositBank> list = this.getStationDepositBankList(stationId, BankInfo.USDT.getBankName(), null, Constants.STATUS_ENABLE);
        StationDepositBank stationDepositBank = list.stream().findFirst().orElseThrow(() -> new ParamException(BaseI18nCode.tronLinkAddrNotExist));
        //stationDepositBank.setDepositRate(new BigDecimal(0.1));
	//	stationDepositBank.setWithdrawRate(new BigDecimal(0.1));
		ExchangeUSDTDto dto = new ExchangeUSDTDto();
		if (ObjectUtils.isNotEmpty(stationDepositBank.getDepositRate())) {
			dto.setDepositRate(stationDepositBank.getDepositRate());
		} else {
			String depositRate = StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_usdt_rate);
			dto.setDepositRate(new BigDecimal(depositRate));
		}
		if (ObjectUtils.isNotEmpty(stationDepositBank.getWithdrawRate())) {
			dto.setWithdrawRate(stationDepositBank.getWithdrawRate());
		} else {
			String withdrawRate = StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_withdraw_usdt_rate);
			dto.setWithdrawRate(new BigDecimal(withdrawRate));
		}

		return dto;
	}
}
