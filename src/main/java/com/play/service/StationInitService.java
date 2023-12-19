package com.play.service;

import com.play.model.Station;

public interface StationInitService {
	
	void initEvolution(Integer p);
	
	void init(Station staiton);

	void initAdminPwd(String pwdPwd, Long stationId);

}
