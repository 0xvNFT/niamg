package com.play.spring.config;

import com.play.service.StationWhiteAreaService;
import com.play.service.StationWhiteIpService;
import com.play.web.utils.mlangIpCommons.core.ContryIpContext;
import com.play.web.utils.mlangIpCommons.core.MLangContryIpContext;
import com.play.web.utils.mlangIpCommons.core.apply.AreaListApply;
import com.play.web.utils.mlangIpCommons.core.apply.IPListApply;
import com.play.web.utils.mlangIpCommons.core.checks.AreaIPCheck;
import com.play.web.utils.mlangIpCommons.core.checks.FastAreaIPCheck;
import com.play.web.utils.mlangIpCommons.core.checks.FastSingleIPCheck;
import com.play.web.utils.mlangIpCommons.core.checks.SingleIPCheck;
import com.play.web.utils.mlangIpCommons.parser.FileReadIpParser;
import com.play.web.utils.mlangIpCommons.parser.IpParser;
import com.play.web.utils.mlangIpCommons.template.IpCheckTemplate;
import com.play.web.utils.mlangIpCommons.template.MLangIpCheckTemplate;
import com.play.web.utils.mlangIpCommons.template.WeakCacheIpCheckTemplateAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MlangIPConfigurer {

//	@Bean
//	public SingleIPCheck singleIPCheck(StationWhiteIpService stationWhiteIpService){
//		IPListApply ipListApply = new IPListApply();
//		ipListApply.setStationWhiteIpService(stationWhiteIpService);
//		return new SingleIPCheck(ipListApply);
//	}
//
//	@Bean
//	public AreaIPCheck areaIPCheck(StationWhiteAreaService stationWhiteAreaService, ContryIpContext contryIpContext){
//		AreaListApply areaListApply = new AreaListApply();
//		areaListApply.setStationWhiteAreaService(stationWhiteAreaService);
//		return new AreaIPCheck(areaListApply,contryIpContext);
//	}

	//	@Bean
//	public IpCheckTemplate ppCheckTemplate(SingleIPCheck singleIPCheck, AreaIPCheck areaIPCheck){
//		return new WeakCacheIpCheckTemplateAdapter(new MLangIpCheckTemplate(singleIPCheck,areaIPCheck));
//	}

    @Bean
	public FastSingleIPCheck singleIPCheck(StationWhiteIpService stationWhiteIpService){
		IPListApply ipListApply = new IPListApply();
		ipListApply.setStationWhiteIpService(stationWhiteIpService);
		return new FastSingleIPCheck(ipListApply);
	}

	@Bean
	public FastAreaIPCheck areaIPCheck(StationWhiteAreaService stationWhiteAreaService, ContryIpContext contryIpContext){
		AreaListApply areaListApply = new AreaListApply();
		areaListApply.setStationWhiteAreaService(stationWhiteAreaService);
		return new FastAreaIPCheck(areaListApply,contryIpContext);
	}

	@Bean
	public ContryIpContext contryIpContext(IpParser ipParser){
		return new MLangContryIpContext(ipParser);
	}

	@Bean
	public IpParser ipParser(){
		return new FileReadIpParser();
	}

	@Bean
	public IpCheckTemplate ppCheckTemplate(FastSingleIPCheck singleIPCheck, FastAreaIPCheck areaIPCheck){
		return new WeakCacheIpCheckTemplateAdapter(new MLangIpCheckTemplate(singleIPCheck,areaIPCheck));
	}

}