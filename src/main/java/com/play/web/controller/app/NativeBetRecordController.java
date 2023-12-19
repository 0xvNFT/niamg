package com.play.web.controller.app;

import static com.play.web.utils.ControllerRender.renderJSON;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.play.common.utils.DateUtil;
import com.play.service.*;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.play.common.SystemConfig;
import com.play.common.utils.PlatformTypeUtil;
import com.play.core.PlatformType;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.model.SysUser;
import com.play.model.ThirdGame;
import com.play.model.vo.ThirdPlatformSwitch;
import com.play.model.vo.ThirdSearchVo;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE + "/v2")
public class NativeBetRecordController extends BaseNativeController {

	//Logger logger = LoggerFactory.getLogger(NativeHomeController.class);
	@Autowired
	private ThirdCenterService thirdCenterService;
	@Autowired
	private SysUserService userService;
	@Autowired
	ThirdGameService thirdGameService;
	@Autowired
	private ThirdPlatformService platformService;
	@Autowired
	private YGCenterService ygCenterService;

	private JSONObject toJsonRecord(String content) {
		if (StringUtils.isEmpty(content)) {
			return null;
		}
		return JSONObject.parseObject(content);
	}

	/**
	 * 真人投注记录
	 */
	@ResponseBody
	@RequestMapping("/liveRecord")
	public void liveRecord(ThirdSearchVo v) {
		initSearchVo(v);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", toJsonRecord(thirdCenterService.getLivePage(v)));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 电子投注记录
	 */
	@ResponseBody
	@RequestMapping("/egameRecord")
	public void egameRecord(ThirdSearchVo v) {
		initSearchVo(v);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", toJsonRecord(thirdCenterService.getEgamePage(v)));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * pt电子记录
	 *
	 * @param v
	 */
	@ResponseBody
	@RequestMapping("/ptRecord")
	public void ptRecord(ThirdSearchVo v) {
		initSearchVo(v);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", toJsonRecord(thirdCenterService.getPtPage(v)));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 捕鱼投注记录
	 */
	@ResponseBody
	@RequestMapping("/fishingRecord")
	public void fishingRecord(ThirdSearchVo v) {
		initSearchVo(v);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", toJsonRecord(thirdCenterService.getFishingPage(v)));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 电竞投注记录
	 */
	@ResponseBody
	@RequestMapping("/esportRecord")
	public void esportRecord(ThirdSearchVo v) {
		initSearchVo(v);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", toJsonRecord(thirdCenterService.getEsportPage(v)));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 体育投注记录
	 */
	@ResponseBody
	@RequestMapping("/sportRecord")
	public void sportRecord(ThirdSearchVo v) {
		initSearchVo(v);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", toJsonRecord(thirdCenterService.getSportPage(v)));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 棋牌投注记录
	 */
	@ResponseBody
	@RequestMapping("/chessRecord")
	public void chessRecord(ThirdSearchVo v) {
		initSearchVo(v);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", toJsonRecord(thirdCenterService.getChessPage(v)));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 彩票投注记录
	 */
	@ResponseBody
	@RequestMapping("/lotRecord")
	public void lotRecord(ThirdSearchVo v) {

		if (StringUtils.isBlank(v.getStartTime())) {
			Date start = DateUtil.dayFirstTime(new Date(), -1);
			v.setStartTime(DateUtil.toDatetimeStr(start));
		}

		if (StringUtils.isBlank(v.getEndTime())) {
			Date end = DateUtil.dayEndTime(new Date(), 0);
			v.setEndTime(DateUtil.toDatetimeStr(end));
		}

		if (StringUtils.isEmpty(v.getUsername())) {
			v.setUsername(LoginMemberUtil.getUsername());
		}

		v.setStation(SystemUtil.getStation());
		String lotteryPage = thirdCenterService.getLotteryPage(v);
//		JSONObject lotteryOrder = ygCenterService.getLotteryOrder(v.getOrderId(), v.getUsername(), v.getQiHao(),
//				v.getLotCode(), DateUtil.toDatetimeStr(start), DateUtil.toDatetimeStr(end), v.getPageNumber(), v.getPageSize());


		JSONObject obj = JSONObject.parseObject(lotteryPage);
		if (obj.getBooleanValue("success") && !obj.getBoolean("success")) {
			renderJSON(obj);
			return;
		}

		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", ygCenterService.convertYGData(obj));
		json.put("accessToken", ServletUtils.getSession().getId());

		renderJSON(json);
	}

	private ThirdSearchVo initSearchVo(ThirdSearchVo v) {
		v.setStation(SystemUtil.getStation());
		SysUser user = LoginMemberUtil.currentUser();
		String parentIds = null;
		Long recommendId = null;
		boolean isMember = Objects.equals(user.getType(), UserTypeEnum.MEMBER.getType());
		String username = v.getUsername();
		boolean isContainSub = (v.getContainSub() != null && v.getContainSub());
		if (StationConfigUtil.isOff(user.getStationId(), StationConfigEnum.proxy_view_account_data)) {
			username = user.getUsername();
		} else {
			if (StringUtils.isNotEmpty(username) && !user.getUsername().equals(username)) {
				SysUser su = userService.findOneByUsername(username, user.getStationId(), null);
				if (su == null) {
					throw new ParamException(BaseI18nCode.searchUserNotExist);
				}
				if (isMember) {// 会员则判断是否是推荐好友来的
					if (!su.getRecommendId().equals(user.getId())) {
						throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
					}
					username = su.getUsername();
				} else {// 代理推广来的
					if (user.getParentIds() == null || !user.getParentIds().contains("," + user.getId() + ",")) {
						throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
					}
					if (isContainSub && su.getType() == UserTypeEnum.PROXY.getType()) {
						parentIds = "," + su.getId() + ",";
					}
				}
			} else {
				if (isContainSub) {
					if (isMember) {
						recommendId = user.getId();
					} else {
						parentIds = "," + user.getId() + ",";
					}
				}
				username = user.getUsername();
			}
		}
		v.setUsername(username);
		v.setParentIds(parentIds);
		v.setRecommendId(recommendId);
		return v;
	}

	/**
	 * 获取各类游戏的平台数据
	 *
	 * @param type
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/thirdPlatforms")
	public void thirdPlatforms(String type) {
		Map<String, Object> map = new HashMap<>();
		ThirdGame game = thirdGameService.findOne(SystemUtil.getStationId());
		ThirdPlatformSwitch platform = platformService.getPlatformSwitch(SystemUtil.getStationId());
		if (type.equalsIgnoreCase("live")) {
			PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getLivePlatform());
		} else if (type.equalsIgnoreCase("egame")) {
			PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getEgamePlatform());
		} else if (type.equalsIgnoreCase("sport")) {
			PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getSportPlatforms());
		} else if (type.equalsIgnoreCase("esport")) {
			PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getEsportPlatforms());
		} else if (type.equalsIgnoreCase("fishing")) {
			PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getFishingPlatforms());
		} else if (type.equalsIgnoreCase("chess")) {
			PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getChessPlatform());
		}
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", map);
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	@ResponseBody
	@RequestMapping("/viewDetailLive")
	public void viewDetailLive(Long id, Integer platform) {
		if (platform == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}

		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", toJsonRecord(thirdCenterService.getDetailUrl(id, platform, 1, SystemUtil.getStation())));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}
}
