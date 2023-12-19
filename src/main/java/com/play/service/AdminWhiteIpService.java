package com.play.service;

import java.util.List;

import com.play.model.AdminWhiteIp;
import com.play.model.vo.WhiteIpVo;
import com.play.orm.jdbc.page.Page;

/**
 * 
 *
 * @author admin
 *
 */
public interface AdminWhiteIpService {

	Page<AdminWhiteIp> page(Long partnerId, Long stationId, String ip,Integer type);
	
	void delete(Long id, Long stationId);
	
	String save(AdminWhiteIp wip);
	
	List<WhiteIpVo> getIps(Long stationId);

}