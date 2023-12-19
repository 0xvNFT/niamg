package com.play.web.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.play.web.utils.mlangIpCommons.template.IpCheckTemplate;
import com.play.web.var.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.ip.IpUtils;
import com.play.core.LanguageEnum;
import com.play.core.StationConfigEnum;
import com.play.core.UserGroupEnum;
import com.play.core.UserTypeEnum;
import com.play.model.AdminUser;
import com.play.model.AgentUser;
import com.play.model.ManagerUser;
import com.play.model.PartnerUser;
import com.play.model.Station;
import com.play.model.StationDomain;
import com.play.model.SysUser;
import com.play.model.vo.WhiteIpVo;
import com.play.service.AdminWhiteIpService;
import com.play.service.AgentWhiteIpService;
import com.play.service.PartnerWhiteIpService;
import com.play.service.StationDomainService;
import com.play.service.StationService;
import com.play.web.exception.BaseException;
import com.play.web.exception.ForbiddenException;
import com.play.web.exception.NotPageException;
import com.play.web.exception.PageNotFoundException;
import com.play.web.exception.UnStationException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.user.online.OnlineManager;
import com.play.web.user.online.OnlineManagerForAdmin;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;

public class GlobalPreActionInterceptor extends HandlerInterceptorAdapter {
    public static final Logger logger = LoggerFactory.getLogger(GlobalPreActionInterceptor.class);

