package com.play.web.controller.front.usercenter;

import java.math.BigDecimal;
import java.util.*;

import com.play.core.*;
import com.play.model.*;
import com.play.service.*;
import com.play.web.utils.ServletUtils;
import com.play.web.var.GuestTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.ip.IPAddressUtils;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.ProxyModelUtil;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.exception.BaseException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.HidePartUtil;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;


/**
 * 用户中心
 *
 * @author admin
 */
@Controller
@RequestMapping("/userCenter")
public class UserCenterController extends FrontBaseController {

	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserInfoService userInfoService;
	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;
	@Autowired
	private SysUserMoneyService userMoneyService;
	@Autowired
	private StationScoreExchangeService stationScoreExchangeService;
	@Autowired
	private SysUserScoreService userScoreService;
	@Autowired
	private SysUserScoreHistoryService userScoreHistoryService;
	@Autowired
	private SysUserPermService userPermService;
	@Autowired
	private ThirdTransLogService thirdTransLogService;
	@Autowired
	private SysUserRebateService userRebateService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserDepositService userDepositService;
	@Autowired
	private ThirdGameService gameService;
	@Autowired
	private ThirdPlatformService platformService;
	@Autowired
	private StationRebateService rebateService;
	@Autowired
	private SysUserAvatarService userAvatarService;
	@Autowired
	private StationSignRecordService signRecordService;

	@Autowired
	SysUserBonusService sysUserBonusService;
	@Autowired
	StationPromotionService stationPromotionService;

	/**
	 * 用户中心首页
	 */
	@RequestMapping("/index")
	public String index() {
		return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/member/centerIndex").toString();
	}

	/**
	 * 用户中心首页 测试用的
	 */
	@RequestMapping("/index2")
	public String index2() {
		return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/member/centerIndex2").toString();
	}
	
	/**
	 * 用户中心首页 测试用的
	 */
	@RequestMapping("/moban")
	public String moban() {
		return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/member/centerMoban").toString();
	}
	
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/getStationConfig")
	public void getStationConfig(Map<String, Object> map) {
		JSONObject obj = new JSONObject();
		Long stationId = SystemUtil.getStationId();
		String lang = SystemUtil.getLanguage();
		obj.put("lang", lang);
		obj.put("currency", SystemUtil.getStation().getCurrency());
		obj.put("languages", LanguageEnum.getLangs());
		obj.put("logo", StationConfigUtil.get(stationId, StationConfigEnum.station_logo));
		obj.put("pcsignLogo", StationConfigUtil.get(stationId, StationConfigEnum.pc_sign_logo));
		obj.put("game", gameService.findOne(stationId));
		obj.put("platforms", platformService.getPlatformSwitch(stationId));
		obj.put("isExchange", !(StationConfigUtil.isOff(stationId, StationConfigEnum.mny_score_show) || StationConfigUtil.isOff(stationId, StationConfigEnum.exchange_score)));
		obj.put("onOffCommunication", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_communication));
		obj.put("kfUrl", StationConfigUtil.getKfUrl(stationId));
		obj.put("proxyViewUserData", StationConfigUtil.isOn(stationId, StationConfigEnum.proxy_view_account_data));
		obj.put("qqUrl", StationConfigUtil.get(stationId, StationConfigEnum.online_qq_service_url));
//		obj.put("qqCustomUrl", StationConfigUtil.get(stationId, StationConfigEnum.qq_custom_server_url));
		obj.put("degreeShow", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_degree_show));
		obj.put("isRedBag", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_redbag));
		obj.put("isSignIn", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_sign_in));
		obj.put("isShowOnSign", StationConfigUtil.isOn(stationId, StationConfigEnum.show_money_onsign));
		obj.put("isTurnlate", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_turnlate));
		if (StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange)) {
			obj.put("thirdAutoExchange", true);
			obj.put("moneyChageNav", false);
		} else {
			obj.put("thirdAutoExchange", false);
			obj.put("moneyChageNav", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_moneychange_notice));
		}
		obj.put("deposit_discount_hint", StationConfigUtil.isOn(stationId, StationConfigEnum.deposit_discount_hint));
		obj.put("popShowTime", StationConfigUtil.get(stationId, StationConfigEnum.pop_frame_show_time));
		obj.put("isMsgTips", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_mobile_msg_tips));
		obj.put("isOnlineCustClose",StationConfigUtil.isOn(stationId, StationConfigEnum.close_online_customer_service));
		obj.put("onOffwebpayGuide", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_webpay_guide));
		obj.put("cantUpdContact", StationConfigUtil.isOn(stationId, StationConfigEnum.modify_person_info_after_first_modify_switch));
