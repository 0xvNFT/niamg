package com.play.common;

import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PayNotifyWrapper {

    /**
     * 支付回调接口方法
     * @param request
     * @param response
     * @throws IOException
     */
    public void notify(HttpServletRequest request, HttpServletResponse response,@PathVariable String iconCss,
                       MnyDepositRecordService mnyDepositRecordService,
                       StationDepositOnlineService stationDepositOnlineService) throws Exception;
}
