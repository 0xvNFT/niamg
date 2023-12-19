package com.play.tronscan.business.reload;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.play.model.StationDepositBank;
import com.play.model.dto.TronUSDTDto;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.play.core.CurrencyEnum;
import com.play.core.StationConfigEnum;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.TronTransUserTask;
import com.play.service.StationService;
import com.play.web.utils.StationConfigUtil;

@Component
public class TronUSDTExchange {
	/** log */
	static Logger log = LoggerFactory.getLogger(TronUSDTExchange.class);
	@Autowired
	private StationService stationService;

	/**
	 * 在此完成 USDT->会员货币的额度转换
	 * 
	 */
	public TronUSDTDto doExchangeReload(TronTransUserTask task, SysUser user, StationDepositBank stationDepositBank) {
		Station station = stationService.findOneById(user.getStationId());
		if (station == null) {
		//	log.error("can not find this station={}", user.getStationId());
			throw new RuntimeException("can not find this station..");
		}
		String currency = station.getCurrency();
		if (CurrencyEnum.getCurrency(currency) == null) {
		//	log.error("this station:{}, has no currency...", user.getStationId());
			throw new RuntimeException("can not find station currency...");
		}

		TronUSDTDto dto = new TronUSDTDto();
		if (ObjectUtils.isNotEmpty(stationDepositBank.getDepositRate())) {
			dto.setRate(stationDepositBank.getDepositRate());
		} else {
			String exchange = StationConfigUtil.get(user.getStationId(), StationConfigEnum.pay_tips_deposit_usdt_rate);
			dto.setRate(new BigDecimal(exchange));
		}
		dto.setNum(new BigDecimal(task.getTransValue()));

		BigDecimal exchangeMoney = dto.getNum().multiply(dto.getRate()).setScale(6, BigDecimal.ROUND_HALF_UP);
		dto.setExchangeMoney(exchangeMoney);

	//	log.error("doExchangeReload dto:{}", JSONObject.toJSON(dto));
		return dto;
	}
}
