package com.play.tronscan.alljob.imple;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.play.model.TronTransIndexAddress;
import com.play.service.TronTransIndexAddressService;
import com.play.tronscan.allclient.TronScanClient;
import com.play.tronscan.allclient.alltool.SysPreDefineTron;
import com.play.tronscan.allclient.clientbase.res.TronScanResTRC20Transfer;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransaction;
import com.play.tronscan.business.TronAddressCollectionWorker;

@Component
public class TronJobForAdressCollectionProxy {
	/** log */
	static Logger logger = LoggerFactory.getLogger(TronJobForAdressCollectionProxy.class);
	@Autowired
	private TronAddressCollectionWorker worker;
	@Autowired
	private TronTransIndexAddressService indexService;

	public void doProcess(String tronLinkAddress) {
		TronTransIndexAddress indexModel = indexService.findRecord(tronLinkAddress);
		logger.error("TronJobForAdressCollectionProxy doProcess tronLinkAddress:{}", tronLinkAddress);
		if (indexModel == null) {
			indexService.insert(new Date(), tronLinkAddress);
			logger.error("TronJobForAdressCollectionProxy doProcess insertAddr:{}", tronLinkAddress);
			return;
		}
		doProcess(indexModel, tronLinkAddress);
	}

	void doProcess(TronTransIndexAddress model, String tronLinkAddr) {
		Long current = System.currentTimeMillis();
		Long fromTime = model.getLastTimestamp().getTime() - SysPreDefineTron.DEFAULT_REDUNDENCY_RECORD_MILLE_SECONDS;
		Long toTime = getToTime(model.getLastTimestamp().getTime(), current);
		//抓取用户验证地址时的转帐记录
		List<TronScanResTransaction> trc10List = TronScanClient.getTrc10TransactionRelatedAddress(fromTime, toTime, model.getTronLinkAddr());
		logger.error("TronJobForAdressCollectionProxy doProcess trc10List:{}", JSONObject.toJSON(trc10List));
		List<TronScanResTRC20Transfer> trc20List = TronScanClient.getTrc20TransfersRelatedAddress(fromTime, toTime, model.getTronLinkAddr());
		logger.error("TronJobForAdressCollectionProxy doProcess trc20List:{}", JSONObject.toJSON(trc20List));
		worker.processRecord(toTime, model.getTronLinkAddr(), trc10List, trc20List);
	}

	private Long getToTime(Long lastRunTime, Long current) {
		Long toTime = lastRunTime + SysPreDefineTron.DEFAULT_MAX_COLLECTION_INTERVAL_MILLSECONDS;
		return toTime < current ? toTime : current;
	}
//	public static void main(String[] args) {
//		List<TronScanResTransaction> trc10List = TronScanClient.getTrc10TransactionRelatedAddress(1691583530000L, 1691594330000L, "TQ2KEmrjUwfUrR5tEMcxpZoUKow5gtDcwx");
//		System.out.println(JSON.toJSON(trc10List));
//	}
}
