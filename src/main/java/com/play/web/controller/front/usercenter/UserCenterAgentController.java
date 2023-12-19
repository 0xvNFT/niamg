package com.play.web.controller.front.usercenter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.play.common.Constants;
import com.play.common.utils.DateUtil;
import com.play.common.utils.ProxyModelUtil;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.model.StationRebate;
import com.play.model.StationRegisterConfig;
import com.play.model.SysUser;
import com.play.model.bo.UserRegisterBo;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.service.StationPromotionService;
import com.play.service.StationRebateService;
import com.play.service.StationRegisterConfigService;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.SysUserRebateService;
import com.play.service.SysUserRegisterService;
import com.play.service.SysUserService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.RebateUtil;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;

/**
 * 用户中心 代理管理
 *
 * @author admin
 */
@Controller
@RequestMapping("/userCenter/agentManage")
public class UserCenterAgentController extends FrontBaseController {

	@Autowired
	private SysUserRegisterService userRegisterService;
	@Autowired
	private StationPromotionService stationPromotionService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;
	@Autowired
	private SysUserRebateService userRebateService;
	@Autowired
	private StationRegisterConfigService regConfigService;
	@Autowired
	private StationRebateService stationRebateService;

	/**
	 * 代理最小层级值
	 */
	@ResponseBody
	@RequestMapping(value = "/levelInfo")
	public void levelInfo() {
		JSONObject json = new JSONObject();
		SysUser user = LoginMemberUtil.currentUser();
		if (user.getType() != UserTypeEnum.PROXY.getType()) {
			json.put("lowestLevel", 2);
			renderJSON(json);
			return;
		}
		int level = userService.getLowestLevel(user.getStationId(), user.getId());
		if (level > 1) {
			level = level - user.getLevel() + 1;
		}
		json.put("lowestLevel", level);
		renderJSON(json);
	}

	/**
	 * 用户列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/userListData")
	@SortMapping(mapping = { "createDatetime=create_datetime", "lastLoginDatetime=last_login_datetime",
			"onlineStatus=online_status" })
	public void userListData(Boolean include, String username, BigDecimal minBalance, BigDecimal maxBalance,
			String startTime, String endTime, BigDecimal depositTotal, Integer level, String sortName, String sortOrder,
			Integer pageSize, Integer pageNumber) {
		SysUser user = LoginMemberUtil.currentUser();
		Date begin = DateUtil.toDatetime(startTime);
		Date end = DateUtil.toDatetime(endTime);
		renderJSON(JSONObject.toJSONString(
				userService.getSubordinatePage(user, username, maxBalance, minBalance, begin, end, depositTotal,
						include, level, sortName, sortOrder, pageSize, pageNumber),
				SerializerFeature.WriteDateUseDateFormat));
	}

	/**
	 * 注册和推广链接需要用到的资料
	 */
	@ResponseBody
	@RequestMapping(value = "/agentRegPromotionInfo")
	public void agentRegPromotionInfo(Map<String, Object> map) {
		SysUser user = LoginMemberUtil.currentUser();
		Long stationId = user.getStationId();
		RebateUtil.getRebateMap(stationId, user.getId(), map);
		map.put("proxyModel", ProxyModelUtil.getProxyModel(stationId));
		map.put("memberRegConfigs",
				regConfigService.find(stationId, StationRegisterConfig.platform_member, Constants.STATUS_ENABLE)
						.stream()
						.map(stationRegisterConfig->{
							String name = I18nTool.getMessage(stationRegisterConfig.getCode());
							if (StringUtils.isNotEmpty(name)){
								stationRegisterConfig.setName(name);
							}
							return stationRegisterConfig;
						}).collect(Collectors.toList()));
		map.put("proxyRegConfigs",
				regConfigService.find(stationId, StationRegisterConfig.platform_proxy, Constants.STATUS_ENABLE)
						.stream()
						.map(stationRegisterConfig->{
							String name = I18nTool.getMessage(stationRegisterConfig.getCode());
							if (StringUtils.isNotEmpty(name)){
								stationRegisterConfig.setName(name);
							}
							stationRegisterConfig.setTips("");
							return stationRegisterConfig;
						}).collect(Collectors.toList()));
		renderJSON(map);
	}

