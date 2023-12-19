package com.play.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.play.core.StationConfigEnum;
import com.play.model.Station;
import com.play.service.StationKickbackRecordService;
import com.play.service.StationService;
import com.play.web.utils.StationConfigUtil;

public class AutoBackwaterJob {

    private Logger logger = LoggerFactory.getLogger(AutoBackwaterJob.class);
    @Autowired
    private StationService stationService;
    @Autowired
    private StationKickbackRecordService krService;

    public void doExecute() {
        List<Station> stationList = stationService.getAll();
        if (stationList == null || stationList.isEmpty()) {
            return;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date end = c.getTime();
        //三方体育通常第二天才结算，调整成反三天内的反水记录
        c.add(Calendar.DAY_OF_MONTH, -3);
        Date begin = c.getTime();
        try {
          //  logger.info("自动反水开始");
            for (Station s: stationList) {
                if (StationConfigUtil.isOn(s.getId(), StationConfigEnum.auto_backwater)) {
                    krService.autoBackwater(begin,end,s.getId());
                }
            }
        //    logger.info("自动反水结束");
        } catch (Exception e) {
            logger.error("自动反水发生错误", e);
        }
    }

}
