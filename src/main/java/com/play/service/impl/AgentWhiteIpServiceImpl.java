package com.play.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.utils.LogUtils;
import com.play.dao.AgentDao;
import com.play.dao.AgentWhiteIpDao;
import com.play.model.Agent;
import com.play.model.AgentWhiteIp;
import com.play.model.vo.WhiteIpVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.AgentWhiteIpService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
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
public class AgentWhiteIpServiceImpl implements AgentWhiteIpService {

	@Autowired
	private AgentWhiteIpDao agentWhiteIpDao;
	@Autowired
	private AgentDao agentDao;

	@Override
	public List<WhiteIpVo> getIps(Long agentId, Long stationId) {
		return agentWhiteIpDao.getIps(agentId, stationId);
	}

	@Override
	public Page<AgentWhiteIp> page(Long agentId, Long stationId, String ip, Integer type) {
		return agentWhiteIpDao.page(agentId, stationId, ip, type);
	}

	@Override
	public void delete(Long id, Long stationId) {
		AgentWhiteIp ip = agentWhiteIpDao.findOne(id, stationId);
		if (ip == null) {
			throw new ParamException(BaseI18nCode.ipNotExist);
		}
		agentWhiteIpDao.deleteById(id);
		CacheUtil.delCacheByPrefix(CacheKey.AGENT_IPS);
		LogUtils.delLog("删除代理商后台IP白名单" + ip.getIp());
	}

	@Override
	public String save(AgentWhiteIp wip) {
		if (StringUtils.isEmpty(wip.getIp())) {
			throw new ParamException(BaseI18nCode.inputCorrectIp);
		}
		String allIps = StringUtils.trim(wip.getIp());
		if (StringUtils.isEmpty(allIps)) {
			throw new ParamException(BaseI18nCode.inputCorrectIp);
		}
		Agent agent = agentDao.findOne(wip.getAgentId(), wip.getStationId());
		if (agent == null) {
			throw new ParamException(BaseI18nCode.stationSelectAgent);
		}
		String[] ips = allIps.split(",|，|\n");
		StringBuilder sb = new StringBuilder();
		wip.setCreateDatetime(new Date());
		for (String ip : ips) {
			ip = StringUtils.trim(ip);
			if (!ValidateUtil.isIPAddr(ip)) {
				sb.append(I18nTool.getMessage(BaseI18nCode.formatError, ip));
				continue;
			}
			if (agentWhiteIpDao.exist(ip, wip.getAgentId())) {
				sb.append(I18nTool.getMessage(BaseI18nCode.ipHadAdd, ip));
				continue;
			}
			wip.setIp(ip);
			agentWhiteIpDao.save(wip);
		}
		LogUtils.addLog("添加代理商“" + agent.getName() + "”后台IP白名单 " + allIps);
		CacheUtil.delCacheByPrefix(CacheKey.AGENT_IPS);
		return sb.toString();
	}
}
