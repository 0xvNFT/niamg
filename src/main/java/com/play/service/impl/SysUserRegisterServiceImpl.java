package com.play.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.play.cache.redis.RedisAPI;
import com.play.core.*;
import com.play.dao.SysUserDao;
import com.play.model.*;
import com.play.model.bo.MnyMoneyBo;
import com.play.service.*;
import com.play.web.utils.*;
import com.play.web.var.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.ip.IpUtils;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.common.utils.ProxyModelUtil;
import com.play.common.utils.RandomStringUtils;
import com.play.common.utils.security.MD5Util;
import com.play.model.bo.UserRegisterBo;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.user.online.OnlineManager;
import com.play.web.vcode.VerifyCodeUtil;

import ua_parser.Client;
import ua_parser.Parser;

@Service
public class SysUserRegisterServiceImpl implements SysUserRegisterService {
	private Logger logger = LoggerFactory.getLogger(SysUserRegisterService.class);
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserInfoService userInfoService;
	@Autowired
	private SysUserBetNumService betNumService;
	@Autowired
	private SysUserDepositService userDepositService;
	@Autowired
	private SysUserMoneyService userMoneyService;
	@Autowired
	private SysUserLoginService userLoginService;
	@Autowired
	private SysUserPermService userPermService;
	@Autowired
	private SysUserProxyNumService userProxyNumService;
	@Autowired
	private SysUserScoreService userScoreService;
	@Autowired
	private SysUserWithdrawService userWithdrawService;
	@Autowired
	private StationDailyRegisterService stationDailyRegisterService;
	@Autowired
	private SysUserRebateService userRebateService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private SysUserGroupService userGroupService;
	@Autowired
	private ThirdGameService thirdGameService;
	@Autowired
	private StationRebateService stationRebateService;
	@Autowired
	private StationPromotionService promotionService;
	@Autowired
	private StationRegisterConfigService registerConfigService;
	@Autowired
	private StationMessageService messageService;
	@Autowired
	private SysUserMoneyService sysUserMoneyService;
	@Autowired
	private SysUserScoreService sysUserScoreService;
	@Autowired
	private SysUserBetNumService sysUserBetNumService;
	@Autowired
	private SysUserBonusService sysUserBonusService;
	@Autowired
	private StationRegisterStrategyService stationRegisterStrategyService;
	@Autowired
	private SysUserDao sysUserDao;

	@Override
	public void adminSaveMember(UserRegisterBo rbo) {
		if (StringUtils.isEmpty(rbo.getUsername())) {
			throw new BaseException(BaseI18nCode.usernameCanntEmpty);
		}
		if (StringUtils.isEmpty(rbo.getPwd())) {
			throw new BaseException(BaseI18nCode.stationPasswordNotNull);
		}
		if (!ValidateUtil.isPassword(rbo.getPwd())) {
			throw new BaseException(BaseI18nCode.stationLoginPassMustZ);
		}
		Long stationId = SystemUtil.getStationId();
		String username = rbo.getUsername().trim().toLowerCase();
		if (!ValidateUtil.isUsername(username)) {
			throw new BaseException(BaseI18nCode.stationUserNameMustNum);
		}
		if (!StationConfigUtil.isOn(stationId, StationConfigEnum.allnumber_switch_when_register)) {
			if (ValidateUtil.isNumber(username)) {
				throw new BaseException(BaseI18nCode.stationNotIncludeNum);
			}
		}
		if (sysUserService.exist(username, stationId, null)) {
			throw new BaseException(BaseI18nCode.stationUserByRegist);
		}
		SysUser user = new SysUser();
		user.setPartnerId(SystemUtil.getPartnerId());
		user.setLevel(1);
		user.setType(UserTypeEnum.MEMBER.getType());
		setAgentInfo(user, rbo.getAgentName(), stationId);
		setProxyInfo(user, rbo.getProxyName(), stationId);
		user.setDegreeId(getDegreeId(rbo.getDegreeId(), stationId));
		user.setGroupId(getGroupId(rbo.getGroupId(), stationId));
		user.setStatus(Constants.STATUS_ENABLE);
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setUsername(username);
		user.setPassword(MD5Util.pwdMd5Member(username, rbo.getPwd(), user.getSalt()));
		user.setCreateDatetime(new Date());
		user.setCreateUserId(LoginAdminUtil.getUserId());
		user.setCreateUsername(LoginAdminUtil.getUsername());
		user.setStationId(stationId);
		user.setOnlineWarn(Constants.STATUS_DISABLE);
		user.setLockDegree(SysUser.DEGREE_UNLOCK);
		user.setUpdatePwdDatetime(user.getCreateDatetime());
		user.setMoneyStatus(SysUser.moneyStatusNormal);
		user.setRemark(rbo.getRemark());
		setRegisterSystem(user);
		saveUserAllInfo(stationId, user, rbo);
		LogUtils.addLog("后台添加会员:" + user.getUsername());
	}

	private void saveUserAllInfo(Long stationId, SysUser user, UserRegisterBo rbo) {
		setRegisterInfo(user);
		sysUserService.save(user);
		// 保存用户基本信息
		userInfoService.init(user, rbo);
		betNumService.init(user.getId(), stationId);
		userDepositService.init(user.getId(), stationId);
		if (GuestTool.isGuest(user)) {
			// 试玩用户初始化试玩金
			userMoneyService.init(user.getId(), new BigDecimal(StationConfigUtil.get(stationId, StationConfigEnum.visitor_user_init_money)));
		} else {
			userMoneyService.init(user.getId(), BigDecimal.ZERO);
		}
		userScoreService.init(user.getId(), stationId);
		userLoginService.init(user.getId(), stationId);
		userWithdrawService.init(user.getId(), stationId);
		userPermService.init(user.getId(), stationId, user.getUsername(), user.getType());
		if (user.getType() == UserTypeEnum.PROXY.getType()) {
			userProxyNumService.init(user.getId());
			userRebateService.init(user.getId(), stationId, rbo.getLive(), rbo.getEgame(), rbo.getChess(),
					rbo.getSport(), rbo.getEsport(), rbo.getFishing(), rbo.getLottery());
		}
		// 添加站点每日注册信息表
		if (user.getProxyId() != null) {
			stationDailyRegisterService.registerHandle(stationId, user.getPartnerId(), user.getType(), true);
		} else {
			stationDailyRegisterService.registerHandle(stationId, user.getPartnerId(), user.getType(), false);
		}
	}

	private Long getGroupId(Long groupId, Long stationId) {
		if (groupId != null) {
			SysUserGroup g = userGroupService.findOne(groupId, stationId);
			if (g == null) {
				throw new BaseException(BaseI18nCode.userGroupNotExist);
			}
			return groupId;
		} else {
			return userGroupService.getDefaultId(stationId);
		}
	}

	private Long getDegreeId(Long degreeId, Long stationId) {
		if (degreeId != null) {
			SysUserDegree d = userDegreeService.findOne(degreeId, stationId);
			if (d == null) {
				throw new BaseException(BaseI18nCode.defaultDegreeUnExist);
			}
			return d.getId();
		} else {
			return userDegreeService.getDefaultId(stationId);
		}
	}

	private void setAgentInfo(SysUser user, String agentName, Long stationId) {
		if (StringUtils.isEmpty(agentName)) {
			return;
		}
		Agent agent = agentService.findOneByName(agentName, stationId);
		if (agent == null) {
			throw new BaseException(BaseI18nCode.agentUnExist);
		}
		user.setAgentName(user.getAgentName());
	}

	@Override
	public void adminSaveProxy(UserRegisterBo rbo) {
		if (StringUtils.isEmpty(rbo.getUsername())) {
			throw new BaseException(BaseI18nCode.usernameCanntEmpty);
		}
		if (StringUtils.isEmpty(rbo.getPwd())) {
			throw new BaseException(BaseI18nCode.stationPasswordNotNull);
		}
		if (!ValidateUtil.isPassword(rbo.getPwd())) {
			throw new BaseException(BaseI18nCode.stationLoginPassMustZ);
		}
		Long stationId = SystemUtil.getStationId();
		String username = rbo.getUsername().trim().toLowerCase();
		if (!ValidateUtil.isUsername(username)) {
			throw new BaseException(BaseI18nCode.stationUserNameMustNum);
		}
		if (!StationConfigUtil.isOn(stationId, StationConfigEnum.allnumber_switch_when_register)) {
			if (ValidateUtil.isNumber(username)) {
				throw new BaseException(BaseI18nCode.stationNotIncludeNum);
			}
		}
		if (sysUserService.exist(username, stationId, null)) {
			throw new BaseException(BaseI18nCode.stationUserByRegist);
		}

		SysUser user = new SysUser();
		user.setPartnerId(SystemUtil.getPartnerId());
		user.setLevel(1);
		user.setType(UserTypeEnum.PROXY.getType());
		//no need setAgentInfo no data in the agent table
		//setAgentInfo(user, rbo.getAgentName(), stationId);
		SysUser proxy = setProxyInfo(user, rbo.getProxyName(), stationId);
		validatorRebate(proxy, rbo, stationId);
		user.setDegreeId(getDegreeId(rbo.getDegreeId(), stationId));
		user.setGroupId(getGroupId(rbo.getGroupId(), stationId));
		user.setStatus(Constants.STATUS_ENABLE);
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setUsername(username);
		user.setPassword(MD5Util.pwdMd5Member(username, rbo.getPwd(), user.getSalt()));

		user.setCreateDatetime(new Date());
		user.setCreateUserId(LoginAdminUtil.getUserId());
		user.setCreateUsername(LoginAdminUtil.getUsername());
		user.setStationId(stationId);
		user.setOnlineWarn(Constants.STATUS_DISABLE);
		user.setLockDegree(SysUser.DEGREE_UNLOCK);
		user.setUpdatePwdDatetime(user.getCreateDatetime());
		user.setMoneyStatus(SysUser.moneyStatusNormal);
		saveUserAllInfo(stationId, user, rbo);
		LogUtils.addLog("后台添加代理:" + user.getUsername());
	}

