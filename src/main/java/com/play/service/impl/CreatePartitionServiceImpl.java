package com.play.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.CreatePartitionDao;
import com.play.service.CreatePartitionService;

@Service
public class CreatePartitionServiceImpl implements CreatePartitionService {
	@Autowired
	private CreatePartitionDao createParttionDao;

	@Override
	public void createLogPartition(Date date) {
		createParttionDao.createLogPartition(date);
	}

	@Override
	public void createDailyMoneyPartition(Date date) {
		createParttionDao.createDailyMoneyPartition(date);
	}

	@Override
	public void createUserBonusPartition(Date date) {
		createParttionDao.createUserBonusPartition(date);
	}

	@Override
	public void createMoneyHistoryPartition(Date date) {
		createParttionDao.createMoneyHistoryPartition(date);
	}

	@Override
	public void createBetHistoryPartition(Date date) {
		createParttionDao.createBetHistoryPartition(date);
	}

	@Override
	public void createTronRecordPartition(Date date) {
		createParttionDao.createTronRecordPartition(date);
	}

	@Override
	public void createScoreHistoryPartition(Date date) {
		createParttionDao.createScoreHistoryPartition(date);
	}

	@Override
	public void createKickbackRecordPartition(Date date) {
		createParttionDao.createKickbackRecordPartition(date);
	}

	@Override
	public void createProxyDailyBetStatPartition(Date date) {
		createParttionDao.createProxyDailyBetStatPartition(date);
	}
}
