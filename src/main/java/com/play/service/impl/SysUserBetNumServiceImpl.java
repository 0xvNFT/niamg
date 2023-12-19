package com.play.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.play.core.LogTypeEnum;
import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.LogUtils;
import com.play.core.BetNumTypeEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.SysUserBetNumDao;
import com.play.dao.SysUserBetNumHistoryDao;
import com.play.dao.SysUserDao;
import com.play.model.AdminUser;
import com.play.model.SysUser;
import com.play.model.SysUserBetNum;
import com.play.model.SysUserBetNumHistory;
import com.play.model.SysUserDegree;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserDegreeService;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginAdminUtil;

/**
 * 会员打码量信息
 *
 * @author admin
 *
 */
@Service
public class SysUserBetNumServiceImpl implements SysUserBetNumService {

	@Autowired
	private SysUserBetNumDao sysUserBetNumDao;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserBetNumHistoryDao hisDao;
	@Autowired
	private SysUserDegreeService sysUserDegreeService;

	@Override
	public void init(Long userId, Long stationId) {
		SysUserBetNum betNum = new SysUserBetNum();
		betNum.setUserId(userId);
		betNum.setBetNum(BigDecimal.ZERO);
		betNum.setDrawNeed(BigDecimal.ZERO);
		betNum.setStationId(stationId);
		betNum.setTotalBetNum(BigDecimal.ZERO);
		sysUserBetNumDao.save(betNum);
	}

	/**
	 * 累加打码量
	 */
	@Override
	public void addBetNumber(Long id, BigDecimal betNumber, Integer type, String remark, String orderId,
			Date bizDatetime, Long stationId) {
		SysUser user = sysUserDao.findOneById(id, stationId);
		SysUserBetNum bn = sysUserBetNumDao.updateBetNum(id, betNumber);
		SysUserBetNumHistory his = new SysUserBetNumHistory();
		his.setPartnerId(user.getPartnerId());
		his.setStationId(stationId);
		his.setAgentName(user.getAgentName());
		his.setProxyId(user.getProxyId());
		his.setProxyName(user.getProxyName());
		his.setParentIds(user.getParentIds());
		his.setUserId(id);
		his.setUsername(user.getUsername());
		his.setBetNum(betNumber);
		his.setBeforeNum(BigDecimalUtil.subtract(bn.getBetNum(), betNumber));
		his.setAfterNum(bn.getBetNum());
		his.setType(type);
		his.setRemark(remark);
		AdminUser admin = LoginAdminUtil.currentUser();
		if (admin != null) {
			his.setOperatorId(admin.getId());
			if (admin.getType() == UserTypeEnum.ADMIN_MASTER.getType()
					|| admin.getType() == UserTypeEnum.ADMIN_MASTER_SUPER.getType()) {
				his.setOperatorName("--");
			} else {
				his.setOperatorName(admin.getUsername());
			}
		}
		his.setCreateDatetime(new Date());
		his.setOrderId(orderId);
		his.setBeforeDrawNeed(bn.getDrawNeed());
		his.setAfterDrawNeed(bn.getDrawNeed());
		his.setDrawNeed(BigDecimal.ZERO);
		if (bizDatetime == null) {
			his.setBizDatetime(new Date());
		} else {
			his.setBizDatetime(bizDatetime);
		}
		hisDao.saveHistory(his);
		// 检查是否启用打码量升级用户层级
		SysUserDegree curBase = sysUserDegreeService.findOne(user.getDegreeId(), stationId);
		if (curBase != null && curBase.getType() != null && curBase.getType() != SysUserDegree.TYPE_DEPOSIT) {
			sysUserDegreeService.changeUserDegree(user, bn.getTotalBetNum(), null);
		}

	}

	@Override
	public void updateDrawNeed(Long id, Long stationId, BigDecimal drawNeed, Integer type, String remark,
							   String orderId) {
		updateDrawNeed(id,stationId,drawNeed,null,type,remark,orderId,false);
	}

