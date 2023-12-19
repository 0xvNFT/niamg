package com.play.web.utils.mlangIpCommons.core.checks;

import com.play.model.StationWhiteIp;
import com.play.model.StationIpContainer;
import com.play.web.utils.mlangIpCommons.core.apply.ContainerApply;


import java.util.HashMap;

import java.util.Map;

//判断ip是否处于 黑白名单，使用hash索引结构，性能较高
public class SingleIPCheck extends AbstractIPCheck<StationWhiteIp> {
	private Map<Long, StationIpContainer> ipContainerMap = new HashMap<>();

	public SingleIPCheck(ContainerApply<StationWhiteIp> containerApply) {
		super(containerApply);
	}


	public boolean checkFrontIp(String ip,long stationId,int type){
		StationIpContainer stationIpContainer = ipContainerMap.get(stationId);
		if (stationIpContainer == null || stationIpContainer.isTimeOut()){
			//细粒化锁，只锁相对应站点 ，此lock在jvm内存只存在一份
			String lock = "ipStation:"+stationId;
			synchronized (lock){
				stationIpContainer = ipContainerMap.get(stationId);
				if (stationIpContainer == null){
					stationIpContainer = new StationIpContainer(stationId);
				}
				if (stationIpContainer.isTimeOut()){
					stationIpContainer.reflesh(apply(stationId));
				}
			}
		}
		StationWhiteIp stationWhiteIp = stationIpContainer.getIp(ip);
		return stationWhiteIp != null && stationWhiteIp.getType() == type;
	}

	@Override
	public boolean checkWhite(String ip, long stationId) {
		return checkFrontIp(ip,stationId,2);
	}

	@Override
	public boolean checkBlack(String ip, long stationId) {
		return checkFrontIp(ip,stationId,1);
	}

}
