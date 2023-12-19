package com.play.tronscan.alljob;

import com.alibaba.fastjson.JSONObject;
import com.play.core.TronLinkStatusEnum;
import com.play.model.StationDepositBank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.play.service.StationDepositBankService;
import com.play.tronscan.allclient.alltool.SimpleTronLinkUtil;
import com.play.tronscan.allclient.alltool.SysPreDefineTron;
import com.play.tronscan.alljob.imple.TronJobForAdressCollectionProxy;

import java.util.List;

public class TronJobForAdressCollection {
	/** log */
	static Logger logger = LoggerFactory.getLogger(TronJobForAdressCollection.class);
	@Autowired
	private TronJobForAdressCollectionProxy proxy;
	@Autowired
	private StationDepositBankService service;

	public void doExecute() {
		do {
//			logger.error("TronJobForAdressCollection job start....");
			try {
				List<StationDepositBank> list = service.getAllUsdtBank();
//				logger.error("TronJobForAdressCollection USDTBank list:{}", JSONObject.toJSON(list));
				list.stream()
						// isValidBase58Address
						.filter(t -> SysPreDefineTron.isValidBase58Address(t.getBankCard()))
						// getTronLinkStatus
						.filter(t -> TronLinkStatusEnum.bind.getType().equals(t.getStatus()))
						// forEach
						.forEach(t -> {
							proxy.doProcess(t.getBankCard());
							// 每次请求完成 等待一会 否则容易被封IP
							SimpleTronLinkUtil.sleepAwhile(6_000L);
						});
				SimpleTronLinkUtil.sleepAwhile(10_000L);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("TronJobForAdressCollection Exception:{}", e);
				SimpleTronLinkUtil.sleepAwhile(10_000L);
			}
//			logger.error("TronJobForAdressCollection job end....");
		} while (true);
	}
}
