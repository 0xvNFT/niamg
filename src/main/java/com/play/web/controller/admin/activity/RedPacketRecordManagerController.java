package com.play.web.controller.admin.activity;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationRedPacketRecord;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationRedPacketService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

/**
 * 抢到红包的记录管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/redPacketRecord")
public class RedPacketRecordManagerController extends AdminBaseController {

	@Autowired
	private StationRedPacketService stationRedPacketService;

	/**
	 * 红包管理首页
	 */
	@Permission("admin:redPacketRecord")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentDate());
		return returnPage("/activity/redPacket/redPacketRecordIndex");
	}

	/**
	 * 抢红包记录
	 */
	@Permission("admin:redPacketRecord")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("抢红包记录列表")
	public void recordList(String username, String startTime, String endTime) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			renderJSON(new Page<StationRedPacketRecord>());
		} else {
			Date begin = DateUtil.toDatetime(startTime);
			Date end = DateUtil.toDatetime(endTime);
			renderJSON(stationRedPacketService.getRecordPage(username, SystemUtil.getStationId(), begin, end));
		}
	}
	

    /**
     * 红包处理界面
     */
    @Permission("admin:redPacketRecord:handel")
    @RequestMapping("/showHandle")
    @NeedLogView("红包处理详情")
    public String showHandle(Long id, Map<Object, Object> map) {
        map.put("record", stationRedPacketService.getRecord(id, SystemUtil.getStationId()));
        return returnPage("/activity/redPacket/redPacketRecordHandel");
    }

    /**
     * 保存红包处理
     */
    @ResponseBody
    @Permission("admin:redPacketRecord:handel")
    @RequestMapping("/handlerRecord")
    public void handlerRecord(Long id, Integer status, String remark) {
        stationRedPacketService.handlerRecord(id, status, remark, SystemUtil.getStationId());
        renderSuccess();
    }
}
