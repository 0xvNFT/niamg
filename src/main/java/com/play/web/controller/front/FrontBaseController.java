package com.play.web.controller.front;

import java.io.IOException;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.play.common.utils.BigDecimalUtil;

import com.play.service.*;
import com.play.web.utils.FbUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.ip.IpUtils;
import com.play.core.StationConfigEnum;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.web.controller.BaseController;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

public class FrontBaseController extends BaseController {
    @Autowired
    protected SysUserService userService;
    @Autowired
    protected SysUserLoginService userLoginService;
    @Autowired
    protected SysUserMoneyService moneyService;
    @Autowired
    protected SysUserDegreeService userDegreeService;
    @Autowired
    protected StationMessageService stationMessageService;
    @Autowired
    protected StationFloatFrameService stationFloatFrameService;
    @Autowired
    protected SysConfigService sysConfigService;
    @Autowired
    protected ThirdGameService thirdGameService;
    @Autowired
    protected SysUserScoreService userScoreService;

    @Autowired
    SysUserRegisterService sysUserRegisterService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysUserLoginService sysUserLoginService;

    protected static Logger logger = LoggerFactory.getLogger(FrontBaseController.class);

    public String memberPage(Map<String, Object> map, String fileName) {
        Station station = SystemUtil.getStation();
        map.put("res", "/member/" + station.getCode());
        stationBase(map, station.getId(), LoginMemberUtil.getUserId());
        userMap(map, station.getId());
        String language = SystemUtil.getLanguage();
    //    logger.info("memberPage...language===>{}",language);
        map.put("language", language);
        String str = new StringBuilder("/member/").append(station.getCode()).append(fileName).toString();
        return str;
    }

    public String commonPage(Map<String, Object> map, String fileName) {
        Station station = SystemUtil.getStation();
        stationBase(map, station.getId(), LoginMemberUtil.getUserId());
        String language = SystemUtil.getLanguage();
        map.put("language", language);
        String str = new StringBuilder("/common/promp").append(fileName).toString();
        return str;
    }

    // 网站基本配置
    private void stationBase(Map<String, Object> map, Long stationId, Long userId) {
        map.put("keywords", StationConfigUtil.get(stationId, StationConfigEnum.page_head_keywords));
        map.put("description", StationConfigUtil.get(stationId, StationConfigEnum.page_head_description));
        map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
        map.put("kfUrl", StationConfigUtil.getKfUrl(stationId));// 在线客服
        map.put("qqUrl", StationConfigUtil.get(stationId, StationConfigEnum.online_qq_service_url));
        map.put("qrcode", StationConfigUtil.get(stationId, StationConfigEnum.app_qr_code_link_ios));
        map.put("appUrl", StationConfigUtil.get(stationId, StationConfigEnum.app_download_link_android));
        map.put("iosAppUrl", StationConfigUtil.get(stationId, StationConfigEnum.app_download_link_ios));
        map.put("azqrcode", StationConfigUtil.get(stationId, StationConfigEnum.app_qr_code_link_android));
        map.put("stationLogo", StationConfigUtil.get(stationId, StationConfigEnum.station_logo));
        map.put("thirdGame", thirdGameService.findOne(stationId));
        String url = ServletUtils.getCurrentUrl();
        if (url.indexOf(SystemConfig.SYS_GENERAL_DOMAIN) == -1) {// 统计代码
            String code = FbUtils.replace( (String) map.get("fid"),(String) map.get("tid"));
            map.put("statisticsCode", code);
        }
        map.put("popShowTime", StationConfigUtil.get(stationId, StationConfigEnum.pop_frame_show_time));
//        map.put("isAllnumber", StationConfigUtil.isOn(stationId, StationConfigEnum.allnumber_switch_when_register));
    }

    // 用户信息
    private void userMap(Map<String, Object> map, Long stationId) {
        // 等级图标
        SysUser user = LoginMemberUtil.currentUser();
        boolean isLogin = user != null;
        map.put("isLogin", isLogin);
        if (isLogin) {
            map.put("money", BigDecimalUtil.formatValue(moneyService.getMoney(user.getId())));
            // 积分
            map.put("score", userScoreService.getScore(user.getId(), stationId));
            map.put("username", user.getUsername());// 用户名
            map.put("degree", userDegreeService.findOne(user.getDegreeId(), stationId));
            map.put("unreadNum", stationMessageService.unreadMessageNum(user.getId(), stationId));
        }
    }

