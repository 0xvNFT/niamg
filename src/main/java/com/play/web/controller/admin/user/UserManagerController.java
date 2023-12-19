package com.play.web.controller.admin.user;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.play.common.utils.BigDecimalUtil;
import com.play.model.*;

import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.play.model.bo.UserRegisterBo;
import com.play.model.so.UserSo;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.orm.jdbc.page.Page;
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
import com.play.service.SysUserRegisterService;
import com.play.service.SysUserScoreService;
import com.play.service.SysUserService;
import com.play.service.SysUserWithdrawService;
import com.play.service.ThirdGameService;
import com.play.service.ThirdPlatformService;
import com.play.web.annotation.AdminReceiptPwdVerify;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.utils.HidePartUtil;
import com.play.web.utils.RebateUtil;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/user")
public class UserManagerController extends AdminBaseController {
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserRegisterService userRegisterService;
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
	private Logger logger = LoggerFactory.getLogger(UserManagerController.class);
	@Permission("admin:user")
	@RequestMapping("/index")
	public String index(Map<String, Object> map, String proxyName, Integer depositStatus, boolean isToday,
			String queryDate, Long degreeId, Long groupId, String proxyPromoCode, String oldProxyName) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("proxyName", proxyName);
		map.put("oldProxyName", oldProxyName);
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
		map.put("viewContact", PermissionForAdminUser.hadPermission("admin:user:view:contact"));
		map.put("proxyModel", ProxyModelUtil.getProxyModel(stationId));
		return returnPage("/user/userIndex");
	}

	@Permission("admin:user")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView(value = "站点域名列表", type = LogTypeEnum.VIEW_LIST)
	@SortMapping(mapping = { "money=money", "score=score", "createDatetime=create_datetime", "status=status",
			"lastLoginDatetime=last_login_datetime" })
	public void list(UserSo so) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		logger.error("query user list usertype = " + LoginAdminUtil.currentUser().getType() + ",can view = " + hasViewAll);
		if ((!hasViewAll || LoginAdminUtil.currentUser().getType() == UserTypeEnum.ADMIN_MASTER.getType())
				&& StringUtils.isEmpty(so.getUsername()) && StringUtils.isEmpty(so.getUsernameLike())
				&& StringUtils.isEmpty(so.getProxyName())) {
			renderPage(new Page<>());
			return;
		}
		boolean viewContact = PermissionForAdminUser.hadPermission("admin:user:view:contact");
		so.setStationId(SystemUtil.getStationId());
		super.renderPage(userService.getPageForAdmin(so, viewContact));
	}

	/**
	 * 会员添加页面
	 */
	@Permission("admin:user:add")
	@RequestMapping("/showAddMember")
	public String showAddMember(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		int proxyModel = ProxyModelUtil.getProxyModel(stationId);
		if (ProxyModelUtil.isAllProxy(proxyModel)) {
			throw new BaseException(BaseI18nCode.stationAllProxyModel);
		}
		map.put("proxyModel", proxyModel);
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/user/userAddMember");
	}

	/**
	 * 会员新增保存
	 */
	@ResponseBody
	@Permission("admin:user:add")
	@RequestMapping("/doAddMember")
	public void doAddMember(UserRegisterBo rbo) {
		userRegisterService.adminSaveMember(rbo);
		renderSuccess();
	}

	/**
	 * 添加代理
	 * 
	 * @param map
	 * @return
	 */
	@Permission("admin:user:add")
	@RequestMapping("/showAddProxy")
	public String showAddProxy(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		int proxyModel = ProxyModelUtil.getProxyModel(stationId);
		if (ProxyModelUtil.isAllMember(proxyModel)) {
			throw new BaseException(BaseI18nCode.stationVipModel);
		}
		map.put("proxyModel", proxyModel);
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("thirdGame", thirdGameService.findOne(stationId));
		RebateUtil.getRebateMap(stationId, null, map);
		return returnPage("/user/userAddProxy");
	}

	/**
	 * 获取代理返点
	 */
	@Permission("admin:user")
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
	 * 代理新增保存
	 */
	@ResponseBody
	@Permission("admin:user:add")
	@RequestMapping("/doAddProxy")
	public void doAddProxy(UserRegisterBo rbo) {
		userRegisterService.adminSaveProxy(rbo);
		renderSuccess();
	}

	/**
	 * 密码修改页面
	 */
	@Permission("admin:user:updatePwd")
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
	@Permission("admin:user:updatePwd")
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
	@Permission("admin:user:updatePayPwd")
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
	@Permission("admin:user:updatePayPwd")
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
	@Permission("admin:user:resetScore")
	@RequestMapping("/scoreToZero")
	public void scoreToZero() {
		userScoreService.scoreToZero(SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 会员代理修改页面
	 */
	@Permission("admin:user:update:proxy")
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
	@Permission("admin:user:update:proxy")
	@RequestMapping("/doModifyProxy")
	public void doModifyProxy(Long id, UserRegisterBo rbo) {
		userService.modifyProxy(id, rbo, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 会员真实姓名修改页面
	 */
	@Permission("admin:user:update:realName")
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
	@Permission("admin:user:update:realName")
	@RequestMapping("/doModifyRealName")
	public void doModifyRealName(Long id, String realName) {
		userInfoService.updateUserRealName(id, SystemUtil.getStationId(), realName);
		renderSuccess();
	}

	/**
	 * 会员等级修改页面
	 */
	@Permission("admin:user:update:degree")
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
	@Permission("admin:user:update:degree")
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
	@Permission("admin:user:update:group")
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
	@Permission("admin:user:update:group")
	@RequestMapping("/doModifyGroup")
	public void doModifyGroup(Long id, Long groupId) {
		userGroupService.changeMemberGroup(id, SystemUtil.getStationId(), groupId);
		renderSuccess();
	}

	/**
	 * 会员联系方式修改
	 */
	@Permission("admin:user:update:contact")
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
	@Permission("admin:user:update:contact")
	@RequestMapping("/doModifyContact")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_acc_update)
	public void doModifyContact(Long id, String facebook, String wechat, String qq, String phone, String email, String line) {
		LoginAdminUtil.checkPerm();
		userInfoService.updateContract(id, SystemUtil.getStationId(), facebook, wechat, qq, phone, email, line);
		renderSuccess();
	}

	/**
	 * 会员可用状态修改
	 */
	@ResponseBody
	@Permission("admin:user:update:status")
	@RequestMapping("/changeStatus")
	public void changeStatus(Long id, Integer status) {
		userService.changeStatus(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}

	/**
	 * 会员备注修改页面
	 */
	@Permission("admin:user:update:remark")
	@RequestMapping("/showModifyRemark")
	public String showModifyRemark(Long id, Map<String, Object> map) {
		SysUser user = userService.findOneById(id, SystemUtil.getStationId());
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		map.put("member", user);
		return returnPage("/user/userModifyRemark");
	}

	/**
	 * 备注修改保存
	 */
	@ResponseBody
	@Permission("admin:user:update:remark")
	@RequestMapping("/doModifyRemark")
	public void doModifyRemark(Long id, String remark) {
		userService.modifyRemark(id, SystemUtil.getStationId(), remark);
		renderSuccess();
	}

	@Permission("admin:user:freezeMoney")
	@ResponseBody
	@RequestMapping("/freezeMoney")
	public void freeMoney(Long id, Integer status) {
		userService.freezeMoney(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}

	/**
	 * 会员导出
	 */
	@Permission("admin:user:export")
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

	/**
	 * 会员等级批量修改页面
	 */
	@Permission("admin:user:update:degree")
	@RequestMapping("/showBatchModifyDegree")
	public String showBatchModifyDegree(Map<String, Object> map) {
		map.put("degrees", userDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
		return returnPage("/user/userBatchModifyDegree");
	}

	/**
	 * 会员等级批量修改保存
	 */
	@ResponseBody
	@Permission("admin:user:update:degree")
	@RequestMapping("/doBatchModifyDegree")
	public void doBatchModifyDegree(String usernames, Long degreeId, Integer lockDegree) {
		try {
			userDegreeService.batchChangeDegree(usernames, SystemUtil.getStationId(), degreeId, lockDegree);
		} catch (Exception e) {
			renderError(e.getMessage());
			return;
		}
		renderSuccess();
	}

	/**
	 * 会员组别批量修改页面
	 */
	@Permission("admin:user:update:group")
	@RequestMapping("/showBatchModifyGroup")
	public String showBatchModifyGroup(Map<String, Object> map) {
		map.put("groups", userGroupService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
		return returnPage("/user/userBatchModifyGroup");
	}

	/**
	 * 会员组别批量修改保存
	 */
	@ResponseBody
	@Permission("admin:user:update:group")
	@RequestMapping("/doBatchModifyGroup")
	public void doBatchModifyGroup(String usernames, Long groupId) {
		try {
			userGroupService.batchChangeGroup(usernames, groupId, SystemUtil.getStationId());
		} catch (Exception e) {
			renderError(e.getMessage());
			return;
		}
		renderSuccess();
	}

	/**
	 * 批量修改代理
	 */
	@Permission("admin:user:update:proxy")
	@RequestMapping("/showBatchModifyProxy")
	public String showBatchModifyProxy(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		int proxyModel = ProxyModelUtil.getProxyModel(stationId);
		if (ProxyModelUtil.isAllMember(proxyModel)) {
			throw new BaseException(BaseI18nCode.stationVipModel);
		}
		map.put("thirdGame", thirdGameService.findOne(stationId));
		RebateUtil.getRebateMap(stationId, null, map);
		return returnPage("/user/userBatchModifyProxy");
	}

	@Permission("admin:user:update:proxy")
	@ResponseBody
	@RequestMapping("/doBatchModifyProxy")
	public void doBatchModifyProxy(String usernames, UserRegisterBo rbo) {
		try {
			userService.batchChangeProxy(rbo, usernames, SystemUtil.getStationId());
		} catch (Exception e) {
			renderError(e.getMessage());
			return;
		}
		renderSuccess();
	}

	/**
	 * 批量禁用用户
	 */
	@Permission("admin:user:update:status")
	@RequestMapping("/showBatchDisableStatus")
	public String showBatchDisableStatus(Long id, Map<String, Object> map) {
		return returnPage("/user/userBatchDisableStatus");
	}

	/**
	 * 批量禁用用户
	 * 
	 * @param usernames
	 */
	@ResponseBody
	@Permission("admin:user:update:status")
	@RequestMapping("/doBatchDisableStatus")
	public void doBatchDisableStatus(String usernames) {
		try {
			userService.batchDisableStatus(usernames, SystemUtil.getStationId());
		} catch (Exception e) {
			renderError(e.getMessage());
			return;
		}
		renderSuccess();
	}

	/**
	 * 用户批量启用修改页面
	 */
	@Permission("admin:user:update:status")
	@RequestMapping("/showBatchEnableStatus")
	public String showBatchEnableStatus(Long id, Map<String, Object> map) {
		return returnPage("/user/userBatchEnableStatus");
	}

	@ResponseBody
	@Permission("admin:user:update:status")
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
	@Permission("admin:user")
	@RequestMapping("/detail")
	@NeedLogView("会员详细信息")
	public String detail(Map<String, Object> map, Long id, String proxyName) {
		//logger.error("UserManagerController===========>"+"111111111111111111111");
		Long stationId = SystemUtil.getStationId();
	//	logger.error("UserManagerController===========>"+"AAAAAAAAAAAAAAAAAAAAAAA");
		SysUser user = null;
		if (Objects.isNull(id)) {
			user = userService.findOneByUsername(proxyName, stationId, null);
		//	logger.error("UserManagerController===========>"+"BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
			if (user == null) {
				throw new BaseException(BaseI18nCode.memberUnExist);
			}
			map.put("money", moneyService.getMoney(user.getId()));
		} else {
			user = userService.findOneById(id, stationId);
			map.put("money", moneyService.getMoney(id));
		}
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		user.setRegisterIp(user.getRegisterIp() + "(" + IPAddressUtils.getCountry(user.getRegisterIp()) + ")");
		map.put("user", user);
		map.put("degreeName", userDegreeService.getName(user.getDegreeId(), stationId));
		map.put("groupName", userGroupService.getName(user.getGroupId(), stationId));
		if (PermissionForAdminUser.hadPermission("admin:user:view:contact")) {
			SysUserInfo userInfo = userInfoService.findOne(id, stationId);
			map.put("userInfo", contactHandle(userInfo));
		}
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

	/**
	 * 会员转成代理页面
	 */
	@Permission("admin:user:update:proxy")
	@RequestMapping("/userToProxy")
	@NeedLogView("会员转代理详情")
	public String userToProxy(Long id, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		SysUser sysUser = userService.findOneById(id, stationId);
		if (sysUser == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}

		SysUserRebate sysUserRebate = null;
		if (sysUser.getProxyId() != null) {
			SysUser proxy = userService.findOneById(sysUser.getProxyId(), stationId);
			RebateUtil.getRebateMap(stationId, proxy.getId(), map);

			sysUserRebate = userRebateService.findOne(sysUser.getProxyId(), stationId);// 父级返点策略
			if(sysUserRebate.getLive() == null){
				sysUserRebate.setLive(BigDecimal.ZERO);
			}
			if(sysUserRebate.getEgame() == null){
				sysUserRebate.setEgame(BigDecimal.ZERO);
			}
			if(sysUserRebate.getSport() == null){
				sysUserRebate.setSport(BigDecimal.ZERO);
			}
			if(sysUserRebate.getChess() == null){
				sysUserRebate.setChess(BigDecimal.ZERO);
			}
			if(sysUserRebate.getFishing() == null){
				sysUserRebate.setFishing(BigDecimal.ZERO);
			}
			if(sysUserRebate.getEsport() == null ){
				sysUserRebate.setEsport(BigDecimal.ZERO);
			}
			if(sysUserRebate.getLottery() == null){
				sysUserRebate.setLottery(BigDecimal.ZERO);
			}
		} else {
			RebateUtil.getRebateMap(stationId, null, map);
			sysUserRebate = new SysUserRebate();
			sysUserRebate.setLive(BigDecimalUtil
					.toBigDecimal(StationConfigUtil.get(stationId, StationConfigEnum.max_live_rebate_value)));
			sysUserRebate.setEgame(BigDecimalUtil
					.toBigDecimal(StationConfigUtil.get(stationId, StationConfigEnum.max_egame_rebate_value)));
			sysUserRebate.setSport(BigDecimalUtil
					.toBigDecimal(StationConfigUtil.get(stationId, StationConfigEnum.max_sport_rebate_value)));
			sysUserRebate.setChess(BigDecimalUtil
					.toBigDecimal(StationConfigUtil.get(stationId, StationConfigEnum.max_chess_rebate_value)));
			sysUserRebate.setFishing(BigDecimalUtil
					.toBigDecimal(StationConfigUtil.get(stationId, StationConfigEnum.max_fishing_rebate_value)));
			sysUserRebate.setEsport(BigDecimalUtil
					.toBigDecimal(StationConfigUtil.get(stationId, StationConfigEnum.max_esport_rebate_value)));
			sysUserRebate.setLottery(BigDecimalUtil
					.toBigDecimal(StationConfigUtil.get(stationId, StationConfigEnum.max_lottery_rebate_value)));
		}
		map.put("member", sysUser);
		map.put("scale", sysUserRebate);
		return returnPage("/user/userToProxyModify");
	}

	/**
	 * 会员转代理
	 */
	@Permission("account:memberToProxy")
	@ResponseBody
	@RequestMapping("/saveUserToProxy")
	public void saveMemberToProxy(Long userId, BigDecimal liveRebate, BigDecimal sportRebate, BigDecimal egameRebate,
								  BigDecimal chessRebate, BigDecimal esportRebate, BigDecimal fishingRebate, BigDecimal lotteryRebate) {
		userService.changeUserToProxy(userId, SystemUtil.getStationId(), liveRebate, sportRebate, egameRebate,
				chessRebate, esportRebate, fishingRebate, lotteryRebate);
		renderSuccess();
	}

}
