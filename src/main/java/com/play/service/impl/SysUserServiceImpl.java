package com.play.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.play.common.utils.security.EncryptDataUtil;
import com.play.core.*;
import com.play.model.*;
import com.play.model.bo.MnyMoneyBo;
import com.play.model.vo.UserPermVo;
import com.play.service.*;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.ip.IPAddressUtils;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.common.utils.ProxyModelUtil;
import com.play.common.utils.RandomStringUtils;
import com.play.common.utils.security.MD5Util;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserProxyNumDao;
import com.play.model.bo.UserRegisterBo;
import com.play.model.so.UserSo;
import com.play.model.vo.UserVo;
import com.play.orm.jdbc.page.Page;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.user.online.OnlineManager;
import com.play.web.utils.RebateUtil;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.ValidateUtil;
import com.play.web.utils.export.ExportToCvsUtil;
import com.play.web.var.GuestTool;
import org.springframework.util.CollectionUtils;

/**
 * 会员账号信息
 *
 * @author admin
 *
 */
@Service
public class SysUserServiceImpl implements SysUserService {

	//private static Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserProxyNumDao sysUserProxyNumDao;
	@Autowired
	private StationPromotionService promotionService;
	@Autowired
	private StationRebateService stationRebateService;
	@Autowired
	private SysUserRebateService userRebateService;
	@Autowired
	private ThirdGameService thirdGameService;
	@Autowired
	private SysUserDegreeService degreeService;
	@Autowired
	private SysUserMoneyService sysUserMoneyService;
	@Autowired
	private SysUserProxyNumService sysUserProxyNumService;
	@Autowired
	private SysUserPermService sysUserPermService;
	@Autowired
	private SysUserInfoService sysUserInfoService;
	@Autowired
	private SysUserBetNumService sysUserBetNumService;
	@Autowired
	private SysUserDepositService sysUserDepositService;
	@Autowired
	private SysUserScoreService sysUserScoreService;
	@Autowired
	private SysUserLoginService sysUserLoginService;
	@Autowired
	private SysUserWithdrawService sysUserWithdrawService;



	@Override
	public SysUser findOneByUsername(String username, Long stationId, Integer type) {
		return sysUserDao.findOneByUsername(username, stationId, type);
	}

	@Override
	public SysUser findOneById(Long id, Long stationId) {
		return sysUserDao.findOneById(id, stationId);
	}

	@Override
	public int getLowestLevel(Long stationId, Long proxyId) {
		return sysUserDao.getLowestLevelIndex(stationId, proxyId);
	}

	@Override
	public Page<UserVo> getPageForAdmin(UserSo so, boolean viewContact) {
		if (so.getNotLoginDay() != null) {
			so.setLastLoginTime(DateUtils.addDays(new Date(), -so.getNotLoginDay()));
		}
		if (StringUtils.isNotEmpty(so.getStartTime())) {
			so.setBegin(DateUtil.toDatetime(so.getStartTime()));
		}
		if (StringUtils.isNotEmpty(so.getEndTime())) {
			so.setEnd(DateUtil.toDatetime(so.getEndTime()));
			if (so.getEnd() != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(so.getEnd());
				c.add(Calendar.SECOND, 1);
				so.setEnd(c.getTime());
			}
		}
		Page<UserVo> page = sysUserDao.pageForAdmin(so, viewContact);
		if (page.hasRows()) {
			page.getRows().forEach(x -> {
				if (StringUtils.isNotEmpty(x.getLastLoginIp())) {
					x.setLastLoginIp(x.getLastLoginIp() + "(" + IPAddressUtils.getCountry(x.getLastLoginIp()) + ")");
				}
				if (StringUtils.isNotEmpty(x.getRegisterIp())) {
					x.setRegisterIp(x.getRegisterIp() + "(" + IPAddressUtils.getCountry(x.getRegisterIp()) + ")");
				}
			});
		}
		final Float[] sumCurrentPageSumMoney = {0f};
		page.getRows().forEach(x->{
			if(x.getMoney()!=null&&x.getMoney().floatValue()>0){
				sumCurrentPageSumMoney[0] +=x.getMoney().floatValue();
			}
		});

		UserVo userVo = new UserVo();
		userVo.setId(-100l);
		userVo.setMoney(new BigDecimal(sumCurrentPageSumMoney[0]));
		userVo.setPhone(I18nTool.getMessage(BaseI18nCode.subTotal) + ":");
		List<UserVo> list = new ArrayList<>();
		list.addAll(page.getRows());
		list.add(userVo);
		page.setRows(list);
		String sysUserSumMoney = sysUserDao.getSysUserSumMoney(so);
		UserVo userVo2 = new UserVo();
		userVo2.setId(-100l);
		userVo2.setMoney(new BigDecimal(sysUserSumMoney));
		userVo2.setPhone((I18nTool.getMessage(BaseI18nCode.total) + ":"));
		list.add(userVo2);

		return page;
	}