	@Override
	public void updateDrawNeed(Long id, Long stationId, BigDecimal drawNeed,BigDecimal money, Integer type, String remark,
			String orderId,boolean clearDrawNeed) {
		SysUserBetNum bn = sysUserBetNumDao.updateDrawNeed(id, drawNeed,money,clearDrawNeed);
		if (bn == null)
			return;
		SysUser user = sysUserDao.findOneById(id, stationId);
		SysUserBetNumHistory his = new SysUserBetNumHistory();
		his.setPartnerId(user.getPartnerId());
		his.setStationId(stationId);
		his.setAgentName(user.getAgentName());
		his.setProxyId(user.getProxyId());
		his.setProxyName(user.getProxyName());
		his.setParentIds(user.getParentIds());
		his.setUserId(id);
		his.setUsername(user.getUsername());
		his.setBetNum(BigDecimal.ZERO);
		his.setBeforeNum(bn.getBetNum());
		his.setAfterNum(BigDecimal.ZERO);
		his.setType(type);
		his.setRemark(remark);
		his.setOrderId(orderId);
		AdminUser admin = LoginAdminUtil.currentUser();
		if (admin != null) {
			his.setOperatorId(admin.getId());
			if (admin.getType() == UserTypeEnum.ADMIN_MASTER.getType()
					|| admin.getType() == UserTypeEnum.ADMIN_MASTER_SUPER.getType()) {
				his.setOperatorName("--");
			} else {
				his.setOperatorName(admin.getUsername());
			}
		}
		his.setCreateDatetime(new Date());
		his.setBeforeDrawNeed(bn.getBeforeDrawNeed());
		his.setAfterDrawNeed(bn.getDrawNeed());
		his.setDrawNeed(drawNeed);
		his.setBizDatetime(new Date());
		his.setStationId(stationId);
		hisDao.saveHistory(his);
	}

	@Override
	public boolean addDrawNeed(Long id, Long stationId, BigDecimal drawNeed, Integer type, String remark,
			Date bizDatetime) {
		SysUserBetNum bn = sysUserBetNumDao.addDrawNeed(id, drawNeed);
		if (bn == null) {
			return false;
		}
		SysUser user = sysUserDao.findOneById(id, stationId);
		if (GuestTool.isGuest(user)) {
			return false;
		}
		SysUserBetNumHistory his = new SysUserBetNumHistory();
		his.setPartnerId(user.getPartnerId());
		his.setStationId(stationId);
		his.setAgentName(user.getAgentName());
		his.setProxyId(user.getProxyId());
		his.setProxyName(user.getProxyName());
		his.setParentIds(user.getParentIds());
		his.setUserId(id);
		his.setUsername(user.getUsername());
		his.setBeforeNum(bn.getBetNum());
		his.setAfterNum(bn.getBetNum());
		his.setBetNum(BigDecimal.ZERO);
		his.setType(type);
		his.setRemark(remark);
		AdminUser admin = LoginAdminUtil.currentUser();
		if (admin != null) {
			his.setOperatorId(admin.getId());
			if (admin.getType() == UserTypeEnum.ADMIN_MASTER.getType()
					|| admin.getType() == UserTypeEnum.ADMIN_MASTER_SUPER.getType()) {
				his.setOperatorName("--");
			} else {
				his.setOperatorName(admin.getUsername());
			}
		}
		his.setCreateDatetime(new Date());
		his.setBeforeDrawNeed(BigDecimalUtil.subtract(bn.getDrawNeed(), drawNeed));
		his.setAfterDrawNeed(bn.getDrawNeed());
		his.setDrawNeed(drawNeed);
		if (bizDatetime == null) {
			his.setBizDatetime(new Date());
		} else {
			his.setBizDatetime(bizDatetime);
		}
		hisDao.saveHistory(his);
		return true;
	}

	@Override
	public SysUserBetNum findOne(Long userId, Long stationId) {
		return sysUserBetNumDao.findOne(userId, stationId);
	}

