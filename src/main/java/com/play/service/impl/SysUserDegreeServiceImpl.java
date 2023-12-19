package com.play.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.play.core.LanguageEnum;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.common.Constants;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.LogUtils;
import com.play.core.BetNumTypeEnum;
import com.play.core.MoneyRecordType;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserDegreeDao;
import com.play.dao.SysUserDegreeLogDao;
import com.play.dao.SysUserDepositDao;
import com.play.model.SysUser;
import com.play.model.SysUserBetNum;
import com.play.model.SysUserDegree;
import com.play.model.SysUserDegreeLog;
import com.play.model.SysUserDeposit;
import com.play.model.bo.MnyMoneyBo;
import com.play.model.vo.UserPermVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationMessageService;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserPermService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginAdminUtil;

/**
 * 会员层级信息
 *
 * @author admin
 *
 */
@Service
public class SysUserDegreeServiceImpl implements SysUserDegreeService {

	//Logger logger = LoggerFactory.getLogger(SysUserDegreeServiceImpl.class.getSimpleName());

	@Autowired
	private SysUserDegreeDao sysUserDegreeDao;
	@Autowired
	private SysUserDepositDao sysUserDepositDao;
	@Autowired
	private SysUserBetNumService sysUserBetNumService;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserDegreeLogDao degreeLogDao;
	@Autowired
	private SysUserPermService userPermService;
	@Autowired
	private SysUserMoneyService userMoneyService;
	@Autowired
	private StationMessageService messageService;

	@Override
	public void init(Long stationId, Long partnerId) {
		SysUserDegree d = new SysUserDegree();
		d.setStationId(stationId);
		d.setPartnerId(partnerId);
		d.setMemberCount(0L);
		d.setDepositMoney(BigDecimal.ZERO);
		d.setStatus(Constants.STATUS_ENABLE);
		d.setName(BaseI18nCode.stationDefaultLevel.getMessage());
		d.setCreateDatetime(new Date());
		d.setOriginal(Constants.USER_ORIGINAL);
		d.setUpgradeSendMsg(Constants.STATUS_DISABLE);
		d.setUpgradeMoney(BigDecimal.ZERO);
		d.setBetRate(BigDecimal.ZERO);
		d.setBetNum(BigDecimal.ZERO);
		d.setType(SysUserDegree.TYPE_DEPOSIT);
		d.setSkipMoney(BigDecimal.ZERO);
		sysUserDegreeDao.save(d);
	}

	@Override
	public List<SysUserDegree> find(Long stationId, Integer status) {
		List<SysUserDegree> sysUserDegrees = sysUserDegreeDao.find(stationId, status);
		for (SysUserDegree sysUserDegree : sysUserDegrees){
			BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(sysUserDegree.getName());
			if (baseI18nCode != null) {
				// 越南站，会员等级显示英文
				if(LanguageEnum.vi.name().equals(SystemUtil.getLanguage())) {
					sysUserDegree.setName(I18nTool.getMessage(baseI18nCode, Locale.ENGLISH));
				} else {
					sysUserDegree.setName(I18nTool.getMessage(baseI18nCode));
				}
			}
		}
		return sysUserDegrees;
	}

	@Override
	public SysUserDegree getDefault(Long stationId) {
		return sysUserDegreeDao.getDefault(stationId);
	}

	@Override
	public Long getDefaultId(Long stationId) {
		SysUserDegree d = sysUserDegreeDao.getDefault(stationId);
		if (d == null) {
			throw new BaseException(BaseI18nCode.defaultDegreeUnExist);
		}
		return d.getId();
	}

	@Override
	public Page<SysUserDegree> page(Long stationId) {
		SysUserDegree d = sysUserDegreeDao.getDefault(stationId);
		if (d.getType() == null) {
			d.setType(SysUserDegree.TYPE_DEPOSIT);
		}
		Page<SysUserDegree> page = sysUserDegreeDao.page(stationId, d.getType());
		if (page.hasRows()){
			for (SysUserDegree sysUserDegree : page.getRows()){
				BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(sysUserDegree.getName());
				if (baseI18nCode != null) {
					// 越南站，会员等级显示英文
					if(LanguageEnum.vi.name().equals(SystemUtil.getLanguage())) {
						sysUserDegree.setName(I18nTool.getMessage(baseI18nCode, Locale.ENGLISH));
					} else {
						sysUserDegree.setName(I18nTool.getMessage(baseI18nCode));
					}
				}
			}
		}

		return page;
	}

