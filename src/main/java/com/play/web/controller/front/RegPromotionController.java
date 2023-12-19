package com.play.web.controller.front;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.play.core.StationConfigEnum;
import com.play.model.StationDomain;

import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.SystemConfig;
import com.play.common.WapConstants;
import com.play.common.utils.CookieHelper;
import com.play.common.utils.ProxyModelUtil;
import com.play.core.UserTypeEnum;
import com.play.model.Agent;
import com.play.model.StationPromotion;
import com.play.service.AgentService;
import com.play.service.StationPromotionService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.qrcode.QRCodeUtil;
import com.play.web.var.MobileUtil;
import com.play.web.var.SystemUtil;

/**
 * 推广链接注册管理
 *
 * @author admin
 */
@Controller
public class RegPromotionController extends FrontBaseController {
	@Autowired
	private StationPromotionService stationPromotionService;
	@Autowired
	private AgentService agentService;

	/**
	 * 推广链接注册
	 */
	@NotNeedLogin
	@RequestMapping("/r/{linkKey}")
	public String regPromotion(@PathVariable String linkKey, Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response,String fid,String tid) {
		Long stationId = SystemUtil.getStationId();
		// 获取推广码信息
		StationPromotion p = stationPromotionService.findOneByCode(linkKey, stationId);
		if (p == null) {
			throw new ParamException(BaseI18nCode.linkNotExistCheck);
		}
		if (Objects.equals(p.getType(), UserTypeEnum.PROXY.getType())) {
			if (!ProxyModelUtil.isMultiOrAllProxy(stationId)) {
				// 不是全部代理，也不是多级代理 则当前是代理不能再添加代理
				throw new ParamException(BaseI18nCode.stationMutiProxy);
			}
		} else {// 全部代理模式，不能出现会员推会员
			if (Objects.equals(StationPromotion.MODE_MEMBER, p.getMode())
					&& !ProxyModelUtil.canBeRecommend(stationId)) {
				throw new ParamException(BaseI18nCode.stationMemberFirst);
			}
		}
		// 存入缓存
		CacheUtil.addCache(CacheKey.USER_PROMO_CODE, ServletUtils.getSession().getId(), linkKey, 1800);
		// 存入cookie
		CookieHelper.set(response, "linkKey", linkKey, 86400);
		CookieHelper.set(response, "fid", fid, 86400);
		CookieHelper.set(response, "tid", tid, 86400);
		// 添加访问人数
		stationPromotionService.addNum(p.getId(), stationId, 1, false);
		String salt = ServletUtils.getDomain();
		//PC前台如果默认要访问手机端，则转发到手机路径
//		boolean pcDefaultMobile = StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.pc_default_visit_mobile);
//		if (MobileUtil.isMoblie(ServletUtils.getRequest()) || pcDefaultMobile) {
//			try {
//				return indexMobile(ServletUtils.getRequest(), ServletUtils.getResponse());
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//		}

//		boolean isMobile = MobileUtil.isMoblie(request);
//		if (isMobile) {
//			salt = ServletUtils.getDomain() + SystemConfig.CONTROL_PATH_MOBILE;
//		}
		switch (p.getAccessPage()) {
		case StationPromotion.ACCESS_PAGE_TYPE_INDEX:
//			if (isMobile) {
//				return "redirect:" + salt + WapConstants.wap_page_index;
//			}
			return "redirect:" + salt + "/index.do";
//			return "redirect:" + salt + "/promp/index.do";
		case StationPromotion.ACCESS_PAGE_TYPE_ACTIVITY:
//			if (isMobile) {
////				return "redirect:" + salt + WapConstants.wap_page_active;
//				return "redirect:" + salt + WapConstants.wap_page_index;
//			}
//			if (salt.equals(SystemConfig.CONTROL_PATH_MOBILE)) {
////				return "redirect:" + salt + "/active.do";
//				return "redirect:" + salt + WapConstants.wap_page_index;
//			}
//			return "redirect:" + salt + "/promp/activity.do";
			return "redirect:" + salt + "/activity.do";
		default:
//			if (isMobile) {
////				return "redirect:" + salt + WapConstants.wap_page_reg;
//				return "redirect:" + salt + WapConstants.wap_page_index;
//			}
//			return "redirect:" + salt + "/promp/register.do";
			return "redirect:" + salt + "/register.do";
		}
	}