	@Override
	public JSONObject addOrGetBetInfo(Long stationId, String username) {
		SysUser user = sysUserDao.findOneByUsername(username, stationId, null);
		if (user == null || GuestTool.isGuest(user)) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("username", user.getUsername());
		SysUserBetNum betNum = sysUserBetNumDao.findOne(user.getId(), stationId);
		if (betNum == null) {
			init(user.getId(), stationId);
			json.put("drawNeed", 0);
			json.put("betNum", 0);
		} else {
			json.put("drawNeed", betNum.getDrawNeed());
			json.put("betNum", betNum.getBetNum());
		}
		return json;
	}

	@Override
	public void clearbetNumOpe(Long id,Long stationId, String remark,int opeType ) {
		String opeTypeName = "manual clear draw need";
		List <String> remarkList = I18nTool.convertCodeToList(opeTypeName,";",remark);
		if (!clearbetNumOpe2(id, stationId, opeType, I18nTool.convertCodeToArrStr(remarkList))) {
			throw new BaseException(BaseI18nCode.operateError);
		} else {
			LogUtils.log("clear draw need bet num success", LogTypeEnum.DEFAULT_TYPE);
		}
	}

	public boolean clearbetNumOpe2(Long id, Long stationId,Integer type, String remark) {
		SysUserBetNum old = sysUserBetNumDao.findOneByAccountId(id,stationId);
		if ( old==null) {
			return false;
		}
		BigDecimal drawNeed =old.getDrawNeed();
		BigDecimal hasBetNum =old.getBetNum();
		if (drawNeed.compareTo(BigDecimal.ZERO)==0){
			return true;
		}
		SysUserBetNum bn = sysUserBetNumDao.clearDrawNeedBetNumOpe(id);
		if (bn == null ) {
			return false;
		}
		SysUser acc = sysUserDao.findOneById(id, stationId);
		if (GuestTool.isGuest(acc)) {
			return false;
		}
		SysUserBetNumHistory his = new SysUserBetNumHistory();
		his.setUserId(id);
		his.setBeforeNum(hasBetNum);
		his.setAfterNum(hasBetNum);
		his.setBetNum(BigDecimal.ZERO);
		his.setType(type);
		his.setRemark(remark);
		AdminUser admin = LoginAdminUtil.currentUser();
		if (admin != null) {
			his.setOperatorId(admin.getId());
			if (admin.getType() != UserTypeEnum.ADMIN.getType()) {
				his.setOperatorName("--");
			} else {
				his.setOperatorName(admin.getUsername());
			}
		}
		his.setCreateDatetime(new Date());
		his.setParentIds(acc.getParentIds());
		his.setUsername(acc.getUsername());
		his.setBeforeDrawNeed(drawNeed);
		his.setAfterDrawNeed(BigDecimal.ZERO);
		his.setDrawNeed(drawNeed.negate());
		his.setBizDatetime(new Date());
		his.setStationId(stationId);
//		his.setAgentUser(acc.getAgentUser());
		hisDao.saveHistory(his);
		return true;
	}

	@Override
	public void adminUpdateBetNum(Long accountId, Long stationId, BigDecimal num, String remark, Integer type) {
		if (num == null) {
			throw new ParamException(BaseI18nCode.betNumRequired);
		}
		if (StringUtils.isEmpty(remark)) {
			throw new ParamException(BaseI18nCode.remarkRequired);
		}
		BetNumTypeEnum te = BetNumTypeEnum.getType(type);
		if (te == null) {
			throw new ParamException(BaseI18nCode.operatingTypeError);
		}
		if (te != BetNumTypeEnum.drawneedSub && te != BetNumTypeEnum.drawneedAdd) {
			throw new ParamException(BaseI18nCode.operatingTypeError);
		}
		if (te.equals(BetNumTypeEnum.drawneedSub)) {
			num = num.negate();
		}
		if (!addDrawNeed(accountId, stationId, num, te.getType(), I18nTool.getMessage("BetNumTypeEnum." + te.name(),Locale.ENGLISH)
                + I18nTool.getMessage(BaseI18nCode.operReason, Locale.ENGLISH) + remark, null)) {
			throw new ParamException(BaseI18nCode.updateBetNumException);
		} else {
			LogUtils.modifyLog(te.getTitle() + "打码量 " + num + " 成功");
		}
	}
}
