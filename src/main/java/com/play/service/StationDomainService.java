package com.play.service;

import java.util.List;
import java.util.Map;

import com.play.model.StationDomain;
import com.play.orm.jdbc.page.Page;

/**
 * 站点域名信息
 *
 * @author admin
 *
 */
public interface StationDomainService {

	Page<StationDomain> getPartnerDomainPage(Long partnerId, String name);

	Page<StationDomain> getDomainPage(Long stationId, String name, String proxyUsername, Integer type);

	Page<StationDomain> getPageForAdmin(Long stationId, String name, String proxyUsername);

	void changeStatus(Long id, Integer status);

	void changeStatusForAdmin(Long id, Long stationId, Integer status);

	String addPartnerDomain(Long partnerId, String domains, Integer ipMode, String remark);

	StationDomain findOneById(Long id);

	void modifyPartnerDomain(Long id, Integer ipMode, String remark);

	void delete(Long id);

	String addStationDomain(Long stationId, Integer type, String domains, Integer ipMode, String proxyUsername,
			String agentName, String defaultHome, Long sortNo, String remark);

	void modifyDomain(Long id, Integer type, Integer ipMode, String proxyUsername, String agentName,
			String defaultHome, String remark);

	StationDomain findByDomain(String domain);

	void modifyDomainForAdmin(Long id, Long stationId, Integer type, Integer ipMode, String proxyUsername,
			String agentName, String defaultHome, Long sortNo);

	void updSwitchStatus(Long id, Long stationId, Integer status);

	void deleteForAdmin(Long id, Long stationId);

	StationDomain findOneById(Long id, Long stationId);

	void remarkModify(Long id, Long stationId, String remark);

	List<String> listDomainByStationId(Long stationId, Integer limit, Integer switchDomain);

	Map<String, String> getAllStationDomain();

}