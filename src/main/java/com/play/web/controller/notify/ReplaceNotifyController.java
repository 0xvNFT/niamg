package com.play.web.controller.notify;

import com.play.common.ReplaceNotifyWrapper;
import com.play.common.ReplaceNotifyWrapperFactory;
import com.play.dao.MnyDrawRecordDao;

import com.play.service.MnyDrawRecordService;

import com.play.service.StationReplaceWithDrawService;
import com.play.web.annotation.NotNeedLogin;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/onlinePay/ReplaceNotify")
public class ReplaceNotifyController {
    private static Logger logger = LoggerFactory.getLogger(ReplaceNotifyController.class);
    @Autowired
    private MnyDrawRecordDao mnyDrawRecordDao;
    @Autowired
    private MnyDrawRecordService mnyDrawRecordService;
    @Autowired
    private StationReplaceWithDrawService stationReplaceWithDrawService;

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/v2/{iconCss}")
    public void notify(HttpServletRequest request, HttpServletResponse response, @PathVariable String iconCss) throws Exception {
        if(StringUtils.isEmpty(iconCss)){
            logger.error("ReplaceNotify error, payment is not exist, payment:{}", iconCss);
        }
        try {
            ReplaceNotifyWrapper replaceNotifyController = ReplaceNotifyWrapperFactory.getReplaceNotifyController(iconCss);
            replaceNotifyController.replaceNotify(request,response,iconCss,mnyDrawRecordDao,mnyDrawRecordService,stationReplaceWithDrawService);
        }catch (Exception e){
            logger.error("ReplaceNotify exception:{}", e.getMessage(), e);
        }

    }
}
