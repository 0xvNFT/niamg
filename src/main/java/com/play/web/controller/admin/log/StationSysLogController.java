package com.play.web.controller.admin.log;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.ip.IPAddressUtils;
import com.play.common.utils.DateUtil;
import com.play.core.LogTypeEnum;
import com.play.model.SysLog;
import com.play.service.SysLogService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/oplog")
public class StationSysLogController extends AdminBaseController {
	@Autowired
	private SysLogService logService;

	@Permission("admin:oplog")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("types", LogTypeEnum.values());
		map.put("cusDate", DateUtil.getCurrentDate());
		return returnPage("/log/logIndex");
	}

	@Permission("admin:oplog")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public void list(String username, String ip, Integer type, String begin, String end, String content) {
		renderPage(logService.page(null, SystemUtil.getStationId(), username, ip, type, null, begin, end, content));
	}

	@Permission("admin:oplog")
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@NeedLogView(value = "操作日志详情", type = LogTypeEnum.VIEW_DETAIL)
	public String detail(Map<String, Object> map, Long id, Long createTime) {
		SysLog log = logService.findOne(id, new Date(createTime));
		map.put("log", log);
		for (LogTypeEnum lt : LogTypeEnum.values()) {
			if (Objects.equals(lt.getType(), log.getType())) {
				map.put("typeName", lt.getDesc());
			}
		}
		if (StringUtils.isNotEmpty(log.getIp())) {
			map.put("ipArea", IPAddressUtils.getCountry(log.getIp()));
		}
		return returnPage("/log/logDetail");
	}
}
