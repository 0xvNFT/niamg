package com.play.web.controller.manager.partner;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.model.PartnerWhiteIp;
import com.play.service.PartnerService;
import com.play.service.PartnerWhiteIpService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;
import com.play.web.i18n.BaseI18nCode;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/partnerWhiteIp")
public class PartnerWhiteIpManagerController extends ManagerBaseController {
	@Autowired
	private PartnerWhiteIpService partnerWhiteIpService;
	@Autowired
	private PartnerService partnerService;

	@Permission("zk:partnerWhiteIp")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("partners", partnerService.getAll());
		return returnPage("/partner/ip/whiteIpIndex");
	}

	@Permission("zk:partnerWhiteIp")
	@NeedLogView(value = "合作商后台IP白名单列表", type = LogTypeEnum.VIEW_LIST)
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public void list(Long partnerId, String ip, Integer type) {
		renderPage(partnerWhiteIpService.page(partnerId, ip, type));
	}

	@Permission("zk:partnerWhiteIp:del")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(Long id) {
		partnerWhiteIpService.delete(id, null);
		CacheUtil.delCacheByPrefix(CacheKey.PARTNER_IPS);
		renderSuccess();
	}

	@Permission("zk:partnerWhiteIp:add")
	@RequestMapping(value = "/showAdd", method = RequestMethod.GET)
	public String showAdd(Map<String, Object> map) {
		map.put("partners", partnerService.getAll());
		return returnPage("/partner/ip/addWhiteIp");
	}

	@Permission("zk:partnerWhiteIp:add")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(PartnerWhiteIp ip) {
		String r = partnerWhiteIpService.save(ip);
		CacheUtil.delCacheByPrefix(CacheKey.PARTNER_IPS);
		if (StringUtils.isNotEmpty(r)) {
			renderSuccess(I18nTool.getMessage(BaseI18nCode.inputIpError, r));
			return;
		}
		renderSuccess();
	}
}
