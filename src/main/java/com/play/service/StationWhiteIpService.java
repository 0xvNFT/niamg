package com.play.service;

import java.util.List;

import com.play.model.StationWhiteIp;
import com.play.model.vo.WhiteIpVo;
import com.play.orm.jdbc.page.Page;

/**
 * 站点前台IP白名单列表
 *
 * @author admin
 *
 */
public interface StationWhiteIpService {

	Page<StationWhiteIp> page(Long partnerId, Long stationId, String ip, Integer type);

	void delete(Long id, Long stationId);

	String save(StationWhiteIp wip);

	List<WhiteIpVo> getIps(Long stationId);

	List<StationWhiteIp> find(Long stationId);
}