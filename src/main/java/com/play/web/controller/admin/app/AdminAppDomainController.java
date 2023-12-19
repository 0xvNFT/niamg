package com.play.web.controller.admin.app;

import com.alibaba.fastjson.JSONObject;
import com.play.common.SystemConfig;
import com.play.common.utils.LogUtils;
import com.play.core.LogTypeEnum;
import com.play.model.SysAppDomain;
import com.play.orm.jdbc.page.Page;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.app.NativeJsonUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * app 动态域名管理
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/appDomain")
public class AdminAppDomainController extends AdminBaseController {

	Logger logger = LoggerFactory.getLogger(AdminAppDomainController.class.getSimpleName());

	/**
	 * 首页
	 */
	@Permission("admin:appDomain")
	@RequestMapping("/index")
	public String noticeIndex(Map<String, Object> map) {
		return returnPage("/app/appDomain/list");
	}

	/**
	 * 列表数据获取
	 */
	@Permission("admin:appDomain")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("app域名列表")
	public void list(Map<String, Object> map) {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("stationCode", SystemUtil.getStation().getCode());
			String content = NativeUtils.asyncRequest(SystemConfig.APP_DOWNLOAD_URL + "/domain/getDomainList", params);
			if (StringUtils.isNotEmpty(content)) {
				List<SysAppDomain> list = NativeJsonUtil.toList(content, SysAppDomain.class);
				Page<SysAppDomain> page = new com.play.orm.jdbc.page.Page<>();
				page.setRows(list);
				page.setTotal(list.size());
				renderJSON(page);
			} else {
				throw new BaseException(BaseI18nCode.stationCatchListFail);
			}
		} catch (BaseException e) {
			logger.error("app domain list error = ", e);
			renderError("APP域名:" + e.getMessage());
		}
	}

	/**
	 * 新增页面
	 */
	@Permission("admin:appDomain:add")
	@RequestMapping("/add")
	public String add(Map<String, Object> map) {
		return returnPage("/app/appDomain/add");
	}

	/**
	 * 保存
	 */
	@Permission("admin:appDomain:add")
	@RequestMapping("/addSave")
	@ResponseBody
	public void save(SysAppDomain sysAppDomain) {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("domainUrl", sysAppDomain.getDomainUrl());
			params.put("hasVertify", String.valueOf(sysAppDomain.getHasVertify()));
			params.put("stationCode", SystemUtil.getStation().getCode());
			String content = NativeUtils.asyncRequest(SystemConfig.APP_DOWNLOAD_URL + "/domain/saveAppDomain", params);
			//logger.error("save domain content = " + content);
			if (StringUtils.isNotEmpty(content)) {
				JSONObject jsonObj = JSONObject.parseObject(content);
				if (jsonObj != null && jsonObj.containsKey("success")) {
					if (jsonObj.getBooleanValue("success")) {
						LogUtils.log("新增app域名:" + sysAppDomain.getDomainUrl(), LogTypeEnum.APP);
						renderSuccess();
					} else {
						String msg = jsonObj.getString("msg");
						renderError(msg);
					}
				}
			} else {
				renderError(BaseI18nCode.stationInsertFail.getMessage());
			}
		} catch (Exception e) {
			logger.error("save launch img error = ", e);
			super.renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
		}

	}

	//
	// /**
	// * 删除
	// */
	@Permission("admin:appDomain:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		Map<String, String> params = new HashMap<>();
		params.put("id", String.valueOf(id));
		String content = NativeUtils.asyncRequest(SystemConfig.APP_DOWNLOAD_URL + "/domain/deleteDomain", params);
		if (StringUtils.isNotEmpty(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (jsonObj != null && jsonObj.containsKey("success")) {
				if (jsonObj.getBooleanValue("success")) {
					LogUtils.log("删除app域名",LogTypeEnum.APP);
					renderSuccess();
				} else {
					String msg = jsonObj.getString("msg");
					renderError(msg);
				}
			}
		} else {
			renderError(BaseI18nCode.stationInsertFail.getMessage());
		}
	}

	//
	// /**
	// * 修改
	// */
	@Permission("admin:appDomain:modify")
	@RequestMapping("/modify")
	@NeedLogView("app域名修改详情")
	public String modify(Map<String, Object> map, Long id) {

		Map<String, String> params = new HashMap<>();
		params.put("id", String.valueOf(id));
		String content = NativeUtils.asyncRequest(SystemConfig.APP_DOWNLOAD_URL + "/domain/getOneDomain", params);
		if (StringUtils.isNotEmpty(content)) {
			SysAppDomain sysAppDomain = NativeJsonUtil.toBean(content, SysAppDomain.class);
			map.put("data", sysAppDomain);
		} else {
			renderError(BaseI18nCode.stationUpdateFail.getMessage());
		}
		return returnPage("/app/appDomain/modify");
	}

	//
	// /**
	// * 保存修改
	// */
	@Permission("admin:appDomain:modify")
	@RequestMapping("/modifySave")
	@ResponseBody
	public void modifySave(SysAppDomain sysAppDomain) {
		Map<String, String> params = new HashMap<>();
		params.put("domainUrl", sysAppDomain.getDomainUrl());
		params.put("id", String.valueOf(sysAppDomain.getId()));
		if (sysAppDomain.getHasVertify() != null) {
			params.put("hasVertify", String.valueOf(sysAppDomain.getHasVertify()));
		}else{
			params.put("hasVertify", "2");
		}
		params.put("stationCode", SystemUtil.getStation().getCode());
		String content = NativeUtils.asyncRequest(SystemConfig.APP_DOWNLOAD_URL + "/domain/modifyAppDomain", params);
		if (StringUtils.isNotEmpty(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (jsonObj != null && jsonObj.containsKey("success")) {
				if (jsonObj.getBooleanValue("success")) {
					LogUtils.log("修改app域名：" + sysAppDomain.getDomainUrl(),LogTypeEnum.APP);
					renderSuccess();
				} else {
					String msg = jsonObj.getString("msg");
					renderError(msg);
				}
			}
		} else {
			renderError(BaseI18nCode.stationUpdateFail.getMessage());
		}
	}

	//
	// /**
	// * 修改状态
	// */
	@RequestMapping("/changeStatus")
	@ResponseBody
	public void changeStatus(Long id, Integer status) {
		Map<String, String> params = new HashMap<>();
		params.put("id", String.valueOf(id));
		params.put("status", String.valueOf(status));
		String content = NativeUtils.asyncRequest(SystemConfig.APP_DOWNLOAD_URL + "/domain/changeStatus", params);
		if (StringUtils.isNotEmpty(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (jsonObj != null && jsonObj.containsKey("success")) {
				if (jsonObj.getBooleanValue("success")) {
					LogUtils.log("修改app域名状态",LogTypeEnum.APP);
					renderSuccess();
				} else {
					String msg = jsonObj.getString("msg");
					renderError(msg);
				}
			}
		} else {
			renderError(BaseI18nCode.stationUpdateStatusFail.getMessage());
		}
		renderSuccess();
	}

}
