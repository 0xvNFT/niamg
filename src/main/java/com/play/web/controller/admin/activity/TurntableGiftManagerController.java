package com.play.web.controller.admin.activity;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.play.common.SystemConfig;
import com.play.model.StationTurntableGift;
import com.play.service.StationTurntableGiftService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;


@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/turntableGift")
public class TurntableGiftManagerController extends AdminBaseController {
	@Autowired
	private StationTurntableGiftService giftService;

	//图片的宽度
	private final int imageWithNum = 30;
	//图片的高度
	private final int imageHeightNum = 30;

	/**
	 * 会员大转盘记录首页
	 */
	@Permission("admin:turntableGift")
	@RequestMapping("/index")
	public String index(Map<Object, Object> map) {
		return returnPage("/activity/turntableGift/turntableGiftIndex");
	}

	@Permission("admin:turntableGift")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("大转盘奖品列表")
	public void turntableAwardList() {
		renderJSON(giftService.getPage(SystemUtil.getStationId()));
	}

	/**
	 * 大转盘活动奖品添加
	 */
	@Permission("admin:turntableGift:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		return returnPage("/activity/turntableGift/turntableGiftAdd");
	}

	/**
	 * 大转盘活动奖品保存
	 */
	@Permission("admin:turntableGift:add")
	@RequestMapping("/doAdd")
	@ResponseBody
	public void doAdd(StationTurntableGift gift) {
		gift.setStationId(SystemUtil.getStationId());
		giftService.save(gift);
		renderSuccess();
	}
	/**
	 * 大转盘活动奖品删除
	 */
	@Permission("admin:turntableGift:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		giftService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 大转盘活动奖品添加
	 */
	@Permission("admin:turntableGift:modify")
	@RequestMapping("/showModify")
	@NeedLogView("大转盘奖品修改详情")
	public String showModify(Map<String, Object> map, Long id) {
		map.put("gift", giftService.findOne(id, SystemUtil.getStationId()));
		return returnPage("/activity/turntableGift/turntableGiftModify");
	}
	/**
	 * 大转盘活动奖品保存
	 */
	@Permission("admin:turntableGift:modify")
	@RequestMapping("/doModify")
	@ResponseBody
	public void doModify(StationTurntableGift gift) {
		gift.setStationId(SystemUtil.getStationId());
		giftService.modify(gift);
		renderSuccess();
	}
}
