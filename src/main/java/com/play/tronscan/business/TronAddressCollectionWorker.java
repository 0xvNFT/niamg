package com.play.tronscan.business;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.play.service.TronTransIndexAddressService;
import com.play.tronscan.allclient.clientbase.res.TronScanResTRC20Transfer;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransaction;
import com.play.tronscan.business.record.TronUserUSDTRecord;
import com.play.tronscan.business.userbind.TronUserAddressBind;

/**
 * 按【地址】查询交易
 */
@Component
public class TronAddressCollectionWorker {
	/** log */
	static Logger log = LoggerFactory.getLogger(TronAddressCollectionWorker.class);
	@Autowired
	private TronUserAddressBind userBind;
	@Autowired
	private TronUserUSDTRecord usdtRecord;
	@Autowired
	private TronTransIndexAddressService indexService;

	@Transactional(rollbackFor = Exception.class)
	public void processRecord(Long toTimestamp, String tronLinkAddr, List<TronScanResTransaction> trc10List, List<TronScanResTRC20Transfer> trc20List) {
		indexService.update(new Date(toTimestamp), tronLinkAddr);
		userBind.tryBind(tronLinkAddr, trc10List);
		usdtRecord.saveRecord(tronLinkAddr, trc20List);
	}
}
