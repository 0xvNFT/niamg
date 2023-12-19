package com.play.service.impl;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.common.utils.RandomStringUtils;
import com.play.common.utils.security.MD5Util;
import com.play.core.UserTypeEnum;
import com.play.dao.AgentDao;
import com.play.dao.AgentUserDao;
import com.play.model.Agent;
import com.play.model.AgentUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.AgentUserService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ValidateUtil;
import com.play.web.var.LoginAdminUtil;

/**
 * 站点下代理商账号表
 *
 * @author admin
 *
 */
@Service
public class AgentUserServiceImpl implements AgentUserService {

	@Autowired
	private AgentUserDao agentUserDao;
	@Autowired
	private AgentDao agentDao;

	@Override
	public AgentUser findOneByUsername(String username, Long stationId) {
		return agentUserDao.findOneByUsername(username, stationId);
	}

	@Override
	public AgentUser findOneById(Long id, Long stationId) {
		return agentUserDao.findOne(id, stationId);
	}

	@Override
	public Page<AgentUser> page(Long stationId, Long agentId, String username) {
		return agentUserDao.page(stationId, agentId, username);
	}

	@Override
	public void save(AgentUser user) {
		String username = StringUtils.trim(user.getUsername());
		if (StringUtils.isEmpty(username)) {
			throw new ParamException(BaseI18nCode.inputUsername);
		}
		if (!ValidateUtil.isUsername(username)) {
			throw new ParamException(BaseI18nCode.usernameFormatTip);
		}
		user.setUsername(username.toLowerCase());
		Agent agent = agentDao.findOne(user.getAgentId(), user.getStationId());
		if (agent == null) {
			throw new ParamException(BaseI18nCode.stationSelectAgent);
		}
		if (agentUserDao.exist(user.getUsername(), user.getStationId())) {
			throw new ParamException(BaseI18nCode.userExist);
		}
		user.setCreateDatetime(new Date());
		user.setCreateUser(LoginAdminUtil.getUsername());
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setPassword(MD5Util.pwdMd5Agent(user.getUsername(), user.getPassword(), user.getSalt()));
		user.setStatus(Constants.STATUS_ENABLE);
		user.setType(UserTypeEnum.AGENT.getType());
		agentUserDao.save(user);
		LogUtils.addUserLog("新增代理商" + agent.getName() + " 的账号：" + username);
	}

	@Override
	public void changeStatus(Long id, Integer status, Long stationId) {
		if (id == null || id <= 0) {
			return;
		}
		if (status == null) {
			return;
		}
		AgentUser old = agentUserDao.findOne(id, stationId);
		if (old == null) {
			return;
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, old.getStatus())) {
			agentUserDao.changeStatus(id, status, stationId);
			LogUtils.modifyLog("修改代理商账号:" + old.getUsername() + " 状态:" + str);
		}
	}

	@Override
	public void delete(Long id, Long stationId) {
		AgentUser old = agentUserDao.findOne(id, stationId);
		if (old != null) {
			agentUserDao.delete(id, stationId);
			LogUtils.delLog("删除代理商账号:" + old.getUsername());
		}
	}

	@Override
	public void modifyRealName(Long id, String realName, Long stationId) {
		AgentUser old = agentUserDao.findOne(id, stationId);
		if (old == null) {
			return;
		}
		agentUserDao.modifyRealName(id, realName, stationId);
		LogUtils.modifyLog("修改代理商账号:" + old.getUsername() + " 的真实姓名: 旧:" + old.getRealName() + " 新：" + realName);
	}

	@Override
	public void modifyRemark(Long id, String remark, Long stationId) {
		AgentUser old = agentUserDao.findOne(id, stationId);
		if (old == null) {
			return;
		}
		agentUserDao.modifyRemark(id, remark, stationId);
		LogUtils.modifyLog("修改代理商账号:" + old.getUsername() + " 的备注: 旧:" + old.getRemark() + " 新：" + remark);
	}
	
	@Override
	public void resetPwd(Long id, Long stationId) {
		AgentUser old = agentUserDao.findOne(id, stationId);
		if (old == null) {
			return;
		}
		old.setSalt(RandomStringUtils.randomAll(10));
		old.setPassword(MD5Util.pwdMd5Agent(old.getUsername(),"Aa123456", old.getSalt()));
		agentUserDao.updatePwd(id, old.getSalt(),old.getPassword(), stationId);
		LogUtils.modifyPwdLog("重置代理商账号密码:" + old.getUsername() );		
	}
}
