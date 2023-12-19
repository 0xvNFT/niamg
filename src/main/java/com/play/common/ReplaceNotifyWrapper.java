package com.play.common;

import com.play.dao.MnyDrawRecordDao;
import com.play.service.MnyDepositRecordService;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.service.StationReplaceWithDrawService;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ReplaceNotifyWrapper {

    /**
     * 代付回调接口方法
     * @param request
     * @param response
     * @throws IOException
     */
    public void replaceNotify(HttpServletRequest request, HttpServletResponse response, @PathVariable String iconCss,
                              MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService,
                       StationReplaceWithDrawService stationReplaceWithDrawService) throws Exception;
}
