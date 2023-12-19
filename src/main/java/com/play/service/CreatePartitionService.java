package com.play.service;

import java.util.Date;

public interface CreatePartitionService {

	void createLogPartition(Date date);

	void createDailyMoneyPartition(Date date);
	void createUserBonusPartition(Date date);

	void createMoneyHistoryPartition(Date date);

	void createBetHistoryPartition(Date date);

	void createTronRecordPartition(Date date);

	void createScoreHistoryPartition(Date date);

	void createKickbackRecordPartition(Date date);

	void createProxyDailyBetStatPartition(Date date);

}