	/**
	 * 原生手机端可能存在获取不到正确系统的问题
	 * @param user
	 */
	private void setRegisterInfo(SysUser user) {
		HttpServletRequest request = ServletUtils.getRequest();
		if (request == null) {
			return;
		}
		user.setRegisterIp(IpUtils.getIp());
		String domain = ServletUtils.getDomainName(request.getRequestURL().toString());
		if (!StringUtils.contains(domain, SystemConfig.SYS_GENERAL_DOMAIN)) {
			user.setRegisterUrl(domain);
		} else {
			user.setRegisterUrl("--");
		}
		String uaStr = request.getHeader("User-Agent");
		if (StringUtils.isNotEmpty(uaStr)) {
			try {
				Parser uaParser = new Parser();
				Client c = uaParser.parse(uaStr);
				user.setRegisterOs(c.os.family);
			} catch (Exception e) {
				user.setRegisterOs(BaseI18nCode.unKnowExpection.getMessage());
			}
		}
	}

	/**
	 * 原生手机端可能存在获取不到正确系统的问题
	 * @param user
	 */
	private void setRegisterSystem(SysUser user) {
		HttpServletRequest request = ServletUtils.getRequest();
		if (request == null) {
			return;
		}
		String userAgent = request.getHeader("User-Agent");
		if (StringUtils.contains(userAgent, "ios/") || StringUtils.contains(userAgent, "android/")
				|| StringUtils.contains(userAgent, "wap/")) {
			user.setRegisterOs(userAgent);
		} else {
			int terminal = MobileUtil.isMoblie(ServletUtils.getRequest()) ? SysUserLogin.TERMINAL_WAP : SysUserLogin.TERMINAL_PC;
			String fingerprint = ServletUtils.getFingerprint(terminal);
			if (StringUtils.isNotEmpty(userAgent)) {
				userAgent += "-" + fingerprint;
				if (userAgent.length() > 200) {
					user.setRegisterOs(userAgent.substring(0, 200));
				} else {
					user.setRegisterOs(userAgent);
				}
			} else {
				user.setRegisterOs(userAgent);
			}
		}
	}

	private SysUser setProxyInfo(SysUser user, String proxyName, Long stationId) {
		// 代理验证
		if (StringUtils.isNotEmpty(proxyName)) {
			if (Objects.equals(user.getType(), UserTypeEnum.PROXY.getType())) {
				if (!ProxyModelUtil.isMultiOrAllProxy(stationId)) {
					// 不是全部代理，也不是多级代理 则当前是代理不能再添加代理
					throw new BaseException(BaseI18nCode.stationMutiProxy);
				}
			}
			SysUser proxy = sysUserService.findOneByUsername(proxyName, stationId, UserTypeEnum.PROXY.getType());
			if (proxy == null) {
				throw new BaseException(BaseI18nCode.proxyUnExist);
			}
			// 添加代理下级人数
			userProxyNumService.updateProxyNum(user.getType() == UserTypeEnum.PROXY.getType(), 1, proxy.getId());
			String proParentIds = StringUtils.isEmpty(proxy.getParentIds()) ? "," : proxy.getParentIds();
			String proParentNames = StringUtils.isEmpty(proxy.getParentNames()) ? "," : proxy.getParentNames();
			user.setProxyId(proxy.getId());
			user.setParentIds(proParentIds + proxy.getId() + ",");
			user.setParentNames(proParentNames + proxy.getUsername() + ",");
			user.setProxyName(proxy.getUsername());
			user.setLevel(proxy.getLevel() + 1);
			return proxy;
		}
		return null;
	}