	private String indexMobile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StationDomain domain = SystemUtil.getDomain();
		String defaultHome = domain.getDefaultHome();
		if (StringUtils.isNotEmpty(defaultHome) && !LoginMemberUtil.isLogined()) {
			return "forward:/" + defaultHome;
		}
		if (StringUtils.isNotEmpty(defaultHome) && defaultHome.startsWith("regpage")
				|| defaultHome.startsWith("/regpage")) {
			return "forward:" + SystemConfig.CONTROL_PATH_MOBILE + defaultHome;
		}
		return "forward:" + SystemConfig.CONTROL_PATH_MOBILE + "/index.do";
	}

	/**
	 * 加密推广链接，为了防止qq等聊天攻击标识为危险网址，顾使用vt_开头，伪装成投票系统
	 */
	@NotNeedLogin
	@RequestMapping("/vt_{linkKey}")
	public String memberVote(@PathVariable String linkKey, Map<String, Object> map, HttpServletRequest request) {
		Long stationId = SystemUtil.getStationId();
		// 获取推广码信息
		StationPromotion p = stationPromotionService.findOneByCode(linkKey, stationId);
		if (p == null) {
			throw new ParamException(BaseI18nCode.linkNotExistCheck);
		}
		if (Objects.equals(p.getType(), UserTypeEnum.PROXY.getType())) {
			if (!ProxyModelUtil.isMultiOrAllProxy(stationId)) {
				// 不是全部代理，也不是多级代理 则当前是代理不能再添加代理
				throw new ParamException(BaseI18nCode.stationMutiProxy);
			}
		} else {// 全部代理模式，不能出现会员推会员
			if (Objects.equals(StationPromotion.MODE_MEMBER, p.getMode())
					&& !ProxyModelUtil.canBeRecommend(stationId)) {
				throw new ParamException(BaseI18nCode.stationMemberFirst);
			}
		}
		String url = ServletUtils.getDomain() + "/r/" + linkKey + ".do";
		map.put("url", encodeUrl(url));
		return "/common/member/jump";
	}

	/**
	 * 将url根据规则生成为加密串
	 */
	static private String encodeUrl(String url) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < url.length(); i++) {
			if (i > 0) {
				sb.append("+");
			}
			sb.append("l(").append(encodeUrlChar(url.charAt(i))).append(")");
		}
		return sb.toString();
	}

	/**
	 * url字符加密规则
	 *
	 * @param c
	 * @return
	 */
	static private int encodeUrlChar(char c) {
		return c * 78 * 20000 + (int) (Math.random() * 20000);
	}

	/**
	 * 代理商推广注册
	 */
	@NotNeedLogin
	@RequestMapping("/a/{linkKey}")
	public String agentRegPromotion(@PathVariable String linkKey, Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取代理商信息
		Long stationId = SystemUtil.getStationId();
		Agent agent = agentService.findOneByPromoCode(linkKey, stationId);
		// 存入缓存
		CacheUtil.addCache(CacheKey.USER_PROMO_CODE, "A:" + ServletUtils.getSession().getId(), linkKey, 180);
		// 存入cookie
		CookieHelper.set(response, "agentCode", linkKey, 86400);
		String salt = ServletUtils.getDomain();
		boolean useVueNewModule = StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.new_vue_module);
		if (useVueNewModule) {
			salt += "/mb.do#/index";
		}
		boolean isMobile = MobileUtil.isMoblie(request);
		if (isMobile) {
			salt = ServletUtils.getDomain() + SystemConfig.CONTROL_PATH_MOBILE;
		}
		switch (agent.getAccessPage()) {
		case Agent.ACCESS_PAGE_TYPE_INDEX:
			if (isMobile) {
				return "redirect:" + salt + WapConstants.wap_page_index;
			}
			if (useVueNewModule) {
				return "redirect:" + salt + "?type=index&pcode=" + linkKey;
			}
			return "redirect:" + salt + "/index.do";
		case Agent.ACCESS_PAGE_TYPE_ACTIVITY:
			if (isMobile) {
				return "redirect:" + salt + WapConstants.wap_page_active;
			}
			if (salt.equals(SystemConfig.CONTROL_PATH_MOBILE)) {
				if (useVueNewModule) {
					return "redirect:" + salt + "?type=active&pcode=" + linkKey;
				}
				return "redirect:" + salt + "/active.do";
			}
			return "redirect:" + salt + "/activity.do";
		default:
			if (isMobile) {
				return "redirect:" + salt + WapConstants.wap_page_reg;
			}
			if (useVueNewModule) {
				return "redirect:" + salt + "?type=register&pcode=" + linkKey;
			}
			return "redirect:" + salt + "/register.do";
		}

	}

	// 二维码推广页面
	@NotNeedLogin
	@RequestMapping("/qr/{linkKey}")
	public String qrCodePromotion(@PathVariable String linkKey, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取打开浏览器型号，如果是微信，跳转到引导会员用浏览器打开页面
		String agent = request.getHeader("User-Agent");
		if (agent.contains("MicroMessenger")) {
			return "/common/error/wechatLead";
		}
		return "redirect:/r/" + linkKey + ".do";
	}

	@RequestMapping("/qr/getPromotionQrCode")
	public void getPromotionQrCode(String linkKey, HttpServletResponse response) {
		String key = ServletUtils.getDomain() + "/qr/" + linkKey + ".do";
		try {
			QRCodeUtil.encode(key, response.getOutputStream());
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}
}
