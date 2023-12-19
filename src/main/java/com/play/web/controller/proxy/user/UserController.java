package com.play.web.controller.proxy.user;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.ip.IPAddressUtils;
import com.play.common.utils.DateUtil;
import com.play.common.utils.ProxyModelUtil;
import com.play.core.LogTypeEnum;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.model.StationRebate;
import com.play.model.SysUser;
import com.play.model.SysUserGroup;
import com.play.model.SysUserInfo;
import com.play.model.bo.UserRegisterBo;
import com.play.model.so.UserSo;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.service.MnyDepositRecordService;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationRebateService;
import com.play.service.SysUserBankService;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserDepositService;
import com.play.service.SysUserGroupService;
import com.play.service.SysUserInfoService;
import com.play.service.SysUserLoginService;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserRebateService;
import com.play.service.SysUserScoreService;
import com.play.service.SysUserService;
import com.play.service.SysUserWithdrawService;
import com.play.service.ThirdGameService;
import com.play.service.ThirdPlatformService;
import com.play.web.annotation.AdminReceiptPwdVerify;
import com.play.web.annotation.NeedLogView;
import com.play.web.controller.proxy.ProxyBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.HidePartUtil;
import com.play.web.utils.RebateUtil;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_PROXY + "/user")
public class UserController extends ProxyBaseController {
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserInfoService userInfoService;
	@Autowired
	private SysUserScoreService userScoreService;
	@Autowired
	private SysUserGroupService userGroupService;
	@Autowired
	private ThirdGameService thirdGameService;
	@Autowired
	private ThirdPlatformService platformService;
	@Autowired
	private SysUserLoginService userLoginService;
	@Autowired
	private SysUserMoneyService moneyService;
	@Autowired
	private SysUserRebateService userRebateService;
	@Autowired
	private SysUserDepositService userDepositService;
	@Autowired
	private SysUserWithdrawService userWithdrawService;
	@Autowired
	private MnyDepositRecordService mnyDepositRecordService;
	@Autowired
	private MnyDrawRecordService mnyDrawRecordService;
	@Autowired
	private SysUserBetNumService userBetNumService;
	@Autowired
	private SysUserBankService userBankService;
	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;
	@Autowired
	private StationRebateService stationRebateService;

	////@Permission("admin:user")
	@RequestMapping("/index")
	public String index(Map<String, Object> map, Integer depositStatus, boolean isToday,
			String queryDate, Long degreeId, Long groupId, String proxyPromoCode,String proxyName) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));

