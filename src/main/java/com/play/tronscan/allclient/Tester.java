package com.play.tronscan.allclient;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.play.tronscan.allclient.alltool.SimpleTronLinkUtil;
import com.play.tronscan.allclient.clientbase.TronScanBaseClient;
import com.play.tronscan.allclient.clientbase.res.TronScanResTRC20Transfer;

/**
 * Tester
 */
public class Tester {
	public static void testClientBase() {
		System.out.println(JSON.toJSONString(TronScanBaseClient.sysStatus()));
		System.out.println(JSON.toJSONString(TronScanBaseClient.latestBlock()));
		System.out.println(JSON.toJSONString(TronScanBaseClient.account("TX4Fbg6RCm9KgddVdcmdRpY17DF1wGfgp7")));
		System.out.println(JSON.toJSONString(TronScanBaseClient.trc20Transfers(50, 0, "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t", null, 1686485667000L, null)));
	}

	public static void testClientHigh() {
		// System.out.println(JSON.toJSONString(TronScanClient.getUSDTTransfers(1686485659000L,
		// "d1948a5b2209a96cec9191de148b49f4c6cf2827d7932f889c861011dac824c0")));
		System.out.println(JSON.toJSONString(TronScanClient.getTrc20TransfersRelatedAddress(1687093200000L, 1687100406994L, "TKD3XGU1Uw6NRf6pjfBzKMSK7F2x616PD2")));
	}

	public static void main(String[] args) {
//		testClientBase();
//		testClientHigh();
		List<TronScanResTRC20Transfer> aa = TronScanClient.getTrc20TransfersRelatedAddress(1687093200000L, 1687100406994L, "TKD3XGU1Uw6NRf6pjfBzKMSK7F2x616PD2");
		aa.stream()
				// filter 最小值【10】USDT
				.filter(t -> SimpleTronLinkUtil.isValidUSDTTransaction(t, "TKD3XGU1Uw6NRf6pjfBzKMSK7F2x616PD2", 10L))
				// forEach
				.forEach(t -> {
					System.out.println(JSON.toJSONString(t));
				});

	}
}