	/**
	 * 注册保存
	 */
	@ResponseBody
	@RequestMapping(value = "/registerSave")
	public void save(UserRegisterBo registerBo) {
		userRegisterService.proxyAddUser(registerBo);
		renderSuccess();
	}

	/**
	 * 推广链接列表
	 */
	@ResponseBody
	@RequestMapping(value = "/agentRegPromotion/list")
	public void regPromotionList(HttpServletRequest request) {
		SysUser user = LoginMemberUtil.currentUser();
		validatorProxy(user);
		renderJSON(JSONObject.toJSONString(
				stationPromotionService.getPage(user.getId(), user.getStationId(), null, null, null, null),
				SerializerFeature.WriteDateUseDateFormat));
	}

	/**
	 * 保存推广链接
	 */
	@ResponseBody
	@RequestMapping(value = "/agentRegPromotion/save")
	public void saveAgentRegPromotion(Integer type, BigDecimal live, BigDecimal egame, BigDecimal sport,
			BigDecimal chess, BigDecimal esport, BigDecimal fishing, BigDecimal lottery, Integer accessPage) {
		SysUser user = LoginMemberUtil.currentUser();
		validatorProxy(user);
		stationPromotionService.saveProxyPromoLink(type, live, egame, sport, chess, esport, fishing, lottery,
				accessPage);
		renderSuccess();
	}

	/**
	 * 删除推广链接
	 */
	@ResponseBody
	@RequestMapping(value = "/agentRegPromotion/delete")
	public void deleteAgentRegPromotion(Long id) {
		SysUser user = LoginMemberUtil.currentUser();
		validatorProxy(user);
		stationPromotionService.deleteByProxyId(id, user.getId(), user.getStationId());
		renderSuccess();
	}

	/**
	 * 修改推广链接页面
	 */
	@ResponseBody
	@RequestMapping("/updateAccessPage")
	public void updatePromoLink(Long id, Integer accessPage) {
		SysUser user = LoginMemberUtil.currentUser();
		stationPromotionService.updateAccessPage(id, user.getId(), user.getStationId(), accessPage);
		renderSuccess();
	}