//		if (StringUtils.isEmpty(proxyName)){
//			proxyName = LoginMemberUtil.getUsername();
//		}else {
//			SysUser sysUser = userService.findOneByUsername(proxyName,SystemUtil.getStationId(),null);
//			if (sysUser!=null&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
//				proxyName = LoginMemberUtil.getUsername();
//			}
//		}

		map.put("proxyName", proxyName);
		map.put("depositStatus", depositStatus);
		map.put("degreeId", degreeId);
		map.put("groupId", groupId);
		map.put("proxyPromoCode", proxyPromoCode);
		if (isToday) {
			map.put("startTime", DateUtil.toDateStr(new Date()) + " 00:00:00");
			map.put("endTime", DateUtil.toDateStr(new Date()) + " 23:59:59");
		} else if (StringUtils.isNotEmpty(queryDate)) {
			map.put("startTime", queryDate + " 00:00:00");
			map.put("endTime", queryDate + " 23:59:59");
		}
		map.put("lowestLevel", userService.getLowestLevel(stationId, null));
		map.put("stationAdmin", LoginAdminUtil.isStationAdmin());
		map.put("showScore", StationConfigUtil.isOn(stationId, StationConfigEnum.mny_score_show));
		map.put("viewContact", false);
		map.put("proxyModel", ProxyModelUtil.getProxyModel(stationId));
		return returnPage("/user/userIndex");
	}


	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView(value = "站点域名列表", type = LogTypeEnum.VIEW_LIST)
	@SortMapping(mapping = { "money=money", "score=score", "createDatetime=create_datetime", "status=status",
			"lastLoginDatetime=last_login_datetime" })
	public void list(UserSo so) {
		String proxyName = so.getProxyName();
		if (StringUtils.isNotEmpty(proxyName)){
			SysUser sysUser = userService.findOneByUsername(proxyName,SystemUtil.getStationId(),null);
			if (sysUser!=null&&!sysUser.getUsername().equals(LoginMemberUtil.getUsername())&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
				proxyName = LoginMemberUtil.getUsername();
                so.setProxyName(proxyName);
			}
		}else if (StringUtils.isEmpty(so.getUsername())){
            proxyName = LoginMemberUtil.getUsername();
            so.setProxyName(proxyName);
        }
		String userName = so.getUsername();
        if (StringUtils.isNotEmpty(userName)){
            SysUser sysUser = userService.findOneByUsername(userName,SystemUtil.getStationId(),null);
            if (sysUser!=null&&!sysUser.getUsername().equals(LoginMemberUtil.getUsername())&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
                userName = LoginMemberUtil.getUsername();
                so.setUsername(userName);
            }
        }
		so.setStationId(SystemUtil.getStationId());
		super.renderPage(userService.getPageForAdmin(so, false));
	}



	/**
	 * 获取代理返点
	 */
	////@Permission("admin:user")
	@ResponseBody
	@RequestMapping("/getUserRebate")
	public void getUserRebate(Map<String, Object> map, String proxyName) {
		Long stationId = SystemUtil.getStationId();
		SysUser user = userService.findOneByUsername(proxyName, stationId, UserTypeEnum.PROXY.getType());
		JSONObject object = new JSONObject();
		if (user == null) {
			object.put("success", false);
			object.put("msg", BaseI18nCode.proxyUnExist.getMessage());
			renderJSON(object);
			return;
		}
		RebateUtil.getRebateMap(stationId, user.getId(), map);
		object.put("success", true);
		object.put("egameArray", map.get("egameArray"));
		object.put("liveArray", map.get("liveArray"));
		object.put("sportArray", map.get("sportArray"));
		object.put("chessArray", map.get("chessArray"));
		object.put("esportArray", map.get("esportArray"));
		object.put("fishingArray", map.get("fishingArray"));
		object.put("lotteryArray", map.get("lotteryArray"));
		renderJSON(object);
	}



	/**
	 * 密码修改页面
	 */
	////@Permission("admin:user:updatePwd")
	@RequestMapping("/showModifyPwd")
	public String showModifyPwd(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		SysUser user = userService.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		map.put("id", id);
		map.put("username", user.getUsername());
		map.put("receiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_acc_pwd_update));
		return returnPage("/user/userModifyPwd");
	}

	/**
	 * 密码修改保存
	 */
	@ResponseBody
	////@Permission("admin:user:updatePwd")
	@RequestMapping("doUpdatePwd")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_acc_pwd_update)
	public void doUpdatePwd(Long id, String pwd, String rpwd) {
		LoginAdminUtil.checkPerm();
		// 修改登陆密码
		userService.adminPwdModify(id, SystemUtil.getStationId(), pwd, rpwd);
		renderSuccess();
	}

	/**
	 * 密码修改页面
	 */
	////@Permission("admin:user:updatePayPwd")
	@RequestMapping("/showModifyPayPwd")
	public String showModifyPayPwd(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		SysUser user = userService.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		map.put("id", id);
		map.put("username", user.getUsername());
		map.put("receiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_acc_pwd_update));
		return returnPage("/user/userModifyPayPwd");
	}

	/**
	 * 密码修改保存
	 */
	@ResponseBody
	////@Permission("admin:user:updatePayPwd")
	@RequestMapping("doUpdatePayPwd")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_acc_pwd_update)
	public void doUpdatePayPwd(Long id, String pwd, String rpwd) {
		LoginAdminUtil.checkPerm();
		// 修改支付密码
		userInfoService.adminPayPwdModify(id, SystemUtil.getStationId(), pwd, rpwd);
		renderSuccess();
	}

	/**
	 * 会员积分清零
	 */
	@ResponseBody
	////@Permission("admin:user:resetScore")
	@RequestMapping("/scoreToZero")
	public void scoreToZero() {
		userScoreService.scoreToZero(SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 会员代理修改页面
	 */
	////@Permission("admin:user:update:proxy")
	@RequestMapping("/showModifyProxy")
	@NeedLogView("代理修改页面")
	public String showModifyProxy(Long id, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		SysUser user = userService.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		map.put("member", user);
		int proxyModel = ProxyModelUtil.getProxyModel(stationId);
		if (user.getType() == UserTypeEnum.MEMBER.getType()) {
			if (ProxyModelUtil.isAllMember(proxyModel)) {
				throw new BaseException(BaseI18nCode.stationAllVipWebMod);
			}
			if (ProxyModelUtil.isAllProxy(proxyModel)) {
				throw new BaseException(BaseI18nCode.stationAllWebProxy);
			}
			return returnPage("/user/userModifyProxyForMember");
		}
		if (!ProxyModelUtil.isMultiOrAllProxy(proxyModel)) {
			throw new BaseException(BaseI18nCode.stationMutilProxyMod);
		}
		map.put("thirdGame", thirdGameService.findOne(stationId));
		map.put("rebate", userRebateService.findOne(id, stationId));
		RebateUtil.getRebateMap(stationId, user.getProxyId(), map);
		return returnPage("/user/userModifyProxy");
	}

	/**
	 * 代理修改保存
	 */
	@ResponseBody
	////@Permission("admin:user:update:proxy")
	@RequestMapping("/doModifyProxy")
	public void doModifyProxy(Long id, UserRegisterBo rbo) {
		userService.modifyProxy(id, rbo, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 会员真实姓名修改页面
	 */
	////@Permission("admin:user:update:realName")
	@RequestMapping("/showModifyRealName")
	public String showModifyRealName(Long id, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		SysUser user = userService.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		map.put("memberInfo", userInfoService.findOne(id, stationId));
		return returnPage("/user/userModifyRealName");
	}

	/**
	 * 真实姓名修改保存
	 */
	@ResponseBody
	////@Permission("admin:user:update:realName")
	@RequestMapping("/doModifyRealName")
	public void doModifyRealName(Long id, String realName) {
		userInfoService.updateUserRealName(id, SystemUtil.getStationId(), realName);
		renderSuccess();
	}

	/**
	 * 会员等级修改页面
	 */
	////@Permission("admin:user:update:degree")
	@RequestMapping("/showModifyDegree")
	public String showModifyDegree(Long id, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		SysUser user = userService.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		map.put("member", user);
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/user/userModifyDegree");
	}

	/**
	 * 会员等级修改保存
	 */
	@ResponseBody
	////@Permission("admin:user:update:degree")
	@RequestMapping("/doModifyDegree")
	public void doModifyDegree(Long id, Long degreeId, Integer lockDegree) {
		//userDegreeService.changeMemberDegree(id, SystemUtil.getStationId(), degreeId, lockDegree, "手动修改", true);
		List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.stationManuallyModify.getCode());
		userDegreeService.changeMemberDegree(id, SystemUtil.getStationId(), degreeId, lockDegree, I18nTool.convertCodeToArrStr(remarkList), true);
		renderSuccess();
	}

	/**
	 * 会员组别修改页面
	 */
	////@Permission("admin:user:update:group")
	@RequestMapping("/showModifyGroup")
	public String showModifyGroup(Long id, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		SysUser user = userService.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		List<SysUserGroup> groups = userGroupService.find(stationId, Constants.STATUS_ENABLE);
		if (groups == null || groups.isEmpty()) {
			throw new BaseException(BaseI18nCode.userGroupEmpty);
		}
		map.put("member", user);
		map.put("groups", groups);
		return returnPage("/user/userModifyGroup");
	}

	/**
	 * 会员组别修改保存
	 */
	@ResponseBody
	////@Permission("admin:user:update:group")
	@RequestMapping("/doModifyGroup")
	public void doModifyGroup(Long id, Long groupId) {
		userGroupService.changeMemberGroup(id, SystemUtil.getStationId(), groupId);
		renderSuccess();
	}

	/**
	 * 会员联系方式修改
	 */
	////@Permission("admin:user:update:contact")
	@RequestMapping("/showModifyContact")
	@NeedLogView("会员联系方式修改详细")
	public String showModifyContact(Long id, Map<String, Object> map) {
		LoginAdminUtil.checkPerm();
		Long stationId = SystemUtil.getStationId();
		SysUser user = userService.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		map.put("username", user.getUsername());
		map.put("memberInfo", userInfoService.findOne(id, stationId));
		map.put("receiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_acc_pwd_update));
		return returnPage("/user/userModifyContact");
	}

	/**
	 * 联系方式修改保存
	 */
	@ResponseBody
	////@Permission("admin:user:update:contact")
	@RequestMapping("/doModifyContact")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_acc_update)
	public void doModifyContact(Long id, String facebook, String wechat, String qq, String phone, String email, String line) {
		LoginAdminUtil.checkPerm();
		userInfoService.updateContract(id, SystemUtil.getStationId(), facebook, wechat, qq, phone, email, line);
		renderSuccess();
	}


	/**
	 * 会员备注修改页面
	 */
	////@Permission("admin:user:update:remark")
	@RequestMapping("/showModifyRemark")
	public String showModifyRemark(Long id, Map<String, Object> map) {
		SysUser user = userService.findOneById(id, SystemUtil.getStationId());
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		map.put("member", user);
		return returnPage("/user/userModifyRemark");
	}



	////@Permission("admin:user:freezeMoney")
	@ResponseBody
	@RequestMapping("/freezeMoney")
	public void freeMoney(Long id, Integer status) {
		userService.freezeMoney(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}

	/**
	 * 会员导出
	 */
	////@Permission("admin:user:export")
	@RequestMapping("/export")
	@ResponseBody
	@SortMapping(mapping = { "money=money", "score=score", "createDatetime=create_datetime",
			"onlineStatus=online_status", "status=status", "lastLoginDatetime=last_login_datetime" })
	public void export(UserSo so) {
		LoginAdminUtil.checkPerm();
		so.setStationId(SystemUtil.getStationId());
		userService.export(so);
		renderSuccess();
	}


	@ResponseBody
	////@Permission("admin:user:update:status")
	@RequestMapping("/doBatchEnableStatus")
	public void doBatchEnableStatus(String usernames) {
		try {
			userService.batchEnableStatus(usernames, SystemUtil.getStationId());
		} catch (Exception e) {
			renderError(e.getMessage());
			return;
		}
		renderSuccess();
	}

	/**
	 * 用户详细
	 */
	////@Permission("admin:user")
	@RequestMapping("/detail")
	@NeedLogView("会员详细信息")
	public String detail(Map<String, Object> map, Long id, String proxyName) {
		Long stationId = SystemUtil.getStationId();
		SysUser user = null;
		if (Objects.isNull(id)) {
			user = userService.findOneByUsername(proxyName, stationId, null);
			map.put("money", moneyService.getMoney(user.getId()));
		} else {
			user = userService.findOneById(id, stationId);
			map.put("money", moneyService.getMoney(id));
		}

		if (user == null||(!user.getUsername().equals(LoginMemberUtil.getUsername())&&!user.getParentNames().contains(LoginMemberUtil.getUsername()))) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}

		user.setRegisterIp(user.getRegisterIp() + "(" + IPAddressUtils.getCountry(user.getRegisterIp()) + ")");
		map.put("user", user);
		map.put("degreeName", userDegreeService.getName(user.getDegreeId(), stationId));
		map.put("groupName", userGroupService.getName(user.getGroupId(), stationId));
//		if (PermissionForAdminUser.hadPermission("admin:user:view:contact")) {
//			SysUserInfo userInfo = userInfoService.findOne(id, stationId);
//			map.put("userInfo", contactHandle(userInfo));
//		}
		map.put("loginInfo", userLoginService.findOne(id, stationId));
		map.put("parentNames", StringUtils.isEmpty(user.getParentNames()) ? ""
				: user.getParentNames().substring(1, user.getParentNames().length() - 1));
		map.put("deposit", userDepositService.findOne(user.getId(), stationId));
		map.put("draw", userWithdrawService.findOne(user.getId(), stationId));
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date start = c.getTime();
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date end = c.getTime();
		map.put("todayDeposit", mnyDepositRecordService.getMoneyVo(user.getId(), stationId, start, end));
		map.put("todayDraw", mnyDrawRecordService.getMoneyVo(user.getId(), stationId, start, end));
		map.put("todayMoney", userDailyMoneyService.getDailyBet(user.getId(), stationId, start, end, null, null, null,
				null, null, null, null));
		map.put("bet", userBetNumService.findOne(id, stationId));
		map.put("thirdGame", thirdGameService.findOne(stationId));
		map.put("platforms", platformService.find(stationId));
		map.put("userScore", userScoreService.getScore(user.getId(), stationId));// 用户积分
		map.put("banks", userBankService.findByUserId(user.getId(), stationId, Constants.STATUS_ENABLE, null));
		if (user.getType() == UserTypeEnum.PROXY.getType()) {
			map.put("rebate", userRebateService.findOne(id, stationId));
		} else {
			if (!StationConfigUtil.isOff(stationId, StationConfigEnum.on_off_member_recommend)) {
				map.put("rebate", stationRebateService.findOne(stationId, StationRebate.USER_TYPE_MEMBER));
			}
		}
		return returnPage("/user/userDetail");
	}

	private SysUserInfo contactHandle(SysUserInfo info) {
		if (UserTypeEnum.ADMIN != SystemUtil.getUserType()) {// 不是站点管理员，则因此账号联系人信息
			info.setPhone(HidePartUtil.removePart(info.getPhone()));
			info.setRealName(HidePartUtil.removePart(info.getRealName()));
			info.setWechat(HidePartUtil.removePart(info.getWechat()));
			info.setEmail(HidePartUtil.removePart(info.getEmail()));
			info.setQq(HidePartUtil.removePart(info.getQq()));
			info.setFacebook(HidePartUtil.removePart(info.getFacebook()));
		}
		return info;
	}
}
