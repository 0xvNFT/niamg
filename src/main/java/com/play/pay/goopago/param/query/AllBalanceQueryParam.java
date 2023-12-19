package com.play.pay.goopago.param.query;

import com.play.pay.goopago.param.BaseParam;

/**
 * @Author Ben Anderson
 * @Date 2023/4/27 11:22
 * @Description : 商户所有余额查询请求参数
 * @Since version-1.0.0
 */
public class AllBalanceQueryParam extends BaseParam {

	private Long areaId;

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
}