    @Autowired
    private StationService stationService;
    @Autowired
    private StationDomainService domainService;
    @Autowired
    private PartnerWhiteIpService partnerWhiteIpService;
    @Autowired
    private AdminWhiteIpService adminWhiteIpService;
    @Autowired
    private AgentWhiteIpService agentWhiteIpService;
    @Autowired
    IpCheckTemplate ipCheckTemplate;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String basePath = request.getContextPath();
        request.setAttribute("base", basePath);
        switch (SystemUtil.getStationType()) {
            case MANAGER:
                request.setAttribute("managerBase", basePath + SystemConfig.CONTROL_PATH_MANAGER);
                break;
            case PARTNER:
                request.setAttribute("partnerBase", basePath + SystemConfig.CONTROL_PATH_PARTNER);
                break;
            case ADMIN:
                request.setAttribute("adminBase", basePath + SystemConfig.CONTROL_PATH_ADMIN);
                break;
            case AGENT:
                request.setAttribute("agentBase", basePath + SystemConfig.CONTROL_PATH_AGENT);
                break;
            case PROXY:
                request.setAttribute("proxyBase", basePath + SystemConfig.CONTROL_PATH_PROXY);
                break;
            case FRONT_MOBILE:
                request.setAttribute("mobileBase", basePath + SystemConfig.CONTROL_PATH_MOBILE);
                break;
            default:
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SysThreadObject threadObj = new SysThreadObject();
        SysThreadVariable.set(threadObj);
        String url = ServletUtils.getRequestURI(request);
        String domain = ServletUtils.getDomainName(request.getRequestURL().toString());
        String basePath = request.getContextPath();
        String curIp = IpUtils.getSafeIpAdrress(request);

        if (StringUtils.equals(domain, SystemConfig.SYS_CONTROL_DOMAIN)) {// 总控
            handleHostManager(url, basePath, curIp, threadObj, request.getSession());
//			threadObj.setLanguage(getLanguage(request, response, null));
            threadObj.setLanguage(LanguageEnum.cn);
        } else if (isReceiveDomain(domain, basePath, url)) {// 接收数据
            threadObj.setStationType(StationType.RECEIVE);
            threadObj.setUserType(UserTypeEnum.RECEIVE);
        } else {// 站点，合作商
            if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_MANAGER + "/")) {
                throw new NotPageException();
            }
            StationDomain sysDomain = getStationDomain(domain);
            threadObj.setDomain(sysDomain);
            curIp = IpUtils.getIpAddr(request, sysDomain.getIpMode());
            threadObj.setIp(curIp);
            setStationToThreadObj(threadObj, url, basePath, sysDomain);
            switch (sysDomain.getPlatform()) {
                case StationDomain.PLATFORM_PARTNER:// 合作商
                    threadObj.setLanguage(getLanguage(request, response, null));
                    return handlePartnerBg(basePath, url, curIp, sysDomain.getPartnerId(), threadObj, request.getSession());
                case StationDomain.PLATFORM_STATION:// 站点
                    return handleStation(domain, basePath, url, curIp, sysDomain, threadObj, request, response);
            }
        }
        return true;
    }

    /**
     * 获取当前语言类别
     *
     * @param request
     * @return
     */
    private LanguageEnum getLanguage(HttpServletRequest request, HttpServletResponse response, Station station) {
    	boolean isFront = SysThreadVariable.get().isFrontStation();
    	String sessionKey = isFront ? Constants.SESSION_KEY_LANGUAGE : Constants.SESSION_KEY_ADMIN_LANGUAGE;
        String lang = (String) request.getSession().getAttribute(sessionKey);
        if (StringUtils.isEmpty(lang) && station != null) {
            lang = station.getLanguage();
        }
        if ("pt".equals(lang)){
            lang="br";
        }
        LanguageEnum le = LanguageEnum.getLanguageEnum2(lang);
        RequestContextUtils.getLocaleResolver(request).setLocale(request, response, le.getLocale());
   //     System.out.println("lang=" + le.getLang());
        return le;
    }


    /**
     * 初始化总控信息
     *
     * @param url
     * @param basePath
     * @param curIp
     * @param threadObj
     * @param session
     */
    private void handleHostManager(String url, String basePath, String curIp, SysThreadObject threadObj, HttpSession session) {
        if (!url.startsWith(basePath + SystemConfig.CONTROL_PATH_MANAGER + "/") && !url.equals(basePath) && !url.equals(basePath + "/")) {
            logger.error("handleHostManager path error, url:{}, basePath:{}", url, basePath);
            throw new PageNotFoundException(BaseI18nCode.pageNotFound);
        }
        if (!IpUtils.isManagerWhiteIp(curIp)) {
            throw new ForbiddenException(curIp);
        }
        threadObj.setStationType(StationType.MANAGER);
        ManagerUser u = (ManagerUser) session.getAttribute(Constants.SESSION_KEY_MANAGER);
        if (u != null) {
            threadObj.setManagerUser(u);
            UserTypeEnum type = UserTypeEnum.getUserType(u.getType());
            if (type.getGroup() != UserGroupEnum.head_quarters) {
                throw new ForbiddenException(curIp);
            }
            threadObj.setUserType(type);
        } else {
            threadObj.setUserType(UserTypeEnum.MANAGER);
        }
        threadObj.setIp(curIp);
    }

    /**
     * 合作商后台
     *
     * @param basePath
     * @param url
     * @param curIp
     * @param partnerId
     * @param threadObj
     * @param session
     * @return
     */
    private boolean handlePartnerBg(String basePath, String url, String curIp, Long partnerId, SysThreadObject threadObj, HttpSession session) {
        if (!url.startsWith(basePath + SystemConfig.CONTROL_PATH_PARTNER + "/") && !url.equals(basePath) && !url.equals(basePath + "/")) {
            logger.error("handlePartnerBg path error, url:{}, basePath:{}", url, basePath);
            throw new PageNotFoundException(BaseI18nCode.pageNotFound);
        }
        threadObj.setStationType(StationType.PARTNER);
        PartnerUser u = (PartnerUser) session.getAttribute(Constants.SESSION_KEY_PARTNER);
        if (u != null) {
            if (!Objects.equals(partnerId, u.getPartnerId())) {
                throw new ForbiddenException(BaseI18nCode.userNotAPartner);
            }
            UserTypeEnum type = UserTypeEnum.getUserType(u.getType());
            if (type.getGroup() != UserGroupEnum.partner) {
                throw new ForbiddenException(BaseI18nCode.canntVistPartner, curIp);
            }
            threadObj.setPartnerUser(u);
            threadObj.setUserType(type);
            if (u.getType() == UserTypeEnum.PARTNER_SUPER.getType()) {
                if (!isPartnerSuperUserWhiteIp(curIp)) {
                    throw new ForbiddenException(BaseI18nCode.canntVistPartner, curIp);
                }
            } else if (!isPartnerAdminWhiteIp(curIp, u.getPartnerId())) {
                throw new ForbiddenException(BaseI18nCode.canntVistPartner, curIp);
            }
        } else {
            threadObj.setUserType(UserTypeEnum.PARTNER);
        }
        return true;
    }

    /**
     * 站点相关处理
     *
     * @param domain
     * @param basePath
     * @param url
     * @param curIp
     * @param sysDomain
     * @param threadObj
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    private boolean handleStation(String domain, String basePath, String url, String curIp, StationDomain sysDomain, SysThreadObject threadObj, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Station station = stationService.findOneById(sysDomain.getStationId(), sysDomain.getPartnerId());
        if (station == null || station.getStatus() == Constants.STATUS_DISABLE) {
            throw new ForbiddenException(BaseI18nCode.stationDisabled);
        }
        if (StringUtils.contains(domain, SystemConfig.SYS_GENERAL_DOMAIN)) {
            if (!IpUtils.isManagerWhiteIp(curIp)) {
                throw new ForbiddenException(curIp);
            }
        }
        threadObj.setLanguage(getLanguage(request, response, station));
        threadObj.setStation(station);
        request.setAttribute("language", threadObj.getLanguage());
        switch (sysDomain.getType()) {
            case StationDomain.TYPE_ADMIN:// 站点后台
                return handleAdmin(basePath, url, curIp, station, threadObj, request.getSession());
            case StationDomain.TYPE_AGENT:// 代理商后台
                return handleAgent(basePath, url, curIp, station, threadObj, request.getSession());
            case StationDomain.TYPE_FRONT:// 前台
            case StationDomain.TYPE_APP:// APP
                return handleFront(basePath, url, curIp, station, threadObj, request, response);
//			return handleFrontApp(basePath, url, curIp, station, threadObj, request, response);
            case StationDomain.TYPE_COMMON:// 通用
                return handleCommon(basePath, url, curIp, station, threadObj, request, response);
        }
        return true;
    }
    
    private void setStationToThreadObj(SysThreadObject threadObj, String url, String basePath, StationDomain sysDomain) {
    	 switch (sysDomain.getType()) {
         case StationDomain.TYPE_ADMIN:// 站点后台
         case StationDomain.TYPE_AGENT:// 代理商后台
        	 threadObj.setFrontStation(false);
        	 return;
         case StationDomain.TYPE_FRONT:// 前台
         case StationDomain.TYPE_APP:// APP
        	 threadObj.setFrontStation(true);
        	 return;
         case StationDomain.TYPE_COMMON:// 通用
        	 if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_ADMIN + "/")) {// 站点后台
        		 threadObj.setFrontStation(false);
             } else if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_AGENT + "/")) {// 代理商后台
            	 threadObj.setFrontStation(false);
             } else if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_NATIVE + "/")) {// 前端app
            	 threadObj.setFrontStation(true);
             } else {// 前端
            	 threadObj.setFrontStation(true);
             }
     }
    }

    /**
     * 站点通用域名
     *
     * @param basePath
     * @param url
     * @param curIp
     * @param station
     * @param threadObj
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    private boolean handleCommon(String basePath, String url, String curIp, Station station, SysThreadObject threadObj, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_ADMIN + "/")) {// 站点后台
            return handleAdmin(basePath, url, curIp, station, threadObj, request.getSession());
        } else if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_AGENT + "/")) {// 代理商后台
            return handleAgent(basePath, url, curIp, station, threadObj, request.getSession());
        } else if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_NATIVE + "/")) {// 前端app
            return handleFrontApp(basePath, url, curIp, station, threadObj, request, response);
        } else {// 前端
            return handleFront(basePath, url, curIp, station, threadObj, request, response);
        }
    }

    /**
     * 站点后台
     *
     * @param basePath
     * @param url
     * @param curIp     //* @param sysDomain
     * @param threadObj
     * @param session
     */
    private boolean handleAdmin(String basePath, String url, String curIp, Station station, SysThreadObject threadObj, HttpSession session) {
        if (!url.startsWith(basePath + SystemConfig.CONTROL_PATH_ADMIN + "/") && !url.equals(basePath) && !url.equals(basePath + "/")
                // 后台主页游戏管理，有调用到该接口获取资源
                && !url.contains("/native/v2/get_game_datas.do")) {
            logger.error("handleAdmin path error, url:{}, basePath:{}", url, basePath);
            throw new PageNotFoundException(BaseI18nCode.pageNotFound);
        }
        if (station.getBgStatus() == Constants.STATUS_DISABLE) {
            throw new ForbiddenException(BaseI18nCode.stationBgDisabled);
        }

        threadObj.setStationType(StationType.ADMIN);
        AdminUser u = (AdminUser) session.getAttribute(Constants.SESSION_KEY_ADMIN);
        if (u != null) {
            if (!Objects.equals(station.getId(), u.getStationId())) {
                throw new ForbiddenException(BaseI18nCode.userNotBelongToSite);
            }
            OnlineManagerForAdmin.checkLoginError(u.getId(), u.getStationId());
            UserTypeEnum type = UserTypeEnum.getUserType(u.getType());
            if (type.getGroup() != UserGroupEnum.admin) {
                throw new ForbiddenException(BaseI18nCode.canntVistStationBg, curIp);
            }
            threadObj.setAdminUser(u);
            threadObj.setUserType(type);
            if (u.getType() == UserTypeEnum.ADMIN_MASTER_SUPER.getType() || u.getType() == UserTypeEnum.ADMIN_MASTER.getType()) {
                if (!isSuperAdminUserWhiteIp(curIp)) {
                    throw new ForbiddenException(BaseI18nCode.canntVistStationBg, curIp);
                }
            } else if (!isAdminWhiteIp(threadObj.getIp(), u.getStationId())) {
                throw new ForbiddenException(BaseI18nCode.canntVistStationBg, curIp);
            }
        } else {
            threadObj.setUserType(UserTypeEnum.ADMIN);
        }
        return true;
    }

    /**
     * 站点代理商后台
     *
     * @param basePath
     * @param url
     * @param curIp
     * @param station
     * @param threadObj
     * @param session
     * @return
     */
    private boolean handleAgent(String basePath, String url, String curIp, Station station, SysThreadObject threadObj, HttpSession session) {
        if (!url.startsWith(basePath + SystemConfig.CONTROL_PATH_AGENT + "/") && !url.equals(basePath) && !url.equals(basePath + "/")) {
            logger.error("handleAgent path error, url:{}, basePath:{}", url, basePath);
            throw new PageNotFoundException(BaseI18nCode.pageNotFound);
        }
        if (station.getBgStatus() == Constants.STATUS_DISABLE) {
            throw new ForbiddenException(BaseI18nCode.stationBgDisabled);
        }
        threadObj.setStationType(StationType.AGENT);
        AgentUser u = (AgentUser) session.getAttribute(Constants.SESSION_KEY_AGENT);
        if (u != null) {
            if (!Objects.equals(station.getId(), u.getStationId())) {
                throw new ForbiddenException(BaseI18nCode.userNotBelongToSite);
            }
            UserTypeEnum type = UserTypeEnum.getUserType(u.getType());
            if (type.getGroup() != UserGroupEnum.agent) {
                throw new ForbiddenException(BaseI18nCode.canntVistStationBg, curIp);
            }
            threadObj.setAgentUser(u);
            threadObj.setUserType(type);
            if (!isAgentWhiteIp(threadObj.getIp(), u.getId(), u.getStationId())) {
                throw new ForbiddenException(BaseI18nCode.canntVistAgentBg, curIp);
            }
        } else {
            threadObj.setUserType(UserTypeEnum.AGENT);
        }
        return true;
    }

    /**
     * 前端页面
     *
     * @param basePath
     * @param url
     * @param curIp     // * @param sysDomain
     * @param threadObj
     * @param request
     * @throws IOException
     */
    private boolean handleFront(String basePath, String url, String curIp, Station station, SysThreadObject threadObj, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_MANAGER + "/") || url.startsWith(basePath + SystemConfig.CONTROL_PATH_PARTNER + "/") || url.startsWith(basePath + SystemConfig.CONTROL_PATH_ADMIN + "/")) {
//				|| url.startsWith(basePath + SystemConfig.CONTROL_PATH_NATIVE + "/")) {
            // 前端域名，不能访问总控，合作商后台，站点后台，app路径
            logger.error("handleFront path error, url:{}, basePath:{}", url, basePath);
            throw new PageNotFoundException(BaseI18nCode.pageNotFound);
        }
        if (StationConfigUtil.isOn(station.getId(), StationConfigEnum.maintenance_switch)
                && (url.indexOf("/maintenance.do") == -1 && url.indexOf("zd.do") == -1)) {
            // 站点开启维护
            request.setAttribute("base", basePath);
            response.sendRedirect(basePath + "/maintenance.do");
            return false;
        }
        //如果是APP的链接前缀则处理APP
        if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_NATIVE + "/")) {
            handleFrontApp(basePath, url, curIp, station, threadObj, request, response);
            return true;
        }
        if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_PROXY + "/")) {
            // 是否开启了代理后台
            threadObj.setStationType(StationType.PROXY);
            threadObj.setUserType(UserTypeEnum.PROXY);
            SysUser u = (SysUser) request.getSession().getAttribute(Constants.SESSION_KEY_PROXY);
            if (u != null) {
                OnlineManagerForAdmin.proxyCheckLoginError(u);
                if (!Objects.equals(station.getId(), u.getStationId())) {
                    throw new ForbiddenException(BaseI18nCode.userNotBelongToSite);
                }
                UserTypeEnum type = UserTypeEnum.getUserType(u.getType());
                if (type != UserTypeEnum.PROXY) {
                    throw new ForbiddenException(BaseI18nCode.userNotBelongToSite);
                }
            }
            threadObj.setUser(u);
        } else {

            if (!ipCheckTemplate.check(station.getId(), curIp)) {
                throw new ForbiddenException(BaseI18nCode.stopAccessIp);
            }

            threadObj.setUserType(UserTypeEnum.MEMBER);
            if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_MOBILE + "/")) {// 手机wap端
                threadObj.setStationType(StationType.FRONT_MOBILE);
            } else {// PC端
                threadObj.setStationType(StationType.FRONT_PC);
            }
            SysUser u = (SysUser) request.getSession().getAttribute(Constants.SESSION_KEY_MEMBER);
            if (u != null) {
                if (!Objects.equals(station.getId(), u.getStationId())) {
                    throw new ForbiddenException(BaseI18nCode.userNotBelongToSite);
                }
                OnlineManager.checkLoginError(request.getSession(), u, Constants.SESSION_KEY_MEMBER);
                UserTypeEnum type = UserTypeEnum.getUserType(u.getType());
                if (type != UserTypeEnum.MEMBER && type != UserTypeEnum.PROXY && !GuestTool.isGuest(type.getType())) {
                    throw new ForbiddenException(BaseI18nCode.userNotBelongToSite);
                }
                threadObj.setUser(u);
                threadObj.setUserType(type);
            }
        }
        return true;
    }

    /**
     * 前端app
     *
     * @param basePath
     * @param url
     * @param curIp
     * @param station
     * @param threadObj
     * @param request
     * @param response
     * @return
     */
    private boolean handleFrontApp(String basePath, String url, String curIp, Station station, SysThreadObject threadObj, HttpServletRequest request, HttpServletResponse response) {
        if (!url.startsWith(basePath + SystemConfig.CONTROL_PATH_NATIVE + "/") && !url.startsWith(basePath + SystemConfig.CONTROL_PATH_PC_USERCENTER + "/") && !url.startsWith(basePath + SystemConfig.CONTROL_PATH_PC_MH + "/") && !url.startsWith(basePath + SystemConfig.CONTROL_PATH_R + "/") && !url.startsWith(basePath + SystemConfig.CONTROL_PATH_THIRD + "/") && !url.startsWith(basePath + SystemConfig.CONTROL_PATH_THIRDTRANS + "/")) {
            logger.error("handleFrontApp path error, url:{}, basePath:{}", url, basePath);
            throw new PageNotFoundException(BaseI18nCode.pageNotFound);
        }
        if (StationConfigUtil.isOn(station.getId(), StationConfigEnum.maintenance_switch) && (url.indexOf("/maintenance.do") == -1 && url.indexOf("zd.do") == -1)) {
            // 站点开启维护
            StringBuilder msg = new StringBuilder("maintenance_exception:");
            msg.append(StationConfigUtil.get(station.getId(), StationConfigEnum.maintenance_cause));
            throw new UnStationException(msg.toString());
        }
        threadObj.setStationType(StationType.FRONT_APP);// 手机APP端
        threadObj.setUserType(UserTypeEnum.MEMBER);
        SysUser u = (SysUser) request.getSession().getAttribute(Constants.SESSION_KEY_MEMBER);
        if (u != null) {
            if (!Objects.equals(station.getId(), u.getStationId())) {
                throw new ForbiddenException(BaseI18nCode.userNotBelongToSite);
            }
            OnlineManager.checkLoginError(request.getSession(), u, Constants.SESSION_KEY_MEMBER);
            UserTypeEnum type = UserTypeEnum.getUserType(u.getType());
            if (type != UserTypeEnum.MEMBER && type != UserTypeEnum.PROXY && !GuestTool.isGuest(u.getType())) {
                throw new ForbiddenException(BaseI18nCode.userNotBelongToSite);
            }
            threadObj.setUser(u);
            threadObj.setUserType(type);
        }
        return true;
    }

    /**
     * 获取域名信息
     *
     * @param domain
     * @return
     */
    private StationDomain getStationDomain(String domain) {
        StationDomain d = domainService.findByDomain(domain);
        if (d == null) {
            throw new BaseException(BaseI18nCode.domainUnBind, new Object[]{domain});
        }
        if (d.getStatus() == Constants.STATUS_DISABLE) {
            throw new BaseException(BaseI18nCode.domainIsDisabled, new Object[]{domain});
        }
        return d;
    }

    /**
     * 是否是站点后台总控账号IP白名单
     *
     * @param ip
     * @return
     */
    public static boolean isSuperAdminUserWhiteIp(String ip) {
        if (null != SystemConfig.IP_WHITE_ADMIN && !SystemConfig.IP_WHITE_ADMIN.isEmpty()) {
            for (String d : SystemConfig.IP_WHITE_ADMIN) {
                if (StringUtils.equals(d, ip)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 是否是合作商后台超级用户白名单
     *
     * @param ip
     * @return
     */
    public static boolean isPartnerSuperUserWhiteIp(String ip) {
        if (null != SystemConfig.IP_WHITE_PARTNER && !SystemConfig.IP_WHITE_PARTNER.isEmpty()) {
            for (String d : SystemConfig.IP_WHITE_PARTNER) {
                if (StringUtils.equals(d, ip)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 合作商后台ip白名单
     *
     * @param ip
     * @param partnerId
     * @return
     */
    private boolean isPartnerAdminWhiteIp(String ip, Long partnerId) {
        List<WhiteIpVo> list = partnerWhiteIpService.getIps(partnerId);
        if (list == null || list.isEmpty()) {
            return true;
        }
        for (WhiteIpVo d : list) {
            if (StringUtils.equals(d.getIp(), ip)) {
                return (d.getType() != null && d.getType() == Constants.STATUS_ENABLE);
            }
        }
        return false;
    }

    /**
     * 站点后台ip白名单
     *
     * @param ip
     * @param stationId
     * @return
     */
    private boolean isAdminWhiteIp(String ip, Long stationId) {
        List<WhiteIpVo> list = adminWhiteIpService.getIps(stationId);
        if (list == null || list.isEmpty()) {
            return true;
        }
        for (WhiteIpVo d : list) {
            if (StringUtils.equals(d.getIp(), ip)) {
                return (d.getType() != null && d.getType() == Constants.STATUS_ENABLE);
            }
        }
        return false;
    }

    /**
     * 是否是接受数据的域名
     *
     * @param domain
     * @param basePath
     * @param url
     * @return
     */
    private boolean isReceiveDomain(String domain, String basePath, String url) {
        if (null != SystemConfig.IP_WHITE_RECEIVE && !SystemConfig.IP_WHITE_RECEIVE.isEmpty()) {
            for (String d : SystemConfig.IP_WHITE_RECEIVE) {
                if (StringUtils.equals(d, domain)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 代理商后台白名单
     *
     * @param ip        //* @param id
     * @param stationId
     * @return
     */
    private boolean isAgentWhiteIp(String ip, Long agentId, Long stationId) {
        List<WhiteIpVo> list = agentWhiteIpService.getIps(agentId, stationId);
        if (list == null || list.isEmpty()) {
            return true;
        }
        for (WhiteIpVo d : list) {
            if (StringUtils.equals(d.getIp(), ip)) {
                return (d.getType() != null && d.getType() == Constants.STATUS_ENABLE);
            }
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SysThreadVariable.clear();
    }
}