//		obj.put("isOnmultiListDialog", StationConfigUtil.isOn(stationId, StationConfigEnum.multi_list_dialog_switch));
		obj.put("onOffStationAdvice", StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_station_advice));
		obj.put("proxyRebateExplain", StationConfigUtil.get(stationId, StationConfigEnum.proxy_rebate_explain));
//		obj.put("onOffExpandFirstNotice", StationConfigUtil.isOn(stationId, StationConfigEnum.notice_list_dialog_expand_first_notice));
		obj.put("iosDownloadLink", StationConfigUtil.get(stationId, StationConfigEnum.app_download_link_ios));
		obj.put("iosDownloadLinkSuper", StationConfigUtil.get(stationId, StationConfigEnum.super_app_download_link_ios));
		obj.put("androidDownloadLink", StationConfigUtil.get(stationId, StationConfigEnum.app_download_link_android));
		obj.put("androidDownloadLinkSuper", StationConfigUtil.get(stationId, StationConfigEnum.super_app_download_link_android));
		obj.put("iosQrDownloadLink", StationConfigUtil.get(stationId, StationConfigEnum.app_qr_code_link_ios));
		obj.put("iosQrDownloadLinkSuper", StationConfigUtil.get(stationId, StationConfigEnum.super_app_qr_code_link_ios));
		obj.put("androidQrDownloadLink", StationConfigUtil.get(stationId, StationConfigEnum.app_qr_code_link_android));
		obj.put("androidQrDownloadLinkSuper", StationConfigUtil.get(stationId, StationConfigEnum.super_app_qr_code_link_android));
		obj.put("instagram_url", StationConfigUtil.get(stationId, StationConfigEnum.instagram_url));
		obj.put("facebook_url", StationConfigUtil.get(stationId, StationConfigEnum.facebook_url));
		obj.put("telegram_url", StationConfigUtil.get(stationId, StationConfigEnum.telegram_url));
		obj.put("stationName", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
		obj.put("stationIntroduce", StationConfigUtil.get(stationId, StationConfigEnum.station_introduce));
		obj.put("live", PlatformType.getLivePlatformJson());
		obj.put("egame", PlatformType.getEgamePlatformJson());
		obj.put("sport", PlatformType.getSportPlatformsJson());
		obj.put("chess", PlatformType.getChessPlatformJson());
		obj.put("esport", PlatformType.getEsportPlatformsJson());
		obj.put("fish", PlatformType.getFishingPlatformsJson());
		obj.put("lottery", PlatformType.getLotteryPlatformsJson());
		obj.put("stationCode", SystemUtil.getStation().getCode());

		super.renderJSON(obj);
	}
	
	@ResponseBody
	@RequestMapping("/getConfig")
	public void getConfig(Map<String, Object> map) {
		JSONObject obj = new JSONObject();
		SysUser user = LoginMemberUtil.currentUser();
		Long stationId = user.getStationId();
		obj.put("lang", SystemUtil.getLanguage());
		obj.put("currency", SystemUtil.getStation().getCurrency());
		obj.put("languages", LanguageEnum.getLangs());
		obj.put("logo", StationConfigUtil.get(stationId, StationConfigEnum.station_logo));
		obj.put("pcsignLogo", StationConfigUtil.get(stationId, StationConfigEnum.pc_sign_logo));
		obj.put("game", gameService.findOne(stationId));
		obj.put("platforms", platformService.getPlatformSwitch(stationId));
		obj.put("isExchange", !(StationConfigUtil.isOff(stationId, StationConfigEnum.mny_score_show)
				|| StationConfigUtil.isOff(stationId, StationConfigEnum.exchange_score)));
		if ((user.getType() == UserTypeEnum.PROXY.getType())) {
			boolean canBePromo = ProxyModelUtil.canBePromo(stationId);
			obj.put("canBePromo", canBePromo);
			if (canBePromo) {// 可以代理推广
				StationRebate rebate = rebateService.findOne(stationId, StationRebate.USER_TYPE_PROXY);
				if (Objects.equals(rebate.getType(), StationRebate.TYPE_SAME)) {
					obj.put("rebate", clearRebateOtherInfo(rebate));
				} else {
					obj.put("rebate", userRebateService.findOne(user.getId(), stationId));
				}
			}
			obj.put("canBeRecommend", false);
		} else {
			obj.put("canBePromo", false);
			boolean canBeRecommend = ProxyModelUtil.canBeRecommend(stationId);
			obj.put("canBeRecommend", canBeRecommend);
			if (canBeRecommend) {// 可以会员推荐好友
				obj.put("rebate",
						clearRebateOtherInfo(rebateService.findOne(stationId, StationRebate.USER_TYPE_MEMBER)));
			}
		}
		obj.put("onOffCommunication", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_communication));
		obj.put("username", user.getUsername());
		obj.put("isProxy", user.getType() == UserTypeEnum.PROXY.getType());
		SysUserDegree d = userDegreeService.findOne(user.getDegreeId(), stationId);
		if (d != null) {
			obj.put("degreeName", d.getName());
			obj.put("degreeIcon", d.getIcon());
		} else {
			obj.put("degreeName", "");
			obj.put("degreeIcon", "");
		}
		SysUserAvatarRecord nowAvatar = userAvatarService.findOneByUserId(user.getId(), stationId);
		if (nowAvatar != null) {
			obj.put("avatarId", nowAvatar.getAvatarId());
			obj.put("avatarUrl", nowAvatar.getUrl());
		}
		obj.put("kfUrl", StationConfigUtil.getKfUrl(stationId));
		obj.put("proxyViewUserData", StationConfigUtil.isOn(stationId, StationConfigEnum.proxy_view_account_data));
		obj.put("qqUrl", StationConfigUtil.get(stationId, StationConfigEnum.online_qq_service_url));
