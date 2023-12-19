package com.play.tronscan.alljob;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.play.model.TronTransUserTask;
import com.play.service.TronTransUserTaskService;
import com.play.tronscan.allclient.alltool.SimpleTronLinkUtil;
import com.play.tronscan.business.reload.TronUserBalanceReload;
import org.springframework.util.CollectionUtils;

public class TronJobForBalanceReload {
	/** log */
	static Logger logger = LoggerFactory.getLogger(TronJobForBalanceReload.class);
	/** 单次只取出100条记录 */
	private static Integer ontTimeTaskAmout = 100;
	@Autowired
	private TronTransUserTaskService userTaskService;
	@Autowired
	private TronUserBalanceReload userBalanceReload;

	public void doExecute() {
		do {
		//	logger.error("TronJobForBalanceReload job start....");
			try {
				List<TronTransUserTask> taskList = userTaskService.selectList(ontTimeTaskAmout);
//				logger.error("TronJobForBalanceReload taskList:{}", JSONObject.toJSON(taskList));
				if(CollectionUtils.isEmpty(taskList)) {
					SimpleTronLinkUtil.sleepAwhile(2000L);
					continue;
				}
				taskList.forEach(t -> userBalanceReload.doReload(t));
				/** 如果小于【ontTimeTaskAmout】说明任务已经执行完了 */
				if (taskList.size() < ontTimeTaskAmout)
					SimpleTronLinkUtil.sleepAwhile(2000L);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("catch an exception, e:{}", e);
				SimpleTronLinkUtil.sleepAwhile(2000L);
			}
		//	logger.error("TronJobForBalanceReload job end....");
		} while (true);
	}
}
