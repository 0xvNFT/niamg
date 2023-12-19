package com.play.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.play.core.BankInfo;
import com.play.service.SysUserTronLinkService;
import com.play.tronscan.allclient.alltool.SysPreDefineTron;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.core.StationConfigEnum;
import com.play.dao.SysUserBankDao;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserInfoDao;
import com.play.model.StationBank;
import com.play.model.SysUser;
import com.play.model.SysUserBank;
import com.play.model.SysUserInfo;
import com.play.model.vo.BankVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationBankService;
import com.play.service.SysUserBankService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.ValidateUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户银行卡信息表
 *
 * @author admin
 *
 */
@Service
public class SysUserBankServiceImpl implements SysUserBankService {
//	private Logger logger = LoggerFactory.getLogger(SysUserBankServiceImpl.class);
	@Autowired
	private SysUserBankDao sysUserBankDao;
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private SysUserInfoDao userInfoDao;
	@Autowired
	private StationBankService stationBankService;
	@Autowired
	private SysUserTronLinkService sysUserTronLinkService;

	@Override
	public Page<SysUserBank> adminPage(Long stationId, String username, String cardNo, String degreeIds,
			String bankName, String realname, String bankAddress) {
		Long userId = null;
		if (StringUtils.isNotEmpty(username)) {
			SysUser user = userDao.findOneByUsername(username, stationId, null);
			if (user == null) {
				throw new ParamException(BaseI18nCode.userNotExist);
			}
			userId = user.getId();
		}
		return sysUserBankDao.page(stationId, userId, cardNo, degreeIds, bankName, realname, bankAddress);
	}

	@Override
	public void adminAddBank(SysUserBank bank) {
		bank.setUsername(StringUtils.trim(bank.getUsername()));
		if (StringUtils.isEmpty(bank.getUsername())) {
			throw new ParamException(BaseI18nCode.usernameCanntEmpty);
		}
		bank.setRealName(StringUtils.trim(bank.getRealName()));
		bank.setCardNo(StringUtils.trim(bank.getCardNo()));
		bank.setBankName(StringUtils.trim(bank.getBankName()));
		bank.setBankAddress(StringUtils.trim(bank.getBankAddress()));
		if ((!BankInfo.USDT.getBankName().equals(bank.getBankName()) && StringUtils.isEmpty(bank.getRealName()))
				|| StringUtils.isEmpty(bank.getCardNo())
				|| (!BankInfo.USDT.getBankName().equals(bank.getBankName()) && StringUtils.isEmpty(bank.getBankName()))) {
			throw new ParamException(BaseI18nCode.completeBankInfoRequired);
		}

		// 验证卡号是否有效
		if(BankInfo.USDT.getBankCode().equals(bank.getBankCode())
				|| BankInfo.USDT.getBankName().equals(bank.getBankName())) {
			if (!SysPreDefineTron.isValidBase58Address(bank.getCardNo())) {
				throw new ParamException(BaseI18nCode.tronLinkAddrIllegal);
			}
		} else {
			if (!ValidateUtil.isBankCardNo(bank.getCardNo())) {
				throw new ParamException(BaseI18nCode.cardNoFormatError);
			}
		}

		SysUser user = userDao.findOneByUsername(bank.getUsername(), bank.getStationId(), null);
		if (user == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}

		if(BankInfo.USDT.getBankCode().equals(bank.getBankCode())
				|| BankInfo.USDT.getBankName().equals(bank.getBankName())) {
			// 一个会员只能绑定一个TronLink地址
			if (ObjectUtils.isNotEmpty(sysUserTronLinkService.findOne(user.getId(), user.getStationId()))) {
				throw new ParamException(BaseI18nCode.tronLinkAddrExist);
			}
		} else {
			// 验证用户是否绑定过这张卡
			if (sysUserBankDao.exist(user.getId(), bank.getStationId(), bank.getCardNo())) {
				throw new ParamException(BaseI18nCode.bankCardNoExist);
			}
		}

		// 验证多账号一张卡
		if (!StationConfigUtil.isOn(bank.getStationId(), StationConfigEnum.user_use_same_bank_card)) {
			if (sysUserBankDao.exist(null, bank.getStationId(), bank.getCardNo())) {
				throw new ParamException(BaseI18nCode.bankCardNoExist);
			}
		}
		// 验证真实姓名
		SysUserInfo info = userInfoDao.findOne(user.getId(), bank.getStationId());
		if (info == null) {
			throw new ParamException(BaseI18nCode.userInfoUnExist);
		}
		if (StringUtils.isEmpty(info.getRealName())) {
			userInfoDao.updateRealName(user.getId(), bank.getStationId(), bank.getRealName());
			info.setRealName(bank.getRealName());
		}
		// 是否开启了银行卡所属人唯一
		if(!BankInfo.USDT.getBankCode().equals(bank.getBankCode())
				&& !BankInfo.USDT.getBankName().equals(bank.getBankName())) {
			if (StationConfigUtil.isOn(bank.getStationId(), StationConfigEnum.bank_real_name_only)) {
				if (!info.getRealName().equals(bank.getRealName())) {
					throw new ParamException(BaseI18nCode.bankRealNameMustSameInfo);
				}
			}
		}
		bank.setUserId(user.getId());
		bank.setCreateTime(new Date());

		// 若添加银行卡类型为USDT，初始状态为禁用，验证初始金额后，通过定时任务置为启用
		if(BankInfo.USDT.getBankCode().equals(bank.getBankCode())
				|| BankInfo.USDT.getBankName().equals(bank.getBankName())) {
			bank.setStatus(Constants.STATUS_DISABLE);
			// 写入sys_user_tron_link
			sysUserTronLinkService.addUserTronLink(user, user.getStationId(), bank.getCardNo());
		} else {
			bank.setStatus(Constants.STATUS_ENABLE);
		}

		sysUserBankDao.save(bank);
		LogUtils.modifyLog("新增会员 " + bank.getUsername() + "银行卡信息成功,卡号:" + bank.getCardNo());
	}