	/**
	 * 下级代理的返点数，不能大于所属代理的返点数
	 *
	 * // * @param p
	 * @param proxy 上级代理
	 */
	private void validatorRebate(SysUser proxy, UserRegisterBo rbo, Long stationId) {
		StationRebate r = stationRebateService.findOne(stationId, StationRebate.USER_TYPE_PROXY);
		if (r.getType() == StationRebate.TYPE_SAME) {
			rbo.setLive(r.getLive());
			rbo.setEgame(r.getEgame());
			rbo.setChess(r.getChess());
			rbo.setEsport(r.getEsport());
			rbo.setSport(r.getSport());
			rbo.setFishing(r.getFishing());
			rbo.setLottery(r.getLottery());
			r.setSport(r.getSport());
			return;
		}
		// 获取当前用户的返点
		SysUserRebate scale = null;
		if (proxy != null) {
			scale = userRebateService.findOne(proxy.getId(), stationId);
		}
		ThirdGame game = thirdGameService.findOne(stationId);

		BigDecimal diff = r.getLevelDiff();
		BigDecimal maxDiff = r.getMaxDiff();
		rbo.setLive(BigDecimalUtil.absOr0(rbo.getLive()));
		rbo.setEgame(BigDecimalUtil.absOr0(rbo.getEgame()));
		rbo.setSport(BigDecimalUtil.absOr0(rbo.getSport()));
		rbo.setEsport(BigDecimalUtil.absOr0(rbo.getEsport()));
		rbo.setChess(BigDecimalUtil.absOr0(rbo.getChess()));
		rbo.setFishing(BigDecimalUtil.absOr0(rbo.getFishing()));
		rbo.setLottery(BigDecimalUtil.absOr0(rbo.getLottery()));
		// 判断返点值是否正确
		if (Objects.equals(game.getSport(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getSport(), scale == null ? r.getSport() : scale.getSport(), diff, maxDiff,
					HotGameTypeEnum.sport.getTitle());
		}
		if (Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getEgame(), scale == null ? r.getEgame() : scale.getEgame(), diff, maxDiff,
					HotGameTypeEnum.dianzi.getTitle());
		}
		if (Objects.equals(game.getLive(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getLive(), scale == null ? r.getLive() : scale.getLive(), diff, maxDiff,
					HotGameTypeEnum.real.getTitle());
		}
		if (Objects.equals(game.getChess(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getChess(), scale == null ? r.getChess() : scale.getChess(), diff, maxDiff,
					HotGameTypeEnum.qipai.getTitle());
		}
		if (Objects.equals(game.getFishing(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getFishing(), scale == null ? r.getFishing() : scale.getFishing(), diff,
					maxDiff, HotGameTypeEnum.buyu.getTitle());
		}
		if (Objects.equals(game.getEsport(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getEsport(), scale == null ? r.getEsport() : scale.getEsport(), diff,
					maxDiff, HotGameTypeEnum.dianjing.getTitle());
		}
		if (Objects.equals(game.getLottery(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(rbo.getLottery(), scale == null ? r.getLottery() : scale.getLottery(), diff,
					maxDiff, HotGameTypeEnum.caipiao.getTitle());
		}
	}

	private void validEmail(String activeCode, String email) {
		if (StringUtils.isEmpty(email)) {
			throw new BaseException(BaseI18nCode.stationEmailNotNull);
		}
		if (StringUtils.isEmpty(activeCode)) {
			throw new BaseException(BaseI18nCode.captchaError);
		}
//      String decryptEmail = AESUtil.decrypt(email, Constants.DEFAULT_KEY, Constants.DEFAULT_IV);
		if (SystemConfig.EMAIL_CODE.equals(activeCode)) {
			return;
		}
		String key = new StringBuilder("activeEmail:email:").append(email).append(":").append("sid:").append(SystemUtil.getStationId()).toString();
		String savedActiveCode = RedisAPI.getCache(key);
	//	logger.error("email active code = " + savedActiveCode);
		if (StringUtils.isEmpty(savedActiveCode) || !savedActiveCode.equalsIgnoreCase(activeCode)) {
			throw new BaseException(BaseI18nCode.captchaError);
		}
    }

	private void validPhoneSms(String vcode, String phone) {
		if (StringUtils.isEmpty(phone)) {
			throw new BaseException(BaseI18nCode.stationMobileNotNull);
		}
		if (StringUtils.isEmpty(vcode)) {
			throw new BaseException(BaseI18nCode.captchaError);
		}
		if (SystemConfig.SMS_CODE.equals(vcode)) {
			return;
		}
		String key = new StringBuilder("reqSmsCode:phone:").append(phone).append(":").append("sid:").append(SystemUtil.getStationId()).toString();
		String savedActiveCode = RedisAPI.getCache(key);
	//	logger.error("sms active code = " + savedActiveCode);
		if (StringUtils.isEmpty(savedActiveCode) || !savedActiveCode.equalsIgnoreCase(vcode)) {
			throw new BaseException(BaseI18nCode.captchaError);
		}
	}

	@Override
	public void doEmailRegister(UserRegisterBo rbo) {
		try {
			// 验证是否是黑名单
			Long stationId = SystemUtil.getStationId();
			if (StringUtils.isNotEmpty(rbo.getCaptcha())) {
				VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_REGISTER_KEY, rbo.getCaptcha());
			}
			// 验证当前ip注册会员个数
			validatorIpRegisterNumToday(stationId, IpUtils.getIp(), null);
			validatorOsRegisterNumToday(stationId, LogUtils.getOs(), null);
			checkPromotionCode(rbo, stationId);
			validEmail(rbo.getCode(), rbo.getEmail());
			if (StringUtils.isEmpty(rbo.getUsername())) {
				rbo.setUsername(rbo.getEmail());
			}
			//随机生成一串UID，方便用户用于登陆
			String ranUserID = RandomStringUtils.randomInt(12);
			rbo.setUid(ranUserID);

			getRegisterTerminal(rbo);//根据user-agent判断终端类型
			SysUser sa = checkEmailRegisterBo(rbo);
			setRegisterSystem(sa);
			memberRegisterSetProxyInfo(sa, rbo, stationId);
			// 保存层级
			sa.setDegreeId(userDegreeService.getDefaultId(stationId));
			sa.setGroupId(userGroupService.getDefaultId(stationId));
			sa.setUserRegisterType(SysUserRegisterTypeEnum.Email.getType());
			sysUserService.save(sa);
			userInfoService.init(sa, rbo);
			betNumService.init(sa.getId(), stationId);// 保存打码
			userDepositService.init(sa.getId(), stationId);
			userMoneyService.init(sa.getId(), BigDecimal.ZERO);
			userScoreService.init(sa.getId(), stationId);
			userLoginService.initForRegister(sa, rbo.getTerminal());
			userWithdrawService.init(sa.getId(), stationId);
			userPermService.init(sa.getId(), stationId,
					StringUtils.isNotEmpty(sa.getEmail())?sa.getEmail():sa.getUsername(), sa.getType());
			if (UserTypeEnum.PROXY.getType() == sa.getType()) {// 如果是代理，新增代理返点表,新增代理下级数量表
				userProxyNumService.init(sa.getId());
				userRebateService.init(sa.getId(), stationId, rbo.getLive(), rbo.getEgame(), rbo.getChess(),
						rbo.getSport(), rbo.getEsport(), rbo.getFishing(), rbo.getLottery());
			}
			// 添加站点每日注册信息表
			stationDailyRegisterService.registerHandle(stationId, sa.getPartnerId(), sa.getType(),
					sa.getProxyId() != null);
			OnlineManager.doOnline(sa, Constants.SESSION_KEY_MEMBER);
			// 发送站内信
			String msg = StationConfigUtil.get(stationId, StationConfigEnum.member_register_send_msg_text);
			if (StringUtils.isNotEmpty(msg)) {

				messageService.sysMessageSend(stationId, sa.getId(), sa.getUsername(),
						I18nTool.convertCodeToArrStr(BaseI18nCode.stationSuccRegist.getCode()), I18nTool.convertCodeToArrStr(msg),
						Constants.STATUS_ENABLE);
			}
			SysThreadVariable.get().setUser(sa);
			// 保存登录日志
			LogUtils.loginLog(sa, true, "");
			LogUtils.addUserLog("用户注册:" + sa.getUsername());
			//给会员赠送注册彩金，读取注册赠送策略
			rollRegAwardForUser(sa);
		} catch (Exception e) {
			logger.error("注册发生错误", e);
			throw e;
		}
	}

	@Override
	public void doSmsRegister(UserRegisterBo rbo) {
		try {
			// 验证是否是黑名单
			Long stationId = SystemUtil.getStationId();
			if (StringUtils.isNotEmpty(rbo.getCaptcha())) {
				VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_REGISTER_KEY, rbo.getCaptcha());
			}
			// 验证当前ip注册会员个数
			validatorIpRegisterNumToday(stationId, IpUtils.getIp(), null);
			validatorOsRegisterNumToday(stationId, LogUtils.getOs(), null);
			checkPromotionCode(rbo, stationId);
			validPhoneSms(rbo.getCode(), rbo.getPhone());
			if (StringUtils.isEmpty(rbo.getUsername())) {
				rbo.setUsername(rbo.getPhone());
			}
			//随机生成一串UID，方便用户用于登陆
			String ranUserID = RandomStringUtils.randomInt(10);
			rbo.setUid(ranUserID);
			getRegisterTerminal(rbo);//根据user-agent判断终端类型
			SysUser sa = checkSmsRegisterBo(rbo);
			setRegisterSystem(sa);
			memberRegisterSetProxyInfo(sa, rbo, stationId);
			// 保存层级
			sa.setDegreeId(userDegreeService.getDefaultId(stationId));
			sa.setGroupId(userGroupService.getDefaultId(stationId));
			sa.setUserRegisterType(SysUserRegisterTypeEnum.Sms.getType());
			sysUserService.save(sa);
			userInfoService.init(sa, rbo);
			betNumService.init(sa.getId(), stationId);// 保存打码
			userDepositService.init(sa.getId(), stationId);
			userMoneyService.init(sa.getId(), BigDecimal.ZERO);
			userScoreService.init(sa.getId(), stationId);
			userLoginService.initForRegister(sa, rbo.getTerminal());
			userWithdrawService.init(sa.getId(), stationId);
			userPermService.init(sa.getId(), stationId,
					StringUtils.isNotEmpty(sa.getEmail())?sa.getEmail():sa.getUsername(), sa.getType());
			if (UserTypeEnum.PROXY.getType() == sa.getType()) {// 如果是代理，新增代理返点表,新增代理下级数量表
				userProxyNumService.init(sa.getId());
				userRebateService.init(sa.getId(), stationId, rbo.getLive(), rbo.getEgame(), rbo.getChess(),
						rbo.getSport(), rbo.getEsport(), rbo.getFishing(), rbo.getLottery());
			}
			// 添加站点每日注册信息表
			stationDailyRegisterService.registerHandle(stationId, sa.getPartnerId(), sa.getType(),
					sa.getProxyId() != null);
			OnlineManager.doOnline(sa, Constants.SESSION_KEY_MEMBER);
			// 发送站内信
			String msg = StationConfigUtil.get(stationId, StationConfigEnum.member_register_send_msg_text);
			if (StringUtils.isNotEmpty(msg)) {

				messageService.sysMessageSend(stationId, sa.getId(), sa.getUsername(),
						I18nTool.convertCodeToArrStr(BaseI18nCode.stationSuccRegist.getCode()), I18nTool.convertCodeToArrStr(msg),
						Constants.STATUS_ENABLE);
			}
			SysThreadVariable.get().setUser(sa);
			// 保存登录日志
			LogUtils.loginLog(sa, true, "");
			LogUtils.addUserLog("用户注册:" + sa.getUsername());
			//给会员赠送注册彩金，读取注册赠送策略
			rollRegAwardForUser(sa);
		} catch (Exception e) {
			logger.error("短信注册发生错误", e);
			throw e;
		}
	}

	private void getRegisterTerminal(UserRegisterBo rbo){
		String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
	//	logger.error("native/wap 登录 user-agent:" + userAgent);
		if (StringUtils.contains(userAgent, "ios/")) {
			rbo.setTerminal(SysUserLogin.TERMINAL_APP_IOS);
		}else if (StringUtils.contains(userAgent, "android/")) {
			rbo.setTerminal(SysUserLogin.TERMINAL_APP_ANDROID);
		}else if (StringUtils.contains(userAgent, "wap/")) {
			rbo.setTerminal(SysUserLogin.TERMINAL_WAP);
		}else{
			if (!MobileUtil.isMoblie(ServletUtils.getRequest())) {
				rbo.setTerminal(SysUserLogin.TERMINAL_PC);
			}else{
				rbo.setTerminal(SysUserLogin.TERMINAL_WAP);
			}
		}
	}

	@Override
	public void doRegisterMember(UserRegisterBo rbo) {
		try {
			// 验证是否是黑名单
			Long stationId = SystemUtil.getStationId();
			VerifyCodeUtil.validateLine(VerifyCodeUtil.VERIFY_CODE_LINE_KEY, rbo.getLine());
			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_REGISTER_KEY, rbo.getCaptcha());
			// 验证当前ip注册会员个数
			validatorIpRegisterNumToday(stationId, IpUtils.getIp(), null);
			validatorOsRegisterNumToday(stationId, LogUtils.getOs(), null);
			checkPromotionCode(rbo, stationId);
			getRegisterTerminal(rbo);//根据user-agent判断终端类型
			SysUser sa = checkUserRegisterBo(rbo);
			setRegisterSystem(sa);
			memberRegisterSetProxyInfo(sa, rbo, stationId);
			// 保存层级
			sa.setDegreeId(userDegreeService.getDefaultId(stationId));
			sa.setGroupId(userGroupService.getDefaultId(stationId));
			sa.setUserRegisterType(SysUserRegisterTypeEnum.Local.getType());
			sysUserService.save(sa);

			userInfoService.init(sa, rbo);
			betNumService.init(sa.getId(), stationId);// 保存打码
			userDepositService.init(sa.getId(), stationId);
			userMoneyService.init(sa.getId(), BigDecimal.ZERO);
			userScoreService.init(sa.getId(), stationId);
			userLoginService.initForRegister(sa, rbo.getTerminal());
			userWithdrawService.init(sa.getId(), stationId);
			userPermService.init(sa.getId(), stationId, sa.getUsername(), sa.getType());
			if (UserTypeEnum.PROXY.getType() == sa.getType()) {// 如果是代理，新增代理返点表,新增代理下级数量表
				userProxyNumService.init(sa.getId());
				userRebateService.init(sa.getId(), stationId, rbo.getLive(), rbo.getEgame(), rbo.getChess(),
						rbo.getSport(), rbo.getEsport(), rbo.getFishing(), rbo.getLottery());
			}
			// 添加站点每日注册信息表
			stationDailyRegisterService.registerHandle(stationId, sa.getPartnerId(), sa.getType(),
					sa.getProxyId() != null);
			OnlineManager.doOnline(sa, Constants.SESSION_KEY_MEMBER);
			// 发送站内信
			String msg = StationConfigUtil.get(stationId, StationConfigEnum.member_register_send_msg_text);
			if (StringUtils.isNotEmpty(msg)) {

				messageService.sysMessageSend(stationId, sa.getId(), sa.getUsername(),
						I18nTool.convertCodeToArrStr(BaseI18nCode.stationSuccRegist.getCode()), I18nTool.convertCodeToArrStr(msg),
						Constants.STATUS_ENABLE);
			}
			SysThreadVariable.get().setUser(sa);
			// 保存登录日志
			LogUtils.loginLog(sa, true, "");
			LogUtils.addUserLog("用户注册:" + sa.getUsername());
			//给会员赠送注册彩金，读取注册赠送策略
			rollRegAwardForUser(sa);
			//注册成功后，自动生成一条推广链接 代理/会员推广链接
			if (StationConfigUtil.isOn(stationId, StationConfigEnum.auto_generate_link_register)) {
				promotionService.autoGenerateLink(sa);
			}
		} catch (Exception e) {
			logger.error("注册发生错误", e);
			throw e;
		}
	}
	
	@Override
	public void doRegisterMemberGuest(Long stationId, UserRegisterBo rbo) {

		// 验证当前ip注册会员个数
		validatorIpRegisterNumToday(stationId, IpUtils.getIp(), null);
		validatorOsRegisterNumToday(stationId, LogUtils.getOs(), null);

		//根据user-agent判断终端类型
		getRegisterTerminal(rbo);

		// 注册试玩账户
		rbo.setUsername("guest" + RandomStringUtils.getCode(5));
		rbo.setPwd(SystemConfig.INIT_SHIWAN_PWD);
		rbo.setUserType(UserTypeEnum.GUEST.getType());
		SysUser userGuest = this.saveUserGuest(stationId, rbo);

		// 试玩用户登录
		userLoginService.updateUserLoginStatus(userGuest, IpUtils.getIp(), Constants.SESSION_KEY_MEMBER, rbo.getTerminal());
		SysThreadVariable.get().setUser(userGuest);

		// 添加日志
		LogUtils.addUserLog("前台试玩注册账号:" + userGuest.getUsername());
	}

	/**
	 * 会员注册赠送
	 * @param user   当前注册用户
	 */
	private void rollRegAwardForUser(SysUser user) {
		boolean regGiftSwitch = StationConfigUtil.isOff(SystemUtil.getStationId(), StationConfigEnum.register_gift_switch);
		if (regGiftSwitch) {
			return;
		}
		if (user == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		List<StationRegisterStrategy> strategyList = stationRegisterStrategyService.filter(user, user.getCreateDatetime());
		if (strategyList != null && !strategyList.isEmpty()) {
			for (StationRegisterStrategy ds : strategyList) {
				BigDecimal amount = null;
				BigDecimal backAmount = null;
				BigDecimal curDrawNeed = BigDecimal.ZERO;
				// 固定数额
				if (ds.getGiftType()!=null && ds.getGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
					amount = ds.getGiftValue();
				}
				if (ds.getBackGiftType()!=null && ds.getBackGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
					backAmount = ds.getBonusBackValue();
				} else {// 浮动比例
					BigDecimal money = ds.getGiftValue() != null ? ds.getGiftValue() : BigDecimal.ZERO;
					backAmount = money.multiply(ds.getBonusBackValue()).divide(BigDecimalUtil.HUNDRED).setScale(4,
							BigDecimal.ROUND_FLOOR);
				}
				if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
					if (ds.getValueType() == StationDepositStrategy.VALUE_TYPE_MONEY) {// 赠送彩金
						MnyMoneyBo mvo = new MnyMoneyBo();
						mvo.setUser(user);
						mvo.setMoney(amount);
						mvo.setMoneyRecordType(MoneyRecordType.REGISTER_GIFT_BACK);
						if (ds.getGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
							List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.registerRewardBonus.getCode(), "：", String.valueOf(mvo.getMoney()),
									"（", BaseI18nCode.fixedNum.getCode(), "：", String.valueOf(ds.getGiftValue()), "）");
							mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
						} else {
							List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.registerRewardBonus.getCode(), "：", String.valueOf(mvo.getMoney()),
									"（", BaseI18nCode.floatScale.getCode(), "：", String.valueOf(ds.getGiftValue()), "%）");
							mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
						}
						mvo.setBizDatetime(user.getCreateDatetime());
						SysUserMoneyHistory r = sysUserMoneyService.updMnyAndRecord(mvo);
						if (ds.getBetRate() != null) {
							// 根据打码量倍数得到打码量
							curDrawNeed = amount.multiply(ds.getBetRate()).setScale(0, RoundingMode.UP);
						}
						//注册赠送的奖金也存入奖金表
						saveRegisterAwardBonus(user, amount, r.getId(), mvo.getMoneyRecordType(),null);
					} else {// 赠送积分
						List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.registerRewardPoint.getCode(), "：", String.valueOf(amount.longValue()));
						sysUserScoreService.operateScore(ScoreRecordTypeEnum.REGISTER_GIFT, user, amount.longValue(), I18nTool.convertCodeToArrStr(remarkList));
					}
					if (curDrawNeed == null || curDrawNeed.compareTo(BigDecimal.ZERO) == 0) {
						return;
					}
					List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.registerRewardBonus.getCode());
					// 更新会员提款的判断所需要的数据
					sysUserBetNumService.updateDrawNeed(user.getId(), user.getStationId(), curDrawNeed, BetNumTypeEnum.registGift.getType(), I18nTool.convertCodeToArrStr(remarkList), null);
				}
				//给上级代理或推荐人返佣
				if (backAmount != null && backAmount.compareTo(BigDecimal.ZERO) > 0) {
					if (ds.getValueType() == StationDepositStrategy.VALUE_TYPE_MONEY) {// 赠送彩金
						try {
							awardBonusForProxy(ds, user, backAmount);
						} catch (Exception e) {
							logger.error("register awardBonusForProxy exception, username:{}, stationId:{}, strategyId:{}",
									user.getUsername(), user.getStationId(), ds.getId(), e);
						}
					}
				}
			}
		}

	}

	/**
	 * 给上级代理或推荐人返佣
	 *
	 * @param ds     使用的注册赠送策略
	 * @param user   当前充值用户
	 * @param amount 返佣额度
	 */
	private void awardBonusForProxy(StationRegisterStrategy ds, SysUser user, BigDecimal amount) {
		boolean backToProxy = StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.activity_backmoney_proxy);
		if (!backToProxy) {
			logger.error("register awardBonusForProxy, the switch is off");
			return;
		}
		//判断当前存款用户的ip是否存在多个其他同IP注册进来的会员，如果超过则不返佣金给上级
		String sameip_limit_back_bonus = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.sameip_limit_back_bonus);
		if (StringUtils.isNotEmpty(sameip_limit_back_bonus)) {
			int userNum = sysUserDao.countRegisterMemberByIp(user.getStationId(), null, null, user.getRegisterIp(), null);
			if (userNum > Integer.parseInt(sameip_limit_back_bonus)) {
				logger.error("register awardBonusForProxy, the same ip limit, username:{}, stationId:{}, userNum:{}",
						user.getUsername(), user.getStationId(), userNum);
				return;
			}
		}
		//判断当前存款用户的设备是否存在多个其他同设备注册进来的会员，如果超过则不返佣金给上级
		String sameos_limit_back_bonus = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.sameos_limit_back_bonus);
		if (StringUtils.isNotEmpty(sameos_limit_back_bonus)) {
			int userOsNum = sysUserDao.countRegisterMemberByOs(user.getStationId(), null, null, user.getRegisterOs(), null);
			if (userOsNum > Integer.parseInt(sameos_limit_back_bonus)) {
				logger.error("register awardBonusForProxy, the same os limit, username:{}, stationId:{}, userOsNum:{}",
						user.getUsername(), user.getStationId(), userOsNum);
				return;
			}
		}
		BigDecimal curDrawNeed = BigDecimal.ZERO;
		SysUser proxy = null;
		List<String> remarkList = new ArrayList<>();
		if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
			if (user.getRecommendId() != null) {
				proxy = sysUserDao.findOneById(user.getRecommendId(), user.getStationId());
			} else if (user.getProxyId() != null) {
				proxy = sysUserDao.findOneById(user.getProxyId(), user.getStationId());
			}
			if (proxy == null) {
				logger.error("register awardBonusForProxy, no proxy or recommend, username:{}, stationId{}, proxyId:{}, recommendId{}",
						user.getUsername(), user.getStationId(), user.getProxyId(), user.getRecommendId());
//				throw new BaseException(BaseI18nCode.noSuperiorUsers);
				return;
			}
			if (ds.getValueType() == StationDepositStrategy.VALUE_TYPE_MONEY) {// 赠送彩金
				MnyMoneyBo mvo = new MnyMoneyBo();
				mvo.setUser(proxy);
				mvo.setMoney(amount);
				mvo.setMoneyRecordType(MoneyRecordType.INVITE_REG_GIFT_BACK);
				if (ds.getBackGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
					I18nTool.convertCodeToList(remarkList, BaseI18nCode.registerRewardBonusRebate.getCode(), "：", user.getUsername(), "：", String.valueOf(mvo.getMoney()),
							"（", BaseI18nCode.fixedNum.getCode(), "：", String.valueOf(ds.getBonusBackValue()), "）");
					mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
				} else {
					I18nTool.convertCodeToList(remarkList, BaseI18nCode.depositRewardBonusRebate.getCode(), "：", user.getUsername(), "：", String.valueOf(mvo.getMoney()),
							"（", BaseI18nCode.floatScale.getCode(), "：", String.valueOf(ds.getBonusBackValue()), "%）");
					mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
				}
				mvo.setBizDatetime(new Date());
				SysUserMoneyHistory r = sysUserMoneyService.updMnyAndRecord(mvo);
				if (ds.getBonusBackBetRate() != null) {
					// 根据打码量倍数得到打码量
					curDrawNeed = amount.multiply(ds.getBonusBackBetRate()).setScale(0, RoundingMode.UP);
				}
				//给上级返佣的奖金也存入奖金表
				saveRegisterAwardBonus(proxy, amount,r.getId(),mvo.getMoneyRecordType(),null);
			} else {// 赠送积分
				I18nTool.convertCodeToList(remarkList, BaseI18nCode.registerRewardPointRebate.getCode(), "：", user.getUsername(), "：", String.valueOf(amount.longValue()));
				sysUserScoreService.operateScore(ScoreRecordTypeEnum.DEPOSIT_GIFT_ACTIVITY, proxy, amount.longValue(), I18nTool.convertCodeToArrStr(remarkList));
			}
		} else {
			amount = BigDecimal.ZERO;
			if (ds.getBonusBackBetRate() != null) {
				// 根据打码量倍数得到打码量
				curDrawNeed = amount.multiply(ds.getBonusBackBetRate()).setScale(0, RoundingMode.UP);
			}
		}
		if (curDrawNeed == null || curDrawNeed.compareTo(BigDecimal.ZERO) == 0 || proxy == null) {
			return;
		}
		// 更新会员提款的判断所需要的数据
		sysUserBetNumService.updateDrawNeed(proxy.getId(), proxy.getStationId(), curDrawNeed,
				BetNumTypeEnum.inviteRebate.getType(), I18nTool.convertCodeToArrStr(remarkList), null);
	}

	private void saveRegisterAwardBonus(SysUser user, BigDecimal amount, Long moneyHistoryId, MoneyRecordType moneyRecordType, Long recordId) {
		SysUserBonus sysUserBonus = new SysUserBonus();
		sysUserBonus.setMoney(amount);
		sysUserBonus.setUserId(user.getId());
		sysUserBonus.setRecordId(recordId);
		sysUserBonus.setCreateDatetime(new Date());
		sysUserBonus.setUsername(user.getUsername());
		sysUserBonus.setRecommendId(user.getRecommendId());
		sysUserBonus.setProxyId(user.getProxyId());
		sysUserBonus.setBonusType(moneyRecordType.getType());
		sysUserBonus.setProxyName(user.getProxyName());
		sysUserBonus.setStationId(user.getStationId());
		sysUserBonus.setUserType(user.getType());
		sysUserBonus.setDailyMoneyId(moneyHistoryId);
		sysUserBonusService.saveBonus(sysUserBonus);
	}

	/**
	 * 检测推广码和代理返点
	 *
	 * @param rbo
	 * @param stationId
	 */
	private void checkPromotionCode(UserRegisterBo rbo, Long stationId) {
		try {
			int proxyModel = ProxyModelUtil.getProxyModel(stationId);
			boolean allProxy = ProxyModelUtil.isAllProxy(proxyModel);
			boolean allMember = ProxyModelUtil.isAllMember(proxyModel);
			if (allProxy) {
				// 如果是全代理模式，注册后就是代理，否则根据推广码来判断用户类型
				rbo.setUserType(UserTypeEnum.PROXY.getType());
			} else if (allMember) {
				rbo.setUserType(UserTypeEnum.MEMBER.getType());
			} else {
				if (rbo.getUserType() == null || (rbo.getUserType() != UserTypeEnum.MEMBER.getType()
						&& rbo.getUserType() != UserTypeEnum.PROXY.getType())) {
					// 用户类型为空，或者不是user 且不是proxy ，则设置为user
					rbo.setUserType(UserTypeEnum.MEMBER.getType());
				}
			}
			StationDomain domain = SystemUtil.getDomain();
			if (StringUtils.isNotEmpty(rbo.getAgentPromoCode())) {
				Agent agent = agentService.findOneByPromoCode(rbo.getAgentPromoCode(), stationId);
				if (agent == null) {
					throw new BaseException(BaseI18nCode.agentUnExist);
				}
				rbo.setAgentName(agent.getName());
			}
			if (StringUtils.isEmpty(rbo.getAgentName())) {
				// 域名是否绑定了默认代理商
				rbo.setAgentName(domain.getAgentName());
			}
			if (StringUtils.isNotEmpty(rbo.getPromoCode())) {// 存在推广码，则获取推广链接信息
				StationPromotion p = promotionService.findOneByCode(rbo.getPromoCode(), stationId);
				if (p == null) {
					throw new ParamException(BaseI18nCode.stationExtendNotLink);
				}
				if ((allProxy && p.getType() != UserTypeEnum.PROXY.getType())
						|| (allMember && p.getType() != UserTypeEnum.MEMBER.getType())) {
					throw new BaseException(BaseI18nCode.stationPushLinkNotRig);
				}
				rbo.setUserType(p.getType());
				// 添加注册人数
				promotionService.addNum(p.getId(), stationId, 1, true);
				if (p.getMode() == StationPromotion.MODE_MEMBER) {
					rbo.setRecommendId(p.getUserId());
					rbo.setRecommendUsername(p.getUsername());
				} else {
					if (ProxyModelUtil.isOneProxy(proxyModel) && rbo.getUserType() == UserTypeEnum.PROXY.getType()) {
						throw new BaseException(BaseI18nCode.stationOneProxyReg);
					}
					rbo.setProxyId(p.getUserId());
					rbo.setProxyName(p.getUsername());
					// 保存推广链接的代理返点信息
					if (p.getType() == UserTypeEnum.PROXY.getType()) {
						rbo.setLive(p.getLive());
						rbo.setSport(p.getSport());
						rbo.setEgame(p.getEgame());
						rbo.setChess(p.getChess());
						rbo.setLottery(p.getLottery());
						rbo.setEsport(p.getEsport());
						rbo.setFishing(p.getFishing());
					}
				}
			} else if (domain.getProxyId() != null) {// 域名代理不为空时
				SysUser proxy = sysUserService.findOneById(domain.getProxyId(), stationId);
				if (proxy == null) {
					throw new BaseException(BaseI18nCode.proxyUnExist);
				}
				rbo.setProxyId(proxy.getId());
				rbo.setProxyName(proxy.getUsername());
				if (StationConfigUtil.isOn(stationId, StationConfigEnum.domain_proxy_bind_promo_lind)) {// 域名代理默认绑定代理最新推广码
					StationPromotion link = promotionService.findOneNewest(proxy.getId(), stationId);
					if (link != null) {
						if ((allProxy && link.getType() != UserTypeEnum.PROXY.getType())
								|| (allMember && link.getType() != UserTypeEnum.MEMBER.getType())) {
							throw new BaseException(BaseI18nCode.stationPushLinkNotRig);
						}
						rbo.setPromoCode(link.getCode());
						rbo.setUserType(link.getType());
						// 添加注册人数
						promotionService.addNum(link.getId(), stationId, 1, true);
						// 保存推广链接的代理返点信息
						if (link.getType() == UserTypeEnum.PROXY.getType()) {
							rbo.setLive(link.getLive());
							rbo.setSport(link.getSport());
							rbo.setEgame(link.getEgame());
							rbo.setChess(link.getChess());
							rbo.setLottery(link.getLottery());
							rbo.setEsport(link.getEsport());
							rbo.setFishing(link.getFishing());
						}
					}
				} else {// 没有使用推广码，直接使用当前代理的返点减去返点层级差
					if (rbo.getUserType() == UserTypeEnum.PROXY.getType()) {
						StationRebate r = stationRebateService.findOne(stationId, StationRebate.USER_TYPE_PROXY);
						SysUserRebate scale = userRebateService.findOne(domain.getProxyId(), stationId);
						if (scale.getSport() != null && scale.getSport().compareTo(BigDecimal.ZERO) > 0) {
							rbo.setSport(scale.getSport().subtract(r.getLevelDiff()));
						}
						if (scale.getLive() != null && scale.getLive().compareTo(BigDecimal.ZERO) > 0) {
							rbo.setLive(scale.getLive().subtract(r.getLevelDiff()));
						}
						if (scale.getEgame() != null && scale.getEgame().compareTo(BigDecimal.ZERO) > 0) {
							rbo.setEgame(scale.getEgame().subtract(r.getLevelDiff()));
						}
						if (scale.getChess() != null && scale.getChess().compareTo(BigDecimal.ZERO) > 0) {
							rbo.setChess(scale.getChess().subtract(r.getLevelDiff()));
						}
						if (scale.getLottery() != null && scale.getLottery().compareTo(BigDecimal.ZERO) > 0) {
							rbo.setLottery(scale.getLottery().subtract(r.getLevelDiff()));
						}
						if (scale.getFishing() != null && scale.getFishing().compareTo(BigDecimal.ZERO) > 0) {
							rbo.setFishing(scale.getFishing().subtract(r.getLevelDiff()));
						}
						if (scale.getEsport() != null && scale.getEsport().compareTo(BigDecimal.ZERO) > 0) {
							rbo.setEsport(scale.getEsport().subtract(r.getLevelDiff()));
						}
					}
				}
				if (ProxyModelUtil.isOneProxy(proxyModel) && rbo.getUserType() == UserTypeEnum.PROXY.getType()) {
					throw new BaseException(BaseI18nCode.stationOneProxyReg);
				}
			} else {
				if (rbo.getUserType() == UserTypeEnum.PROXY.getType()) {
					StationRebate r = stationRebateService.findOne(stationId, StationRebate.USER_TYPE_PROXY);
					if (r != null) {
						rbo.setEgame(r.getEgame());
						rbo.setSport(r.getSport());
						rbo.setLive(r.getLive());
						rbo.setChess(r.getChess());
						rbo.setFishing(r.getFishing());
						rbo.setEsport(r.getEsport());
						rbo.setLottery(r.getLottery());
					}
				}
			}
		} catch (Exception e) {
			logger.error("注册验证推广链接发送错误", e);
			throw e;
		}
	}

	/**
	 * 验证当前IP当天注册数量
	 */
	private void validatorIpRegisterNumToday(Long stationId, String ip, Integer userType) {
		boolean history = false;
		String maxRegisterNum = StationConfigUtil.get(stationId, StationConfigEnum.same_ip_register_num);
		if (StringUtils.isEmpty(maxRegisterNum)) {
			maxRegisterNum = StationConfigUtil.get(stationId, StationConfigEnum.same_ip_history_register_num);
			if(StringUtils.isEmpty(maxRegisterNum)) {
				return;
			}
			history = true;
		}
		Long maxNum = (userType != null && GuestTool.isGuest(userType) ? Constants.MAX_SAME_IP_TRAIL_NUM : Long.parseLong(maxRegisterNum));
		Date begin = history? null : DateUtil.toDatetime(DateUtil.toDateStr(new Date()) + " 00:00:00");
		Date end = history? null : DateUtil.toDatetime(DateUtil.toDateStr(new Date()) + " 23:59:59");
		int reNum = sysUserService.countRegisterMemberByIp(stationId, begin, end, ip, userType);
		if (reNum >= maxNum) {
			throw new UnauthorizedException(history? BaseI18nCode.stationAllIpPersionNumSpeedLimit : BaseI18nCode.stationIpPersionNumSpeedLimit);
		}
	}

	/**
	 * 验证当前设备当天注册数量
	 */
	private void validatorOsRegisterNumToday(Long stationId, String os, Integer userType) {
		boolean history = false;
		String maxRegisterNum = StationConfigUtil.get(stationId, StationConfigEnum.same_os_register_num);
		if (StringUtils.isEmpty(maxRegisterNum)) {
			maxRegisterNum = StationConfigUtil.get(stationId, StationConfigEnum.same_os_history_register_num);
			if(StringUtils.isEmpty(maxRegisterNum)) {
				return;
			}
			history = true;
		}
		int maxNum = (userType != null && GuestTool.isGuest(userType)) ? Constants.MAX_SAME_OS_TRAIL_NUM : Integer.parseInt(maxRegisterNum);
		Date begin = history? null : DateUtil.toDatetime(DateUtil.toDateStr(new Date()) + " 00:00:00");
		Date end = history? null : DateUtil.toDatetime(DateUtil.toDateStr(new Date()) + " 23:59:59");
		int reNum = sysUserService.countRegisterMemberByOs(stationId, begin, end, os, userType);
		if (reNum >= maxNum) {
			throw new UnauthorizedException(history? BaseI18nCode.stationAllOsPersionNumSpeedLimit : BaseI18nCode.stationOsPersionNumSpeedLimit);
		}
	}

	private SysUser checkEmailRegisterBo(UserRegisterBo rbo) {
		String password = rbo.getPwd();
		if (StringUtils.isEmpty(password)) {
			throw new BaseException(BaseI18nCode.stationPasswordNotNull);
		}
		if (!ValidateUtil.isPassword(password)) {
			throw new BaseException(BaseI18nCode.stationLoginPassMustZ);
		}
		if (StringUtils.isEmpty(rbo.getEmail())) {
			throw new BaseException(BaseI18nCode.stationEmailNotNull);
		}
		Long stationId = SystemUtil.getStationId();
		if (!ValidateUtil.isEmail(rbo.getEmail())) {
			throw new BaseException(BaseI18nCode.stationEmailFormatError);
		}
		if (sysUserService.existByEmail(rbo.getEmail(), stationId, null)) {
			throw new BaseException(BaseI18nCode.stationEmailByUsed);
		}
		SysUser sa = new SysUser();
		sa.setStatus(Constants.STATUS_ENABLE);
		sa.setSalt(RandomStringUtils.randomAll(10));
		sa.setUsername(rbo.getUsername());
		sa.setUid(rbo.getUid());
		sa.setEmail(rbo.getEmail());
		sa.setPassword(MD5Util.pwdMd5Member(rbo.getUsername(), password, sa.getSalt()));
		sa.setType(rbo.getUserType());
		sa.setCreateDatetime(new Date());
		sa.setCreateUserId(0L);
		sa.setStationId(stationId);
		sa.setRegisterIp(IpUtils.getIp());
		sa.setPromoCode(rbo.getPromoCode());
		sa.setOnlineWarn(Constants.STATUS_DISABLE);
		sa.setLockDegree(SysUser.DEGREE_UNLOCK);
		sa.setMoneyStatus(SysUser.moneyStatusNormal);
		sa.setPartnerId(SystemUtil.getPartnerId());
		sa.setUpdatePwdDatetime(sa.getCreateDatetime());
		return sa;
	}

	private SysUser checkSmsRegisterBo(UserRegisterBo rbo) {
		String password = rbo.getPwd();
		if (StringUtils.isEmpty(password)) {
			throw new BaseException(BaseI18nCode.stationPasswordNotNull);
		}
		if (!ValidateUtil.isPassword(password)) {
			throw new BaseException(BaseI18nCode.stationLoginPassMustZ);
		}
		if (StringUtils.isEmpty(rbo.getPhone())) {
			throw new BaseException(BaseI18nCode.stationMobileNotNull);
		}
		Long stationId = SystemUtil.getStationId();
		if (!ValidateUtil.isPhoneNumber(rbo.getPhone())) {
			throw new BaseException(BaseI18nCode.stationMobileFormatError);
		}
		if (sysUserService.exist(rbo.getUsername(), stationId, null)) {
			throw new BaseException(BaseI18nCode.stationMobileByused);
		}
		SysUser sa = new SysUser();
		sa.setStatus(Constants.STATUS_ENABLE);
		sa.setSalt(RandomStringUtils.randomAll(10));
		sa.setUsername(rbo.getUsername());
		sa.setUid(rbo.getUid());
		sa.setEmail(rbo.getEmail());
		sa.setPassword(MD5Util.pwdMd5Member(rbo.getUsername(), password, sa.getSalt()));
		sa.setType(rbo.getUserType());
		sa.setCreateDatetime(new Date());
		sa.setCreateUserId(0L);
		sa.setStationId(stationId);
		sa.setRegisterIp(IpUtils.getIp());
		sa.setPromoCode(rbo.getPromoCode());
		sa.setOnlineWarn(Constants.STATUS_DISABLE);
		sa.setLockDegree(SysUser.DEGREE_UNLOCK);
		sa.setMoneyStatus(SysUser.moneyStatusNormal);
		sa.setPartnerId(SystemUtil.getPartnerId());
		sa.setUpdatePwdDatetime(sa.getCreateDatetime());
		return sa;
	}

	/**
	 * 检查注册非空项
	 */
	private SysUser checkUserRegisterBo(UserRegisterBo rbo) {
		String password = rbo.getPwd();
		if (StringUtils.isEmpty(password)) {
			throw new BaseException(BaseI18nCode.stationPasswordNotNull);
		}
		if (!ValidateUtil.isPassword(password)) {
			throw new BaseException(BaseI18nCode.stationLoginPassMustZ);
		}
		if (StringUtils.isEmpty(rbo.getUsername())) {
			throw new BaseException(BaseI18nCode.stationUserNameNotNUll);
		}
		Long stationId = SystemUtil.getStationId();
		String username = rbo.getUsername().toLowerCase();
		if (!ValidateUtil.isUsername(username)) {
			throw new BaseException(BaseI18nCode.stationUserNameMustNum);
		}
		if (!StationConfigUtil.isOn(stationId, StationConfigEnum.allnumber_switch_when_register)) {
			if (ValidateUtil.isNumber(username)) {
				throw new BaseException(BaseI18nCode.stationNotIncludeNum);
			}
		}
		if (sysUserService.exist(username, stationId, null)) {
			throw new BaseException(BaseI18nCode.stationUserByRegist);
		}
		if(GuestTool.isGuest(rbo.getUserType()))
			return setUser(rbo, stationId, username);
		if (rbo.isCheckRegConfig()) {
			// 验证注册字段
			List<StationRegisterConfig> registerConfigs = registerConfigService.find(stationId, rbo.getUserType(),
					Constants.STATUS_ENABLE);
			registerConfigs.forEach(x -> {
				switch (x.getEleName()) {
					case "realName":
						if (x.getRequired().equals(2) && StringUtils.isEmpty(rbo.getRealName())) {
							throw new BaseException(BaseI18nCode.realNameRequired);
						}
						// 去除空格
						rbo.setRealName(StringUtils.trim(rbo.getRealName()));
						if (x.getValidate().equals(2) && StringUtils.isNotEmpty(rbo.getRealName())
								&& (!ValidateUtil.isRealName(rbo.getRealName(), SystemUtil.getStation().getLanguage()) || rbo.getRealName().length() < 2)) {
							throw new BaseException(BaseI18nCode.realNameFormatError);
						}
						if (x.getUniqueness().equals(2) && userInfoService.existRealName(rbo.getRealName(), stationId)) {
							throw new BaseException(BaseI18nCode.realNameHasUsed);
						}
						break;
					case "phone":
						if (x.getRequired().equals(2) && StringUtils.isEmpty(rbo.getPhone())) {
							throw new BaseException(BaseI18nCode.stationMobileNotNull);
						}
						rbo.setPhone(StringUtils.deleteWhitespace(rbo.getPhone()));
						if (x.getValidate().equals(2) && StringUtils.isNotEmpty(rbo.getPhone())
								&& !ValidateUtil.isPhoneNumber(rbo.getPhone())) {
							throw new BaseException(BaseI18nCode.stationMobileFormatError);
						}
						if (x.getUniqueness().equals(2) && userInfoService.existPhone(rbo.getPhone(), stationId)) {
							throw new BaseException(BaseI18nCode.stationMobileByused);
						}
						break;
					case "qq":
						if (x.getRequired().equals(2) && StringUtils.isEmpty(rbo.getQq())) {
							throw new BaseException(BaseI18nCode.stationQQNotNull);
						}
						rbo.setQq(StringUtils.deleteWhitespace(rbo.getQq()));
						if (x.getValidate().equals(2) && StringUtils.isNotEmpty(rbo.getQq())
								&& !ValidateUtil.isQq(rbo.getQq())) {
							throw new BaseException(BaseI18nCode.stationQQNumFormatError);
						}
						if (x.getUniqueness().equals(2) && userInfoService.existQq(rbo.getQq(), stationId)) {
							throw new BaseException(BaseI18nCode.stationQQByUserd);
						}
						break;
					case "email":
						if (x.getRequired().equals(2) && StringUtils.isEmpty(rbo.getEmail())) {
							throw new BaseException(BaseI18nCode.stationEmailNotNull);
						}
						rbo.setEmail(StringUtils.deleteWhitespace(rbo.getEmail()));
						if (x.getValidate().equals(2) && StringUtils.isNotEmpty(rbo.getEmail())
								&& !ValidateUtil.isEmail(rbo.getEmail())) {
							throw new BaseException(BaseI18nCode.stationEmailFormatError);
						}
						if (x.getUniqueness().equals(2) && userInfoService.existEmail(rbo.getEmail(), stationId)) {
							throw new BaseException(BaseI18nCode.stationEmailByUsed);
						}
						break;
					case "receiptPwd":
						if (rbo.isAddFromUser()) {// 代理添加，或者会员推荐好友添加账号，不需要验证推广码
							break;
						}
						if (x.getRequired().equals(2) && StringUtils.isEmpty(rbo.getReceiptPwd())) {
							throw new BaseException(BaseI18nCode.stationPassDrawNotNull);
						}
						if (x.getValidate().equals(2) && StringUtils.isNotEmpty(rbo.getReceiptPwd())
								&& !ValidateUtil.isDrawPassword(rbo.getReceiptPwd())) {
							throw new BaseException(BaseI18nCode.stationPassFormatError);
						}
						break;
					case "wechat":
						if (x.getRequired().equals(2) && StringUtils.isEmpty(rbo.getWechat())) {
							throw new BaseException(BaseI18nCode.stationWechatNotNull);
						}
						rbo.setWechat(StringUtils.deleteWhitespace(rbo.getWechat()));
						if (x.getValidate().equals(2) && StringUtils.isNotEmpty(rbo.getWechat())
								&& !ValidateUtil.isWechat(rbo.getWechat())) {
							throw new BaseException(BaseI18nCode.stationWechatFormatError);
						}
						if (x.getUniqueness().equals(2) && userInfoService.existWechat(rbo.getWechat(), stationId)) {
							throw new BaseException(BaseI18nCode.stationWechatByused);
						}
						break;
					case "facebook":
						if (x.getRequired().equals(2) && StringUtils.isEmpty(rbo.getFacebook())) {
							throw new BaseException(BaseI18nCode.stationFaceBookNotNull);
						}
						rbo.setFacebook(StringUtils.deleteWhitespace(rbo.getFacebook()));
						if (x.getValidate().equals(2) && StringUtils.isNotEmpty(rbo.getFacebook())
								&& !ValidateUtil.isFacebook(rbo.getFacebook())) {
							throw new BaseException(BaseI18nCode.stationFaceBookFormatError);
						}
						if (x.getUniqueness().equals(2) && userInfoService.existFacebook(rbo.getFacebook(), stationId)) {
							throw new BaseException(BaseI18nCode.stationFaceBookByused);
						}
						break;
				case "line":
					// 会员注册信息暂未用到Line账户，若以后用到，再做功能实现
					if (x.getRequired().equals(2) && StringUtils.isEmpty(rbo.getLine())) {
						throw new BaseException(BaseI18nCode.stationLineNotNull);
					}
					rbo.setLine(StringUtils.deleteWhitespace(rbo.getLine()));
					if (x.getValidate().equals(2) && StringUtils.isNotEmpty(rbo.getLine())
							&& !ValidateUtil.isLine(rbo.getLine())) {
						throw new BaseException(BaseI18nCode.stationLineFormatError);
					}
					if (x.getUniqueness().equals(2) && userInfoService.existLine(rbo.getLine(), stationId)) {
						throw new BaseException(BaseI18nCode.stationLineByused);
					}
					break;
					case "promoCode":
						if (rbo.isAddFromUser() || SystemUtil.getDomain().getProxyId() != null) {// 代理添加，或者会员推荐好友添加账号，不需要验证推广码
							break;
						}
						if (x.getRequired().equals(2) && StringUtils.isEmpty(rbo.getPromoCode())) {
							throw new BaseException(BaseI18nCode.stationProxyCodeNotNull);
						}
						if (x.getValidate().equals(2) && StringUtils.isNotEmpty(rbo.getPromoCode())
								&& !ValidateUtil.isPromoCode(rbo.getPromoCode())) {
							throw new BaseException(BaseI18nCode.stationProxyCodeFormatError);
						}
						break;
					default:
						break;
				}
			});
		}
		return setUser(rbo, stationId, username);
	}

	private SysUser setUser(UserRegisterBo rbo, Long stationId, String username) {
		SysUser sa = new SysUser();
		sa.setStatus(Constants.STATUS_ENABLE);
		sa.setSalt(RandomStringUtils.randomAll(10));
		sa.setUsername(username);
		sa.setPassword(MD5Util.pwdMd5Member(username, rbo.getPwd(), sa.getSalt()));
		sa.setType(rbo.getUserType());
		sa.setCreateDatetime(new Date());
		sa.setCreateUserId(0L);
		sa.setStationId(stationId);
		sa.setRegisterIp(IpUtils.getIp());
		sa.setPromoCode(rbo.getPromoCode());
		sa.setOnlineWarn(Constants.STATUS_DISABLE);
		sa.setLockDegree(SysUser.DEGREE_UNLOCK);
		sa.setMoneyStatus(SysUser.moneyStatusNormal);
		sa.setPartnerId(SystemUtil.getPartnerId());
		sa.setUpdatePwdDatetime(sa.getCreateDatetime());
		return sa;
	}

	/**
	 * 父级关系赋值
	 */
	private void memberRegisterSetProxyInfo(SysUser sa, UserRegisterBo rbo, Long stationId) {
		sa.setLevel(1);
		// 是否是推广链接
		if (rbo.getProxyId() != null) {
			SysUser proxy = sysUserService.findOneById(rbo.getProxyId(), stationId);
			if (proxy == null) {
				throw new BaseException(BaseI18nCode.proxyUnExist);
			}
			sa.setLevel(proxy.getLevel() + 1);
			sa.setProxyId(proxy.getId());
			sa.setProxyName(proxy.getUsername());
			sa.setParentIds(proxy.getParentIds() == null ? "," + proxy.getId() + ","
					: proxy.getParentIds() + proxy.getId() + ",");
			sa.setParentNames(proxy.getParentNames() == null ? "," + proxy.getUsername() + ","
					: proxy.getParentNames() + proxy.getUsername() + ",");
			userProxyNumService.updateProxyNum(sa.getType() == UserTypeEnum.PROXY.getType(), 1, proxy.getId());
		}
		sa.setRecommendId(rbo.getRecommendId());
		sa.setRecommendUsername(rbo.getRecommendUsername());
		sa.setAgentName(rbo.getAgentName());
	}

	/**
	 * 会员推荐添加会员
	 * 
	 * @param rbo
	 */
	@Override
	public void memberAddMember(UserRegisterBo rbo) {
		rbo.setAddFromUser(true);
		SysUser curUser = LoginMemberUtil.currentUser();
		if (!Objects.equals(curUser.getType(), UserTypeEnum.MEMBER.getType())) {
			throw new BaseException(BaseI18nCode.stationNotVipNum);
		}
		Long stationId = curUser.getStationId();
		if (!ProxyModelUtil.canBeRecommend(stationId)) {
			throw new BaseException(BaseI18nCode.stationMemberFirst);
		}
		rbo.setUserType(UserTypeEnum.MEMBER.getType());
		SysUser sa = checkUserRegisterBo(rbo);

		sa.setRecommendId(curUser.getId());
		sa.setRecommendUsername(curUser.getUsername());
		sa.setLevel(curUser.getLevel() + 1);
		sa.setDegreeId(getDegreeId(null, stationId));
		sa.setGroupId(getGroupId(null, stationId));
		sa.setCreateUserId(curUser.getId());
		sa.setCreateUsername(curUser.getUsername());
		saveUserAllInfo(stationId, sa, rbo);
		LogUtils.addLog("会员推荐添加会员:" + sa.getUsername());
	}

	/**
	 * 代理推广添加用户
	 * 
	 * @param rbo
	 */
	@Override
	public void proxyAddUser(UserRegisterBo rbo) {
		rbo.setAddFromUser(true);
		SysUser proxy = LoginMemberUtil.currentUser();
		if (!Objects.equals(proxy.getType(), UserTypeEnum.PROXY.getType())) {
			throw new BaseException(BaseI18nCode.stationNumProxy);
		}
		Long stationId = proxy.getStationId();
		int proxyModel = ProxyModelUtil.getProxyModel(stationId);// 代理模式
		if (ProxyModelUtil.isAllProxy(proxyModel)) {
			// 如果是全代理模式，只能添加代理
			rbo.setUserType(UserTypeEnum.PROXY.getType());
		} else {
			if (rbo.getUserType() == null) {
				rbo.setUserType(UserTypeEnum.MEMBER.getType());
			}
		}

		SysUser user = checkUserRegisterBo(rbo);

		if (Objects.equals(user.getType(), UserTypeEnum.PROXY.getType())) {
			// 不是全部代理，也不是多级代理 则当前是代理不能再添加代理
			if (!ProxyModelUtil.isMultiOrAllProxy(proxyModel)) {
				throw new BaseException(BaseI18nCode.stationMutiProxy);
			}
		}
		// 添加代理下级人数
		userProxyNumService.updateProxyNum(user.getType() == UserTypeEnum.PROXY.getType(), 1, proxy.getId());

		String proParentIds = StringUtils.isEmpty(proxy.getParentIds()) ? "," : proxy.getParentIds();
		String proParentNames = StringUtils.isEmpty(proxy.getParentNames()) ? "," : proxy.getParentNames();
		user.setProxyId(proxy.getId());
		user.setParentIds(proParentIds + proxy.getId() + ",");
		user.setParentNames(proParentNames + proxy.getUsername() + ",");
		user.setProxyName(proxy.getUsername());
		user.setLevel(proxy.getLevel() + 1);

		validatorRebate(proxy, rbo, stationId);

		user.setDegreeId(getDegreeId(null, stationId));
		user.setGroupId(getGroupId(null, stationId));

		user.setCreateUserId(proxy.getId());
		user.setCreateUsername(proxy.getUsername());
		saveUserAllInfo(stationId, user, rbo);
		//给用注册用户添加积分和彩金
		rollRegAwardForUser(user);
		LogUtils.addLog("代理推广添加用户:" + user.getUsername());
	}

	@Override
	public SysUser doThirdAuthRegister(UserRegisterBo rbo) {
		try {
			// 验证是否是黑名单
			Long stationId = SystemUtil.getStationId();

			// 验证当前ip注册会员个数
			validatorIpRegisterNumToday(stationId, IpUtils.getIp(), null);
			validatorOsRegisterNumToday(stationId, LogUtils.getOs(), null);
			checkPromotionCode(rbo, stationId);
			getRegisterTerminal(rbo);//根据user-agent判断终端类型
			SysUser sa = setUser(rbo, stationId, rbo.getUsername().toLowerCase());
			setRegisterSystem(sa);
			memberRegisterSetProxyInfo(sa, rbo, stationId);
			// 保存层级
			sa.setDegreeId(userDegreeService.getDefaultId(stationId));
			sa.setGroupId(userGroupService.getDefaultId(stationId));
			sa.setUserRegisterType(rbo.getUserRegisterType());
			SysUser sysUser = sysUserService.save(sa);

			userInfoService.init(sa, rbo);
			betNumService.init(sa.getId(), stationId);// 保存打码
			userDepositService.init(sa.getId(), stationId);
			userMoneyService.init(sa.getId(), BigDecimal.ZERO);
			userScoreService.init(sa.getId(), stationId);
			userLoginService.initForRegister(sa, rbo.getTerminal());
			userWithdrawService.init(sa.getId(), stationId);
			userPermService.init(sa.getId(), stationId, sa.getUsername(), sa.getType());
			if (UserTypeEnum.PROXY.getType() == sa.getType()) {// 如果是代理，新增代理返点表,新增代理下级数量表
				userProxyNumService.init(sa.getId());
				userRebateService.init(sa.getId(), stationId, rbo.getLive(), rbo.getEgame(), rbo.getChess(),
						rbo.getSport(), rbo.getEsport(), rbo.getFishing(), rbo.getLottery());
			}
			// 添加站点每日注册信息表
			stationDailyRegisterService.registerHandle(stationId, sa.getPartnerId(), sa.getType(),
					sa.getProxyId() != null);
			OnlineManager.doOnline(sa, Constants.SESSION_KEY_MEMBER);
			// 发送站内信
			String msg = StationConfigUtil.get(stationId, StationConfigEnum.member_register_send_msg_text);
			if (StringUtils.isNotEmpty(msg)) {

				messageService.sysMessageSend(stationId, sa.getId(), sa.getUsername(),
						I18nTool.convertCodeToArrStr(BaseI18nCode.stationSuccRegist.getCode()), I18nTool.convertCodeToArrStr(msg),
						Constants.STATUS_ENABLE);
			}
			SysThreadVariable.get().setUser(sa);
			// 保存登录日志
			LogUtils.loginLog(sa, true, "");
			LogUtils.addUserLog("用户注册:" + sa.getUsername());
			//给会员赠送注册彩金，读取注册赠送策略
			rollRegAwardForUser(sa);
			//注册成功后，自动生成一条推广链接 代理/会员推广链接
			if (StationConfigUtil.isOn(stationId, StationConfigEnum.auto_generate_link_register)) {
				promotionService.autoGenerateLink(sa);
			}
			return sysUser;
		} catch (Exception e) {
			logger.error("注册发生错误", e);
			throw e;
		}
	}

	@Override
	public void adminSaveGuest(Long stationId, UserRegisterBo rbo) {
		if (StringUtils.isEmpty(rbo.getUsername())) {
			throw new BaseException(BaseI18nCode.usernameCanntEmpty);
		}
		if (StringUtils.isEmpty(rbo.getPwd())) {
			throw new BaseException(BaseI18nCode.stationPasswordNotNull);
		}
		if (!ValidateUtil.isPassword(rbo.getPwd())) {
			throw new BaseException(BaseI18nCode.stationLoginPassMustZ);
		}

		rbo.setUserType(UserTypeEnum.GUEST_BACK.getType());
		SysUser userGuest = this.saveUserGuest(stationId, rbo);
		LogUtils.addLog("后台添加试玩账号:" + userGuest.getUsername());
	}

	private SysUser saveUserGuest(Long stationId, UserRegisterBo rbo) {
		String username = rbo.getUsername().trim().toLowerCase();
		if (!ValidateUtil.isUsername(username)) {
			throw new BaseException(BaseI18nCode.stationUserNameMustNum);
		}
		if (!StationConfigUtil.isOn(stationId, StationConfigEnum.allnumber_switch_when_register)) {
			if (ValidateUtil.isNumber(username)) {
				throw new BaseException(BaseI18nCode.stationNotIncludeNum);
			}
		}
		if (sysUserService.exist(username, stationId, null)) {
			throw new BaseException(BaseI18nCode.stationUserByRegist);
		}

		SysUser user = new SysUser();
		user.setPartnerId(SystemUtil.getPartnerId());
		user.setLevel(1);
		user.setType(rbo.getUserType());
		setAgentInfo(user, rbo.getAgentName(), stationId);
		setProxyInfo(user, rbo.getProxyName(), stationId);
		user.setDegreeId(getDegreeId(rbo.getDegreeId(), stationId));
		user.setGroupId(getGroupId(rbo.getGroupId(), stationId));
		user.setStatus(Constants.STATUS_ENABLE);
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setUsername(username);
		user.setPassword(MD5Util.pwdMd5Member(username, rbo.getPwd(), user.getSalt()));
		user.setCreateDatetime(new Date());
//		user.setCreateUserId(null);
//		user.setCreateUsername("");
		user.setStationId(stationId);
		user.setOnlineWarn(Constants.STATUS_DISABLE);
		user.setLockDegree(SysUser.DEGREE_UNLOCK);
		user.setUpdatePwdDatetime(user.getCreateDatetime());
		user.setMoneyStatus(SysUser.moneyStatusNormal);
		user.setUserRegisterType(SysUserRegisterTypeEnum.Local.getType());
		user.setRemark(rbo.getRemark());

		setRegisterSystem(user);
		saveUserAllInfo(stationId, user, rbo);
		return user;
	}

}
