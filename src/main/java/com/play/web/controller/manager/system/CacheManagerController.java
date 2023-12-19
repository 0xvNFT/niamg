package com.play.web.controller.manager.system;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.CacheKey;
import com.play.cache.redis.RedisAPI;
import com.play.common.SystemConfig;
import com.play.common.utils.LogUtils;
import com.play.core.LogTypeEnum;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/cache")
public class CacheManagerController extends ManagerBaseController {
	@Permission("zk:cache")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		return returnPage("/cache/cacheIndex");
	}

	@Permission("zk:cache")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	@NeedLogView(value = "缓存管理列表", type = LogTypeEnum.VIEW_LIST)
	public void list() {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject k = null;
		k = new JSONObject();
		for (CacheKey key : CacheKey.getList()) {
			k = new JSONObject();
			k.put("name", key.getTitle());
			k.put("key", key.name());
			k.put("db", key.getDb());
			k.put("timeout", key.getTimeout());
			arr.add(k);
		}
		obj.put("data", arr);
		renderJSON(obj.toJSONString());
	}

	@Permission("zk:cache:del")
	@RequestMapping(value = "/deleteCache", method = RequestMethod.GET)
	public String deleteCache(Map<String, Object> map) {
		return returnPage("/cache/deleteCache");
	}

	@ResponseBody
	@Permission("zk:cache:del")
	@RequestMapping("/delCache")
	public void delCache(String key, Integer db) {
		if (db == null || db > 16 || db < 0 || StringUtils.isEmpty(key)) {
			renderError("请选中要删除的库和缓存key");
			return;
		}
		Set<String> set = RedisAPI.getKeys(key, db);
		if (set == null || set.isEmpty()) {
			renderError("库＝" + db + "  key=" + key + " 缓存不存在");
			return;
		}
		RedisAPI.delByPrefix(key, db);
		LogUtils.delLog("清理缓存：" + db + " key:" + key);
		renderSuccess("总共清理缓存数：" + set.size());
	}

	@Permission("zk:cache:view")
	@RequestMapping(value = "/viewDb", method = RequestMethod.GET)
	@NeedLogView(value = "缓存管理查看详情", type = LogTypeEnum.VIEW_DETAIL)
	public String viewDb(Map<String, Object> map, Integer db) {
		if (db == null || db > 16 || db < 0) {
			throw new ParamException(BaseI18nCode.redisDbError);
		}
		map.put("db", db);
		map.put("list", RedisAPI.getKeys("", db));
		return returnPage("/cache/viewDbCache");
	}

	/**
	 * 刷新缓存
	 */
	@Permission("zk:cache:flush")
	@RequestMapping(value = "/flushCache", method = RequestMethod.POST)
	@ResponseBody
	public void flushCache(Integer db, String name) {
		if (db == null || db > 16 || db < 0) {
			throw new ParamException(BaseI18nCode.redisDbError);
		}
		if (name == null || name.equals("DEFAULT")) {
			name = "";
		}
		RedisAPI.delByPrefix(name.trim(), db);
		LogUtils.delLog("刷新缓存：" + db + " name:" + name);
		super.renderSuccess();
	}

	@Permission("zk:cache:view")
	@RequestMapping(value = "/getCacheContext", method = RequestMethod.GET)
	@ResponseBody
	@NeedLogView(value = "缓存管理查看详情明细", type = LogTypeEnum.VIEW_DETAIL)
	public void getCacheContext(String key, Integer db) {
		super.renderText(RedisAPI.getCache(key, db));
	}
}
