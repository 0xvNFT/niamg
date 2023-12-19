package com.play.web.controller.admin.app;

import com.alibaba.fastjson.JSONObject;
import com.play.common.SystemConfig;
import com.play.common.utils.RandomStringUtils;
import com.play.model.SysAppLauncher;
import com.play.orm.jdbc.page.Page;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.app.NativeJsonUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.SystemUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * app 动态域名管理
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/appLauncher")
public class AdminAppLauncherController extends AdminBaseController {

	Logger logger = LoggerFactory.getLogger(AdminAppLauncherController.class.getSimpleName());

	/**
	 * 首页
	 */
	@Permission("admin:appLauncher")
	@RequestMapping("/index")
	public String launcherIndex(Map<String, Object> map) {
		map.put("pic_space_web_domain", SystemConfig.PIC_SPACE_WEB_DOMAIN);
		return returnPage("/app/launchImg/list");
	}

	/**
	 * 列表数据获取
	 */
	@Permission("admin:appLauncher")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("app启动图列表")
	public void list(Map<String, Object> map) {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("stationCode", SystemUtil.getStation().getCode());
			String content = NativeUtils.asyncRequest(SystemConfig.APP_DOWNLOAD_URL + "/launcher/list", params);
			if (StringUtils.isNotEmpty(content)) {
				List<SysAppLauncher> list = NativeJsonUtil.toList(content, SysAppLauncher.class);
				Page<SysAppLauncher> page = new com.play.orm.jdbc.page.Page<>();
				page.setRows(list);
				page.setTotal(list.size());
				renderJSON(page);
			} else {
				throw new BaseException(BaseI18nCode.stationCatchListFail);
			}
		} catch (BaseException e) {
			logger.error("app domain list error = ", e);
			renderError("APP启动图:" + e.getMessage());
		}
	}

	/**
	 * 新增页面
	 */
	@Permission("admin:appLauncher:add")
	@RequestMapping("/add")
	public String add(Map<String, Object> map) {
		return returnPage("/app/launchImg/add");
	}

	/**
	 * 保存
	 */
	@Permission("admin:appLauncher:add")
	@RequestMapping("/addSave")
	@ResponseBody
	public void save(MultipartFile file) {
		try {
			String ext = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
			if (!ext.equals("jpg") && !ext.equals("jpeg") && !ext.equals("png") && !ext.equals("gif") && !ext.equals("bmp")
					&& !ext.equals("ico")) {
				throw new ParamException(BaseI18nCode.fileTypeError);
			}
			Map<String, String> params = new HashMap<>();
			params.put("stationCode", SystemUtil.getStation().getCode());
			Integer statusCode = NativeUtils.filePost(SystemConfig.APP_DOWNLOAD_URL + "/launcher/save",
					file.getInputStream(), file.getSize(),
					SystemUtil.getStation().getCode() + "_" + RandomStringUtils.randomStr(4) + ".jpg");
		//	logger.error("save launch img code = " + statusCode);
			if (statusCode != null && statusCode == 200) {
				renderSuccess();
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
	@Permission("admin:appLauncher:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		Map<String, String> params = new HashMap<>();
		params.put("id", String.valueOf(id));
		String content = NativeUtils.asyncRequest(SystemConfig.APP_DOWNLOAD_URL + "/launcher/delete", params);
		if (StringUtils.isNotEmpty(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (jsonObj != null && jsonObj.containsKey("success")) {
				if (jsonObj.getBooleanValue("success")) {
					renderSuccess();
				} else {
					String msg = jsonObj.getString("msg");
					renderError(msg);
				}
			}
		}
	}

	// /**
	// * 修改状态
	// */
	@RequestMapping("/changeStatus")
	@ResponseBody
	public void changeStatus(Long id, Integer status) {
		Map<String, String> params = new HashMap<>();
		params.put("id", String.valueOf(id));
		params.put("status", String.valueOf(status));
		String content = NativeUtils.asyncRequest(SystemConfig.APP_DOWNLOAD_URL + "/launcher/changeStatus", params);
		if (StringUtils.isNotEmpty(content)) {
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (jsonObj != null && jsonObj.containsKey("success")) {
				if (jsonObj.getBooleanValue("success")) {
					renderSuccess();
				} else {
					String msg = jsonObj.getString("msg");
					renderError(msg);
				}
			}
		}
		renderSuccess();
	}

}
