package com.play.web.controller.admin.system;

import com.play.cache.redis.DistributedLockUtil;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationAdviceContent;
import com.play.model.StationAdviceFeedback;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationAdviceFeedbackService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * 租户建议反馈管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/adviceFeedback")
public class AdminAdviceController extends AdminBaseController {

    @Autowired
    private StationAdviceFeedbackService stationAdviceFeedbackService;

    @Permission("admin:adviceFeedback")
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        map.put("startTime", DateUtil.getCurrentDate() + " 00:00:00");
        map.put("endTime", DateUtil.getCurrentDate() + " 23:59:59");
        return returnPage("/system/advice/adviceIndex");
    }

    @Permission("admin:adviceFeedback")
    @RequestMapping("/list")
    @ResponseBody
    @NeedLogView("建议反馈列表")
    public void list(Integer sendType, String sendAccount, String startTime, String endTime) {
        boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
        if (!hasViewAll && StringUtils.isEmpty(sendAccount)) {
            renderJSON(new Page<StationAdviceFeedback>());
        } else {
            Date begin = DateUtil.toDatetime(startTime);
            Date end = DateUtil.toDatetime(endTime);
            renderJSON(stationAdviceFeedbackService.getAdminPage(SystemUtil.getStationId(), sendType, sendAccount,
                    begin, end));
        }
    }

    @Permission("admin:adviceFeedback")
    @RequestMapping("/item")
    public String item(Map<String, Object> map, Long id) {
        map.put("advcie", stationAdviceFeedbackService.findOne(id, SystemUtil.getStationId()));
        return returnPage("/system/advice/adviceItem");
    }

    @Permission("admin:adviceFeedback:del")
    @ResponseBody
    @RequestMapping("/delete")
    public void delete(String ids) {
        stationAdviceFeedbackService.sendDelete(ids, SystemUtil.getStationId());
        renderSuccess();
    }


    @Permission("admin:adviceFeedback:replay")
    @RequestMapping("/reply")
    public String reply(Map<String, Object> map, Long id) {
        map.put("adviceId",id);
        map.put("advcie", stationAdviceFeedbackService.findOne(id, SystemUtil.getStationId()));
        map.put("adviceList", stationAdviceFeedbackService.getStationAdviceContentList(id));
        return returnPage("/system/advice/adviceReply");
    }

    @Permission("admin:adviceFeedback:replay")
    @RequestMapping("/saveAdviceReply")
    @ResponseBody
    public void saveAdviceReply(StationAdviceContent advice, Long adviceId) {
        Long stationId = SystemUtil.getStationId();
        if (!DistributedLockUtil
                .tryGetDistributedLock("advice_saveAdviceReply_station_" + stationId+"_adviceId_"+adviceId, 3)) {
            throw new ParamException(BaseI18nCode.concurrencyLimit30);
        }
        advice.setContentType(StationAdviceContent.CONTENT_TYPE_ADMIN);
        advice.setUserId(LoginAdminUtil.getUserId());
        advice.setUsername(LoginAdminUtil.getUsername());
        advice.setCreateTime(new Date());
        stationAdviceFeedbackService.saveAdviceReply(advice);
        renderSuccess();
    }
}