//		obj.put("qqCustomUrl", StationConfigUtil.get(stationId, StationConfigEnum.qq_custom_server_url));
		obj.put("degreeShow", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_degree_show));
		obj.put("isRedBag", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_redbag));
		obj.put("isSignIn", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_sign_in));
		obj.put("isTurnlate", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_turnlate));
		if (StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange)) {
			obj.put("thirdAutoExchange", true);
			obj.put("moneyChageNav", false);
		} else {
			obj.put("thirdAutoExchange", false);
			obj.put("moneyChageNav", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_moneychange_notice));
		}
		obj.put("popShowTime", StationConfigUtil.get(stationId, StationConfigEnum.pop_frame_show_time));
		obj.put("isMsgTips", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_mobile_msg_tips));
		obj.put("isOnlineCustClose",
				StationConfigUtil.isOn(stationId, StationConfigEnum.close_online_customer_service));
		obj.put("onOffwebpayGuide", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_webpay_guide));
		obj.put("cantUpdContact",
				StationConfigUtil.isOn(stationId, StationConfigEnum.modify_person_info_after_first_modify_switch));
//		obj.put("isOnmultiListDialog", StationConfigUtil.isOn(stationId, StationConfigEnum.multi_list_dialog_switch));
		obj.put("onOffStationAdvice", StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_station_advice));
		obj.put("proxyRebateExplain", StationConfigUtil.get(stationId, StationConfigEnum.proxy_rebate_explain));