	@Override
	public void export(UserSo so) {
		if (so.getNotLoginDay() != null) {
			so.setLastLoginTime(DateUtils.addDays(new Date(), -so.getNotLoginDay()));
		}
		if (StringUtils.isNotEmpty(so.getStartTime())) {
			so.setBegin(DateUtil.toDatetime(so.getStartTime()));
		}
		if (StringUtils.isNotEmpty(so.getEndTime())) {
			so.setEnd(DateUtil.toDatetime(so.getEndTime()));
			if (so.getEnd() != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(so.getEnd());
				c.add(Calendar.SECOND, 1);
				so.setEnd(c.getTime());
			}
		}
		List<UserVo> userList = sysUserDao.exportList(so);
		String[] rowsName = new String[] { I18nTool.getMessage(BaseI18nCode.stationSerialNumber),
				I18nTool.getMessage(BaseI18nCode.stationMember), I18nTool.getMessage(BaseI18nCode.stationRealName),
				I18nTool.getMessage(BaseI18nCode.stationType), I18nTool.getMessage(BaseI18nCode.stationProxyBelong),
				I18nTool.getMessage(BaseI18nCode.stationOneProxy), I18nTool.getMessage(BaseI18nCode.stationTwoProxy),
				I18nTool.getMessage(BaseI18nCode.stationInfulMoney),
				I18nTool.getMessage(BaseI18nCode.stationScoreInful),
				I18nTool.getMessage(BaseI18nCode.stationMemberLevel), I18nTool.getMessage(BaseI18nCode.stationRemark)
				, I18nTool.getMessage(BaseI18nCode.newMobile)
				, I18nTool.getMessage(BaseI18nCode.newMail)};
		List<Object[]> dataList = new ArrayList<>();
		if (!userList.isEmpty()) {
			Object[] objs;
			String parents;
			int i = 1;
			for (UserVo u : userList) {
				objs = new Object[13];
				objs[0] = i;
				objs[1] = getStrNull(u.getUsername());
				objs[2] = getStrNull(u.getRealName());
				objs[3] = u.getType() == UserTypeEnum.MEMBER.getType() ? BaseI18nCode.stationMember
						: BaseI18nCode.stationAgent;
				objs[4] = getStrNull(u.getProxyName());
				parents = u.getParentNames();
				if (StringUtils.isEmpty(parents)) {
					objs[5] = BaseI18nCode.isNoExist;
					objs[6] = BaseI18nCode.isNoExist;
				} else {
					String[] p = parents.split(",");
					objs[5] = p[1];
					objs[6] = p.length < 3 ? BaseI18nCode.isNoExist : p[2];
				}
				objs[7] = getStrNull(u.getMoney());
				objs[8] = getStrNull(u.getScore());
				objs[9] = getStrNull(u.getDegreeName());
				BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode((String) objs[9]);
				if (baseI18nCode != null) {
					objs[9] = (I18nTool.getMessage(baseI18nCode));
				}
				objs[10] = getStrNull(u.getRemark());
				String phone = EncryptDataUtil.decryptData(u.getPhone());
				objs[11] = getStrNull(phone);
				String email = EncryptDataUtil.decryptData(u.getEmail());
				objs[12] = getStrNull(email);
				dataList.add(objs);
				i++;
			}
		}
		ExportToCvsUtil.export(BaseI18nCode.stationVipInfo.getMessage(), rowsName, dataList);
		LogUtils.log("导出会员信息", LogTypeEnum.EXPORT);
	}

	private String getStrNull(Object obj) {
		if (obj == null || obj.toString().isEmpty()) {
			return "-";
		}
		return obj.toString();
	}

	@Override
	public boolean exist(String username, Long stationId, Long escapeId) {
		return sysUserDao.exist(username, stationId, escapeId);
	}

	@Override
	public boolean existByEmail(String email, Long stationId, Long escapeId) {
		return sysUserDao.existEmail(email, stationId, escapeId);
	}

	@Override
	public SysUser save(SysUser user) {
		return sysUserDao.save(user);
	}

