package com.play.pay.goopago.result;

/**
 * @author hy
 */
@SuppressWarnings("serial")
public class PaymeCollectionResult extends CollectionResult {

	private String steps;// 步骤描述

	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}
}
