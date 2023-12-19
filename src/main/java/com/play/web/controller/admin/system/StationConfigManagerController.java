package com.play.web.controller.admin.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.SystemConfig;
import com.play.model.StationConfig;
import com.play.service.StationConfigService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/stationConfig")
public class StationConfigManagerController extends AdminBaseController {
	@Autowired
	private StationConfigService stationConfigService;

	@Permission("admin:stationConfig")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		return returnPage("/system/configIndex");
	}

	@Permission("admin:stationConfig")
	@NeedLogView("站点配置列表")
	@RequestMapping(value = "/list")
	@ResponseBody
	public void list() {
		Map<String, Object> map = new HashMap<>();
		List<StationConfig> list = stationConfigService.getAll(SystemUtil.getStationId(), 2);
	//	Long stationId = SystemUtil.getStationId();

		// 解析出StationConfigEnum类中，未添加到数据库station_config表中的记录
//		List<StationConfig> diffList = Stream.of(StationConfigEnum.values())
//				// 过滤每个StationConfigEnum元素，解析出不存在的元素
//				.filter(e -> {
//					if (e.getVisible() != 2) {
//						return false;
//					}
//
//					int count = 0;	// 是否命中。 0：未命中  >0：命中
//					for (int i = 0; i < list.size(); i++) {
//						if (e.name().equals(list.get(i).getKey())) {
//							count++;
//							break;	// 如果命中，直接跳出循环
//						}
//					}
//					return count == 0 ? true : false;
//				})
//				// 映射成所需要的类
//				.map(v -> {
//					StationConfig sc = new StationConfig();
//					sc.setPartnerId(0L);
//					sc.setStationId(stationId);
//					sc.setKey(v.name());
//					sc.setTitle(v.getCname());
//					sc.setGroupName(v.getGroupName());
//					sc.setEleType(v.getEleType());
//					sc.setVisible(v.getVisible());
//					sc.setSortNo(v.getSortNo());
//					return sc;
//				})
//				// 返回List
//				.collect(Collectors.toList());

		// 解析出StationConfigEnum类中，已添加到数据库station_config表中的记录
//		List<StationConfig> commonList = Stream.of(StationConfigEnum.values())
//				.filter(e -> e.getVisible() == 2 && list.stream().filter(l -> e.name().equals(l.getKey())).findAny().isPresent())
//				.map(v -> {
//					StationConfig sc = new StationConfig();
//					sc.setPartnerId(0L);
//					sc.setStationId(stationId);
//					sc.setKey(v.name());
//					sc.setTitle(v.getCname());
//					sc.setGroupName(v.getGroupName());
//					sc.setEleType(v.getEleType());
//					sc.setVisible(v.getVisible());
//					sc.setSortNo(v.getSortNo());
//					return sc;
//				})
//				.collect(Collectors.toList());

//		list.addAll(diffList);

		List<String> groups = new ArrayList<>();
		if (list != null && !list.isEmpty()) {
			for (StationConfig c : list) {
				if (!groups.contains(c.getGroupName())) {
					groups.add(c.getGroupName());
				}
			}
		}
		map.put("configGroups", groups);
		map.put("configs", list);

		renderJSON(map);
	}

	@Permission("admin:stationConfig:modify")
	@ResponseBody
	@RequestMapping("/saveConfig")
	public void saveConfig(String pk, String value) {
		Long stationId = SystemUtil.getStationId();
		stationConfigService.saveConfig(stationId, pk, value);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_CONFIG, "s:" + stationId + ":k:" + pk);
		//这里最好更新一下StationConfigUtil===>configMap
		super.renderSuccess();
	}
}
