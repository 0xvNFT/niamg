package com.play.tronscan.allclient.clientbase.res;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TronScanResCost implements Serializable {
	public Long net_fee;
	public Long energy_usage;
	public Long fee;
	public Long energy_fee;
	public Long energy_usage_total;
	public Long origin_energy_usage;
	public Long net_usage;

	public Long getNet_fee() {
		return net_fee;
	}

	public void setNet_fee(Long net_fee) {
		this.net_fee = net_fee;
	}

	public Long getEnergy_usage() {
		return energy_usage;
	}

	public void setEnergy_usage(Long energy_usage) {
		this.energy_usage = energy_usage;
	}

	public Long getFee() {
		return fee;
	}

	public void setFee(Long fee) {
		this.fee = fee;
	}

	public Long getEnergy_fee() {
		return energy_fee;
	}

	public void setEnergy_fee(Long energy_fee) {
		this.energy_fee = energy_fee;
	}

	public Long getEnergy_usage_total() {
		return energy_usage_total;
	}

	public void setEnergy_usage_total(Long energy_usage_total) {
		this.energy_usage_total = energy_usage_total;
	}

	public Long getOrigin_energy_usage() {
		return origin_energy_usage;
	}

	public void setOrigin_energy_usage(Long origin_energy_usage) {
		this.origin_energy_usage = origin_energy_usage;
	}

	public Long getNet_usage() {
		return net_usage;
	}

	public void setNet_usage(Long net_usage) {
		this.net_usage = net_usage;
	}
}
