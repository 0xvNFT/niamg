package com.play.job;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.play.common.utils.DateUtil;
import com.play.service.CreatePartitionService;

public class CreatePartitionJob {
	private Logger logger = LoggerFactory.getLogger(CreatePartitionJob.class);
	@Autowired
	private CreatePartitionService createPartitionService;

	public void createTable() {
		Calendar c = Calendar.getInstance();
	//	logger.info("开始创建分表");
		createLogPartition(c.getTime());
		createDailyMoneyPartition(c.getTime());
		createUserBonusPartition(c.getTime());
		createMoneyHistoryPartition(c.getTime());
		createTronRecordPartition(c.getTime());
		createBetHistoryPartition(c.getTime());
		createScoreHistoryPartition(c.getTime());
		createKickbackRecordPartition(c.getTime());
		createProxyDailyBetStatPartition(c.getTime());
		
		// 创建下个月分表
		c.add(Calendar.MONTH, 1);
	//	logger.info("开始创建下个月分表");
		createLogPartition(c.getTime());
		createDailyMoneyPartition(c.getTime());
		createUserBonusPartition(c.getTime());
		createMoneyHistoryPartition(c.getTime());
		createTronRecordPartition(c.getTime());
		createBetHistoryPartition(c.getTime());
		createScoreHistoryPartition(c.getTime());
		createKickbackRecordPartition(c.getTime());
		createProxyDailyBetStatPartition(c.getTime());
	}

	private void createLogPartition(Date date) {
		String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
		try {
		//	logger.info("开始创建日志数据的分表 date=" + dateStr);
			createPartitionService.createLogPartition(date);
			//logger.info("成功创建日志数据的分表 date=" + dateStr);
		} catch (Exception e) {
			logger.error("创建日志的分表发生异常 date=" + dateStr, e);
		}
	}

	private void createDailyMoneyPartition(Date date) {
		String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
		try {
		//	logger.info("开始创建会员日报表的分表 date=" + dateStr);
			createPartitionService.createDailyMoneyPartition(date);
		//	logger.info("成功创建会员日报表的分表 date=" + dateStr);
		} catch (Exception e) {
			logger.error("创建会员日报表的分表发生异常 date=" + dateStr, e);
		}
	}

	private void createUserBonusPartition(Date date) {
		String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
		try {
		//	logger.info("开始创建会员奖金报表的分表 date=" + dateStr);
			createPartitionService.createUserBonusPartition(date);
		//	logger.info("成功创建会员奖金报表的分表 date=" + dateStr);
		} catch (Exception e) {
			logger.error("创建会员奖金报表的分表发生异常 date=" + dateStr, e);
		}
	}
	

	private void createMoneyHistoryPartition(Date date) {
		String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
		try {
		//	logger.info("开始创建会员账变表的分表 date=" + dateStr);
			createPartitionService.createMoneyHistoryPartition(date);
			//logger.info("成功创建会员账变表的分表 date=" + dateStr);
		} catch (Exception e) {
			logger.error("创建会员账变表的分表发生异常 date=" + dateStr, e);
		}
	}
	private void createBetHistoryPartition(Date date) {
		String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
		try {
			//logger.info("开始创建会员打码量表的分表 date=" + dateStr);
			createPartitionService.createBetHistoryPartition(date);
		//	logger.info("成功创建会员打码量表的分表 date=" + dateStr);
		} catch (Exception e) {
			logger.error("创建会员打码量表的分表发生异常 date=" + dateStr, e);
		}
	}
	private void createTronRecordPartition(Date date) {
		String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
		try {
		//	logger.error("开始创建Tron交易记录表的分表 date=" + dateStr);
			createPartitionService.createTronRecordPartition(date);
		//	logger.error("成功创建Tron交易记录表的分表 date=" + dateStr);
		} catch (Exception e) {
			logger.error("创建Tron交易记录表的分表 date=" + dateStr, e);
		}
	}
	private void createScoreHistoryPartition(Date date) {
		String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
		try {
		//	logger.info("开始创建会员积分历史表的分表 date=" + dateStr);
			createPartitionService.createScoreHistoryPartition(date);
		//	logger.info("成功创建会员积分历史表的分表 date=" + dateStr);
		} catch (Exception e) {
			logger.error("创建会员积分历史表的分表发生异常 date=" + dateStr, e);
		}
	}
	
	private void createKickbackRecordPartition(Date date) {
		String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
		try {
		//	logger.info("开始创建station_kickback_record的分表 date=" + dateStr);
			createPartitionService.createKickbackRecordPartition(date);
		//	logger.info("成功创建station_kickback_record的分表 date=" + dateStr);
		} catch (Exception e) {
			logger.error("创建station_kickback_record的分表发生异常 date=" + dateStr, e);
		}
	}
	
	private void createProxyDailyBetStatPartition(Date date) {
		String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
		try {
			//logger.info("开始创建proxy_daily_bet_stat的分表 date=" + dateStr);
			createPartitionService.createProxyDailyBetStatPartition(date);
			//logger.info("成功创建proxy_daily_bet_stat的分表 date=" + dateStr);
		} catch (Exception e) {
			logger.error("创建proxy_daily_bet_stat的分表发生异常 date=" + dateStr, e);
		}
	}
}
