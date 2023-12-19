package com.play.web.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.play.model.StationConfig;
import org.apache.commons.lang3.StringUtils;

import com.play.common.Constants;
import com.play.common.utils.SpringUtils;
import com.play.core.StationConfigEnum;
import com.play.dao.StationConfigDao;
import com.play.model.SysUser;
import com.play.model.vo.StationConfigVo;
import com.play.web.var.LoginMemberUtil;

public class StationConfigUtil {
	private static Map<Long, Map<String, String>> configMap = new HashMap<>();

	public static void initConfigToMap(Long stationId) {
		List<StationConfigVo> list = SpringUtils.getBean(StationConfigDao.class).findAllConfig(stationId);
		if (list != null && !list.isEmpty()) {
			Map<String, String> map = configMap.get(stationId);
			if (map == null) {
				map = new HashMap<>();
				configMap.put(stationId, map);
			}
			for (StationConfigVo v : list) {
				map.put(v.getKey(), v.getValue());
			}
		}
	}

	public static String get(Long stationId, StationConfigEnum configEnum) {
		Map<String, String> map = configMap.get(stationId);
		//先走系统缓存 但是这个值 有时候会失效 拿不到
		if (map != null && map.containsKey(configEnum.name())) return map.get(configEnum.name());
		//如果再缓存里面找不到 应该走数据库 因为添加到缓存需要一定的时间,数据库里面找不到 再去找默认的值
		//更新Staticonconfig 数据库的时候也会删除缓存 但是并没有及时更新configMap所以那里也需要优化一下
		//StationConfigManagerController 120行   同时也应该更新configMap这个缓存(还没有弄)
		StationConfig stationConfig = SpringUtils.getBean(StationConfigDao.class).findOne(stationId,configEnum.name());
        if(stationConfig != null && !StringUtils.isEmpty(stationConfig.getValue())) return stationConfig.getValue();
		return configEnum.getInitValue();
	}

	public static boolean isOff(Long stationId, StationConfigEnum configEnum) {
		return StringUtils.equals(get(stationId, configEnum), Constants.SWITCH_OFF);
	}

	public static boolean isOn(Long stationId, StationConfigEnum configEnum) {
		return StringUtils.equals(get(stationId, configEnum), Constants.SWITCH_ON);
	}

	public static String getKfUrl(Long stationId) {
		String kfUrl = get(stationId, StationConfigEnum.online_customer_service_url);
		if (StringUtils.isNotEmpty(kfUrl)) {
			if (kfUrl.indexOf("http://") == -1 && kfUrl.indexOf("https://") == -1) {
				kfUrl = "http://" + kfUrl;
			}
			if (kfUrl.contains("{userId}") || kfUrl.contains("{username}")) {
				SysUser user = LoginMemberUtil.currentUser();
				if (user != null) {
					kfUrl = kfUrl.replace("{userId}", user.getId().toString()).replace("{username}",
							user.getUsername());
				} else {
					kfUrl = kfUrl.replace("{userId}", "").replace("{username}", "");
				}
			}
		}
		return kfUrl;
	}

}