	@Override
	public void save(SysUserDegree degree) {
		if (degree.getType() == SysUserDegree.TYPE_DEPOSIT) {
			if (degree.getDepositMoney() == null) {
				throw new ParamException(BaseI18nCode.depositMoneyRequired);
			}
			if (degree.getDepositMoney().compareTo(BigDecimal.ZERO) < 0) {
				throw new ParamException(BaseI18nCode.depositMoneyLg0);
			}
			if (sysUserDegreeDao.exist(degree.getStationId(), degree.getDepositMoney(), null, null, degree.getType())) {
				throw new ParamException(BaseI18nCode.degreeDepositMoneyExist);
			}
			degree.setBetNum(BigDecimal.ZERO);
		} else if (degree.getType() == SysUserDegree.TYPE_BETNUM) {
			if (degree.getBetNum() == null) {
				throw new ParamException(BaseI18nCode.betNumRequired);
			}
			if (degree.getBetNum().compareTo(BigDecimal.ZERO) < 0) {
				throw new ParamException(BaseI18nCode.betNumLg0);
			}
			if (sysUserDegreeDao.exist(degree.getStationId(), null, degree.getBetNum(), null, degree.getType())) {
				throw new ParamException(BaseI18nCode.degreeBetNumExist);
			}
			degree.setDepositMoney(BigDecimal.ZERO);
		} else {
			if (degree.getBetNum() == null || degree.getDepositMoney() == null) {
				throw new ParamException(BaseI18nCode.betNumOrMoneyNotNull);
			}
			if (degree.getBetNum().compareTo(BigDecimal.ZERO) < 0
					|| degree.getDepositMoney().compareTo(BigDecimal.ZERO) < 0) {
				throw new ParamException(BaseI18nCode.betNumAndMoneyGtOrEqualZero);
			}
			if (sysUserDegreeDao.exist(degree.getStationId(), degree.getDepositMoney(), degree.getBetNum(), null,
					degree.getType())) {
				throw new ParamException(BaseI18nCode.degreeBetNumAndMoneyExist);
			}
		}
		degree.setSkipMoney(BigDecimal.ZERO);
		degree.setRemark(StringUtils.deleteWhitespace(degree.getRemark()));
		degree.setCreateDatetime(new Date());
		degree.setMemberCount(0L);
		degree.setStatus(Constants.STATUS_ENABLE);
		degree.setOriginal(Constants.USER_UNORIGINAL);
		sysUserDegreeDao.save(degree);
		LogUtils.addLog("添加会员等级" + degree.getName() + " 金额" + degree.getDepositMoney() + " 打码量" + degree.getBetNum());
	}

	@Override
	public SysUserDegree findOne(Long id, Long stationId) {
		SysUserDegree sysUserDegree = sysUserDegreeDao.findOne(id, stationId);
		BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(sysUserDegree.getName());
		if (baseI18nCode != null) {
			// 越南站，会员等级显示英文
			if(LanguageEnum.vi.name().equals(SystemUtil.getLanguage())) {
				sysUserDegree.setName(I18nTool.getMessage(baseI18nCode, Locale.ENGLISH));
			} else {
				sysUserDegree.setName(I18nTool.getMessage(baseI18nCode));
			}
		}
		return sysUserDegree;
	}

	@Override
	public String findDegreeName(Long id, Long stationId) {
		return sysUserDegreeDao.findDegreeName(id, stationId);
	}

	@Override
	public boolean findOneByName(Long stationId, String name) {
		return sysUserDegreeDao.findOneByName(stationId, name);
	}

