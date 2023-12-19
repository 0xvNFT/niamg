package com.play.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.core.UserTypeEnum;
import com.play.dao.PartnerDao;
import com.play.dao.StationDao;
import com.play.dao.StationDomainDao;
import com.play.model.Agent;
import com.play.model.Partner;
import com.play.model.Station;
import com.play.model.StationDomain;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.AgentService;
import com.play.service.StationDomainService;
import com.play.service.SysUserService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 站点域名信息
 *
 * @author admin
 *
 */
@Service
public class StationDomainServiceImpl implements StationDomainService {
	@Autowired
	private PartnerDao partnerDao;
	@Autowired
	private StationDomainDao stationDomainDao;
	@Autowired
	private StationDao stationDao;
	@Autowired
	private SysUserService userService;
	@Autowired
	private AgentService agentService;

	@Override
	public Page<StationDomain> getPartnerDomainPage(Long partnerId, String name) {
		return stationDomainDao.getPage(partnerId, null, name, null, null, null, StationDomain.PLATFORM_PARTNER);
	}

	@Override
	public Page<StationDomain> getDomainPage(Long stationId, String name, String proxyUsername, Integer type) {
		return stationDomainDao.getPage(null, stationId, name, proxyUsername, type, null,
				StationDomain.PLATFORM_STATION);
	}

	@Override
	public Page<StationDomain> getPageForAdmin(Long stationId, String name, String proxyUsername) {
		return stationDomainDao.getPage(null, stationId, name, proxyUsername, null, StationDomain.TYPE_FRONT,
				StationDomain.PLATFORM_STATION);
	}

	@Override
	public StationDomain findByDomain(String domain) {
		if (StringUtils.isEmpty(domain)) {
			return null;
		}
		if (domain.startsWith("www.")) {
			domain = domain.substring(4);
		}
		return stationDomainDao.findByDomain(domain);
	}

