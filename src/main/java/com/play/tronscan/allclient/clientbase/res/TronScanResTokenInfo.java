package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TronScanResTokenInfo implements Serializable {
	public String tokenId;
	public String tokenAbbr;
	public String tokenName;
	public Integer tokenDecimal;
	public Integer tokenCanShow;
	public String tokenType;
	public String tokenLogo;
	public String tokenLevel;
	public Boolean vip;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getTokenAbbr() {
		return tokenAbbr;
	}

	public void setTokenAbbr(String tokenAbbr) {
		this.tokenAbbr = tokenAbbr;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public Integer getTokenDecimal() {
		return tokenDecimal;
	}

	public void setTokenDecimal(Integer tokenDecimal) {
		this.tokenDecimal = tokenDecimal;
	}

	public Integer getTokenCanShow() {
		return tokenCanShow;
	}

	public void setTokenCanShow(Integer tokenCanShow) {
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

	public String getTokenLevel() {
		return tokenLevel;
	}

	public void setTokenLevel(String tokenLevel) {
		this.tokenLevel = tokenLevel;
	}

	public Boolean getVip() {
		return vip;
	}

	public void setVip(Boolean vip) {
		this.vip = vip;
	}
}
