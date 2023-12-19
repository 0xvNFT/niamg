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
import com.play.core.PlatformType;
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
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/lotteryRecord")
public class LotteryRecordController extends AdminBaseController {

	@Autowired
	private ThirdCenterService thirdCenterService;
//	@Autowired
//	private YGCenterService ygCenterService;
	@Autowired
	private SysUserService userService;

	@Permission("admin:lotteryRecord")
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
		map.put("startTime", begin);
		map.put("endTime", end);
		map.put("platforms2", PlatformType.getLotteryPlatforms());
		map.put("platforms", PlatformType.values());
		map.put("proxyName", proxyName);
		return returnPage("/third/lotteryRecordIndex");
	}

	@Permission("admin:lotteryRecord")
	@ResponseBody
	@RequestMapping("/list")
	 public void list(String proxyName, ThirdSearchVo v) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(v.getUsername()) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<>());
		} else {
			//YG彩票走YGAPI获取
//			JSONObject lotteryOrder = ygCenterService.getLotteryOrder(v.getOrderId(), v.getUsername(), v.getQiHao(), v.getLotCode(),
//					v.getStartTime(), v.getEndTime(), v.getPageNumber(), v.getPageSize());
//			renderJSON(ygCenterService.convertYGData(lotteryOrder));
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
			renderJSON(thirdCenterService.getLotteryPage(v));
		}
	}

//	@Permission("admin:lotteryRecord")
//	@RequestMapping("/viewDetail")
//	public String viewDetail(Long id, Integer type) {
//		if (type == null) {
//			throw new ParamException(BaseI18nCode.parameterError);
//		}
//		String url = thirdCenterService.getDetailUrl(id, type, 1, SystemUtil.getStation());
//		if (StringUtils.isEmpty(url)) {
//			throw new ParamException(BaseI18nCode.parameterEmpty);
//		}
//		return "redirect:" + url;
//	}
}
