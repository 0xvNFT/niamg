//package com.play.job;
//
//import java.util.Calendar;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.play.service.CleanDataService;
//
//public class CleanDataJob {
//	private static Logger logger = LoggerFactory.getLogger(CleanDataJob.class);
//	@Autowired
//	private CleanDataService cleanDataService;
//
//	public void doExecute() {
//		Calendar now = Calendar.getInstance();
//		try {
//			now.add(Calendar.DAY_OF_MONTH, -1);
//			logger.error("---开始清理推送任务数据－－－start");
////			cleanDataService.cleanSendResult(now.getTime());
//			logger.error("---清理推送任务数据完－－－end");
//		} catch (Exception e) {
//			logger.error("---清理推送任务数据发生异常---------", e);
//		}
//	}
//}