    public void loadFloatFrame(Map<String, Object> map, SysUser user, Integer showPage, Integer platform) {
        if (user != null) {
            map.put("floatFrameList", stationFloatFrameService.find(user.getStationId(), user.getDegreeId(),
                    user.getGroupId(), Constants.STATUS_ENABLE, showPage, platform));
        } else {
            map.put("floatFrameList", stationFloatFrameService.find(SystemUtil.getStationId(), null, null,
                    Constants.STATUS_ENABLE, showPage, platform));
        }
    }

    public String mobanHelp(Map<String, Object> map, String fileName) {
        Station station = SystemUtil.getStation();
        map.put("kfUrl", StationConfigUtil.getKfUrl(station.getId()));// 在线客服
        map.put("isLogin", LoginMemberUtil.isLogined());
        map.put("keywords", StationConfigUtil.get(station.getId(), StationConfigEnum.page_head_keywords));
        map.put("description", StationConfigUtil.get(station.getId(), StationConfigEnum.page_head_description));
        map.put("pageTitle", StationConfigUtil.get(station.getId(), StationConfigEnum.station_name));
        map.put("qqUrl", StationConfigUtil.get(station.getId(), StationConfigEnum.online_qq_service_url));
        map.put("qrcode", StationConfigUtil.get(station.getId(), StationConfigEnum.app_qr_code_link_ios));
        map.put("appUrl", StationConfigUtil.get(station.getId(), StationConfigEnum.app_download_link_android));
        map.put("iosAppUrl", StationConfigUtil.get(station.getId(), StationConfigEnum.app_download_link_ios));
        map.put("azqrcode", StationConfigUtil.get(station.getId(), StationConfigEnum.app_qr_code_link_android));
        map.put("stationLogo", StationConfigUtil.get(station.getId(), StationConfigEnum.station_logo));
        StringBuilder sb = new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON);
        sb.append("/moban").append(fileName).append("/index");
        return sb.toString();
    }

    public SortedMap<String, String> getRequestData(HttpServletRequest request) {
        Enumeration<String> enums = request.getParameterNames();
        SortedMap<String, String> map = new TreeMap<String, String>();

        while (enums.hasMoreElements()) {
            String paramName = (String) enums.nextElement();
            String paramValue = request.getParameter(paramName);
            map.put(paramName, paramValue);
        }

        return map;
    }

    public Map<String, String> getRequestDataForMap(HttpServletRequest request) {
        Enumeration<String> enums = request.getParameterNames();
        Map<String, String> map = new HashMap<>();

        while (enums.hasMoreElements()) {
            String paramName = (String) enums.nextElement();
            String paramValue = request.getParameter(paramName);
            map.put(paramName, paramValue);
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    public SortedMap<String, Object> getRequestJsonData(HttpServletRequest request) {
        try {
            byte buffer[] = getRequestPostBytes(request);
            String charEncoding = request.getCharacterEncoding();
            if (charEncoding == null) {
                charEncoding = "UTF-8";
            }

            String data = new String(buffer, charEncoding);
         //   logger.error("jsonData:" + data);
            SortedMap<String, Object> map = new TreeMap<String, Object>();

            map = JSONObject.parseObject(data, map.getClass());

            return map;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readlen = request.getInputStream().read(buffer, i, contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 获取带转义字符的JSON字符串
     *
     * @param request
     * @return
     */

    @SuppressWarnings("unchecked")
    public static SortedMap<String, String> getParameterStringMap(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();// 把请求参数封装到Map<String, String[]>中
        SortedMap<String, String> returnMap = new TreeMap<String, String>();
        String name = "";
        for (Map.Entry<String, String[]> entry : properties.entrySet()) {
            name = entry.getKey();
        }
      //  logger.error("name:" + name);
        returnMap = JSONObject.parseObject(name, returnMap.getClass());
        return returnMap;
    }

    public static boolean authenticatedIP(HttpServletRequest request, String ipList) {
        if (StringUtils.isNotEmpty(ipList)) {
            String[] ipListArray = ipList.split("/");
            String realIp = IpUtils.getSafeIpAdrress(request);
         //   logger.error("请求IP地址"+realIp);
            if (!Arrays.asList(ipListArray).contains(realIp)) {
                return false;
            }
        }
        return true;
    }
}
