package com.play.web.controller.admin.activity;

import java.math.BigDecimal;
import java.util.Map;

import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationDummyData;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.service.StationDummyDataService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.var.SystemUtil;

/**
 * 假数据管理
 * 
 * @author macair
 *
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/dummyData")
public class DummyDataManagerController extends AdminBaseController {

	@Autowired
	private StationDummyDataService dummyDataService;

	@Permission("admin:dummyData")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		return returnPage("/activity/dummyData/dummyDataIndex");
	}

	@ResponseBody
	@Permission("admin:dummyData")
	@RequestMapping("/list")
	@NeedLogView("假数据列表")
	@SortMapping(mapping = { "winMoney=win_money", "winTime=win_time" })
	public void list(Integer type, String begin, String end) {
		renderJSON(dummyDataService.getPage(SystemUtil.getStationId(), type, begin, end));
	}

	@Permission("admin:dummyData:add")
	@RequestMapping("/showAdd")
	public String showAdd() {
		return returnPage("/activity/dummyData/dummyDataAdd");
	}

	@ResponseBody
	@Permission("admin:dummyData:add")
	@RequestMapping("/doAdd")
	public void showAdd(StationDummyData data, String winTimeStr) {
		data.setWinTime(DateUtil.toDatetime(winTimeStr));
		data.setStationId(SystemUtil.getStationId());
		dummyDataService.save(data);
		renderSuccess();
	}

	@ResponseBody
	@Permission("admin:dummyData:delete")
	@RequestMapping("/delete")
	public void delete(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new BaseException(BaseI18nCode.parameterError);
		}
		String[] dids = ids.split(",");
		Long stationId=SystemUtil.getStationId();
		for (String did : dids) {
			dummyDataService.delete(Long.parseLong(did), stationId);
		}
		renderSuccess();
	}

	/**
	 * 随机生成假数据
	 *
	 * @return
	 */
	@Permission("admin:dummyData:add")
	@RequestMapping("/showAddWinData")
	public String showAddWinData() {
		return returnPage("/activity/dummyData/dummyDataAddWinData");
	}

	@Permission("admin:dummyData:add")
	@RequestMapping("/saveWinData")
	@ResponseBody
	public void saveWinData(StationDummyData data, Integer generateNum, String winTimeStr, String winTimeEnd,
			BigDecimal winMoneyMax) {
		data.setStationId(SystemUtil.getStationId());
		dummyDataService.saveWinData(data, generateNum, winTimeStr, winTimeEnd, winMoneyMax);
		renderSuccess();
	}
}
