package com.play.web.controller.admin.third;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.UserTypeEnum;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.vo.ThirdSearchVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserService;
import com.play.service.ThirdCenterService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/ptRecord")
public class PtRecordController extends AdminBaseController {

    @Autowired
    private ThirdCenterService thirdCenterService;

    @Autowired
    private SysUserService userService;

    @Permission("admin:ptRecord")
    @RequestMapping("/index")
    public String index(String begin, String end, String username, Map<String, Object> map, String proxyName) {
        if (StringUtils.isEmpty(begin)) {
            begin = DateUtil.getCurrentDate();
        }
        if (StringUtils.length(begin) < 19) {
            begin += " 00:00:00";
        }
        if (StringUtils.isEmpty(end)) {
            end = DateUtil.getCurrentDate();
        }
        if (StringUtils.length(end) < 19) {
            end += " 23:59:59";
        }
        map.put("username", username);
        map.put("proxyName",proxyName);
        map.put("startTime", begin);
        map.put("endTime", end);
        return returnPage("/third/ptRecordIndex");
    }


    @Permission("admin:ptRecord")
    @RequestMapping("/list")
    @ResponseBody
    public void list(String proxyName, ThirdSearchVo v) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(v.getUsername()) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<>());
		} else {
			Station station = SystemUtil.getStation();
			v.setStation(station);
			if (StringUtils.isNotEmpty(proxyName)) {
				SysUser u = userService.findOneByUsername(proxyName, station.getId(), null);
				if (u != null) {
					if (Objects.equals(u.getType(), UserTypeEnum.MEMBER.getType())) {
						v.setRecommendId(u.getId());
					} else {
						String parentIds = u.getParentIds();
						if (StringUtils.isNotEmpty(parentIds)) {
							parentIds = parentIds + u.getId() + ",";
						} else {
							parentIds = "," + u.getId() + ",";
						}
						v.setParentIds(parentIds);
					}
				} else {
					throw new ParamException(BaseI18nCode.proxyUnExist);
				}
			}
			renderJSON(thirdCenterService.getPtPage(v));
		}
	}

}
