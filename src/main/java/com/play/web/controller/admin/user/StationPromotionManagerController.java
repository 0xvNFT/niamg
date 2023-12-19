package com.play.web.controller.admin.user;

import java.util.Map;

import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.ProxyModelUtil;
import com.play.model.StationPromotion;
import com.play.model.StationRebate;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationPromotionService;
import com.play.service.StationRebateService;
import com.play.service.ThirdGameService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.utils.RebateUtil;
import com.play.web.var.SystemUtil;

/**
 * 会员推广链接管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/promotion")
public class StationPromotionManagerController extends AdminBaseController {

	@Autowired
	private StationPromotionService promotionService;
	@Autowired
	private ThirdGameService thirdGameService;
	@Autowired
	private StationRebateService stationRebateService;

	@Permission("admin:promotion")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("game", thirdGameService.findOne(stationId));
		map.put("rebate", stationRebateService.findOne(stationId, StationRebate.USER_TYPE_PROXY));
		int proxyModel = ProxyModelUtil.getProxyModel(stationId);
		map.put("proxyModel",proxyModel);
		map.put("memberRecommend", ProxyModelUtil.canBeRecommend(stationId, proxyModel));// 是否可以创建会员推荐好友
		return returnPage("/user/promotion/promotionIndex");
	}

	@ResponseBody
	@Permission("admin:promotion")
	@RequestMapping("/list")
	@NeedLogView("推广链接列表")
	public void list(Integer mode, Integer type, String username, String code) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			renderJSON(new Page<StationPromotion>());
		} else {
			renderJSON(promotionService.getPage(null, SystemUtil.getStationId(), mode, type, username, code));
		}

	}

	@ResponseBody
	@Permission("admin:promotion:delete")
	@RequestMapping("/delete")
	public void delete(Long id) {
		promotionService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	@Permission("admin:promotion:add")
	@RequestMapping("/showAddMember")
	public String showAddMember(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		if (!ProxyModelUtil.canBeRecommend(stationId)) {
			throw new BaseException(BaseI18nCode.stationMemberLinker);
		}
		map.put("game", thirdGameService.findOne(stationId));
		return returnPage("/user/promotion/promotionAddMember");
	}

	@ResponseBody
	@Permission("admin:promotion:add")
	@RequestMapping("/doAddMember")
	public void doAddMember(StationPromotion p) {
		Long stationId = SystemUtil.getStationId();
		if (!ProxyModelUtil.canBeRecommend(stationId)) {
			throw new BaseException(BaseI18nCode.stationMemberLinker);
		}
		p.setStationId(stationId);
		promotionService.adminSaveMember(p);
		renderSuccess();
	}

	@Permission("admin:promotion:add")
	@RequestMapping("/showAddProxy")
	public String showAddProxy(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		int proxyModel = ProxyModelUtil.getProxyModel(stationId);
		if (!ProxyModelUtil.canBePromo(proxyModel)) {
			throw new BaseException(BaseI18nCode.stationProxySpeedLink);
		}
		map.put("proxyModel",proxyModel);
		map.put("game", thirdGameService.findOne(stationId));
		RebateUtil.getRebateMap(stationId, null, map);
		return returnPage("/user/promotion/promotionAddProxy");
	}

	@ResponseBody
	@Permission("admin:promotion:add")
	@RequestMapping("/doAddProxy")
	public void doAddProxy(StationPromotion p) {
		Long stationId = SystemUtil.getStationId();
		if (!ProxyModelUtil.canBePromo(stationId)) {
			throw new BaseException(BaseI18nCode.stationProxySpeedLink);
		}
		p.setStationId(stationId);
		promotionService.adminSaveProxy(p);
		renderSuccess();
	}

	@Permission("admin:promotion:modify")
	@RequestMapping("/showModify")
	public String showModify(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		StationPromotion p = promotionService.findOne(id, stationId);
		map.put("p", p);
		int proxyModel = ProxyModelUtil.getProxyModel(stationId);
		if (p.getMode() == StationPromotion.MODE_MEMBER) {
			if(!ProxyModelUtil.canBeRecommend(stationId, proxyModel)) {
				throw new BaseException(BaseI18nCode.stationMemberLinker);
			}
			return returnPage("/user/promotion/promotionModifyMember");
		}
		if (!ProxyModelUtil.canBePromo(proxyModel)) {
			throw new BaseException(BaseI18nCode.stationProxySpeedLink);
		}
		map.put("proxyModel",proxyModel);
		map.put("game", thirdGameService.findOne(stationId));
		RebateUtil.getRebateMap(stationId, p.getUserId(), map);
		return returnPage("/user/promotion/promotionModifyProxy");
	}

	@ResponseBody
	@Permission("admin:promotion:modify")
	@RequestMapping("/doModifyMember")
	public void doModifyMember(Long id, String domain, Integer accessPage) {
		promotionService.adminModifyMember(id, SystemUtil.getStationId(), domain, accessPage);
		renderSuccess();
	}

	@ResponseBody
	@Permission("admin:promotion:modify")
	@RequestMapping("/doModifyProxy")
	public void doModifyProxy(StationPromotion p) {
		p.setStationId(SystemUtil.getStationId());
		promotionService.adminModifyProxy(p);
		renderSuccess();
	}

	@Permission("admin:promotion:modify")
	@RequestMapping("/showModifyAccessPage")
	public String showModifyAccessPage(Map<String, Object> map) {
		return returnPage("/user/promotion/promotionModifyAccessPage");
	}

	@ResponseBody
	@Permission("admin:promotion:modify")
	@RequestMapping("/doModifyAccessPage")
	public void doModifyAccessPage(Integer accessPage) {
		promotionService.adminUpdateAccessPage(SystemUtil.getStationId(), accessPage);
		renderSuccess();
	}

}
