package com.play.tronscan.alljob;


import com.play.model.SysUserBank;
import com.play.model.SysUserTronLink;
import com.play.service.SysUserBankService;

import org.springframework.beans.factory.annotation.Autowired;

import com.play.service.SysUserTronLinkService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 先这么写，后面再优化
 */
public class TronJobForUserAddressBindClean {
	/** log */
//	static Logger logger = LoggerFactory.getLogger(TronJobForUserAddressBindClean.class);
	@Autowired
	private SysUserTronLinkService userTronLinkService;
	@Autowired
	private SysUserBankService sysUserBankService;

	/** 十分钟一次 */
	@Transactional
	public void doExecute() throws InterruptedException {
	//	logger.error("TronJobForUserAddressBindClean job start....");

		List<SysUserTronLink> unBindList = userTronLinkService.getUnBindList();
		//logger.error("TronJobForUserAddressBindClean unBindList:{}", JSONObject.toJSON(unBindList));

		List<Long> tronLinkIds = new ArrayList<>();
		for(SysUserTronLink sysUserTronLink : unBindList) {
			// 15分钟超时
			if (LocalDateTime.now().plusMinutes(-3).isBefore(sysUserTronLink.getInitValidDatetime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
				continue;
			}
			tronLinkIds.add(sysUserTronLink.getId());
		}
		//logger.error("TronJobForUserAddressBindClean tronLinkIds:{}", tronLinkIds);

		List<SysUserBank> disableList = sysUserBankService.getUSDTDisableList();
		//logger.error("TronJobForUserAddressBindClean disableList:{}", JSONObject.toJSON(disableList));

		List<Long> userBankIds = new ArrayList<>();
		for(SysUserBank sysUserBank : disableList) {
			// 15分钟超时
			if (LocalDateTime.now().plusMinutes(-3).isBefore(sysUserBank.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
				continue;
			}
			userBankIds.add(sysUserBank.getId());
		}
	//	logger.error("TronJobForUserAddressBindClean userBankIds:{}", userBankIds);

		sysUserBankService.delUSDTTimeout(userBankIds);
		userTronLinkService.deleteTimeoutInit(tronLinkIds);

	//	logger.error("TronJobForUserAddressBindClean job end....");
	}
}