	@Override
	public void changeStatus(Long id, Integer status) {
		StationDomain domain = stationDomainDao.findOneById(id);
		if (domain == null) {
			throw new ParamException(BaseI18nCode.domainUnExist);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, domain.getStatus())) {
			stationDomainDao.changeStatus(id, status);
			LogUtils.modifyStatusLog("修改域名" + domain.getName() + " 状态为：" + str);
		}
	}

	@Override
	public void changeStatusForAdmin(Long id, Long stationId, Integer status) {
		StationDomain domain = stationDomainDao.findOne(id, stationId);
		if (domain == null) {
			throw new ParamException(BaseI18nCode.domainUnExist);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, domain.getStatus())) {
			stationDomainDao.changeStatus(id, status);
			LogUtils.modifyStatusLog("修改域名" + domain.getName() + " 状态为：" + str);
		}
	}

	@Override
	public String addPartnerDomain(Long partnerId, String domains, Integer ipMode, String remark) {
		if (partnerId == null) {
			throw new ParamException(BaseI18nCode.selectPartnerPlease);
		}
		String domainNames = StringUtils.lowerCase(StringUtils.trim(domains));
		if (StringUtils.isEmpty(domainNames)) {
			throw new ParamException(BaseI18nCode.inputDomain);
		}
		Partner p = partnerDao.findOneById(partnerId);
		if (p == null) {
			throw new ParamException(BaseI18nCode.selectPartnerPlease);
		}
		String[] ds = domainNames.split(",|，|\n");
		StringBuilder info = new StringBuilder();
		StationDomain domain = null;
		Date date = new Date();
		StringBuilder log = new StringBuilder(
				BaseI18nCode.stationPartner.getMessage() + p.getName() + BaseI18nCode.stationDomain.getMessage());
		for (String dom : ds) {
			try {
				dom = StringUtils.trim(dom);
				if (StringUtils.isNotEmpty(dom)) {
					if (stationDomainDao.exist(dom, null)) {
						info.append(I18nTool.getMessage(BaseI18nCode.domainExist, dom)).append("<br>");
						continue;
					}
					domain = new StationDomain();
					domain.setName(dom);
					domain.setType(StationDomain.TYPE_ADMIN);
					domain.setPartnerId(partnerId);
					domain.setCreateDatetime(date);
					domain.setStatus(Constants.STATUS_ENABLE);
					domain.setRemark(remark);
					domain.setIpMode(ipMode);
					domain.setSortNo(0L);
					domain.setPlatform(StationDomain.PLATFORM_PARTNER);
					stationDomainDao.save(domain);
					log.append(dom);
				}
			} catch (Exception e) {
				info.append(e.getMessage()).append("<br>");
			}
		}
		LogUtils.addLog(log.toString());
		return info.toString();
	}

	@Override
	public String addStationDomain(Long stationId, Integer type, String domains, Integer ipMode, String proxyUsername,
			String agentName, String defaultHome, Long sortNo, String remark) {
		if (stationId == null) {
			throw new ParamException(BaseI18nCode.stationMustSelect);
		}
		String domainNames = StringUtils.lowerCase(StringUtils.trim(domains));
		if (StringUtils.isEmpty(domainNames)) {
			throw new ParamException(BaseI18nCode.inputDomain);
		}
		Station station = stationDao.findOneById(stationId);
		if (station == null) {
			throw new ParamException(BaseI18nCode.stationMustSelect);
		}
		Long proxyId = null;
		if (StringUtils.isNotEmpty(proxyUsername)) {
			SysUser proxy = userService.findOneByUsername(proxyUsername, stationId, UserTypeEnum.PROXY.getType());
			if (proxy == null) {
				throw new ParamException(BaseI18nCode.proxyUnExist);
			}
			if (!Objects.equals(proxy.getType(), UserTypeEnum.PROXY.getType())) {
				throw new ParamException(BaseI18nCode.noProxyUser);
			}
			proxyId = proxy.getId();
		}
		String agentPromoCode = null;
		if (StringUtils.isNotEmpty(agentName)) {
			Agent agent = agentService.findOneByName(agentName, stationId);
			if (agent == null) {
				throw new ParamException(BaseI18nCode.agentUnExist);
			}
			agentPromoCode = agent.getPromoCode();
		}
		String[] ds = domainNames.split(",|，|\n");
		StringBuilder info = new StringBuilder();
		StationDomain domain = null;
		Date date = new Date();
		StringBuilder log = new StringBuilder(BaseI18nCode.stationWebSite.getMessage() + station.getName() + "["
				+ station.getCode() + "] " + BaseI18nCode.stationAddDomain.getMessage());
		for (String dom : ds) {
			try {
				dom = StringUtils.trim(dom);
				if (StringUtils.isNotEmpty(dom)) {
					if (stationDomainDao.exist(dom, null)) {
						info.append(I18nTool.getMessage(BaseI18nCode.domainExist, dom)).append("<br>");
						continue;
					}
					domain = new StationDomain();
					domain.setName(dom);
					domain.setType(type);
					domain.setPartnerId(station.getPartnerId());
					domain.setStationId(stationId);
					domain.setCreateDatetime(date);
					domain.setStatus(Constants.STATUS_ENABLE);
					domain.setSwitchDomain(Constants.STATUS_DISABLE);
					domain.setRemark(remark);
					domain.setDefaultHome(defaultHome);
					domain.setProxyUsername(proxyUsername);
					domain.setAgentName(agentName);
					domain.setAgentPromoCode(agentPromoCode);
					domain.setIpMode(ipMode);
					domain.setSortNo(sortNo);
					domain.setProxyId(proxyId);
					domain.setProxyUsername(proxyUsername);
					domain.setPlatform(StationDomain.PLATFORM_STATION);
					stationDomainDao.save(domain);
					log.append(dom);
				}
			} catch (Exception e) {
				info.append(e.getMessage()).append("<br>");
			}
		}
		LogUtils.addLog(log.toString());
		return info.toString();
	}

	@Override
	public StationDomain findOneById(Long id) {
		return stationDomainDao.findOneById(id);
	}

	@Override
	public StationDomain findOneById(Long id, Long stationId) {
		return stationDomainDao.findOne(id, stationId);
	}

	@Override
	public void modifyPartnerDomain(Long id, Integer ipMode, String remark) {
		StationDomain domain = stationDomainDao.findOneById(id);
		if (domain == null) {
			throw new ParamException(BaseI18nCode.domainUnExist);
		}
		stationDomainDao.updateTypeAndRemark(id, StationDomain.TYPE_ADMIN, ipMode, remark);
		LogUtils.modifyLog("修改合作商后台域名备注和ip获取方式：" + domain.getName());
	}

	@Override
	public void modifyDomain(Long id, Integer type, Integer ipMode, String proxyUsername, String agentName,
			String defaultHome, String remark) {
		StationDomain domain = stationDomainDao.findOneById(id);
		if (domain == null) {
			throw new ParamException(BaseI18nCode.domainUnExist);
		}
		Long proxyId = null;
		if (StringUtils.isNotEmpty(proxyUsername)) {
			SysUser proxy = userService.findOneByUsername(proxyUsername, domain.getStationId(),
					UserTypeEnum.PROXY.getType());
			if (proxy == null) {
				throw new ParamException(BaseI18nCode.proxyUnExist);
			}
			if (!Objects.equals(proxy.getType(), UserTypeEnum.PROXY.getType())) {
				throw new ParamException(BaseI18nCode.noProxyUser);
			}
			proxyId = proxy.getId();
		}
		String agentPromoCode = null;
		if (StringUtils.isNotEmpty(agentName)) {
			Agent agent = agentService.findOneByName(agentName, domain.getStationId());
			if (agent == null) {
				throw new ParamException(BaseI18nCode.agentUnExist);
			}
			agentPromoCode = agent.getPromoCode();
		}
		stationDomainDao.modifyDomain(id, type, ipMode, proxyId, proxyUsername, agentPromoCode, agentName, defaultHome,
				domain.getSortNo(), remark);
		LogUtils.modifyLog("修改站点域名：" + domain.getName());
	}

	@Override
	public void modifyDomainForAdmin(Long id, Long stationId, Integer type, Integer ipMode, String proxyUsername,
			String agentName, String defaultHome, Long sortNo) {
		StationDomain domain = stationDomainDao.findOne(id, stationId);
		if (domain == null) {
			throw new ParamException(BaseI18nCode.domainUnExist);
		}
		Long proxyId = null;
		if (StringUtils.isNotEmpty(proxyUsername)) {
			SysUser proxy = userService.findOneByUsername(proxyUsername, stationId, UserTypeEnum.PROXY.getType());
			if (proxy == null) {
				throw new ParamException(BaseI18nCode.proxyUnExist);
			}
			if (!Objects.equals(proxy.getType(), UserTypeEnum.PROXY.getType())) {
				throw new ParamException(BaseI18nCode.noProxyUser);
			}
			proxyId = proxy.getId();
		}
		String agentPromoCode = null;
		if (StringUtils.isNotEmpty(agentName)) {
			Agent agent = agentService.findOneByName(agentName, stationId);
			if (agent == null) {
				throw new ParamException(BaseI18nCode.agentUnExist);
			}
			agentPromoCode = agent.getPromoCode();
		}
		stationDomainDao.modifyDomain(id, type, ipMode, proxyId, proxyUsername, agentPromoCode, agentName, defaultHome,
				sortNo, domain.getRemark());
		LogUtils.modifyLog("站点管理员修改站点域名：" + domain.getName());
	}

	@Override
	public void delete(Long id) {
		StationDomain domain = stationDomainDao.findOneById(id);
		if (domain == null) {
			throw new ParamException(BaseI18nCode.domainUnExist);
		}
		stationDomainDao.deleteById(id);
		LogUtils.delLog("删除域名:" + domain.getName() + " partnerId=" + domain.getPartnerId());
	}

	@Override
	public void deleteForAdmin(Long id, Long stationId) {
		StationDomain domain = stationDomainDao.findOneById(id);
		if (domain == null) {
			throw new ParamException(BaseI18nCode.domainUnExist);
		}
		stationDomainDao.deleteById(id);
		LogUtils.delLog("删除域名:" + domain.getName());
	}

	@Override
	public void updSwitchStatus(Long id, Long stationId, Integer status) {
		StationDomain domain = stationDomainDao.findOneById(id);
		if (domain == null) {
			throw new ParamException(BaseI18nCode.domainUnExist);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, domain.getSwitchDomain())) {
			stationDomainDao.updSwitchStatus(id, status);
			LogUtils.modifyLog("修改域名" + domain.getName() + " 线路检测状态为：" + str);
		}
	}

	@Override
	public void remarkModify(Long id, Long stationId, String remark) {
		StationDomain domain = stationDomainDao.findOne(id, stationId);
		if (domain == null) {
			throw new ParamException(BaseI18nCode.domainUnExist);
		}
		stationDomainDao.modifyRemark(id, stationId, remark);
		LogUtils.modifyLog("域名:" + domain.getName() + " 的备注从 " + domain.getRemark() + " 修改成 " + remark);
	}

	@Override
	public List<String> listDomainByStationId(Long stationId, Integer limit, Integer switchDomain) {
		return stationDomainDao.listDomainByStationId(stationId, limit, switchDomain);
	}

	@Override
	public Map<String, String> getAllStationDomain() {
		return stationDomainDao.getAllStationDomain();
	}
}
