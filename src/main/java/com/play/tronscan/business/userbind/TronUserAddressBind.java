package com.play.tronscan.business.userbind;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.play.core.TronLinkStatusEnum;
import com.play.service.SysUserBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.play.model.SysUserTronLink;
import com.play.service.SysUserTronLinkService;
import com.play.tronscan.allclient.alltool.SimpleTronLinkUtil;
import com.play.tronscan.allclient.clientbase.res.TronScanResTransaction;

@Component
public class TronUserAddressBind {
	/** log */
	static Logger logger = LoggerFactory.getLogger(TronUserAddressBind.class);
	@Autowired
	private SysUserTronLinkService service;
	@Autowired
	private SysUserBankService sysUserBankService;

	public void tryBind(String tronLinkAddr, List<TronScanResTransaction> recordSegment) {
		/** 可能的用户初始化 */
		recordSegment.stream()
				// filter 最小值【0.0001】TRX
				.filter(t -> SimpleTronLinkUtil.isValidTRXTransaction(t, tronLinkAddr, 100L))
				// forEach
				.forEach(t -> tryBind_i(t));
	}

	private void tryBind_i(TronScanResTransaction transaction) {
		SysUserTronLink model = service.findOne(transaction.ownerAddress);
		//logger.error("tryBind_i model:{}", JSONObject.toJSON(model));
		// 为空
		if (model == null ) {
	//		logger.error("tryBind_i null ");
			return;
		}
		// 已绑定
		if (TronLinkStatusEnum.bind.getType().equals(model.getBindStatus())) {
			//logger.error("tryBind_i already bind, tronLinkAddr:{}", model.getTronLinkAddr());
			return;
		}
		// 金额不相等
		if (model.getInitTrx().subtract(SimpleTronLinkUtil.getReadableValue(transaction.getTrxAmount())).setScale(4, BigDecimal.ROUND_HALF_DOWN).compareTo(BigDecimal.ZERO) != 0) {
		//	logger.error("tryBind_i not equal, tronLinkAddr:{}, initTrx:{}, trxAmount:{}", model.getTronLinkAddr(), model.getInitTrx(), transaction.getTrxAmount());
			return;
		}
		// 15分钟超时
		if (LocalDateTime.now().plusMinutes(-15).isAfter(model.getInitValidDatetime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
			//logger.error("tryBind_i timeOut, tronLinkAddr:{}", model.getTronLinkAddr());
			return;
		}
		service.updateInitSuccess(transaction.ownerAddress);
		sysUserBankService.updateUSDTInitBind(model.getUserId(), model.getStationId(), model.getTronLinkAddr());
	}

}
