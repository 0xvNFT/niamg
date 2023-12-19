package com.play.web.controller.admin.finance;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.MoneyRecordType;
import com.play.model.SysUserMoneyHistory;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserMoneyHistoryService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
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
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/finance/moneyHistory")
public class MoneyHistoryController extends AdminBaseController {

    @Autowired
    private SysUserMoneyHistoryService sysUserMoneyHistoryService;

    @Permission("admin:moneyHistory")
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
    @Permission("admin:moneyHistory")
    @RequestMapping("/list")
    @NeedLogView("账变记录列表")
    @SortMapping(mapping = {"money=money"})
    public void list(String startTime, String endTime, String username, String proxyName, String orderId, String type,
                     BigDecimal minMoney, BigDecimal maxMoney, String operatorName, String agentUser, String remark,
                     String bgRemark,String referrer) {
        boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
        if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
            renderJSON(new Page<SysUserMoneyHistory>());
        } else {
            Date begin = DateUtil.toDatetime(startTime);
            Date end = DateUtil.toDatetime(endTime);
            renderJSON(sysUserMoneyHistoryService.adminPage(SystemUtil.getStationId(), begin, end, username,
                    proxyName, type, orderId, minMoney, maxMoney, operatorName, agentUser, remark, bgRemark,referrer));
        }
    }

    @Permission("admin:moneyHistory")
    @RequestMapping("/showRemarkModify")
    public String remarkModify(Long id, Map<String, Object> map) {
        map.put("money", sysUserMoneyHistoryService.findOne(id, SystemUtil.getStationId()));
        return returnPage("/finance/moneyRdRemarkModify");
    }

    @ResponseBody
    @Permission("admin:moneyHistory")
    @RequestMapping("/remarkModifySave")
    public void withdrawRemarkModifySave(String remark, Long id) {
        sysUserMoneyHistoryService.remarkModify(id, SystemUtil.getStationId(), remark);
        renderSuccess();
    }
}
