package com.play.job;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.play.common.utils.DateUtil;
import com.play.model.Station;
import com.play.service.StationDailyDepositService;
import com.play.service.StationService;

public class StationDailyDepositReportHandlerJob {
    private static final Logger logger = LoggerFactory.getLogger(StationDailyDepositReportHandlerJob.class);
    @Autowired
    private StationService stationService;
    @Autowired
    private StationDailyDepositService stationDailyDepositService;


    public void doExecute() {
        List<Station> stations = stationService.getAll();
        if (stations == null || stations.isEmpty()) {
            logger.info("站点为空");
            return;
        }
      //  logger.error("站点每日存款报表开始进行统计。");
        Date date = DateUtil.dayFirstTime(new Date(),-1);
        for (Station station: stations) {
            try {
            	stationDailyDepositService.generateDepositReport(date,station);
            }catch (Exception e) {
                logger.error("存款报表统计发生错误，站点"+station.getCode(),e);
            }
        }
    }
}
