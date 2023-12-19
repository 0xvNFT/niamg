package com.play.web.controller.notify;
import com.play.common.PayNotifyWrapper;
import com.play.common.PayNotifyWrapperFactory;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
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
@RequestMapping("/onlinePay/notify")
public class PayNotifyController {
    private static Logger logger = LoggerFactory.getLogger(PayNotifyController.class);
    @Autowired
    private MnyDepositRecordService mnyDepositRecordService;
    @Autowired
    private StationDepositOnlineService stationDepositOnlineService;
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/v2/{iconCss}")
    public void notify(HttpServletRequest request, HttpServletResponse response, @PathVariable String iconCss) throws Exception {
        if(StringUtils.isEmpty(iconCss)){
            logger.error("PayNotify error, payment is not exist, payment:{}", iconCss);
        }
        try {
            PayNotifyWrapper payNotifyWrapper = PayNotifyWrapperFactory.getNotifyController(iconCss);
            payNotifyWrapper.notify(request,response,iconCss,mnyDepositRecordService,stationDepositOnlineService);
        }catch (Exception e){
            logger.error("PayNotify exception:{}", e.getMessage(), e);
        }

    }
}
