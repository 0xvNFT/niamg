package com.play.web.controller.proxy.third;

import com.alibaba.fastjson.JSON;
import com.play.common.SystemConfig;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.core.PlatformType;
import com.play.core.UserTypeEnum;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.vo.ThirdSearchVo;

import com.play.service.SysUserService;
import com.play.service.ThirdCenterService;

import com.play.web.controller.proxy.ProxyBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_PROXY + "/egameRecord")
public class ProxyEgameRecordController extends ProxyBaseController {

    @Autowired
    private ThirdCenterService thirdCenterService;

    @Autowired
    private SysUserService userService;

    //@Permission("admin:egameRecord")
    @RequestMapping("/index")
    public String index(String begin, String end, String username, Map<String, Object> map, String proxyName) {
        if (StringUtils.isEmpty(begin)) {
            begin = DateUtil.getCurrentDate();
        }
        if (StringUtils.length(begin) < 19) {
            begin += " 00:00:00";
        }
        if (StringUtils.isEmpty(end)) {
            end = DateUtil.getCurrentDate();
        }
        if (StringUtils.length(end) < 19) {
            end += " 23:59:59";
        }
        map.put("username", username);
        map.put("proxyName",proxyName);
        map.put("startTime", begin);
        map.put("endTime", end);
        map.put("platforms2", PlatformType.getEgamePlatform());
        map.put("platforms", PlatformType.values());
        return returnPage("/third/egameRecordIndex");
    }


    //@Permission("admin:egameRecord")
    @RequestMapping("/list")
    @ResponseBody
    public void list(ThirdSearchVo v,String proxyName) {

        Station station = SystemUtil.getStation();
        v.setStation(station);

        String proxy = null;
        String user = null;
        if (StringUtils.isEmpty(proxyName)) {
            proxyName = LoginMemberUtil.getUsername();
            v.setUsername(proxyName);
            proxy = thirdCenterService.getEgamePage(v);
            v.setUsername(null);
        }

        SysUser u = userService.findOneByUsername(proxyName, SystemUtil.getStationId(), null);
        if (u != null && StringUtils.isNotEmpty(u.getParentNames()) && !u.getParentNames().contains(LoginMemberUtil.getUsername())) {
            u = LoginMemberUtil.currentUser();
        }
        if (u != null) {
            if (Objects.equals(u.getType(), UserTypeEnum.MEMBER.getType())) {
                v.setRecommendId(u.getId());
            } else {
                String parentIds = u.getParentIds();
                if (StringUtils.isNotEmpty(parentIds)) {
                    parentIds = parentIds + u.getId() + ",";
                } else {
                    parentIds = "," + u.getId() + ",";
                }
                v.setParentIds(parentIds);
            }
        } else {
            throw new ParamException(BaseI18nCode.proxyUnExist);
        }
        user = thirdCenterService.getEgamePage(v);

        Map userMap  = (Map) JSON.parse(user);
        if (StringUtils.isNotEmpty(proxy)){
            Map proxyMap = (Map) JSON.parse(proxy);
            userMap.put("total",(Integer)userMap.get("total")+(Integer)proxyMap.get("total"));
            Map aggsDataMap = (Map)userMap.get("aggsData");
            Map proxyAggsDataMap =(Map)proxyMap.get("aggsData");
            aggsDataMap.put("bettingMoneyCount", BigDecimalUtil.addAll((BigDecimal) aggsDataMap.get("bettingMoneyCount"),(BigDecimal) proxyAggsDataMap.get("bettingMoneyCount")));
            aggsDataMap.put("realBettingMoneyCount", BigDecimalUtil.addAll((BigDecimal) aggsDataMap.get("realBettingMoneyCount"),(BigDecimal) proxyAggsDataMap.get("realBettingMoneyCount")));
            aggsDataMap.put("winMoneyCount", BigDecimalUtil.addAll((BigDecimal) aggsDataMap.get("winMoneyCount"),(BigDecimal) proxyAggsDataMap.get("winMoneyCount")));

            List list = (List)userMap.get("rows");
            List proxylist = (List)proxyMap.get("rows");
            list.addAll(proxylist);
        }
        renderJSON(userMap);


	}

}
