package com.play.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.utils.LogUtils;
import com.play.dao.StationDao;
import com.play.dao.StationWhiteIpDao;
import com.play.model.Station;
import com.play.model.StationWhiteIp;
import com.play.model.vo.WhiteIpVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationWhiteIpService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ValidateUtil;

/**
 * 站点前台IP白名单列表
 *
 * @author admin
 *
 */
@Service
public class StationWhiteIpServiceImpl implements StationWhiteIpService {

	@Autowired
	private StationWhiteIpDao stationWhiteIpDao;
	@Autowired
	private StationDao stationDao;

	@Override
	public Page<StationWhiteIp> page(Long partnerId, Long stationId, String ip, Integer type) {
		return stationWhiteIpDao.page(partnerId, stationId, ip, type);
	}

	@Override
	public void delete(Long id, Long stationId) {
		StationWhiteIp ip = stationWhiteIpDao.findOneById(id, stationId);
		if (ip == null) {
			throw new ParamException(BaseI18nCode.ipNotExist);
		}
		stationWhiteIpDao.deleteById(id);
		CacheUtil.delCacheByPrefix(CacheKey.FRONT_STATIOIN_IPS);
		LogUtils.delLog("删除站点后台IP白名单" + ip.getIp() + " stationId=" + ip.getStationId());
	}

	@Override
	public String save(StationWhiteIp wip) {
		if (StringUtils.isEmpty(wip.getIp())) {
			throw new ParamException(BaseI18nCode.inputCorrectIp);
		}
		String allIps = StringUtils.trim(wip.getIp());
		if (StringUtils.isEmpty(allIps)) {
			throw new ParamException(BaseI18nCode.inputCorrectIp);
		}
		Station station = stationDao.findOneById(wip.getStationId());
		String[] ips = allIps.split(",|，|\n");
		StringBuilder sb = new StringBuilder();
		wip.setCreateDatetime(new Date());
		for (String ip : ips) {
			ip = StringUtils.trim(ip);
			if (!ValidateUtil.isIPAddr(ip)) {
				sb.append(I18nTool.getMessage(BaseI18nCode.formatError, ip));
				continue;
			}
			if (stationWhiteIpDao.exist(ip, wip.getStationId())) {
				sb.append(I18nTool.getMessage(BaseI18nCode.ipHadAdd, ip));
				continue;
			}
			wip.setPartnerId(station.getPartnerId());
			wip.setIp(ip);
			stationWhiteIpDao.save(wip);
		}
		LogUtils.addLog("添加站点“" + station.getName() + "[" + station.getCode() + "]”前台IP白名单 " + allIps);
		CacheUtil.delCacheByPrefix(CacheKey.FRONT_STATIOIN_IPS);
		return sb.toString();
	}

	@Override
	public List<WhiteIpVo> getIps(Long stationId) {
		return stationWhiteIpDao.getIps(stationId);
	}

	@Override
	public List<StationWhiteIp> find(Long stationId) {
		return stationWhiteIpDao.findList(stationId);
	}
}
