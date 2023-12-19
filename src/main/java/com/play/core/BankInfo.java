package com.play.core;

import java.util.ArrayList;
import java.util.List;

public enum BankInfo {
	/**
	 * 巴西银行信息
	 */
	PIX("PIX", "", "PIX"),
	USDT("USDT", "", "USDT"),
	OTHER("OTHER", "OTHER", "OTHER"),
	/**
	 * 泰国银行信息
	 */
	SCB("SCB", "", "SIAM-COMMERCIAL-BANK"),
	KBANK("KBANK", "", "KASIKORNBANK-PCL"),
	KTB("KTB", "", "KRUNG-THAI-BANK"),
	BBL("BBL", "", "BANGKOK-BANK"),
	TMB("TMB", "", "TMB-BANK"),
	/**
	 * 泰国银行扫码银行key
	 */
	SCBREVERSE("SCBREVERSE", "", "SIAM-COMMERCIAL-QR"),
	KBANKREVERSE("KBANKREVERSE", "", "KASIKORNBANK-PCL-QR"),
	KTBREVERSE("KTBREVERSE", "", "KRUNG-THAI-BANK-QR"),
	BBLREVERSE("BBLREVERSE", "", "BANGKOK-BANK-QR"),
	TMBREVERSE("TMBREVERSE", "", "TMB-BANK-QR"),

	/**
	 * 印度银行配置
	 */
	CANARN("CANARN", "", "CANARA-BANK"),
	BNIB("BNIB", "", "BNIB-BANK"),
	UPI("UPI", "", "UPI-BANK"),
	PAYTM("PAYTM", "", "PAYTM-BANK"),

	/**
	 * 越南银行配置
	 */
	TCB("TCB", "", "Techcombank"),
	SACOM("SACOM", "", "Sacombank"),
	VCB("VCB", "", "Vietcombank"),
	ACB("ACB", "", "Asia Commercial Bank"),
	DAB("DAB", "", "DongA Bank"),
	VTB("VTB", "", "Vietin Bank"),
	ZALO("ZALO", "", "Zalopay"),
	MOMO("MOMO", "", "Momopay"),
	VTPAY("VTPAY", "", "ViettelPay"),

	;
	private String bankCode;
	private String iconUrl;
	private String bankName;

	private BankInfo(String bankCode, String iconUrl, String bankName) {
		this.bankCode = bankCode;
		this.iconUrl = iconUrl;
		this.bankName = bankName;
	}

	public static BankInfo getBankInfo(String name) {
		try {
			return BankInfo.valueOf(name);
		} catch (Exception e) {
			return OTHER;
		}
	}

	public static BankInfo getBankInfoByCode(String bankCode) {
		try {
			for (BankInfo le : BankInfo.values()) {
				if (le.getBankCode().equals(bankCode)) {
					return le;
				}
			}
		} catch (Exception e) {
			return OTHER;
		}
		return OTHER;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public static List<BankInfo> getList() {
		List<BankInfo> list = new ArrayList<>();
		for (BankInfo le : BankInfo.values()) {
			list.add(le);
		}
		return list;
	}
}