	@Override
	public void update(SysUserDegree degree) {
		degree.setRemark(StringUtils.deleteWhitespace(degree.getRemark()));
		SysUserDegree old = sysUserDegreeDao.findOne(degree.getId(), degree.getStationId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.degreeNotExist);
		}
		String oldName = old.getName();
		BigDecimal money = old.getDepositMoney();
		if (old.getOriginal() != Constants.USER_ORIGINAL) {
			if (old.getType() == SysUserDegree.TYPE_DEPOSIT) {
				if (degree.getDepositMoney() == null) {
					throw new ParamException(BaseI18nCode.depositMoneyRequired);
				}
				if (degree.getDepositMoney().compareTo(BigDecimal.ZERO) < 0) {
					throw new ParamException(BaseI18nCode.depositMoneyLg0);
				}
				if (sysUserDegreeDao.exist(degree.getStationId(), degree.getDepositMoney(), null, old.getId(),
						degree.getType())) {
					throw new ParamException(BaseI18nCode.degreeDepositMoneyExist);
				}
				old.setDepositMoney(degree.getDepositMoney());
			} else if (old.getType() == SysUserDegree.TYPE_BETNUM) {
				if (degree.getBetNum() == null) {
					throw new ParamException(BaseI18nCode.betNumRequired);
				}
				if (degree.getBetNum().compareTo(BigDecimal.ZERO) < 0) {
					throw new ParamException(BaseI18nCode.betNumLg0);
				}
				if (sysUserDegreeDao.exist(degree.getStationId(), null, degree.getBetNum(), old.getId(),
						degree.getType())) {
					throw new ParamException(BaseI18nCode.degreeBetNumExist);
				}
				old.setBetNum(degree.getBetNum());
			} else {
				if (degree.getBetNum() == null || degree.getDepositMoney() == null) {
					throw new ParamException(BaseI18nCode.betNumOrMoneyNotNull);
				}
				if (degree.getBetNum().compareTo(BigDecimal.ZERO) < 0
						|| degree.getDepositMoney().compareTo(BigDecimal.ZERO) < 0) {
					throw new ParamException(BaseI18nCode.betNumAndMoneyGtOrEqualZero);
				}
				if (sysUserDegreeDao.exist(degree.getStationId(), degree.getDepositMoney(), degree.getBetNum(),
						old.getId(), degree.getType())) {
					throw new ParamException(BaseI18nCode.degreeBetNumAndMoneyExist);
				}
				old.setBetNum(degree.getBetNum());
				old.setDepositMoney(degree.getDepositMoney());
			}
			old.setUpgradeMoney(degree.getUpgradeMoney());
			old.setUpgradeSendMsg(degree.getUpgradeSendMsg());
			old.setBetRate(degree.getBetRate());
			old.setSkipMoney(degree.getSkipMoney());
		}
		old.setName(degree.getName());
		old.setRemark(degree.getRemark());
		old.setIcon(degree.getIcon());
		sysUserDegreeDao.updateInfo(old);
		LogUtils.modifyLog(
				"会员等级:" + oldName + " 修改成：" + degree.getName() + " 金额：" + money + " 修改成" + degree.getDepositMoney());
	}

	@Override
	public void updateStatus(Long id, Integer status, Long stationId) {
		SysUserDegree old = sysUserDegreeDao.findOne(id, stationId);
		if (old == null) {
			throw new ParamException(BaseI18nCode.degreeNotExist);
		}
		String s = I18nTool.getMessage(BaseI18nCode.enable);
		if (status == null || status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			s = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (Objects.equals(old.getOriginal(), Constants.USER_ORIGINAL) && status == Constants.STATUS_DISABLE) {
			throw new BaseException(BaseI18nCode.stationDefVipLevelNot);
		}
		boolean memberExist = sysUserDao.findOneByDegreeId(id, stationId, Constants.STATUS_ENABLE);
		if (memberExist) {
			throw new BaseException(BaseI18nCode.memberExistInLevelSwitch);
		}
		if (!Objects.equals(status, old.getStatus())) {
			sysUserDegreeDao.updateStatus(id, status, stationId);
			LogUtils.modifyStatusLog("会员等级:" + old.getName() + " 状态修改成：" + s);
		}
	}

	@Override
	public void reStatMemberAccount(Long stationId) {
		sysUserDegreeDao.degreeChange(0L, 0L, stationId);
		LogUtils.modifyLog("重新统计会员数量");
	}

	@Override
	public void delete(Long id, Long stationId) {
		SysUserDegree old = sysUserDegreeDao.findOne(id, stationId);
		if (old == null) {
			throw new ParamException(BaseI18nCode.degreeNotExist);
		}
		if (old.getOriginal() == Constants.USER_ORIGINAL) {
			throw new ParamException(BaseI18nCode.originalDegreeCanntDel);
		}
		boolean memberExist = sysUserDao.findOneByDegreeId(id, stationId, Constants.STATUS_ENABLE);
		if (memberExist) {
			throw new BaseException(BaseI18nCode.memberExistInLevel);
		}
		sysUserDegreeDao.deleteById(id);
		LogUtils.delLog("删除会员等级:" + old.getName());
	}

	@Override
	public void transfer(Long curId, Long nextId, Long stationId) {
		if (curId == null || nextId == null) {
			throw new ParamException(BaseI18nCode.illegalRequest);
		}
		if (curId.equals(nextId)) {
			return;
		}
		SysUserDegree cur = sysUserDegreeDao.findOne(curId, stationId);
		if (cur == null) {
			throw new ParamException(BaseI18nCode.degreeNotExist);
		}
		SysUserDegree next = sysUserDegreeDao.findOne(nextId, stationId);
		if (next == null) {
			throw new ParamException(BaseI18nCode.degreeNotExist);
		}
		if (next.getStatus() == Constants.STATUS_DISABLE) {
			throw new ParamException(BaseI18nCode.notTransferBanDegree);
		}
		sysUserDegreeDao.degreeChange(curId, nextId, stationId);
		LogUtils.modifyLog("从等级 " + cur.getName() + " 迁移会员到等级 " + next.getName());
	}

	@Override
	public void updLevelModel(Integer type, Long stationId, Map<String, String[]> map) {
		SysUserDegree base = sysUserDegreeDao.getDefault(stationId);
		Map<Long, SysUserDegree> maps = new HashMap<>();
		SysUserDegree b = null;
		if (base != null) {
			// 更新金额和打码量字段
			try {
				Long degreeId = null;
				String[] str = null;
				for (Map.Entry<String, String[]> m : map.entrySet()) {
					if (StringUtils.equals(m.getKey(), "type")) {
						continue;
					}
					str = m.getKey().split("_");
					degreeId = Long.parseLong(str[1]);
					if (!maps.containsKey(degreeId)) {
						b = new SysUserDegree();
						b.setId(degreeId);
						b.setStationId(stationId);
						if (str[0].equals("depositMoney")) {
							b.setDepositMoney(BigDecimalUtil.toBigDecimalDefaultZero(m.getValue()[0]));
						} else {
							b.setBetNum(BigDecimalUtil.toBigDecimalDefaultZero(m.getValue()[0]));
						}
						maps.put(degreeId, b);
					} else {
						if (str[0].equals("depositMoney")) {
							maps.get(degreeId).setDepositMoney(BigDecimalUtil.toBigDecimalDefaultZero(m.getValue()[0]));
						} else {
							maps.get(degreeId).setBetNum(BigDecimalUtil.toBigDecimalDefaultZero(m.getValue()[0]));
						}
					}
				}
				sysUserDegreeDao.batchUpdate(new ArrayList<>(maps.values()));
				sysUserDegreeDao.updateType(type, stationId);
			} catch (Exception e) {
				throw new BaseException(BaseI18nCode.changeDegreeTypeError, e);
			}
			String newName = type == 1 ? BaseI18nCode.stationCashCalLevel.getMessage()
					: type == 2 ? BaseI18nCode.stationWeigCalLevel.getMessage()
							: BaseI18nCode.stationBankCalLevel.getMessage();
			String oldName = (base.getType() == null || base.getType() == 1)
					? BaseI18nCode.stationCashCalLevel.getMessage()
					: base.getType() == 2 ? BaseI18nCode.stationWeigCalLevel.getMessage()
							: BaseI18nCode.stationBankCalLevel.getMessage();
			LogUtils.modifyLog("切换会员层级计算模式由-" + oldName + "-修改为-" + newName);
		}
	}

	@Override
	public void modifyUpgrade(String configs, Long stationId) {
		if (StringUtils.isEmpty(configs)) {
			throw new BaseException(BaseI18nCode.upgradeConfigRequired);
		}
		JSONArray array = JSONArray.parseArray(configs);
		SysUserDegree d = null;
		List<SysUserDegree> ds = new ArrayList<>();
		StringBuilder sb = new StringBuilder(I18nTool.getMessage(BaseI18nCode.stationUpdateConfigLevel));
		JSONObject object = null;
		for (int i = 0; i < array.size(); i++) {
			object = array.getJSONObject(i);
			Long id = object.getLong("id");
			if (id == null) {
				throw new ParamException(BaseI18nCode.illegalRequest);
			}
			d = sysUserDegreeDao.findOne(id, stationId);
			if (d == null) {
				throw new ParamException(BaseI18nCode.degreeNotExist);
			}
			sb.append(d.getName()).append(I18nTool.getMessage(BaseI18nCode.stationOldGoods)).append(d.getUpgradeMoney());
			d.setUpgradeMoney(BigDecimalUtil.toBigDecimalDefaultZero(object.getString("upgradeGift")));
			sb.append(I18nTool.getMessage(BaseI18nCode.stationNewGoods)).append(d.getUpgradeMoney());
			sb.append(I18nTool.getMessage(BaseI18nCode.stationOldJumpGood)).append(d.getSkipMoney());
			d.setSkipMoney(BigDecimalUtil.toBigDecimalDefaultZero(object.getString("skipGift")));
			sb.append(I18nTool.getMessage(BaseI18nCode.stationNewJumpGood)).append(d.getSkipMoney());
			sb.append(I18nTool.getMessage(BaseI18nCode.stationOldWeight)).append(d.getBetRate());
			d.setBetRate(BigDecimalUtil.toBigDecimalDefaultZero(object.getString("betRate")));
			sb.append(I18nTool.getMessage(BaseI18nCode.stationNewWeight)).append(d.getBetRate()).append("；");
			ds.add(d);
		}
		sysUserDegreeDao.levelGiftBatchUpdate(ds);
		LogUtils.modifyLog(sb.toString());
	}

	@Override
	public void changeUserDegree(SysUser user, BigDecimal total, String remark) {
		if (user.getLockDegree() != null && user.getLockDegree() == SysUser.DEGREE_LOCK) {
			return;
		}
		List<SysUserDegree> list = find(user.getStationId(), Constants.STATUS_ENABLE);
		if (list == null || list.isEmpty()) {
			return;
		}
		Integer degreeType = SysUserDegree.TYPE_DEPOSIT;
		SysUserDegree old = sysUserDegreeDao.findOne(user.getDegreeId(), user.getStationId());
		if (old != null && old.getType() != null) {
			degreeType = old.getType();
		}
		BigDecimal betTotal = total;
		BigDecimal depositTotal = total;
		SysUserDegree newDegree = null;
		if (degreeType == SysUserDegree.TYPE_DEPOSIT) {
			SysUserDeposit deposit = sysUserDepositDao.findOne(user.getId(), user.getStationId());
			depositTotal = deposit.getTotalMoney();
			list.sort(Comparator.comparing(SysUserDegree::getDepositMoney));
		} else if (degreeType == SysUserDegree.TYPE_BETNUM) {
			list.sort(Comparator.comparing(SysUserDegree::getBetNum));
		} else {
			SysUserBetNum bet = sysUserBetNumService.findOne(user.getId(), user.getStationId());
			betTotal = bet.getTotalBetNum() == null ? BigDecimal.ZERO : bet.getTotalBetNum();
			SysUserDeposit deposit = sysUserDepositDao.findOne(user.getId(), user.getStationId());
			depositTotal = deposit.getTotalMoney();
			list.sort(Comparator.comparing(SysUserDegree::getDepositMoney));
		}
		for (SysUserDegree l : list) {
			if (degreeType == SysUserDegree.TYPE_DEPOSIT) {
				if (l.getDepositMoney() == null) {
					throw new BaseException(BaseI18nCode.degreeDepositMoneyNull);
				}
				if (l.getDepositMoney().compareTo(depositTotal) <= 0) {
					newDegree = l;
				}
			} else if (degreeType == SysUserDegree.TYPE_BETNUM) {
				if (l.getBetNum() == null) {
					throw new BaseException(BaseI18nCode.degreeBetNumNull);
				}
				if (betTotal != null && l.getBetNum().compareTo(betTotal) <= 0) {
					newDegree = l;
				}
			} else {
				if (l.getDepositMoney() == null) {
					throw new BaseException(BaseI18nCode.degreeDepositMoneyNull);
				}
				if (l.getBetNum() == null) {
					throw new BaseException(BaseI18nCode.degreeBetNumNull);
				}
				if (betTotal != null && l.getBetNum().compareTo(betTotal) <= 0
						&& l.getDepositMoney().compareTo(depositTotal) <= 0) {
					newDegree = l;
				}
			}

		}
		if (newDegree != null && (user.getDegreeId() == null || !user.getDegreeId().equals(newDegree.getId()))) {
			if (remark == null) {
				remark = "";
			}
			List <String> msgList = I18nTool.convertCodeToList(I18nTool.convertArrStrToList(remark));
			if (degreeType == SysUserDegree.TYPE_DEPOSIT) {
//				remark = remark + I18nTool.getMessage(BaseI18nCode.stationPayCount) + depositTotal.floatValue()
//						+ I18nTool.getMessage(BaseI18nCode.stationArrival) + newDegree.getName() + I18nTool.getMessage(BaseI18nCode.stationRiskRule);

				I18nTool.convertCodeToList(msgList,BaseI18nCode.stationPayCount.getCode(), String.valueOf(depositTotal.floatValue()),
						BaseI18nCode.stationArrival.getCode(), newDegree.getName(),BaseI18nCode.stationRiskRule.getCode());

			} else if (degreeType == SysUserDegree.TYPE_BETNUM) {
//				remark = I18nTool.getMessage(BaseI18nCode.stationCountWeight) + betTotal.floatValue()
//						+ I18nTool.getMessage(BaseI18nCode.stationArrival) + newDegree.getName() + I18nTool.getMessage(BaseI18nCode.stationRiskRule);

				I18nTool.convertCodeToList(msgList,BaseI18nCode.stationCountWeight.getCode(),String.valueOf(betTotal.floatValue()),
						BaseI18nCode.stationArrival.getCode(),newDegree.getName(),BaseI18nCode.stationRiskRule.getCode());

			} else {
//				remark = remark + I18nTool.getMessage(BaseI18nCode.stationPayAdd) + depositTotal.floatValue() + ","
//						+ I18nTool.getMessage(BaseI18nCode.stationCountWeight) + betTotal.floatValue()
//						+ I18nTool.getMessage(BaseI18nCode.stationArrival) + newDegree.getName() + I18nTool.getMessage(BaseI18nCode.stationRiskRule);

				I18nTool.convertCodeToList(msgList,BaseI18nCode.stationPayAdd.getCode(), String.valueOf(depositTotal.floatValue()), ",",
						BaseI18nCode.stationCountWeight.getCode(),String.valueOf(betTotal.floatValue()),
						BaseI18nCode.stationArrival.getCode(), newDegree.getName(),BaseI18nCode.stationRiskRule.getCode());
			}
			changeMemberDegree(user, newDegree, null, I18nTool.convertCodeToArrStr(msgList), false);
			user.setDegreeId(newDegree.getId());
		}
	}

	private void changeMemberDegree(SysUser user, SysUserDegree newDegree, Integer lockDegree, String remark,
			boolean isHandle) {
		Long userId = user.getId();
		Long stationId = user.getStationId();
		Long degreeId = newDegree.getId();
		SysUserDegree oldDegree = sysUserDegreeDao.findOne(user.getDegreeId(), stationId);
		String oldName = "";
		if (oldDegree != null) {
			oldName = oldDegree.getName();
		}
		String lockStr = I18nTool.getMessage(BaseI18nCode.stationVipLevelLock);
		if (lockDegree == null || lockDegree != SysUser.DEGREE_LOCK) {
			lockDegree = SysUser.DEGREE_UNLOCK;
			lockStr = I18nTool.getMessage(BaseI18nCode.stationVipLevelNoLock);
		}
		sysUserDao.changeDegree(userId, stationId, degreeId, lockDegree);
		// 试玩账号不记录变更记录
		if (!GuestTool.isGuest(user)) {
			SysUserDegreeLog degreeLog = new SysUserDegreeLog();
			degreeLog.setPartnerId(user.getPartnerId());
			degreeLog.setStationId(stationId);
			degreeLog.setUserId(userId);
			degreeLog.setUsername(user.getUsername());
			degreeLog.setContent(remark);
			degreeLog.setCreateDatetime(new Date());
			degreeLog.setOldId(user.getDegreeId());
			degreeLog.setOldName(oldName);
			degreeLog.setNewId(degreeId);
			degreeLog.setNewName(newDegree.getName());
			degreeLog.setOperator(LoginAdminUtil.getUsername());
			degreeLog.setOperatorId(LoginAdminUtil.getUserId());
			degreeLogDao.save(degreeLog);

			BigDecimal upGift = BigDecimal.ZERO;
			// 如果有设置奖金，自动发放奖金
			if (!isHandle && newDegree.getUpgradeMoney() != null) {
				// 验证权限
				UserPermVo perm = userPermService.getPerm(userId, stationId);
				if (perm == null || perm.isUpgradeGift()) {
                    upGift =  newDegree.getUpgradeMoney();
					// 等级赠送调整成通过跨级奖励计算
					if (newDegree.getSkipMoney() != null && newDegree.getSkipMoney().compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal oldGift = null;
						if (oldDegree == null || oldDegree.getSkipMoney() == null
								|| oldDegree.getSkipMoney().compareTo(BigDecimal.ZERO) < 0) {
							oldGift = BigDecimal.ZERO;
						} else {
							oldGift = oldDegree.getSkipMoney();
						}
						if (newDegree.getSkipMoney().compareTo(oldGift) > 0) {
							upGift = newDegree.getSkipMoney().subtract(oldGift);
						}
					}
					if (upGift.compareTo(BigDecimal.ZERO) > 0) {
						MnyMoneyBo giftBo = new MnyMoneyBo();
						giftBo.setUser(user);
						List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.degreeUpgradeMoney.getCode());
						giftBo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
						giftBo.setMoney(upGift);
						giftBo.setMoneyRecordType(MoneyRecordType.LEVEL_UPGRADE_GIFT);
						userMoneyService.updMnyAndRecord(giftBo);
						if (newDegree.getBetRate() != null && newDegree.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							// 取得消费比例 等级晋级打码量
							remarkList = I18nTool.convertCodeToList(BaseI18nCode.stationLevelUpGrade.getCode());
							sysUserBetNumService.updateDrawNeed(userId, stationId,
									giftBo.getMoney().multiply(newDegree.getBetRate()),
									BetNumTypeEnum.upgradeDegree.getType(),
									I18nTool.convertCodeToArrStr(remarkList), newDegree.getId().toString());
						}
					}
				}
			}
			// 是否发送站内信
			if (newDegree.getUpgradeSendMsg() != null && newDegree.getUpgradeSendMsg() == Constants.STATUS_ENABLE
					&& !isHandle) {
				//站内信需要翻译成站点对应语言，而newDegree不一定是站点对应语言，需要处理一下变成站点对应的
				Locale locale = Locale.ENGLISH;
				BaseI18nCode oldNameCode = I18nTool.getCode(oldName,locale);
				if (oldNameCode != null) {
					oldName = I18nTool.getMessage(oldNameCode,Locale.ENGLISH);
				}
				BaseI18nCode newNameCode = I18nTool.getCode(newDegree.getName(),locale);
				if (newNameCode != null) {
					newDegree.setName(I18nTool.getMessage(newNameCode,Locale.ENGLISH));
				}
				if (oldNameCode != null && newNameCode != null) {
					List<String> contentList = I18nTool.convertCodeToList(BaseI18nCode.stationConLevelFrom.getCode(), oldNameCode.getCode(),
							BaseI18nCode.stationUpFrom.getCode(), newNameCode.getCode());
					if (upGift != null && upGift.compareTo(BigDecimal.ZERO) > 0) {
						I18nTool.convertCodeToList(contentList, BaseI18nCode.stationUpFound.getCode(),
								String.valueOf(upGift), BaseI18nCode.stationYmbSendAccount.getCode());
					}
					messageService.sysMessageSend(stationId, userId, user.getUsername(), I18nTool.convertCodeToArrStr(BaseI18nCode.stationLevelUpGrade.getCode()),
							I18nTool.convertCodeToArrStr(contentList), Constants.STATUS_ENABLE);
				}
			}
			LogUtils.modifyLog("会员“" + user.getUsername() + "”等级从" + oldName + " 改成 " + newDegree.getName() + lockStr);
		}
	}

	@Override
	public void changeMemberDegree(Long userId, Long stationId, Long degreeId, Integer lockDegree, String remark,
			boolean isHandle) {
		SysUserDegree newDegree = sysUserDegreeDao.findOne(degreeId, stationId);
		if (newDegree == null) {
			throw new BaseException(BaseI18nCode.degreeNotExist);
		}
		SysUser user = sysUserDao.findOne(userId, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		changeMemberDegree(user, newDegree, lockDegree, remark, isHandle);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void batchChangeDegree(String usernames, Long stationId, Long degreeId, Integer lockDegree) {
		if (degreeId == null || lockDegree == null || StringUtils.isEmpty(usernames)) {
			throw new ParamException();
		}
		SysUserDegree newDegree = sysUserDegreeDao.findOne(degreeId, stationId);
		if (newDegree == null) {
			throw new BaseException(BaseI18nCode.degreeNotExist);
		}
		String[] uns = usernames.split(" |,|\n");
		StringBuilder errors = new StringBuilder();
		SysUser user = null;
		for (String un : uns) {
			un = StringUtils.trim(un);
			if (StringUtils.isEmpty(un)) {
				continue;
			}
			user = sysUserDao.findOneByUsername(un.toLowerCase(), stationId, null);
			if (user == null || GuestTool.isGuest(user)) {
				errors.append(un).append(I18nTool.getMessage(BaseI18nCode.stationUserNotExist));
				continue;
			}
			try {
				changeMemberDegree(user, newDegree, lockDegree, I18nTool.getMessage(BaseI18nCode.stationAdminAllUpdate), true);
			} catch (Exception e) {
				errors.append(un).append(":").append(e);
			}
		}
		if (errors.length() > 0) {
			errors.deleteCharAt(errors.length() - 1);
			throw new BaseException(BaseI18nCode.stationErrorMessage + errors.toString());
		}
	}

	@Override
	public String getDegreeNames(String degreeIds, Long stationId) {
		if (StringUtils.isNotEmpty(degreeIds)) {
			SysUserDegree d = null;
			StringBuilder sb = new StringBuilder();
			for (String id : degreeIds.split(",")) {
				long l = NumberUtils.toLong(id);
				if (l > 0) {
					d = sysUserDegreeDao.findOne(l, stationId);
					if (d != null) {
						sb.append(d.getName()).append(",");
					}
				}
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public String getName(Long id, Long stationId) {
		SysUserDegree d = sysUserDegreeDao.findOne(id, stationId);
		if (d != null) {
			return d.getName();
		}
		return "";
	}
}
