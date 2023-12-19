package com.play.web.controller.front.usercenter;

import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;
import com.play.service.YGCenterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.utils.DateUtil;
import com.play.common.utils.PlatformTypeUtil;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.model.SysUser;
import com.play.model.SysUserLogin;
import com.play.model.ThirdGame;
import com.play.model.vo.ThirdPlatformSwitch;
import com.play.model.vo.ThirdSearchVo;
import com.play.service.ThirdCenterService;
import com.play.service.ThirdPlatformService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

import static com.play.web.utils.ControllerRender.renderJSON;


/**
 * 用户中心
 *
 * @author admin
 */
@Controller
@RequestMapping("/userCenter/third")
public class UserCenterThirdController extends FrontBaseController {

	@Autowired
	private ThirdCenterService thirdCenterService;
	@Autowired
	private ThirdPlatformService platformService;
	@Autowired
	private YGCenterService ygCenterService;

	/**
	 * 额度转换页面需要的资料
	 */
	@ResponseBody
	@RequestMapping("/exchangePageInfo")
	public void index(Map<String, Object> map) {
		SysUser user = LoginMemberUtil.currentUser();
		ThirdGame game = thirdGameService.findOne(user.getStationId());
		ThirdPlatformSwitch platform = platformService.getPlatformSwitch(user.getStationId());
		PlatformTypeUtil.getThirdInfo(map, game, platform, user, SystemUtil.getStation());
		SysUserLogin sal = userLoginService.findOne(user.getId(), user.getStationId());
		if (sal != null) {
			map.put("lastLoginTime", DateUtil.toDateStr(sal.getLastLoginDatetime()));
		}
		map.put("score", userScoreService.getScore(user.getId(), user.getStationId()));
		renderJSON(map);
	}

	@ResponseBody
	@RequestMapping("/liveRecord")
	public void liveRecord(ThirdSearchVo v) {
		initSearchVo(v);
		renderJSON(thirdCenterService.getLivePage(v));
	}

	@ResponseBody
	@RequestMapping("/egameRecord")
	public void egameRecord(ThirdSearchVo v) {
		initSearchVo(v);
		renderJSON(thirdCenterService.getEgamePage(v));
	}

	@ResponseBody
	@RequestMapping("/ptRecord")
	public void ptRecord(ThirdSearchVo v) {
		initSearchVo(v);
		renderJSON(thirdCenterService.getPtPage(v));
	}

	@ResponseBody
	@RequestMapping("/chessRecord")
	public void chessRecord(ThirdSearchVo v) {
		initSearchVo(v);
		renderJSON(thirdCenterService.getChessPage(v));
	}

	@ResponseBody
	@RequestMapping("/fishRecord")
	public void fishRecord(ThirdSearchVo v) {
		initSearchVo(v);
		renderJSON(thirdCenterService.getFishingPage(v));
	}

	@ResponseBody
	@RequestMapping("/esportRecord")
	public void esportRecord(ThirdSearchVo v) {
		initSearchVo(v);
		renderJSON(thirdCenterService.getEsportPage(v));
	}

	@ResponseBody
	@RequestMapping("/sportRecord")
	public void sportRecord(ThirdSearchVo v) {
		initSearchVo(v);
		renderJSON(thirdCenterService.getSportPage(v));
	}

	@ResponseBody
	@RequestMapping("/lotRecord")
	public void lotRecord(ThirdSearchVo v) {
		initSearchVo(v);
		String lotteryPage = thirdCenterService.getLotteryPage(v);
		JSONObject obj = JSONObject.parseObject(lotteryPage);
		if (obj.getBooleanValue("success") && !obj.getBoolean("success")) {
			renderJSON(obj);
			return;
		}
		renderJSON(ygCenterService.convertYGData(obj));
//		Date end = DateUtil.toDatetime(v.getEndTime());
//		Date start = DateUtil.toDatetime(v.getStartTime());
//		if (start == null) {
//			start = DateUtil.dayFirstTime(new Date(), -7);
//		}
//		if (end == null) {
//			end = DateUtil.dayEndTime(new Date(), 0);
//		}
//		JSONObject lotteryOrder = ygCenterService.getLotteryOrder(v.getOrderId(), v.getUsername(), v.getQiHao(),
//				v.getLotCode(), DateUtil.toDatetimeStr(start), DateUtil.toDatetimeStr(end), v.getPageNumber(), v.getPageSize());
//		renderJSON(ygCenterService.convertYGData(lotteryOrder));
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
}
