package com.play.web.controller.proxy.finance;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.MoneyRecordType;
import com.play.model.SysUser;

import com.play.orm.jdbc.annotation.SortMapping;

import com.play.service.SysUserMoneyHistoryService;
import com.play.service.SysUserService;
import com.play.web.annotation.NeedLogView;

import com.play.web.controller.proxy.ProxyBaseController;

import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 账变记录管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_PROXY + "/finance/moneyHistory")
public class ProxyMoneyHistoryController extends ProxyBaseController {

    @Autowired
    private SysUserMoneyHistoryService sysUserMoneyHistoryService;
    @Autowired
    private SysUserService sysUserService;

    //@Permission("proxy:moneyHistory")
    @RequestMapping("/index")
    public String index(Map<String, Object> map, String username, String startTime, String endTime, String proxyName,
                        String moneyType, String agentUser) {
        map.put("startTime",
                StringUtils.isEmpty(startTime) ? DateUtil.formatDate(new Date(), "yyyy-MM-dd") + " 00:00:00"
                        : startTime);
        map.put("endTime",
                StringUtils.isEmpty(endTime) ? DateUtil.formatDate(new Date(), "yyyy-MM-dd" + " 23:59:59") : endTime);
        map.put("username", username);
        map.put("proxyName", proxyName);
        map.put("moneyType", moneyType);
        map.put("agentUser", agentUser);
        map.put("types", MoneyRecordType.getTypes());
        return returnPage("/finance/money/moneyHistoryIndex");
    }

    @ResponseBody
    //@Permission("proxy:moneyHistory")
    @RequestMapping("/list")
    @NeedLogView("账变记录列表")
    @SortMapping(mapping = {"money=money"})
    public void list(String startTime, String endTime, String username, String orderId, String type,
                     BigDecimal minMoney, BigDecimal maxMoney, String operatorName, String agentUser, String remark,
                     String bgRemark,String referrer,String proxyName) {

            Date begin = DateUtil.toDatetime(startTime);
            Date end = DateUtil.toDatetime(endTime);


        if (StringUtils.isNotEmpty(proxyName)){
            SysUser sysUser = sysUserService.findOneByUsername(proxyName,SystemUtil.getStationId(),null);

            if (sysUser!=null&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
                proxyName = LoginMemberUtil.getUsername();
            }
        }else {
            proxyName = LoginMemberUtil.getUsername();
        }

            renderJSON(sysUserMoneyHistoryService.adminPage(SystemUtil.getStationId(), begin, end, username,
                    proxyName, type, orderId, minMoney, maxMoney, operatorName, agentUser, remark, bgRemark,referrer));

    }

}
