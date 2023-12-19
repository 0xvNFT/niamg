package com.play.web.controller.admin.page;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.core.LanguageEnum;
import com.play.orm.jdbc.page.Page;
import com.play.web.annotation.NotNeedLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.ArticleTypeEnum;
import com.play.model.StationArticle;
import com.play.service.StationArticleService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.Logical;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/article")
public class StationArticleController extends AdminBaseController {

	@Autowired
	private StationArticleService stationArticleService;

	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;

	/**
	 * 公告发布
	 */
	@Permission("admin:article:notice")
	@RequestMapping("/notice/index")
	public String noticeIndex(Map<String, Object> map) {
		return returnPage("/page/article/noticeIndex");
	}

	/**
	 * 新闻资讯
	 */
	@Permission("admin:article:news")
	@RequestMapping("/news/index")
	public String newsIndex(Map<String, Object> map) {
		return returnPage("/page/article/newsIndex");
	}

	/**
	 * 站点资料
	 */
	@Permission("admin:article:material")
	@RequestMapping("/material/index")
	public String materialIndex(Map<String, Object> map) {
		return returnPage("/page/article/materialIndex");
	}

	/**
	 * 列表数据获取
	 */
	@Permission(value = { "admin:article:material", "admin:article:news",
			"admin:article:notice" }, logical = Logical.OR)
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("公告、新闻、站点资料列表")
	public void list(Map<String, Object> map, Integer type) {
		if (type == null) {
			type = 1;
		}
		//修改语种显示并传给前端
		Page<StationArticle> page = stationArticleService.pageByStationArticle(SystemUtil.getStationId(), ArticleTypeEnum.getCodeList(type));
		LanguageEnum[] languageEnums =LanguageEnum.values();
		for (StationArticle stationArticle : page.getRows()){
			for (LanguageEnum languageEnum : languageEnums){
				if (languageEnum.getLocale().getLanguage().equals(stationArticle.getLanguage())){
					stationArticle.setLanguage(languageEnum.getLang());
					continue;
				}
			}
		}
		renderJSON(page);
	}

	/**
	 * 新增站点资料页面
	 */
	@Permission(value = { "admin:article:material:add", "admin:article:notice:add",
			"admin:article:news:add" }, logical = Logical.OR)
	@RequestMapping("/add")
	public String add(Map<String, Object> map, Integer type) {
		JSONArray articleTypeEnum = ArticleTypeEnum.getList(type);
		JSONArray filteredArticleTypeEnum = articleTypeEnum.stream()
				.filter(articleType -> {
					JSONObject jsonObject = (JSONObject) articleType;
					return jsonObject.getInteger("code") != 23;
				})
				.collect(Collectors.toCollection(JSONArray::new));

		map.put("types", filteredArticleTypeEnum);
		map.put("beginDate", DateUtil.getCurrentDate());
		map.put("endDate", DateUtil.afterMonth(new Date(), 12));
		map.put("degrees", userDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));

		// 巴西、印度 Local语言编码(pt,hi)与枚举元素名(br,in)不一致，导致前端传参无法取到，需特殊处理
		Map<String, String> langMap = Arrays.stream(LanguageEnum.values())
				.filter(e -> !"br".equals(e.name()) && !"in".equals(e.name()))
				.collect(Collectors.toMap(LanguageEnum::name, LanguageEnum::getLang, (v1, v2) -> v1));
		langMap.put("br", LanguageEnum.br.getLang());
		langMap.put("in", LanguageEnum.in.getLang());
		map.put("languages", langMap);
		return returnPage("/page/article/add");
	}

	/**
	 * 保存站点资料页面
	 */
	@Permission(value = { "admin:article:material:add", "admin:article:notice:add",
			"admin:article:news:add" }, logical = Logical.OR)
	@RequestMapping("/addSave")
	@ResponseBody
	public void save(StationArticle article, String[] degreeIds, String[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			article.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		}
		if (groupIds != null && groupIds.length > 0) {
			article.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		}
		if (article.getPopupStatus() == null) {
			article.setPopupStatus(1);
		}

		article.setStationId(SystemUtil.getStationId());

		stationArticleService.addSave(article);
		renderSuccess();
	}


	/**
	 * 删除
	 */
	@Permission(value = { "admin:article:material:delete", "admin:article:notice:delete",
			"admin:article:news:delete" }, logical = Logical.OR)
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		stationArticleService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 修改
	 */
	@Permission(value = { "admin:article:material:modify", "admin:article:notice:modify",
			"admin:article:news:modify" }, logical = Logical.OR)
	@RequestMapping("/modify")
	public String modify(Map<String, Object> map, Integer type, Long id) {
		StationArticle article = stationArticleService.findOne(id, SystemUtil.getStationId());
		map.put("types", ArticleTypeEnum.getList(type));
		map.put("article", article);
		map.put("degrees", userDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
		Map<String, String> langMap = Arrays.stream(LanguageEnum.values())
				.filter(e -> !"br".equals(e.name()) && !"in".equals(e.name()))
				.collect(Collectors.toMap(LanguageEnum::name, LanguageEnum::getLang, (v1, v2) -> v1));
		langMap.put("br", LanguageEnum.br.getLang());
		langMap.put("in", LanguageEnum.in.getLang());
		map.put("languages", langMap);
		return returnPage("/page/article/modify");
	}

	/**
	 * 保存修改
	 */
	@Permission(value = { "admin:article:material:modify", "admin:article:notice:modify",
			"admin:article:news:modify" }, logical = Logical.OR)
	@RequestMapping("/modifySave")
	@ResponseBody
	public void modifySave(StationArticle article, String[] degreeIds, String[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			article.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		}
		if (groupIds != null && groupIds.length > 0) {
			article.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		}
		if (article.getPopupStatus() == null) {
			article.setPopupStatus(1);
		}
		article.setStationId(SystemUtil.getStationId());
		stationArticleService.eidtSave(article);
		renderSuccess();
	}

	/**
	 * 修改状态
	 */
	@Permission(value = { "admin:article:material:modify", "admin:article:notice:modify",
			"admin:article:news:modify" }, logical = Logical.OR)
	@RequestMapping("/changeStatus")
	@ResponseBody
	public void changeStatus(Long id, Integer status) {
		stationArticleService.changeStatus(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}

	@Permission(value = { "admin:article:material:modify", "admin:article:notice:modify",
			"admin:article:news:modify" }, logical = Logical.OR)
	@RequestMapping("/changePopupStatus")
	@ResponseBody
	public void changePopupStatus(Long id, Integer popupStatus) {
		stationArticleService.changePopupStatus(id, SystemUtil.getStationId(), popupStatus);
		renderSuccess();
	}

}
