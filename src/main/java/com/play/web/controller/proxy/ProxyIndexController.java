package com.play.web.controller.proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.core.LanguageEnum;
import com.play.spring.config.i18n.I18nTool;

import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.core.StationConfigEnum;
import com.play.model.SysUser;
import com.play.model.ThirdGame;
import com.play.model.vo.MenuVo;
import com.play.model.vo.ThirdPlatformSwitch;
import com.play.service.SysUserLoginService;
import com.play.service.SysUserService;
import com.play.service.ThirdGameService;
import com.play.service.ThirdPlatformService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.user.online.OnlineManager;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import com.play.web.vcode.VerifyCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_PROXY)
public class ProxyIndexController extends ProxyBaseController {
	@Autowired
	private ThirdGameService gameService;
	@Autowired
	private ThirdPlatformService platformService;
	@Autowired
	private SysUserLoginService userLoginService;
	@Autowired
	private SysUserService userService;

	@RequestMapping("/index")
	@NotNeedLogin
	public String index(Map<String, Object> map) {
		map.put("language", SystemUtil.getLanguage());
		map.put("languages", LanguageEnum.values());
		if (LoginMemberUtil.isLogined()) {
			return returnPage("/index");
		} else {
			return returnPage("/login");
		}
	}

	@NotNeedLogin
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Map<String, Object> map) {
		return returnPage("/login");
	}

	@NotNeedLogin
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(String username, String password, String verifyCode) {
		userLoginService.doLoginForProxy(username, password, verifyCode);
		renderSuccess();
	}

	@NotNeedLogin
	@RequestMapping(value = "/changeLanguage", method = RequestMethod.GET)
	public String changeLanguage(String lang, HttpServletRequest request, HttpServletResponse response) {// 设置语言
		I18nTool.changeLocale(lang, request, response);
		CacheUtil.delCacheByPrefix(CacheKey.ADMIN, Constants.cache_key_admin_perm);
		return "redirect:" + SystemConfig.CONTROL_PATH_PROXY + "/index.do";
	}

	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/logVerifycode")
	public void logVerifycode() throws Exception {
		VerifyCodeUtil.createVerifyCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, StationConfigEnum.login_verify_code_type);
	}

	@RequestMapping("/home")
	public String home() {
		return returnPage("/home");
	}

	@NotNeedLogin
	@RequestMapping("/loginDialog")
	public String loginDialog() {
		return returnPage("/loginDialog");
	}

	@ResponseBody
	@RequestMapping("/getBaseInfo")
	public void getBaseInfo() {
		Map<String, Object> map = new HashMap<>();
		Long stationId = SystemUtil.getStationId();
		Integer onlineNum = OnlineManager.getOnlineCount(stationId, LoginMemberUtil.getUserId());
		map.put("onlineUser", onlineNum);
		renderJSON(map);
	}

	@ResponseBody
	@RequestMapping("/nav")
	public void nav1() {
		renderJSON(JSON.toJSONString(getMenuVoStr(SystemUtil.getStationId()),
				SerializerFeature.DisableCircularReferenceDetect));
	}

	@RequestMapping("/modifyPwd")
	public String modifyPwd(Map<String, Object> map) {
		map.put("loginUser", LoginMemberUtil.currentUser());
		return returnPage("/modifyPwd");
	}

	/**
	 * 密码修改保存
	 */
	@ResponseBody
	@RequestMapping("/updloginpwd")
	public void pwdModifySave(Map<String, Object> map, String opwd, String pwd, String rpwd) {
		// 修改登陆密码
		SysUser user = LoginMemberUtil.currentUser();
		userService.modifyLoginPwd(opwd, pwd, rpwd, user.getId(), user.getStationId(), user.getUsername());
		renderSuccess();
	}

	@NotNeedLogin
	@RequestMapping("/logout")
	public String logout(Map<String, Object> map) {
		userLoginService.doLoginOut(Constants.SESSION_KEY_PROXY);
		return login(map);
	}

	private List<MenuVo> getMenuVoStr(Long stationId) {
		ThirdGame g = gameService.findOne(stationId);
		ThirdPlatformSwitch ps = platformService.getPlatformSwitch(stationId);
		StringBuilder sb = new StringBuilder("[");
		// 财务管理=》充值记录，提款记录,团队帐变
		sb.append("{\"children\":[");
		sb.append(
				"{\"children\":[],\"href\":\"/finance/deposit/index.do\",\"id\": 11,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.deposit")+"\"},");
		sb.append(
				"{\"children\":[],\"href\":\"/finance/withdraw/index.do\",\"id\": 12,\"sortNo\": 90,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.withdraw")+"\"}");
		sb.append(
				"{\"children\":[],\"href\":\"/finance/moneyHistory/index.do\",\"id\": 13,\"sortNo\": 90,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.vip.account.manage")+"\"}");//团队帐变记录
		sb.append("],\"icon\":\"fa-jpy\",\"id\": 1,\"sortNo\": 200,\"spread\": true,\"title\": \""+I18nTool.getMessage("admin.menu.finance")+"\"},");//财务管理
		// 彩票管理=》彩票投注记录
		sb.append("{\"children\":[");
		if (Objects.equals(g.getLottery(), Constants.STATUS_ENABLE)) {
			sb.append(
					"{\"children\":[],\"href\":\"/lotteryRecord/index.do\",\"id\": 15,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.lottery")+"\"},");
		}

		if (Objects.equals(g.getLive(), Constants.STATUS_ENABLE)) {
			sb.append(
					"{\"children\":[],\"href\":\"/liveRecord/index.do\",\"id\": 21,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.liveRecord")+"\"},");
		}
		if (Objects.equals(g.getEgame(), Constants.STATUS_ENABLE)) {
			sb.append(
					"{\"children\":[],\"href\":\"/egameRecord/index.do\",\"id\": 22,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.egameRecord")+"\"},");
		}
		if (Objects.equals(g.getChess(), Constants.STATUS_ENABLE)) {
			sb.append(
					"{\"children\":[],\"href\":\"/chessRecord/index.do\",\"id\": 23,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.chessRecord")+"\"},");
		}
		if (Objects.equals(g.getSport(), Constants.STATUS_ENABLE)) {
			sb.append(
					"{\"children\":[],\"href\":\"/sportRecord/index.do\",\"id\": 24,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.sportRecord")+"\"},");
		}
		if (ps.isPt()) {
			sb.append(
					"{\"children\":[],\"href\":\"/ptRecord/index.do\",\"id\": 25,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.ptRecord")+"\"},");
		}
		if (Objects.equals(g.getEsport(), Constants.STATUS_ENABLE)) {
			sb.append(
					"{\"children\":[],\"href\":\"/esportRecord/index.do\",\"id\": 27,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.esportRecord")+"\"},");
		}
		if (Objects.equals(g.getFishing(), Constants.STATUS_ENABLE)) {
			sb.append(
					"{\"children\":[],\"href\":\"/fishingRecord/index.do\",\"id\": 28,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.fishingRecord")+"\"},");
		}
		sb.append("],\"icon\":\"fa-jpy\",\"id\": 14,\"sortNo\": 200,\"spread\": false,\"title\": \""+I18nTool.getMessage("manager.betting.record")+"\"},");
		// 会员管理=》下级列表
		sb.append("{\"children\":[");
		sb.append(
				"{\"children\":[],\"href\":\"/user/index.do\",\"id\": 17,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.member.table.show")+"\"},");
		sb.append("],\"icon\":\"fa-jpy\",\"id\": 16,\"sortNo\": 200,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.user.manager")+"\"},");
		// 报表管理=》团队统计
		sb.append("{\"children\":[");
		sb.append(
				"{\"children\":[],\"href\":\"/globalReport/index.do\",\"id\": 19,\"sortNo\": 100,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.all.game.total")+"\"},");
		sb.append(
				"{\"children\":[],\"href\":\"/teamReport/index.do\",\"id\": 20,\"sortNo\": 99,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.teamReport")+"\"},");
		sb.append(
				"{\"children\":[],\"href\":\"/proxyReport/index.do\",\"id\": 30,\"sortNo\": 98,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.proxyReport")+"\"},");
		sb.append("],\"icon\":\"fa-jpy\",\"id\": 18,\"sortNo\": 200,\"spread\": false,\"title\": \""+I18nTool.getMessage("admin.menu.report.manager")+"\"},");

		sb.append("]");
		 //System.out.println(sb.toString());
		return JSON.parseArray(sb.toString(), MenuVo.class);
	}
}
