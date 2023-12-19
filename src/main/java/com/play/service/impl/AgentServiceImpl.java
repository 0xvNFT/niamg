package com.play.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.play.web.exception.ParamException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.dao.AgentDao;
import com.play.model.Agent;
import com.play.orm.jdbc.page.Page;
import com.play.service.AgentService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ValidateUtil;
import com.play.web.var.LoginAdminUtil;

/**
 * 站点代理商信息表
 *
 * @author admin
 *
 */
@Service
public class AgentServiceImpl implements AgentService {

	@Autowired
	private AgentDao agentDao;

	@Override
	public Page<Agent> page(Long stationId) {
		return agentDao.page(stationId);
	}

	@Override
	public List<Agent> getAll(Long stationId) {
		return agentDao.find(stationId);
	}

	@Override
	public void save(Agent agent) {
		String name = StringUtils.trim(agent.getName());
		if (StringUtils.isEmpty(name)) {
			throw new ParamException(BaseI18nCode.stationAgentName);
		}
		agent.setName(name.toLowerCase());
		if(!ValidateUtil.isUsername(agent.getName())) {
			throw new ParamException(BaseI18nCode.stationAgentRightName);
		}
		String promoCode = StringUtils.trimToEmpty(agent.getPromoCode());
		if (StringUtils.isEmpty(name)) {
			throw new ParamException(BaseI18nCode.stationInputCode);
		}
		agent.setPromoCode(promoCode.toLowerCase());
		if (!ValidateUtil.isPromoCode(agent.getPromoCode())) {
			throw new ParamException(BaseI18nCode.stationTrueInputCode);
		}
		if (agentDao.existName(agent.getStationId(), agent.getName())) {
			throw new ParamException(BaseI18nCode.stationAgentNameInExist);
		}

		if (agentDao.existPromoCode(agent.getStationId(), agent.getPromoCode())) {
			throw new ParamException(BaseI18nCode.stationCodeInExist);
		}
		if (agent.getAccessPage() == null) {
			agent.setAccessPage(Agent.ACCESS_PAGE_TYPE_INDEX);
		}
		agent.setCreateUser(LoginAdminUtil.getUsername());
		agent.setCreateDatetime(new Date());
		agent.setStatus(Constants.STATUS_ENABLE);
		agentDao.save(agent);
		LogUtils.addUserLog("新增代理商" + name);
	}

	@Override
	public Agent findById(Long id, Long stationId) {
		return agentDao.findOne(id, stationId);
	}

	@Override
	public Agent findOneByPromoCode(String code, Long stationId) {
		return agentDao.findOneByPromoCode(code, stationId);
	}

	@Override
	public Agent findOneByName(String name, Long stationId) {
		return agentDao.findByName(name, stationId);
	}

	@Override
	public void update(Agent agent) {
		String name = StringUtils.trim(agent.getName());
		if (StringUtils.isEmpty(name)) {
			throw new ParamException(BaseI18nCode.stationAgentName);
		}
		agent.setName(name.toLowerCase());
		String promoCode = StringUtils.trimToEmpty(agent.getPromoCode());
		if (StringUtils.isEmpty(name)) {
			throw new ParamException(BaseI18nCode.stationInputCode);
		}
		agent.setPromoCode(promoCode.toLowerCase());
		if (!ValidateUtil.isPromoCode(agent.getPromoCode())) {
			throw new ParamException(BaseI18nCode.stationAgentRightName);
		}

		Agent old = agentDao.findOne(agent.getId(), agent.getStationId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.agentUnExist);
		}
		if (!StringUtils.equals(agent.getName(), old.getName())
				&& agentDao.existName(agent.getStationId(), agent.getName())) {
			throw new ParamException(BaseI18nCode.stationAgentNameInExist);
		}

		if (!StringUtils.equals(agent.getPromoCode(), old.getPromoCode())
				&& agentDao.existPromoCode(agent.getStationId(), agent.getPromoCode())) {
			throw new ParamException(BaseI18nCode.stationCodeInExist);
		}
		if (agent.getAccessPage() == null) {
			agent.setAccessPage(Agent.ACCESS_PAGE_TYPE_INDEX);
		}
		agentDao.updateInfo(agent);
		LogUtils.modifyLog("修改代理商: 旧名称：" + old.getName() + " 新名称：" + agent.getName() + " 旧推广码：" + old.getPromoCode()
				+ " 新推广码:" + agent.getPromoCode());
	}

	@Override
	public void delete(Long id, Long stationId) {
		Agent old = agentDao.findOne(id, stationId);
		if (old != null) {
			agentDao.deleteById(id);
			LogUtils.delLog("删除代理商:" + old.getName());
		}
	}

	@Override
	public void changeStatus(Long id, Integer status, Long stationId) {
		if (id == null || id <= 0) {
			return;
		}
		if (status == null) {
			return;
		}
		Agent old = agentDao.findOne(id, stationId);
		if (old == null) {
			return;
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, old.getStatus())) {
			agentDao.changeStatus(id, status, stationId);
			LogUtils.modifyLog("修改代理商:" + old.getName() + " 状态:" + str);
		}
	}
}
