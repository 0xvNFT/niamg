package com.play.job;

import com.play.common.utils.DateUtil;
import com.play.dao.StationTaskStrategyDao;
import com.play.model.StationTaskStrategy;
import com.play.service.StationTaskStrategyService;
import com.play.web.var.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;

public class WithdrawalGiftPolicyHandlerJob {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawalGiftPolicyHandlerJob.class);
    @Autowired
    private StationTaskStrategyDao stationTaskStrategyDao;
    @Autowired
    private StationTaskStrategyService stationTaskStrategyService;


    public void doExecute() {
      //  logger.info("站点每日提款赠送策略job");
        Date date = DateUtil.dayFirstTime(new Date(),-1);
        List<StationTaskStrategy> stationTaskStrategies = stationTaskStrategyDao.findByTaskType(StationTaskStrategy.first_withdraw_taskType,
                null,StationTaskStrategy.first_withdraw,date,null);
        if (stationTaskStrategies == null || stationTaskStrategies.isEmpty()) {
        //    logger.error("站点每日提款赠送策略为空");
            return;
        }
        for (StationTaskStrategy stationTaskStrategy: stationTaskStrategies) {
            try {
                stationTaskStrategyService.withdrawalGiftPolicyHandler(stationTaskStrategy,date);
            }catch (Exception e) {
                logger.error("站点每日提款赠送策略job发生错误,站点"+stationTaskStrategy.getStationId(),e);
            }
        }
    }
}
