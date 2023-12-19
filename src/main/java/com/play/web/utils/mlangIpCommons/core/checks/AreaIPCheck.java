package com.play.web.utils.mlangIpCommons.core.checks;

import com.play.model.StationContryContainer;
import com.play.model.StationWhiteArea;
import com.play.web.utils.mlangIpCommons.core.ContryIpContext;
import com.play.web.utils.mlangIpCommons.core.apply.ContainerApply;

import java.util.HashMap;
import java.util.Map;

//区域 实现类
public class AreaIPCheck extends AbstractIPCheck<StationWhiteArea>{

	private Map<Long, StationContryContainer> stationContryContainerMap = new HashMap<>();
	private ContryIpContext contryIpContext;

	private AreaIPCheck(ContainerApply<StationWhiteArea> containerApply) {
		super(containerApply);
	}

	public AreaIPCheck(ContainerApply<StationWhiteArea> containerApply,ContryIpContext contryIpContext) {
		super(containerApply);
		this.contryIpContext = contryIpContext;
	}
	//判断有没有命中区域白名单
	public boolean checkFrontArea(String ip,long stationId){
		StationContryContainer stationContryContainer = stationContryContainerMap.get(stationId);
		//双从检查锁
		if (stationContryContainer == null || stationContryContainer.isTimeOut()){
			String lock = "areaStation:"+stationId;
			//锁细粒化，只锁对应的站点，不会影响其他站点，提高性能
			synchronized (lock){
				stationContryContainer = stationContryContainerMap.get(stationId);
				if (stationContryContainer == null){
					stationContryContainer = new StationContryContainer(stationId);
				}
				if (stationContryContainer.isTimeOut()){
					stationContryContainer.reflesh(apply(stationId));
				}
			}
		}

		return stationContryContainer.checkIn(contryIpContext.getContryCode(ip));
	}

	@Override
	public boolean checkWhite(String ip, long stationId) {
		return checkFrontArea(ip,stationId);
	}

	@Override
	public boolean checkBlack(String ip, long stationId) {
		throw new RuntimeException("不允许访问此方法！");
	}
}