	/**
	 * 团队总览
	 */
	@ResponseBody
	@RequestMapping(value = "/agentTeam")
	public void agentTeam(Map<String, Object> map, String startDate, String endDate, String username) {
		SysUser user = LoginMemberUtil.currentUser();
		Date begin = DateUtil.toDate(startDate);
		if (begin == null) {
			begin = DateUtil.dayFirstTime(new Date(), 0);
		}
		Date end = DateUtil.toDate(endDate);
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(), 0);
		}
		renderJSON(userDailyMoneyService.teamOverview(user, username, begin, end));
	}

	/**
	 * 团队总览用户列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "/userTeamListData")
	public void userTeamListData(Boolean include, String proxyName, String start, String end, Integer type,
			Integer pageSize, Integer pageNumber) {
		if (include == null) {
			include = true;
		}
		SysUser user = LoginMemberUtil.currentUser();
		Date beginTime = DateUtil.toDatetime(start);
		Date endTime = DateUtil.toDatetime(end);
		if (type != 7) {
			renderJSON(JSONObject.toJSONString(userService.getUserTeamDataPage(user, proxyName, beginTime, endTime,
					type, include, pageSize, pageNumber), SerializerFeature.WriteDateUseDateFormat));
		} else {
			renderJSON(JSONObject.toJSONString(
					userDailyMoneyService.betUserPage(user, beginTime, endTime, pageSize, pageNumber),
					SerializerFeature.WriteDateUseDateFormat));
		}
	}

	/**
	 * 推荐总览列表
	 */
	@ResponseBody
	@RequestMapping("/recommendList")
	public void recommendList(String date, String username, Integer pageSize, Integer pageNumber) {
		SysUser user = LoginMemberUtil.currentUser();
		validatorMember(user);
		renderJSON(
				JSONObject.toJSONString(userDailyMoneyService.recommendList(user, date, username, pageSize, pageNumber),
						SerializerFeature.WriteDateUseDateFormat));
	}

	/**
	 * 返点重设
	 */
	@ResponseBody
	@RequestMapping(value = "/kickbackResetInfo")
	public void kickbackResetInfo(Map<String, Object> map, Long userId) {
		SysUser loginUser = LoginMemberUtil.currentUser();
		validatorProxy(loginUser);
		if (Objects.equals(userId, loginUser.getId())) {
			throw new ParamException();
		}
		SysUser user = userService.findOneById(userId, loginUser.getStationId());
		if (user == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		if (!user.getParentIds().contains("," + loginUser.getId() + ",")) {
			throw new ParamException();
		}
		if (user.getType() != UserTypeEnum.PROXY.getType()) {
			throw new BaseException(BaseI18nCode.noProxyUser);
		}
		map.put("userId", userId);
		RebateUtil.getRebateMap(loginUser.getStationId(), loginUser.getId(), map);
		map.put("userRebate", userRebateService.findOne(userId, loginUser.getStationId()));
		renderJSON(map);
	}

	@ResponseBody
	@RequestMapping("/kickbackResetSave")
	public void kickbackRestSave(Long userId, BigDecimal live, BigDecimal sport, BigDecimal egame, BigDecimal chess,
			BigDecimal esport, BigDecimal fishing, BigDecimal lot) {
		SysUser loginUser = LoginMemberUtil.currentUser();
		validatorProxy(loginUser);
		if (Objects.equals(userId, loginUser.getId())) {
			throw new ParamException();
		}
		userService.modifyUserRebate(loginUser, userId, live, sport, egame, chess, esport, fishing, lot);
		renderSuccess();
	}

	@ResponseBody
	@RequestMapping(value = "/kickbackInfoForMember")
	public void kickbackInfoForMember(Map<String, Object> map, Long userId) {
		SysUser loginUser = LoginMemberUtil.currentUser();
		validatorProxy(loginUser);
		if (!StationConfigUtil.isOn(loginUser.getStationId(), StationConfigEnum.on_off_member_recommend)) {
			throw new BaseException(BaseI18nCode.stationMemberFirst);
		}
		SysUser user = userService.findOneById(userId, loginUser.getStationId());
		if (user == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		if (!user.getParentIds().contains("," + loginUser.getId() + ",")) {
			throw new ParamException();
		}
		if (user.getType() != UserTypeEnum.MEMBER.getType()) {
			throw new BaseException(BaseI18nCode.stationNotVipNum);
		}
		map.put("userId", userId);
		map.put("rebate", stationRebateService.findOne(loginUser.getStationId(), StationRebate.USER_TYPE_MEMBER));
		renderJSON(map);
	}

	/**
	 * 我的推荐
	 */
	@ResponseBody
	@RequestMapping("/recommendInfo")
	public void recommendInfo(Map<String, Object> map) {
		SysUser loginUser = LoginMemberUtil.currentUser();
		validatorMember(loginUser);
		renderJSON(JSONObject.toJSONString(
				stationPromotionService.memberRecommendLink(loginUser.getId(), loginUser.getStationId()),
				SerializerFeature.WriteDateUseDateFormat));
	}

	/**
	 * 我的推荐新增会员
	 */
	@ResponseBody
	@RequestMapping("/recommendAddMember")
	public void recommendAddMember(UserRegisterBo registerBo) {
		userRegisterService.memberAddMember(registerBo);
		renderSuccess();
	}

	/**
	 * 判断是否是代理
	 */
	private void validatorProxy(SysUser user) {
		if (user.getType() != UserTypeEnum.PROXY.getType()) {
			throw new ParamException();
		}
	}

	/**
	 * 判断是否是会员
	 * 
	 * @param user
	 */
	private void validatorMember(SysUser user) {
		if (user.getType() != UserTypeEnum.MEMBER.getType()) {
			throw new ParamException();
		}
	}

}
