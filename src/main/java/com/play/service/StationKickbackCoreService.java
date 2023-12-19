package com.play.service;

import java.math.BigDecimal;

import com.play.model.AdminUser;
import com.play.model.StationKickbackRecord;
import com.play.model.StationKickbackStrategy;

public interface StationKickbackCoreService {

	StationKickbackStrategy filterStrategy(StationKickbackRecord record);

	int cancel(StationKickbackRecord r);

	void manualRollbackOne(StationKickbackRecord r, Long stationId, BigDecimal money, AdminUser au);
}