//		obj.put("onOffExpandFirstNotice",
//				StationConfigUtil.isOn(stationId, StationConfigEnum.notice_list_dialog_expand_first_notice));
		obj.put("deposit_discount_hint", StationConfigUtil.isOn(stationId, StationConfigEnum.deposit_discount_hint));
		obj.put("isUserResetPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_user_reset_pwd));
		obj.put("redPacketPageStyle", StationConfigUtil.get(stationId, StationConfigEnum.rob_redpacket_version));
		obj.put("live", PlatformType.getLivePlatformJson());
		obj.put("egame", PlatformType.getEgamePlatformJson());
		obj.put("sport", PlatformType.getSportPlatformsJson());
		obj.put("chess", PlatformType.getChessPlatformJson());
		obj.put("esport", PlatformType.getEsportPlatformsJson());
		obj.put("fish", PlatformType.getFishingPlatformsJson());
		obj.put("lottery", PlatformType.getLotteryPlatformsJson());
		obj.put("stationCode", SystemUtil.getStation().getCode());
		super.renderJSON(obj);
	}

	private StationRebate clearRebateOtherInfo(StationRebate rebate) {
		rebate.setBetRate(null);
		rebate.setId(null);
		rebate.setLevelDiff(null);
		rebate.setMaxDiff(null);
		rebate.setRebateMode(null);
		rebate.setStationId(null);
		rebate.setType(null);
		rebate.setUserType(null);
		return rebate;
	}

	/**
	 * 个人总览
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/userAllInfo")
	public void userAllInfo(String startTime, String endTime) {
		SysUser user = LoginMemberUtil.currentUser();
		if (null == user) {
			JSONObject obj = new JSONObject();
			obj.put("money", "0");
			obj.put("login", false);

			renderJSON(obj);
			return;
		}

		Long stationId = user.getStationId();
		Date start = DateUtil.toDatetime(startTime);
		Date end = DateUtil.toDatetime(endTime);
		if (start == null) {
			start = DateUtil.dayFirstTime(new Date(), 0);
		}
		if (end == null) {
			end = DateUtil.dayFirstTime(new Date(), 1);
		}
		if (DateUtil.getDiscrepantDays(start, end) > 31) {
			throw new BaseException(BaseI18nCode.timeRangeNotOver31);
		}
		JSONObject obj = new JSONObject();
		obj.put("username", user.getUsername());
		obj.put("money", BigDecimalUtil.formatValue(moneyService.getMoney(user.getId())));
		obj.put("login", true);
		obj.put("userId", user.getId());
		obj.put("type", user.getType());
		obj.put("dailyMoney", userDailyMoneyService.personOverview(stationId, user.getId(), start, end));
		SysUserDegree degree = userDegreeService.findOne(user.getDegreeId(), stationId);
		obj.put("userDegreeeIcon", degree.getIcon());
		List<SysUserDegree> list = userDegreeService.find(stationId, Constants.STATUS_ENABLE);
		SysUserDeposit deposit = userDepositService.findOne(user.getId(), stationId);
		SysUserDegree newDegree = null;
		BigDecimal depositTotal = BigDecimal.ZERO;
		BigDecimal curDegreedepositTotal = BigDecimal.ZERO;
		if (deposit != null && deposit.getTotalMoney() != null) {
			depositTotal = deposit.getTotalMoney();
		}
		if (degree != null && degree.getDepositMoney() != null) {
			curDegreedepositTotal = degree.getDepositMoney();
		}
		//最高级标志
		boolean isMax = false;
		if (list != null && deposit != null) {
			list.sort(new Comparator<SysUserDegree>() {
				@Override
				public int compare(SysUserDegree o1, SysUserDegree o2) {
					return o1.getDepositMoney().compareTo(o2.getDepositMoney());
				}
			});

			for (SysUserDegree l : list) {
				//取最高级别
				SysUserDegree sysUserDegree = list.get(list.size()-1);
				//如果充值金额比最高级别还要高,那么显示最高级
				if(sysUserDegree.getDepositMoney()!=null && depositTotal.compareTo(sysUserDegree.getDepositMoney()) > 0){
					degree = sysUserDegree;
					isMax = true;
				}
				if (l.getDepositMoney() != null) {
					if (l.getDepositMoney().compareTo(depositTotal) > 0
							&& l.getDepositMoney().compareTo(curDegreedepositTotal) > 0) {
						newDegree = l;
					}
					if (newDegree != null)
						break;
				}
			}
		}
		if (degree != null) {
			obj.put("curDegreeDepositMoney", depositTotal);
			obj.put("curDegreeName", degree.getName());
		}
		if (newDegree != null) {
			obj.put("newDegreeDepositMoney", newDegree.getDepositMoney());
			obj.put("newDegreeName", newDegree.getName());
			obj.put("nextDegreeDepositMoney", BigDecimalUtil.subtract(newDegree.getDepositMoney(), depositTotal));
		} else {
			obj.put("newDegreeDepositMoney", BigDecimal.ZERO);
			obj.put("newDegreeName", "");
			//显示最顶级的名称
			if(isMax) obj.put("newDegreeName", degree.getName());
			obj.put("nextDegreeDepositMoney", BigDecimal.ZERO);
		}
		obj.put("degreeShow", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_degree_show));
		renderJSON(obj);
	}

	/**
	 * 获取安全信息
	 */
	@ResponseBody
	@RequestMapping("/getSecurityInfo")
	public void getSecurityInfo() {
		// 获取会员信息
		SysUser user = LoginMemberUtil.currentUser();
		SysUserInfo userInfo = userInfoService.findOne(user.getId(), user.getStationId());
		SysUserLogin login = userLoginService.findOne(user.getId(), user.getStationId());
		JSONObject obj = new JSONObject();
		int securityLevel = 1;
		boolean hasWithdrawalPassword = StringUtils.isNotEmpty(userInfo.getReceiptPwd());
		boolean hasPhone = StringUtils.isNotEmpty(userInfo.getPhone());
		boolean hasEmail = StringUtils.isNotEmpty(userInfo.getEmail());
		boolean hasWechat = StringUtils.isNotEmpty(userInfo.getWechat());
		boolean hasQQ = StringUtils.isNotEmpty(userInfo.getQq());
		boolean hasRealName = StringUtils.isNotEmpty(userInfo.getRealName());
		boolean hasFacebook = StringUtils.isNotEmpty(userInfo.getFacebook());
		boolean hasLine = StringUtils.isNotEmpty(userInfo.getLine());
		if (login != null) {
			if (StringUtils.isNotEmpty(login.getLastLoginIp())) {
				String ipArea = IPAddressUtils.getCountry(login.getLastLoginIp());
				//obj.put("lastLoginIp", login.getLastLoginIp() + "(" + ipArea + ")");
				obj.put("lastLoginIp", login.getLastLoginIp());
			}
			obj.put("lastLoginTime", DateUtil.toDatetimeStr(login.getLastLoginDatetime()));
		}
		if (hasWithdrawalPassword && hasPhone) {
			securityLevel = 2;
		}
		if (hasWithdrawalPassword && hasPhone && hasEmail && hasWechat && hasQQ && hasRealName && hasFacebook) {
			securityLevel = 3;
		}
		obj.put("securityLevel", securityLevel);
		obj.put("hasWithdrawalPassword", hasWithdrawalPassword);
		obj.put("hasPhone", hasPhone);
		obj.put("hasEmail", hasEmail);
		obj.put("hasWechat", hasWechat);
		obj.put("hasQQ", hasQQ);
		obj.put("hasRealName", hasRealName);
		obj.put("hasFacebook", hasFacebook);
		obj.put("hasLine", hasLine);
		// 用户基本信息
		JSONObject info = new JSONObject();
		info.put("wechat", HidePartUtil.removePart(userInfo.getWechat()));
		info.put("realName", HidePartUtil.removePart(userInfo.getRealName()));
		info.put("qq", HidePartUtil.removePart(userInfo.getQq()));
		info.put("phone", HidePartUtil.removePart(userInfo.getPhone()));
		info.put("email", HidePartUtil.removePart(userInfo.getEmail()));
		info.put("facebook", HidePartUtil.removePart(userInfo.getFacebook()));
		info.put("line", HidePartUtil.removePart(userInfo.getLine()));
		obj.put("userData", info);
		renderJSON(obj.toJSONString());
	}

	/**
	 * 更新安全中心用户信息
	 */
	@ResponseBody
	@RequestMapping("/updateSecurityInfo")
	public void updateSecurityInfo(String newContact, String oldContact, String type) {
		SysUser user = LoginMemberUtil.currentUser();
		userInfoService.updateSecurityInfo(newContact, oldContact, type, user.getId(), user.getStationId());
		renderSuccess();
	}

	/**
	 * 更新用户真实姓名
	 */
	@ResponseBody
	@RequestMapping("/updateRealName")
	public void updateRealName(String realName) {
		if (StringUtils.isEmpty(realName)) {
			throw new BaseException(BaseI18nCode.realNameRequired);
		}
		SysUser user = LoginMemberUtil.currentUser();
		SysUserInfo info = userInfoService.findOne(user.getId(), user.getStationId());
		if (StringUtils.isNotEmpty(info.getRealName())) {
			throw new BaseException(BaseI18nCode.realNameNotEmpty);
		}
		userInfoService.updateUserRealName(user.getId(), user.getStationId(), realName);
		renderSuccess();
	}

	/**
	 * 保存修改密码
	 */
	@ResponseBody
	@RequestMapping("/userPwdModifySave")
	public void modifyPwdSave(String oldPwd, String newPwd, String okPwd, Integer type) {
		SysUser user = LoginMemberUtil.currentUser();
		if (StationConfigUtil.isOff(user.getStationId(), StationConfigEnum.switch_user_reset_pwd)) {
			throw new BaseException(BaseI18nCode.userResetPwdClosed);
		}
		if (type == null || type == 1) {
			userService.modifyLoginPwd(oldPwd, newPwd, okPwd, user.getId(), user.getStationId(), user.getUsername());
		} else {
			userInfoService.modifyReceiptPwd(oldPwd, newPwd, okPwd, user.getId(), user.getStationId(),
					user.getUsername());
		}
		renderSuccess();
	}

	/**
	 * 积分记录数据列表
	 */
	@ResponseBody
	@RequestMapping("/scoreHisData")
	public void scoreHisData(Integer type, String startTime, String endTime) {
		SysUser user = LoginMemberUtil.currentUser();
		// 判断积分显示是否开启
		if (StationConfigUtil.isOff(user.getStationId(), StationConfigEnum.mny_score_show)) {
			throw new UnauthorizedException();
		}
		Date begin = DateUtil.toDatetime(startTime);
		Date end = DateUtil.toDatetime(endTime);
		renderJSON(userScoreHistoryService.getPage(user.getStationId(), null, begin, end, type, user.getId()));
	}

	/**
	 * 积分兑换
	 */
	@ResponseBody
	@RequestMapping("/getScoreExchangeInfo")
	public void getScoreExchangeInfo(Map<String, Object> map) {
		SysUser user = LoginMemberUtil.currentUser();
		Long stationId = user.getStationId();
		// 判断积分兑换积分显示是否开启
		JSONObject obj = new JSONObject();
		if (StationConfigUtil.isOff(stationId, StationConfigEnum.mny_score_show)
				|| StationConfigUtil.isOff(stationId, StationConfigEnum.exchange_score)) {
			obj.put("exchange", false);
			renderJSON(obj);
			return;
		}
		obj.put("exchange", true);
		// 获取积分兑换策略
		obj.put("moneyToScore", stationScoreExchangeService.findOneConfig(StationScoreExchange.MNY_TO_SCORE, stationId,
				Constants.STATUS_ENABLE));
		obj.put("scoreToMoney", stationScoreExchangeService.findOneConfig(StationScoreExchange.SCORE_TO_MNY, stationId,
				Constants.STATUS_ENABLE));
		// 获取积分余额
		obj.put("score", userScoreService.getScore(user.getId(), stationId));
		obj.put("money", userMoneyService.getMoney(user.getId()));
		renderJSON(obj);
	}

	/**
	 * 积分兑换确认
	 */
	@ResponseBody
	@RequestMapping("confirmExchange")
	public void confirmExchange(Long configId, BigDecimal exchangeNum) {
		SysUser user = LoginMemberUtil.currentUser();
		// 判断会员权限
		SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.exchangeScore);
		if (perm == null || perm.getStatus() == null || perm.getStatus() == Constants.STATUS_DISABLE) {
			throw new UnauthorizedException();
		}
		stationScoreExchangeService.confirmExchange(user, exchangeNum, configId);
		renderSuccess();
	}

	/**
	 * 额度转换记录数据列表
	 */
	@ResponseBody
	@RequestMapping("/exchangeHistory")
	public void exchangeHistory(Integer status, Integer platform, Integer type, String startTime, String endTime) {
		SysUser user = LoginMemberUtil.currentUser();
		Date begin = DateUtil.toDatetime(startTime);
		Date end = DateUtil.toDatetime(endTime);
		renderJSON(JSONObject.toJSONString(
				thirdTransLogService.page(null, user.getId(), platform, user.getStationId(), status, type, begin, end),
				SerializerFeature.WriteDateUseDateFormat));
	}

	/**
	 * 用户签到
	 */
	@ResponseBody
	@RequestMapping("/sign")
	public void sign() {
		SysUser user = LoginMemberUtil.currentUser();
		if(GuestTool.isGuest(user)) {
			 throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}
		signRecordService.userSignInNew(user, 0);
		renderSuccess();
	}

	/**
	 * 签到于額
	 */
	@ResponseBody
	@RequestMapping("/accInfo")
	public void accInfo() {
		SysUser user = LoginMemberUtil.currentUser();
		Map map = new HashMap();
		map.put("userName",user==null?"":user.getUsername());
		map.put("money",user==null?"":userMoneyService.getMoney(user.getId()));
		renderJSON(map);
	}

	/**
	 * 获取邀请人获得的返佣概况
	 */
	@ResponseBody
	@RequestMapping(value = "/inviteOverview", method = RequestMethod.GET)
	public void inviteOverview() {
		SysUser sysUser = LoginMemberUtil.currentUser();
		JSONObject result = sysUserBonusService.inviteOverview(sysUser,null);
		if (result != null) {
			result.put("prompInfo", getPrompLink());
		}
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", result);
		json.put("moneyUnit", StationConfigUtil.get(sysUser.getStationId(), StationConfigEnum.money_unit));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	public StationPromotion getPrompLink() {
		StationPromotion stationPromotion = new StationPromotion();
		if (ProxyModelUtil.canBeRecommend(SystemUtil.getStationId())
				&& LoginMemberUtil.currentUser().getType() == UserTypeEnum.MEMBER.getType()) {
			stationPromotion = stationPromotionService.memberRecommendLink(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
		} else if (LoginMemberUtil.currentUser().getType() == UserTypeEnum.PROXY.getType()) {
			List<StationPromotion> links = stationPromotionService.getList(LoginMemberUtil.getUserId(), SystemUtil.getStationId(),
					LoginMemberUtil.getUsername(), null);
			if (links != null && !links.isEmpty()) {
				stationPromotion = links.get(0);
				stationPromotion.setLinkUrl(stationPromotion.getDomain() + "/r/" + stationPromotion.getCode() + ".do");
			}
		}
		return stationPromotion;
	}

	/**
	 * 获取被邀请人的存款信息列表
	 * @param startTime
	 * @param endTime
	 */
	@ResponseBody
	@RequestMapping(value = "/inviteDeposits", method = RequestMethod.GET)
	public void invitePersons(String startTime,String endTime, Integer bonusType) {
		SysUser sysUser = LoginMemberUtil.currentUser();
		Date begin = DateUtil.toDatetime(startTime);
		Date end = DateUtil.toDatetime(endTime);
		if (begin == null) {
			begin = DateUtil.dayFirstTime(new Date(),-15);
		}
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(),0);
		}
		//默认拿下级存款记录
		List<SysUserBonus> list = sysUserBonusService.depositOfInvites(sysUser, begin, end);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", list);
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 获取被邀请人的奖金信息列表
	 * @param startDate
	 * @param endDate
	 */
	@ResponseBody
	@RequestMapping(value = "/inviteBonus", method = RequestMethod.GET)
	public void invitePersonsBonus(String startDate,String endDate) {
		SysUser sysUser = LoginMemberUtil.currentUser();
		Date begin = DateUtil.toDatetime(startDate);
		Date end = DateUtil.toDatetime(endDate);
		if (begin == null) {
			begin = DateUtil.dayFirstTime(new Date(),-30);
		}
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(),0);
		}
		List<SysUserBonus> list = sysUserBonusService.inviteBonus(sysUser, begin, end);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", list);
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}
}