	@Override
	public void adminPwdModify(Long id, Long stationId, String pwd, String rpwd) {
		if (!StringUtils.equals(pwd, rpwd)) {
			throw new BaseException(BaseI18nCode.stationNewPassNotRig);
		}
		if (!ValidateUtil.isPassword(pwd)) {
			throw new BaseException(BaseI18nCode.stationNewPassFormatError);
		}
		SysUser user = sysUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new ParamException(BaseI18nCode.memberUnExist);
		}
		String newSalt = RandomStringUtils.randomAll(10);
		String newPwdMd5 = MD5Util.pwdMd5Member(user.getUsername(), pwd, newSalt);
		sysUserDao.changeLoginPwd(id, stationId, newPwdMd5, newSalt);
		LogUtils.modifyPwdLog("修改用户:" + user.getUsername() + " 登陆密码");
	}

	@Override
	public void modifyProxy(Long id, UserRegisterBo rbo, Long stationId) {
		SysUser user = sysUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new ParamException(BaseI18nCode.memberUnExist);
		}
		modifyProxy(user, rbo);
	}

	private void modifyProxy(SysUser user, UserRegisterBo rbo) {
		Long stationId = user.getStationId();
		String oldProxyName = user.getProxyName();
		Long oldProxyId = user.getProxyId();
		String oldParentNames = user.getParentNames();
		String oldParentIds = user.getParentIds();
		Long newProxyId = null;
		// 代理处理
		if (!(StringUtils.isEmpty(oldProxyName) && StringUtils.isEmpty(rbo.getProxyName()))
				&& !StringUtils.equals(oldProxyName, rbo.getProxyName())) {
			if (StringUtils.isEmpty(rbo.getProxyName())) {
				user.setProxyId(null);
				user.setParentIds(null);
				user.setParentNames(null);
				user.setProxyName(null);
				user.setLevel(1);
			} else {
				if (Objects.equals(user.getType(), UserTypeEnum.PROXY.getType())
						&& !ProxyModelUtil.isMultiOrAllProxy(stationId)) {
					// 不是全部代理，也不是多级代理 则当前是代理不能再添加代理
					throw new ParamException(BaseI18nCode.canntAssignProxy);
				}
				SysUser proxy = sysUserDao.findOneByUsername(rbo.getProxyName(), stationId,
						UserTypeEnum.PROXY.getType());
				if (proxy == null) {
					throw new BaseException(BaseI18nCode.proxyUnExist);
				}
				if (StringUtils.contains(proxy.getParentIds(), "," + user.getId() + ",")) {
					throw new BaseException(BaseI18nCode.stationUpProxyNoSettingDwon);
				}
				if (user.getId().equals(proxy.getId())) {
					throw new BaseException(BaseI18nCode.stationOwnerNoOwnerPorxy);
				}
				newProxyId = proxy.getId();
				String proParentIds = StringUtils.isEmpty(proxy.getParentIds()) ? "," : proxy.getParentIds();
				String proParentNames = StringUtils.isEmpty(proxy.getParentNames()) ? "," : proxy.getParentNames();
				user.setProxyId(newProxyId);
				user.setParentIds(proParentIds + newProxyId + ",");
				user.setParentNames(proParentNames + proxy.getUsername() + ",");
				user.setProxyName(proxy.getUsername());
				user.setLevel(proxy.getLevel() + 1);
			}
			if (user.getType() == UserTypeEnum.PROXY.getType()) {
				// 修改当前账号子孙代理的父级代理
				sysUserDao.updateChildrenProxy(oldParentIds, user.getParentIds(), oldParentNames, user.getParentNames(),
						user.getId());
			}
		}
		// 处理其他返点
		if (user.getType() == UserTypeEnum.PROXY.getType() && user.getProxyId() != null) {
			saveProxyRebate(user, stationId, rbo);
		}
		// TODO 修改旧代理新代理下级人数,当新旧代理不一致的时候才做更改
		if (!StringUtils.equals(rbo.getProxyName(), oldProxyName)) {
			if (oldProxyId != null) {
				sysUserProxyNumDao.updateProxyNum(user.getType() == UserTypeEnum.PROXY.getType(), -1, oldProxyId);
			}
			if (newProxyId != null) {
				sysUserProxyNumDao.updateProxyNum(user.getType() == UserTypeEnum.PROXY.getType(), 1, newProxyId);
			}
		}
		sysUserDao.changeProxy(user);
		LogUtils.modifyLog("修改用户" + user.getUsername() + " 的代理，旧代理：" + oldProxyName + "  新代理:" + rbo.getProxyName());
	}

	private void saveProxyRebate(SysUser user, Long stationId, UserRegisterBo rbo) {
		StationRebate sr = stationRebateService.findOne(stationId, StationRebate.USER_TYPE_PROXY);
		if (sr.getType() == StationRebate.TYPE_SAME) {// 所有层级返点相同
			SysUserRebate rebate = userRebateService.findOne(user.getId(), stationId);
			rebate.setLive(sr.getLive());
			rebate.setEgame(sr.getEgame());
			rebate.setChess(sr.getChess());
			rebate.setSport(sr.getSport());
			rebate.setEsport(sr.getEsport());
			rebate.setFishing(sr.getFishing());
			rebate.setLottery(sr.getLottery());
			userRebateService.update(rebate);
			return;
		}
		ThirdGame game = thirdGameService.findOne(stationId);
		SysUserRebate proxyRebate = userRebateService.findOne(user.getProxyId(), stationId);
		if (proxyRebate == null) {
			throw new BaseException(BaseI18nCode.stationParentProxyError);
		}
		rbo.setLive(BigDecimalUtil.absOr0(rbo.getLive()));
		rbo.setEgame(BigDecimalUtil.absOr0(rbo.getEgame()));
		rbo.setSport(BigDecimalUtil.absOr0(rbo.getSport()));
		rbo.setEsport(BigDecimalUtil.absOr0(rbo.getEsport()));
		rbo.setChess(BigDecimalUtil.absOr0(rbo.getChess()));
		rbo.setFishing(BigDecimalUtil.absOr0(rbo.getFishing()));
		rbo.setLottery(BigDecimalUtil.absOr0(rbo.getLottery()));
		if (Objects.equals(game.getLive(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getLive(), proxyRebate.getLive(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.real.getTitle());
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(rbo.getLive(), sr.getLevelDiff()), "live") > 0) {
				throw new BaseException(BaseI18nCode.stationCurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getEgame(), proxyRebate.getEgame(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.dianzi.getTitle());
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(rbo.getEgame(), sr.getLevelDiff()), "egame") > 0) {
				throw new BaseException(BaseI18nCode.stationEgameCurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getSport(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getSport(), proxyRebate.getSport(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.sport.getTitle());
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(rbo.getSport(), sr.getLevelDiff()), "sport") > 0) {
				throw new BaseException(BaseI18nCode.stationSportCurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getEsport(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getEsport(), proxyRebate.getEsport(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.dianjing.getTitle());
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(rbo.getEsport(), sr.getLevelDiff()), "esport") > 0) {
				throw new BaseException(BaseI18nCode.stationECurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getChess(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getChess(), proxyRebate.getChess(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.qipai.getTitle());
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(rbo.getChess(), sr.getLevelDiff()), "chess") > 0) {
				throw new BaseException(BaseI18nCode.stationChessCurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getFishing(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getFishing(), proxyRebate.getFishing(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.buyu.getTitle());
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(rbo.getFishing(), sr.getLevelDiff()), "fishing") > 0) {
				throw new BaseException(BaseI18nCode.stationFishCurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getLottery(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getLottery(), proxyRebate.getLottery(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.caipiao.getTitle());
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(rbo.getLottery(), sr.getLevelDiff()), "lottery") > 0) {
				throw new BaseException(BaseI18nCode.stationLotterCurrentMaxDownPoint);
			}
		}
		SysUserRebate rebate = userRebateService.findOne(user.getId(), stationId);
		rebate.setLive(rbo.getLive());
		rebate.setEgame(rbo.getEgame());
		rebate.setChess(rbo.getChess());
		rebate.setSport(rbo.getSport());
		rebate.setEsport(rbo.getEsport());
		rebate.setFishing(rbo.getFishing());
		rebate.setLottery(rbo.getLottery());
		userRebateService.update(rebate);
		updatePromotionToZero(user, rebate);
	}

	private void updatePromotionToZero(SysUser user, SysUserRebate rebate) {
		List<StationPromotion> codes = promotionService.getList(user.getId(), user.getStationId(), null, null);
		if (codes != null && !codes.isEmpty()) {
			boolean update = false;
			for (StationPromotion code : codes) {
				update = false;
				if (code.getLive().compareTo(rebate.getLive()) > 0) {
					code.setLive(BigDecimal.ZERO);
					update = true;
				}
				if (code.getEgame().compareTo(rebate.getEgame()) > 0) {
					code.setEgame(BigDecimal.ZERO);
					update = true;
				}
				if (code.getChess().compareTo(rebate.getChess()) > 0) {
					code.setChess(BigDecimal.ZERO);
					update = true;
				}
				if (code.getSport().compareTo(rebate.getSport()) > 0) {
					code.setSport(BigDecimal.ZERO);
					update = true;
				}
				if (code.getEsport().compareTo(rebate.getEsport()) > 0) {
					code.setEsport(BigDecimal.ZERO);
					update = true;
				}
				if (code.getFishing().compareTo(rebate.getFishing()) > 0) {
					code.setFishing(BigDecimal.ZERO);
					update = true;
				}
				if (code.getLottery().compareTo(rebate.getLottery()) > 0) {
					code.setLottery(BigDecimal.ZERO);
					update = true;
				}
				if (update) {
					promotionService.update(code);
				}
			}
			LogUtils.modifyLog(user.getUsername() + "修改推广链接的代理返点比例");
		}
	}

	@Override
	public void changeStatus(Long id, Long stationId, Integer status) {
		SysUser user = sysUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		String msg = I18nTool.getMessage(BaseI18nCode.enable);
		if (status == null || Constants.STATUS_ENABLE != status) {
			status = Constants.STATUS_DISABLE;
			msg = I18nTool.getMessage(BaseI18nCode.disable);
			// 将会员踢下线
			OnlineManager.forcedOffLine(id, stationId);
		}
		if (!Objects.equals(user.getStatus(), status)) {
			sysUserDao.changeStatus(id, stationId, status);
			LogUtils.modifyLog("修改用户" + user.getUsername() + " 状态为：" + msg);
		}
	}

	@Override
	public void freezeMoney(Long id, Long stationId, Integer status) {
		SysUser user = sysUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		String msg = I18nTool.getMessage(BaseI18nCode.thawYes);
		if (status == null || SysUser.moneyStatusNormal != status) {
			status = SysUser.moneyStatusFreeze;
			msg = I18nTool.getMessage(BaseI18nCode.thawNo);
		}
		if (!Objects.equals(user.getMoneyStatus(), status)) {
			sysUserDao.freezeMoney(id, stationId, status);
			LogUtils.modifyLog(msg + "会员:" + user.getUsername() + "余额");
		}
	}

	@Override
	public void modifyRemark(Long id, Long stationId, String remark) {
		SysUser user = sysUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		sysUserDao.modifyRemark(id, stationId, remark);
		LogUtils.modifyLog("用户:" + user.getUsername() + " 的备注从 " + user.getRemark() + " 修改成 " + remark);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void batchChangeProxy(UserRegisterBo rbo, String usernames, Long stationId) {
		if (StringUtils.isEmpty(usernames)) {
			throw new BaseException(BaseI18nCode.stationInputVipName);
		}
		String[] uns = usernames.split(" |,|\n");
		if (uns.length > 50) {
			throw new BaseException(BaseI18nCode.stationOneOperMoreF, new Object[]{50});
		}
		StringBuilder errors = new StringBuilder();
		SysUser user = null;
		for (String un : uns) {
			un = StringUtils.trim(un);
			if (StringUtils.isEmpty(un)) {
				continue;
			}
			user = sysUserDao.findOneByUsername(un.toLowerCase(), stationId, null);
			if (user == null || GuestTool.isGuest(user)) {
				errors.append(un).append(BaseI18nCode.stationUserNotExist);
				continue;
			}
			try {
				this.modifyProxy(user, rbo);
			} catch (Exception e) {
				errors.append(un).append(":").append(e.getMessage()).append(";");
			}
		}
		if (errors.length() > 0) {
			errors.deleteCharAt(errors.length() - 1);
			throw new BaseException(
					BaseI18nCode.stationErrorMessage + errors.toString() + BaseI18nCode.stattionErrorFreshPage);
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void batchDisableStatus(String usernames, Long stationId) {
		if (StringUtils.isEmpty(usernames)) {
			throw new BaseException(BaseI18nCode.stationInputVipName);
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
				errors.append(un).append(BaseI18nCode.stationUserNotExist);
				continue;
			}
			// 将账号踢下线
			OnlineManager.forcedOffLine(user.getId(), stationId);
			sysUserDao.changeStatus(user.getId(), stationId, Constants.STATUS_DISABLE);
		}
		LogUtils.modifyStatusLog("批量禁用用户状态:" + usernames);
		if (errors.length() > 0) {
			errors.deleteCharAt(errors.length() - 1);
			throw new BaseException(BaseI18nCode.stationErrorMessage + errors.toString());
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void batchEnableStatus(String usernames, Long stationId) {
		if (StringUtils.isEmpty(usernames)) {
			throw new BaseException(BaseI18nCode.stationInputVipName);
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
				errors.append(un).append(BaseI18nCode.stationUserNotExist);
				continue;
			}
			sysUserDao.changeStatus(user.getId(), stationId, Constants.STATUS_ENABLE);
		}
		LogUtils.modifyStatusLog("批量启用用户状态:" + usernames);
		if (errors.length() > 0) {
			errors.deleteCharAt(errors.length() - 1);
			throw new BaseException(BaseI18nCode.stationErrorMessage, new Object[] { errors });
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void errorPwdDisableAccount(SysUser user) {
		sysUserDao.changeStatus(user.getId(), user.getStationId(), Constants.STATUS_DISABLE);
		// 将会员踢下线
		OnlineManager.forcedOffLine(user.getId(), user.getStationId());
		LogUtils.modifyStatusLog(user.getUsername() + "会员密码失败次数过多，自动禁用账号");
	}

	@Override
	public void resetLoginPwd(String pwd, String okPwd, Long userId, Long stationId, String username) {
		if (!StringUtils.equals(pwd, okPwd)) {
			throw new BaseException(BaseI18nCode.pwdNotSame);
		}
		if (!ValidateUtil.isPassword(pwd)) {
			throw new BaseException(BaseI18nCode.pwdFormatError);
		}
		String newSalt = RandomStringUtils.randomAll(10);
		String newPwdMd5 = MD5Util.pwdMd5Member(username, pwd, newSalt);
		sysUserDao.changeLoginPwd(userId, stationId, newPwdMd5, newSalt);
		LogUtils.modifyPwdLog(username + "导入数据自动重置登陆密码");
	}

	@Override
	public int countRegisterMemberByIp(Long stationId, Date begin, Date end, String ip, Integer userType) {
		return sysUserDao.countRegisterMemberByIp(stationId, begin, end, ip, userType);
	}

	@Override
	public int countRegisterMemberByOs(Long stationId, Date begin, Date end, String os, Integer userType) {
		return sysUserDao.countRegisterMemberByOs(stationId, begin, end, os, userType);
	}


	@Override
	public Integer countEffectiveNum(Long stationId, boolean effective) {
		return sysUserDao.countEffectiveNum(stationId, effective);
	}

	@Override
	public void modifyLoginPwd(String oldPwd, String newPwd, String okPwd, Long userId, Long stationId,
			String username) {
		if (StringUtils.isEmpty(newPwd)) {
			throw new BaseException(BaseI18nCode.stationPasswordNotNull);
		}
		if (!ValidateUtil.isPassword(newPwd)) {
			throw new BaseException(BaseI18nCode.stationLoginPassMustZ);
		}
		if (!StringUtils.equals(newPwd, okPwd)) {
			throw new BaseException(BaseI18nCode.stationNewPassNotRig);
		}
		if (StringUtils.equals(oldPwd, newPwd)) {
			throw new BaseException(BaseI18nCode.pwdDontSameOld);
		}
		if (!ValidateUtil.isPassword(newPwd)) {
			throw new BaseException(BaseI18nCode.stationNewPassFormatError);
		}
		SysUser user = sysUserDao.findOneById(userId, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		if (!StringUtils.equals(user.getPassword(), MD5Util.pwdMd5Member(username, oldPwd, user.getSalt()))) {
			throw new BaseException(BaseI18nCode.pwdOldError);
		}
		String newSalt = RandomStringUtils.randomAll(10);
		String newPwdMd5 = MD5Util.pwdMd5Member(username, newPwd, newSalt);
		sysUserDao.changeLoginPwd(userId, stationId, newPwdMd5, newSalt);
		LogUtils.modifyPwdLog(username + "修改登陆密码成功");
	}

	@Override
	public Page<UserVo> getSubordinatePage(SysUser user, String searchUsername, BigDecimal maxBalance,
			BigDecimal minBalance, Date start, Date end, BigDecimal depositTotal, Boolean include, Integer level,
			String sortName, String sortOrder, Integer pageSize, Integer pageNumber) {
		// 验证下级
		boolean isMember = (user.getType() == UserTypeEnum.MEMBER.getType());
		Long userId = user.getId();
		if (StringUtils.isNotEmpty(searchUsername)) {
			SysUser child = sysUserDao.findOneByUsername(searchUsername, user.getStationId(), null);
			if (child == null) {
				throw new ParamException(BaseI18nCode.searchUserNotExist);
			}
			if (isMember) {// 会员则判断是否是推荐好友来的
				if (!child.getRecommendId().equals(userId)) {
					throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
				}
				level = null;
				include = false;// 会员推广只能查看直属的会员，不能再看下一级
			} else {// 代理推广来的
				if (user.getParentIds() == null || user.getParentIds().contains("," + userId + ",")) {
					throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
				}
			}
			userId = child.getId();
		}
		String key = "userCenterUserList:" + userId + ":" + SystemUtil.getLanguage() + ":" + maxBalance + ":" + minBalance + ":"
				+ DateUtil.formatDate(start, DateUtil.DATE_FORMAT_STR) + ":"
				+ DateUtil.formatDate(end, DateUtil.DATE_FORMAT_STR) + ":" + pageSize + ":" + pageNumber + ":" + include
				+ ":" + depositTotal + ":" + sortName + ":" + sortOrder + ":" + level;
//		Page<UserVo> page = CacheUtil.getCache(CacheKey.USER_INFO, key, Page.class);
//		if (page != null) {
//			return page;
//		}
//		if (level != null) {
//			level = user.getLevel() + level - 1;
//			include = true;
//		}
		Page<UserVo> page = sysUserDao.getSubordinatePage(user.getStationId(), userId, maxBalance, minBalance, start, end,
				depositTotal, include, isMember, level);
		boolean showDegree = StationConfigUtil.isOn(user.getStationId(), StationConfigEnum.switch_degree_show);
		Date date = new Date();
		page.getRows().stream().parallel().forEach(x -> {
			if (showDegree) {
				String degree = degreeService.findDegreeName(x.getDegreeId(), user.getStationId());
				x.setDegreeName(I18nTool.getMessage(BaseI18nCode.getBaseI18nCode(degree)));
			}
			if (x.getLastLoginDatetime() == null) {
				x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.neverLogin));
			} else {
				int diff = DateUtil.getDayMargin(x.getLastLoginDatetime(), date);
				if (diff >= 3 && diff < 7) {
					x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.moreThan3daysUnLogin));
				} else if (diff >= 7 && diff < 30) {
					int weeks = (int) (diff / 7);
					x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.moreThanWeekUnLogin, new Object[] { weeks }));
				} else if (diff >= 30) {
					int month = (int) (diff / 30);
					x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.moreThanMonthUnLogin, new Object[] { month }));
				} else if (diff == 1) {
					x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.todayLogin));
				} else {
					x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.last3daysLogin));
				}
			}
			x.setLevel(x.getLevel() - user.getLevel() + 1);
		});
		CacheUtil.addCache(CacheKey.USER_INFO, key, JSON.toJSONString(page, SerializerFeature.WriteDateUseDateFormat),
				120);
		return page;
	}

	@Override
	public Page<UserVo> getUserTeamDataPage(SysUser user, String proxyName, Date start, Date end, Integer type,
			Boolean include, Integer pageSize, Integer pageNumber) {
		// 验证下级
		boolean isMember = (user.getType() == UserTypeEnum.MEMBER.getType());
		Long userId = user.getId();
		if (StringUtils.isNotEmpty(proxyName)) {
			SysUser child = sysUserDao.findOneByUsername(proxyName, user.getStationId(), null);
			if (child == null) {
				throw new ParamException(BaseI18nCode.searchUserNotExist);
			}
			if (isMember) {// 会员则判断是否是推荐好友来的
				if (!child.getRecommendId().equals(userId)) {
					throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
				}
				include = false;// 会员推广只能查看直属的会员，不能再看下一级
			} else {// 代理推广来的
				if (user.getParentIds() == null || user.getParentIds().contains("," + userId + ",")) {
					throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
				}
			}
			userId = user.getId();
		}
		String key = "getUserTeamDataPage:" + userId + ":" + DateUtil.formatDate(start, DateUtil.DATE_FORMAT_STR) + ":"
				+ DateUtil.formatDate(end, DateUtil.DATE_FORMAT_STR) + ":" + pageSize + ":" + pageNumber + ":" + include
				+ ":" + type;
//		Page<UserVo> page = CacheUtil.getCache(CacheKey.USER_INFO, key, Page.class);
//		if (page != null) {
//			return page;
//		}
		Page<UserVo> page = sysUserDao.getUserTeamPage(user.getStationId(), userId, start, end, type, include, isMember);
		boolean showDegree = StationConfigUtil.isOn(user.getStationId(), StationConfigEnum.switch_degree_show);
		Date date = new Date();
		page.getRows().stream().parallel().forEach(x -> {
			if (showDegree) {
				String degree = degreeService.findDegreeName(x.getDegreeId(), user.getStationId());
				x.setDegreeName(I18nTool.getMessage(BaseI18nCode.getBaseI18nCode(degree)));
			}
			if (x.getLastLoginDatetime() == null) {
				x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.neverLogin));
			} else {
				int diff = DateUtil.getDayMargin(x.getLastLoginDatetime(), date);
				if (diff >= 3 && diff < 7) {
					x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.moreThan3daysUnLogin));
				} else if (diff >= 7 && diff < 30) {
					int weeks = (int) (diff / 7);
					x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.moreThanWeekUnLogin, new Object[] { weeks }));
				} else if (diff >= 30) {
					int month = (int) (diff / 30);
					x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.moreThanMonthUnLogin, new Object[] { month }));
				} else if (diff == 1) {
					x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.todayLogin));
				} else {
					x.setUnLoginDays(I18nTool.getMessage(BaseI18nCode.last3daysLogin));
				}
			}
		});
		CacheUtil.addCache(CacheKey.USER_INFO, key, JSON.toJSONString(page, SerializerFeature.WriteDateUseDateFormat),
				120);
		return page;
	}

	@Override
	public void modifyUserRebate(SysUser proxy, Long userId, BigDecimal liveRebate, BigDecimal sportRebate,
			BigDecimal egameRebate, BigDecimal chessRebate, BigDecimal esportRebate, BigDecimal fishingRebate,
			BigDecimal lotRebate) {
		Long stationId = proxy.getStationId();
		SysUser user = sysUserDao.findOneById(userId, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		if (!user.getParentIds().contains("," + proxy.getId() + ",")) {
			throw new UnauthorizedException();
		}
		if (user.getType() != UserTypeEnum.PROXY.getType()) {
			throw new BaseException(BaseI18nCode.noProxyUser);
		}
		boolean justUp = StationConfigUtil.isOn(stationId, StationConfigEnum.front_adjust_just_up);
		StationRebate sr = stationRebateService.findOne(stationId, StationRebate.USER_TYPE_PROXY);
		SysUserRebate rebate = userRebateService.findOne(user.getId(), stationId);
		if (sr.getType() == StationRebate.TYPE_SAME) {// 所有层级返点相同
			rebate.setLive(sr.getLive());
			rebate.setEgame(sr.getEgame());
			rebate.setChess(sr.getChess());
			rebate.setSport(sr.getSport());
			rebate.setEsport(sr.getEsport());
			rebate.setFishing(sr.getFishing());
			rebate.setLottery(sr.getLottery());
			userRebateService.update(rebate);
			return;
		}
		sportRebate = BigDecimalUtil.absOr0(sportRebate);
		egameRebate = BigDecimalUtil.absOr0(egameRebate);
		liveRebate = BigDecimalUtil.absOr0(liveRebate);
		chessRebate = BigDecimalUtil.absOr0(chessRebate);
		esportRebate = BigDecimalUtil.absOr0(esportRebate);
		lotRebate = BigDecimalUtil.absOr0(lotRebate);
		fishingRebate = BigDecimalUtil.absOr0(fishingRebate);
		ThirdGame game = thirdGameService.findOne(stationId);
		SysUserRebate proxyRebate = userRebateService.findOne(proxy.getId(), stationId);
		if (proxyRebate == null) {
			throw new BaseException(BaseI18nCode.stationParentProxyError);
		}
		if (Objects.equals(game.getLive(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(liveRebate, proxyRebate.getLive(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.real.getTitle());
			// 验证是否只能调高返点
			if (rebate.getLive() != null && justUp && liveRebate.compareTo(rebate.getLive()) < 0) {
				throw new BaseException(BaseI18nCode.rebateJustUp);
			}
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(liveRebate, sr.getLevelDiff()), "live") > 0) {
				throw new BaseException(BaseI18nCode.stationCurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(egameRebate, proxyRebate.getEgame(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.dianzi.getTitle());
			if (rebate.getEgame() != null && justUp && egameRebate.compareTo(rebate.getEgame()) < 0) {
				throw new BaseException(BaseI18nCode.rebateJustUp);
			}
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(egameRebate, sr.getLevelDiff()), "egame") > 0) {
				throw new BaseException(BaseI18nCode.stationEgameCurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getSport(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(sportRebate, proxyRebate.getSport(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.sport.getTitle());
			if (rebate.getSport() != null && justUp && sportRebate.compareTo(rebate.getSport()) < 0) {
				throw new BaseException(BaseI18nCode.rebateJustUp);
			}
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(sportRebate, sr.getLevelDiff()), "sport") > 0) {
				throw new BaseException(BaseI18nCode.stationSportCurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getEsport(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(esportRebate, proxyRebate.getEsport(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.dianjing.getTitle());
			if (rebate.getEsport() != null && justUp && esportRebate.compareTo(rebate.getEsport()) < 0) {
				throw new BaseException(BaseI18nCode.rebateJustUp);
			}
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(esportRebate, sr.getLevelDiff()), "esport") > 0) {
				throw new BaseException(BaseI18nCode.stationECurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getChess(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(chessRebate, proxyRebate.getChess(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.qipai.getTitle());
			if (rebate.getChess() != null && justUp && chessRebate.compareTo(rebate.getChess()) < 0) {
				throw new BaseException(BaseI18nCode.rebateJustUp);
			}
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(chessRebate, sr.getLevelDiff()), "chess") > 0) {
				throw new BaseException(BaseI18nCode.stationChessCurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getFishing(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(fishingRebate, proxyRebate.getFishing(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.buyu.getTitle());
			if (rebate.getFishing() != null && justUp && fishingRebate.compareTo(rebate.getFishing()) < 0) {
				throw new BaseException(BaseI18nCode.rebateJustUp);
			}
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(fishingRebate, sr.getLevelDiff()), "fishing") > 0) {
				throw new BaseException(BaseI18nCode.stationFishCurrentMaxDownPoint);
			}
		}
		if (Objects.equals(game.getLottery(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(lotRebate, proxyRebate.getLottery(), sr.getLevelDiff(), sr.getMaxDiff(),
					HotGameTypeEnum.caipiao.getTitle());
			if (rebate.getLottery() != null && justUp && lotRebate.compareTo(rebate.getLottery()) < 0) {
				throw new BaseException(BaseI18nCode.rebateJustUp);
			}
			if (userRebateService.countUnusualNum(user.getId(), stationId,
					BigDecimalUtil.addAll(lotRebate, sr.getLevelDiff()), "lottery") > 0) {
				throw new BaseException(BaseI18nCode.stationLotterCurrentMaxDownPoint);
			}
		}
		rebate.setLive(liveRebate);
		rebate.setEgame(egameRebate);
		rebate.setChess(chessRebate);
		rebate.setSport(sportRebate);
		rebate.setEsport(esportRebate);
		rebate.setFishing(fishingRebate);
		rebate.setLottery(lotRebate);
		userRebateService.update(rebate);
		updatePromotionToZero(user, rebate);
	}

	@Override
	public Page<UserVo> adminRiskReport(Long stationId, Date begin, Date end, String proxyName, String username,
			String degreeIds, String agentName) {
		Long userId = null;
		Long proxyId = null;
		boolean isMember = false;
		// 获取会员基本信息
		if (StringUtils.isNotEmpty(proxyName)) {
			SysUser proxy = sysUserDao.findOneByUsername(proxyName, stationId, null);
			if (proxy == null) {
				throw new BaseException(BaseI18nCode.proxyUnExist);
			}
			proxyId = proxy.getId();
			isMember = (proxy.getType() == UserTypeEnum.MEMBER.getType());
		}
		if (StringUtils.isNotEmpty(username)) {
			SysUser user = sysUserDao.findOneByUsername(username, stationId, null);
			if (user == null) {
				throw new BaseException(BaseI18nCode.memberUnExist);
			}
			userId = user.getId();
		}
		Page<UserVo> p = sysUserDao.getUserRiskPage(stationId, begin, end, userId, proxyId, degreeIds, agentName,
				isMember);
		if (p.hasRows()) {
			Map<Long, String> degreeMap = new HashMap<>();
			String tmp;
			for (UserVo v : p.getRows()) {
				tmp = degreeMap.get(v.getDegreeId());
				if (tmp == null) {
					tmp = degreeService.getName(v.getDegreeId(), stationId);
					degreeMap.put(v.getDegreeId(), tmp);
				}
				v.setDegreeName(tmp);
			}
		}
		return p;
	}

	@Override
	public Page<UserVo> adminCheatReport(Long stationId, Date begin, Date end, String username,
										Integer cheatType) {
		Page<UserVo> page = sysUserDao.getUserCheatPage(stationId, begin, end, username, cheatType);
		if (page.hasRows()) {
			page.getRows().forEach(x -> {
				if (StringUtils.isNotEmpty(x.getLastLoginIp())) {
					x.setLastLoginIp(x.getLastLoginIp() + "(" + IPAddressUtils.getCountry(x.getLastLoginIp()) + ")");
				}
				if (StringUtils.isNotEmpty(x.getRegisterIp())) {
					x.setRegisterIp(x.getRegisterIp() + "(" + IPAddressUtils.getCountry(x.getRegisterIp()) + ")");
				}
			});
		}
		return page;
	}

	@Override
	public void batchDisableNoLogin(Date lastLoginDate, int noLoginDay, Long stationId) {
		sysUserDao.batchDisableNoLogin(lastLoginDate, noLoginDay, stationId);
	}

	@Override
	public void changeOnlineWarnStatus(Long id, Long stationId, Integer status) {
		if (status != Constants.STATUS_DISABLE && status != Constants.STATUS_ENABLE) {
			throw new ParamException();
		}
		SysUser user = sysUserDao.findOne(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		sysUserDao.changeOnlineWarnStatus(id, stationId, status);
		// 会员是否在线
		if (RedisAPI.exists(OnlineManager.getOnlineSessionId(user), CacheKey.USER_ONLINE.getDb())) {
			if (status == Constants.STATUS_ENABLE) {
				user.setOnlineWarn(Constants.STATUS_ENABLE);
				OnlineManager.doOnlineWarnUser(user, false);
			} else {
				OnlineManager.delOnlineWarnUser(user, true);
				OnlineManager.delOnlineWarnUser(user, false);
			}
		}
		LogUtils.modifyStatusLog("设置告警会员" + user.getUsername() + ",状态码:" + status);
	}

	@Override
	public Page<UserVo> userGuestPage(UserSo userSo) {
		userSo.setUserType(UserTypeEnum.GUEST_BACK.getType());
		return sysUserDao.pageForAdmin(userSo, false);
	}

	@Override
	public void resetGuestMoney(String ids, BigDecimal money, String operator) {
		if (StringUtils.isEmpty(ids)) {
			throw new ParamException(BaseI18nCode.parameterError);
		}

		List<Long> list = Arrays.stream(ids.split(","))
				// 过滤非数字
				.filter(e -> ValidateUtil.isNumber(e))
				// 转成Long型
				.map(Long::new)
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(list)) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (list.size() > 5) {
			throw new BaseException(BaseI18nCode.stationOneOperMoreF, new Object[]{5});
		}

		Long stationId = SystemUtil.getStationId();
		for (Long id : list) {
			SysUser sysUser = this.findOneById(id, stationId);
			if (!GuestTool.isGuest(sysUser)) {
				continue;
			}
			MnyMoneyBo mvo = new MnyMoneyBo();
			mvo.setUser(sysUser);
			mvo.setMoney(money);
			mvo.setMoneyRecordType(MoneyRecordType.GUEST_TRY);
			sysUserMoneyService.updMnyAndRecord(mvo);
			LogUtils.modifyStatusLog("Reset guest account balance: username:" + sysUser.getUsername() + ", money:" + mvo.getMoney() + ", operator:" + operator);
		}

	}

	@Override
	public void changeUserToProxy(Long userId, Long stationId, BigDecimal liveRebate, BigDecimal sportRebate,
								  BigDecimal egameRebate, BigDecimal chessRebate, BigDecimal esportRebate,
								  BigDecimal fishingRebate, BigDecimal lotteryRebate) {

		SysUser user = sysUserDao.findOne(userId, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}

		if (user.getType() == UserTypeEnum.PROXY.getType()) {
			throw new BaseException(BaseI18nCode.alreadyProxy);
		}

		user.setType(UserTypeEnum.PROXY.getType());

//		saveProxyOtherRebate(user, stationId, liveRebate, sportRebate, egameRebate, chessRebate,
//				esportRebate, fishingRebate, lotteryRebate);

		sysUserDao.update(user);

		SysUserProxyNum sysUserProxyNum = sysUserProxyNumDao.findOneById(user.getId());
		if (sysUserProxyNum == null) {
			sysUserProxyNumService.init(user.getId());
		}
		if (user.getProxyId() != null) {
			sysUserProxyNumService.updateProxyNum(true, 1, user.getProxyId());
			sysUserProxyNumService.updateProxyNum(false, -1, user.getProxyId());
		}
		UserPermVo userPermVo = sysUserPermService.getPerm(user.getId(), stationId);
		if (userPermVo != null) {
			sysUserPermService.changeUserType(userId, stationId, UserTypeEnum.PROXY.getType());
		}
		LogUtils.modifyLog("用户" + user.getUsername() + "转换成代理");

	}

	@Override
	public void deleteGuest(Long id, String username) {
		sysUserDao.deleteById(id);
		this.cleanUserRegisterInfo(id, SystemUtil.getStationId(), null);
		LogUtils.delLog("Guest account delete: id: " + id + ", username: "+ username +", operator: " + LoginAdminUtil.getUsername());
	}

	@Override
	public void deleteBatchGuest(List<Long> ids) {
		sysUserDao.deleteBatchGuest(ids);
		Long stationId = SystemUtil.getStationId();
		ids.forEach(e -> {
			this.cleanUserRegisterInfo(e, stationId, null);
		});
		LogUtils.delLog("Guest account batch delete, ids: " + JSON.toJSONString(ids) + ", operator: " + LoginAdminUtil.getUsername());
	}

	/**
	 * 删除试玩账户，同时清除注册时初始化的信息表，该逻辑是否有必要实现待评估
	 *
	 * @param userId
	 * @param stationId
	 * @param userType
	 */
	@Override
	public void cleanUserRegisterInfo(Long userId, Long stationId, Integer userType) {

//		sysUserInfoService.delete(userId, stationId);
//		sysUserBetNumService.delete(userId, stationId);
//		sysUserDepositService.delete(userId, stationId);
//		sysUserMoneyService.delete(userId);
//		sysUserScoreService.delete(userId, stationId);
//		sysUserLoginService.delete(userId, stationId);
//		sysUserWithdrawService.delete(userId, stationId);
//		sysUserPermService.delete(userId, stationId, userType);
//		if (userType == UserTypeEnum.PROXY.getType()) {
//			sysUserProxyNumService.delete(userId);
//			userRebateService.delete(userId, stationId);
//		}
	}
}
