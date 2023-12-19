package com.play.tronscan.business.record;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.play.model.SysUserTronLink;
import com.play.model.TronTransUser;
import com.play.model.TronTransUserTask;
import com.play.service.SysUserTronLinkService;
import com.play.service.TronTransUserService;
import com.play.service.TronTransUserTaskService;
import com.play.tronscan.allclient.alltool.EnumTronTransactionType;
import com.play.tronscan.allclient.alltool.SimpleTronLinkUtil;
import com.play.tronscan.allclient.clientbase.res.TronScanResTRC20Transfer;

@Component
public class TronUserUSDTRecord {
	/** log */
	static Logger logger = LoggerFactory.getLogger(TronUserUSDTRecord.class);
	@Autowired
	private TronTransUserService userService;
	@Autowired
	private TronTransUserTaskService userTaskService;
	@Autowired
	private SysUserTronLinkService userTronLinkService;

	public void saveRecord(String tronLinkAddr, List<TronScanResTRC20Transfer> recordList) {
		recordList.stream()
				// filter 最小值【1】USDT
				.filter(t -> SimpleTronLinkUtil.isValidUSDTTransaction(t, tronLinkAddr, 1L))
				// forEach
				.forEach(t -> {
					// 检测重复记录 后面需要用redis来缓存 一次
					if (userService.findOne(t.transaction_id) != null)
						return;
					SysUserTronLink user = userTronLinkService.findOne(t.getFromAddress());
					if (user == null)
						return;
					insertUser(user, t);
					insertUserTask(user, t);
				});
	}

	private void insertUser(SysUserTronLink user, TronScanResTRC20Transfer trans) {
		TronTransUser model = new TronTransUser();
		model.setStationId(user.getStationId());
		model.setTransFrom(trans.getFromAddress());
		model.setTransTo(trans.getToAddress());
		model.setTransValue(SimpleTronLinkUtil.getReadableValue(trans.getTrc20Amount()).doubleValue());
		model.setBlock(trans.block);
		model.setTransactionID(trans.transaction_id);
		model.setTransactionType(EnumTronTransactionType.USDT.code);
		model.setTransactionTimestamp(trans.block_ts);
		model.setCreateDatetime(new Date());

	//	logger.error("TronUserUSDTRecord insertUser:{}", JSONObject.toJSON(model));
		userService.insert(model);
	}

	private void insertUserTask(SysUserTronLink user, TronScanResTRC20Transfer trans) {
		TronTransUserTask model = new TronTransUserTask();
		model.setStationId(user.getStationId());
		model.setTransFrom(trans.getFromAddress());
		model.setTransTo(trans.getToAddress());
		model.setTransValue(SimpleTronLinkUtil.getReadableValue(trans.getTrc20Amount()).doubleValue());
		model.setBlock(trans.block);
		model.setTransactionID(trans.transaction_id);
		model.setTransactionType(EnumTronTransactionType.USDT.code);
		model.setTransactionTimestamp(trans.block_ts);
		model.setCreateDatetime(new Date());

	//	logger.error("TronUserUSDTRecord insertUserTask:{}", JSONObject.toJSON(model));
		userTaskService.insert(model);
	}
}
