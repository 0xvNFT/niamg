package com.play.web.controller.admin.activity;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.model.StationScoreExchange;
import com.play.service.StationScoreExchangeService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

/**
 * 积分兑换配置
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/scoreExchange")
public class ScoreExchangeManagerController extends AdminBaseController {

	@Autowired
	private StationScoreExchangeService exchangeService;

	/**
	 * 积分兑换规则页面
	 */
	@Permission("admin:scoreExchange")
	@RequestMapping("/index")
	public String index(Map<Object, Object> map) {
		return returnPage("/activity/scoreExchange/scoreExchangeIndex");
	}

	/**
	 * 积分兑换配置列表
	 */
	@Permission("admin:scoreExchange")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("积分兑换配置列表")
	public void list(Integer type, String name) {
		renderJSON(exchangeService.getPage(type, name, SystemUtil.getStationId()));
	}

	/**
	 * 积分兑换配置添加页面
	 */
	@Permission("admin:scoreExchange:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<Object, Object> map) {
		return returnPage("/activity/scoreExchange/scoreExchangeAdd");
	}

	/**
	 * 积分兑换配置保存
	 */
	@Permission("admin:scoreExchange:add")
	@RequestMapping("/doAdd")
	@ResponseBody
	public void doAdd(StationScoreExchange se) {
		se.setStationId(SystemUtil.getStationId());
		exchangeService.save(se);
		renderSuccess();
	}

	/**
	 * 积分兑换配置修改页面
	 */
	@Permission("admin:scoreExchange:modify")
	@RequestMapping("/showModify")
	@NeedLogView("积分兑换配置详情")
	public String showModify(Map<Object, Object> map, Long id) {
		map.put("se", exchangeService.findOne(id, SystemUtil.getStationId()));
		return returnPage("/activity/scoreExchange/scoreExchangeModify");
	}

	/**
	 * 积分兑换配置保存
	 */
	@Permission("admin:scoreExchange:modify")
	@RequestMapping("/doModify")
	@ResponseBody
	public void doModify(StationScoreExchange se) {
		se.setStationId(SystemUtil.getStationId());
		exchangeService.modify(se);
		renderSuccess();
	}

	/**
	 * 积分兑换配置删除
	 */
	@Permission("admin:scoreExchange:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		exchangeService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 积分兑换配置改变状态
	 */
	@Permission("admin:scoreExchange:status")
	@RequestMapping("/changeStatus")
	@ResponseBody
	public void changeStatus(Long id, Integer status) {
		exchangeService.updStatus(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}
}