	@Override
	public SysUserBank findOneById(Long id, Long stationId) {
		return sysUserBankDao.findOne(id, stationId);
	}

	@Override
	public void modifySave(SysUserBank bank) {
		SysUserBank old = sysUserBankDao.findOne(bank.getId(), bank.getStationId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.userBankUnExist);
		}
		bank.setCardNo(StringUtils.trim(bank.getCardNo()));
		bank.setBankName(StringUtils.trim(bank.getBankName()));
		bank.setBankAddress(StringUtils.trim(bank.getBankAddress()));
		String oldCardNo = old.getCardNo();
		if (StringUtils.isEmpty(bank.getCardNo()) || !ValidateUtil.isBankCardNo(bank.getCardNo())) {
			throw new ParamException(BaseI18nCode.cardNoFormatError);
		}
		if (StringUtils.isEmpty(bank.getBankName()) || StringUtils.isEmpty(bank.getBankCode())) {
			throw new ParamException(BaseI18nCode.bankNameRequired);
		}
		if (!StringUtils.equals(bank.getCardNo(), oldCardNo)) {
			if (StationConfigUtil.isOff(bank.getStationId(), StationConfigEnum.user_use_same_bank_card)
					&& sysUserBankDao.exist(null, bank.getStationId(), bank.getCardNo())) {
				throw new ParamException(BaseI18nCode.bankCardNoExist);
			}
		}
		old.setBankCode(bank.getBankCode());
		old.setBankName(bank.getBankName());
		old.setCardNo(bank.getCardNo());
		old.setRemarks(bank.getRemarks());
		old.setBankAddress(bank.getBankAddress());
		sysUserBankDao.update(old);
		// 更新新增或修改银行卡时间
		userInfoDao.updateModifyBankCardTime(old.getUserId(), old.getStationId(), new Date());
		LogUtils.modifyLog("修改会员 " + old.getUsername() + " 银行卡信息成功,原卡号:" + oldCardNo + "新卡号:" + bank.getCardNo());
	}

	@Override
	public void modifyRealName(Long id, Long stationId, String realName) {
		if (StringUtils.isEmpty(realName)) {
			throw new ParamException(BaseI18nCode.realNameRequired);
		}
		SysUserBank old = sysUserBankDao.findOne(id, stationId);
		if (old == null) {
			throw new ParamException(BaseI18nCode.userBankUnExist);
		}
		if (StringUtils.equals(old.getRealName(), realName)) {
			throw new ParamException(BaseI18nCode.realNameUnChange);
		}
		sysUserBankDao.updateRealName(id, realName);
		LogUtils.modifyLog("修改会员 " + old.getUsername() + " 银行卡信息成功,原真实姓名:" + old.getRealName() + "新真实姓名:" + realName);
	}

	@Override
	public void changeStatus(Long id, Long stationId, Integer status) {
		if (status == null || id == null) {
			throw new ParamException();
		}
		SysUserBank old = sysUserBankDao.findOne(id, stationId);
		if (old == null) {
			throw new ParamException(BaseI18nCode.userBankUnExist);
		}

		// 如果是USDT账户，禁止后台修改会员Tron链地址状态（1.后台定时任务会清除未激活的tron链地址;2.如若设为禁用，前台页面会再次提示转账激活）
		if(BankInfo.USDT.getBankCode().equals(old.getBankCode())
				|| BankInfo.USDT.getBankName().equals(old.getBankName())) {
			throw new ParamException(BaseI18nCode.operateNotAllowed);
		}

		String s = I18nTool.getMessage(BaseI18nCode.enable);
		if (status == null || status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			s = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, old.getStatus())) {
			sysUserBankDao.updateStatus(id, status, stationId);
			userInfoDao.updateModifyBankCardTime(old.getUserId(), old.getStationId(), new Date());
			LogUtils.modifyStatusLog("修改会员 " + old.getUsername() + " 银行卡" + old.getCardNo() + "状态为" + s);
		}
	}

	@Override
	@Transactional
	public void delete(Long id, Long stationId) {
		SysUserBank old = sysUserBankDao.findOne(id, stationId);
		if (old == null) {
			throw new ParamException(BaseI18nCode.userBankUnExist);
		}

		if(BankInfo.USDT.getBankCode().equals(old.getBankCode())
				|| BankInfo.USDT.getBankName().equals(old.getBankName())) {
			sysUserTronLinkService.deleteUserTronLink(old.getUserId(), old.getStationId());
		}

		sysUserBankDao.deleteById(id);
		LogUtils.delLog("删除会员 " + old.getUsername() + " 银行卡：" + old.getCardNo() + "成功");
	}

	@Override
	public SysUserBank findOneByCardNo(String cardNo, Long stationId) {
		return sysUserBankDao.getLastBankInfo(null, stationId, cardNo);
	}

	@Override
	public SysUserBank getLastBankInfo(Long userId, Long stationId) {
		return sysUserBankDao.getLastBankInfo(userId, stationId, null);
	}

	@Override
	public List<SysUserBank> findByUserId(Long userId, Long stationId, Integer status, String type) {
		return sysUserBankDao.findByUserId(userId, stationId, status, type);
	}

	@Override
	public List<SysUserBank> getBanksForUserCenter(Long userId, Long stationId) {
		return sysUserBankDao.getBanksForUserCenter(userId, stationId);
	}

	@Override
	public void save(BankVo vo) {
		if(BankInfo.USDT.getBankCode().equals(vo.getBankCode())
				|| BankInfo.USDT.getBankName().equals(vo.getBankName())) {
			// 验证Tron链地址有效性
			if (!SysPreDefineTron.isValidBase58Address(vo.getCardNo())) {
				throw new ParamException(BaseI18nCode.tronLinkAddrIllegal);
			}
			// 一个会员只能绑定一个Tron链地址
			if (ObjectUtils.isNotEmpty(sysUserTronLinkService.findOne(vo.getUserId(), vo.getStationId()))) {
				throw new ParamException(BaseI18nCode.tronLinkAddrExist);
			}
		} else {
			// 验证卡号是否有效
			if (!ValidateUtil.isBankCardNo(vo.getCardNo())) {
				throw new ParamException(BaseI18nCode.cardNoFormatError);
			}
			if (!ValidateUtil.isRealName(vo.getRealName(), SystemUtil.getStation().getLanguage())) {
			//	logger.error("SysUserBank realName format error, realName:{}, language:{}", vo.getRealName(), SystemUtil.getStation().getLanguage());
				throw new ParamException(BaseI18nCode.realNameFormatError);
			}
			//		if (StringUtils.isEmpty(vo.getBankAddress())) {
//			throw new ParamException(BaseI18nCode.completeBankInfoRequired);
//		}
			// 去除真实姓名空格
			vo.setRealName(StringUtils.trim(vo.getRealName()));

			// 验证第一次绑卡信息，排除USDT银行卡类型
			SysUserBank lastBank = sysUserBankDao.getLastBankInfo(vo.getUserId(), vo.getStationId(), null);
			if (lastBank != null && !(BankInfo.USDT.getBankCode().equals(lastBank.getBankCode())
					|| BankInfo.USDT.getBankName().equals(lastBank.getBankName()))) {
				if (StationConfigUtil.isOff(vo.getStationId(), StationConfigEnum.bank_multi)) {
					throw new ParamException(BaseI18nCode.bankHadBind);
				}
				if (StringUtils.isEmpty(vo.getLastCardNo()) || StringUtils.isEmpty(vo.getLastRealName())) {
					throw new ParamException(BaseI18nCode.lastBankInfoEmpty);
				}
				if (!StringUtils.equals(vo.getLastCardNo(), lastBank.getCardNo())
						|| !StringUtils.equals(vo.getLastRealName(), lastBank.getRealName())) {
					throw new ParamException(BaseI18nCode.lastBankInfoError);
				}
				if (StringUtils.equals(lastBank.getCardNo(), vo.getCardNo())) {
					throw new ParamException(BaseI18nCode.bankCardNoExist);
				}
			}

			// 验证用户是否绑定过这张卡
			if (sysUserBankDao.getLastBankInfo(vo.getUserId(), vo.getStationId(), vo.getCardNo()) != null) {
				throw new ParamException(BaseI18nCode.bankCardNoExist);
			}

			// 验证多账号一张卡
			if (!StationConfigUtil.isOn(vo.getStationId(), StationConfigEnum.user_use_same_bank_card)) {
				if (sysUserBankDao.getLastBankInfo(null, vo.getStationId(), vo.getCardNo()) != null) {
					throw new ParamException(BaseI18nCode.bankCardNoExist);
				}
			}
			// 验证真实姓名
			SysUserInfo userInfo = userInfoDao.findOne(vo.getUserId(), vo.getStationId());
			if (userInfo == null) {
				throw new ParamException(BaseI18nCode.userInfoUnExist);
			}
			if (StringUtils.isEmpty(userInfo.getRealName())) {
				userInfoDao.updateRealName(vo.getUserId(), vo.getStationId(), vo.getRealName());
				userInfo.setRealName(vo.getRealName());
			}
			// 是否开启了银行卡所属人唯一
			if(!BankInfo.USDT.getBankCode().equals(vo.getBankCode())
					&& !BankInfo.USDT.getBankName().equals(vo.getBankName())) {
				if (StationConfigUtil.isOn(vo.getStationId(), StationConfigEnum.bank_real_name_only)) {
					if (!userInfo.getRealName().equals(vo.getRealName())) {
						throw new ParamException(BaseI18nCode.bankRealNameMustSameInfo);
					}
				}
			}
			if (!StringUtils.equals(vo.getBankCode(), StationBank.otherCode)) {// 其他银行
				vo.setBankName(stationBankService.getNameByCode(vo.getBankCode(), vo.getStationId()));
			}
		}

		SysUserBank bank = new SysUserBank();
		bank.setStationId(vo.getStationId());
		bank.setUserId(vo.getUserId());
		bank.setUsername(vo.getUsername());
		bank.setCardNo(vo.getCardNo());
		bank.setRealName(vo.getRealName());
		bank.setBankCode(vo.getBankCode());
		bank.setBankAddress(vo.getBankAddress());
		bank.setBankName(vo.getBankName());
		//bank.setRemarks(I18nTool.getMessage(BaseI18nCode.normal));
		bank.setRemarks(I18nTool.convertCodeToArrStr(I18nTool.convertCodeToList(BaseI18nCode.normal.getCode())));
		bank.setCreateTime(new Date());

		// 若添加银行卡类型为USDT，初始状态为禁用，验证初始金额后，通过定时任务置为启用
		if(BankInfo.USDT.getBankCode().equals(vo.getBankCode())
				|| BankInfo.USDT.getBankName().equals(vo.getBankName())) {
			bank.setStatus(Constants.STATUS_DISABLE);
			// 写入sys_user_tron_link
			SysUser user = new SysUser();
			user.setId(vo.getUserId());
			user.setUsername(vo.getUsername());
			sysUserTronLinkService.addUserTronLink(user, vo.getStationId(), bank.getCardNo());
		} else {
			bank.setStatus(Constants.STATUS_ENABLE);
		}

		sysUserBankDao.save(bank);
		userInfoDao.updateModifyBankCardTime(bank.getUserId(), bank.getStationId(), new Date());
		LogUtils.addLog("用户:" + vo.getUsername() + "新增银行卡" + vo.getCardNo());
	}

	@Override
	public void delUserBank(SysUserBank sysUserBank) {
		this.delete(sysUserBank.getId(), sysUserBank.getStationId());

		// 如果删除的银行卡信息为USDT，需同时删除 sys_user_tron_link对应记录
		if(BankInfo.USDT.getBankCode().equals(sysUserBank.getBankCode())
				|| BankInfo.USDT.getBankName().equals(sysUserBank.getBankName())) {
			sysUserTronLinkService.deleteUserTronLink(sysUserBank.getUserId(), sysUserBank.getStationId());
		}
	}

	@Override
	public void delUSDTTimeout(List<Long> userBankIds) {
		sysUserBankDao.delUSDTTimeout(userBankIds);
	}

	@Override
	public void updateUSDTInitBind(Long userId, Long stationId, String cardNo) {
		sysUserBankDao.updateUSDTInitBind(userId, stationId, cardNo);
	}

	@Override
	public List<SysUserBank> getUSDTDisableList() {
		return sysUserBankDao.getUSDTDisableList();
	}
}
