package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TronScanResTRC20TokenBalance implements Serializable {
	public String tokenId;
	public String balance;
	public String tokenName;
	public String tokenAbbr;
	public Long tokenDecimal;
	public Long tokenCanShow;
	public String tokenType;
	public String tokenLogo;
	public Boolean vip;
	public Double tokenPriceInTrx;
	public Double amount;
	public Long nrOfTokenHolders;
	public Long transferCount;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public String getTokenAbbr() {
		return tokenAbbr;
	}

	public void setTokenAbbr(String tokenAbbr) {
		this.tokenAbbr = tokenAbbr;
	}

	public Long getTokenDecimal() {
		return tokenDecimal;
	}

	public void setTokenDecimal(Long tokenDecimal) {
		this.tokenDecimal = tokenDecimal;
	}

	public Long getTokenCanShow() {
		return tokenCanShow;
	}

	public void setTokenCanShow(Long tokenCanShow) {
		this.tokenCanShow = tokenCanShow;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getTokenLogo() {
		return tokenLogo;
	}

	public void setTokenLogo(String tokenLogo) {
		this.tokenLogo = tokenLogo;
	}

	public Boolean getVip() {
		return vip;
	}

	public void setVip(Boolean vip) {
		this.vip = vip;
	}

	public Double getTokenPriceInTrx() {
		return tokenPriceInTrx;
	}

	public void setTokenPriceInTrx(Double tokenPriceInTrx) {
		this.tokenPriceInTrx = tokenPriceInTrx;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getNrOfTokenHolders() {
		return nrOfTokenHolders;
	}

	public void setNrOfTokenHolders(Long nrOfTokenHolders) {
		this.nrOfTokenHolders = nrOfTokenHolders;
	}

	public Long getTransferCount() {
		return transferCount;
	}

	public void setTransferCount(Long transferCount) {
		this.transferCount = transferCount;
	}
}
