package com.play.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.StationConfigEnum;
import com.play.dao.StationMessageDao;
import com.play.dao.StationMessageReceiveDao;
import com.play.dao.SysUserDao;
import com.play.model.StationMessage;
import com.play.model.StationMessageReceive;
import com.play.model.SysUser;
import com.play.model.bo.StationMessageBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationMessageService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginMemberUtil;

/**
 * 站点站内信发送表
 *
 * @author admin
 *
 */
@Service
public class StationMessageServiceImpl implements StationMessageService {

	@Autowired
	private StationMessageDao stationMessageDao;
	@Autowired
	private StationMessageReceiveDao stationMessageReceiveDao;
	@Autowired
	private SysUserDao sysUserDao;

	@Override
	public Page<StationMessage> getAdminPage(Long stationId, Integer sendType, Integer receiveType, String title,
			String sendUsername, String receiveUsername, Date begin, Date end) {
		Page<StationMessage> page = stationMessageDao.getPage(stationId, sendType, receiveType, title, sendUsername,
				null, receiveUsername, begin, end);
		if (page.hasRows()) {
			for (StationMessage m : page.getRows()) {
				switch (m.getReceiveType()) {
				case StationMessage.TYPE_SINGLE:// 根据会员账号发送
				case StationMessage.TYPE_SELECT_USER:// 根据指定多个账号发送
				case StationMessage.TYPE_PROXY_NAME:// 代理线推送
					m.setReceiveUsernames(stationMessageReceiveDao.findSingleReceiveUsername(m.getId(), stationId));
					break;
				}
				m.setTitle(I18nTool.convertArrStrToMessage(m.getTitle()));
			}
		}
		return page;
	}

	@Override
	public void sendDelete(String ids, Long stationId) {
		if (ids == null || ids == "" || stationId == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		SysUser acc = LoginMemberUtil.currentUser();
		String[] messageId = ids.split(",");
		for (String mes : messageId) {
			Long id = Long.parseLong(mes);
			StationMessage delMessage = stationMessageDao.findOne(id, stationId);
			if (delMessage == null || !delMessage.getStationId().equals(stationId)
					|| acc != null && !acc.getId().equals(delMessage.getSendUserId())) {
				throw new ParamException(BaseI18nCode.illegalRequest);
			}
			stationMessageReceiveDao.delByMessageId(id);
			stationMessageDao.deleteById(id);
		}
		LogUtils.delLog("删除站内信发件箱");
	}

	@Override
	public void sendBo(StationMessageBo bo) {
		Integer receiveType = bo.getReceiveType();
		if (receiveType == null || bo.getSendType() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		Date now = new Date();
		StationMessage m = new StationMessage();
		m.setStationId(bo.getStationId());
		m.setSendType(bo.getSendType());
		m.setContent(bo.getContent());
		m.setTitle(bo.getTitle());
		m.setCreateTime(now);
		m.setDegreeId(bo.getDegreeId());
		m.setGroupId(bo.getGroupId());
		m.setReceiveType(bo.getReceiveType());
		m.setSendUserId(LoginAdminUtil.getUserId());
		m.setSendUsername(LoginAdminUtil.getUsername());
		m.setPopStatus(bo.getPopStatus());
		switch (receiveType) {
		case StationMessage.TYPE_SINGLE:// 根据会员账号发送
			adminSendToUser(m, bo);
			break;
		case StationMessage.TYPE_SELECT_USER:// 根据指定多个账号发送
			adminSendToUsers(m, bo);
			break;
		case StationMessage.TYPE_ALL:
			// 发送所有
			m = stationMessageDao.save(m);
			stationMessageReceiveDao.batchSendMessage(m.getId(), m.getStationId(), null, null, m.getSendType(),
					getLastLoginTime(bo.getLastLogin(), now), null, bo.getPopStatus());
			break;
		case StationMessage.TYPE_DEGREE:// 会员等级发送
			adminDegreeSend(m, bo);
			break;
		case StationMessage.TYPE_GROUP:// 会员组别发送
			adminGroupSend(m, bo);
			break;
		case StationMessage.TYPE_PROXY_NAME:// 代理线推送
			adminProxySend(m, bo);
		}
		LogUtils.addLog("发送站内信");
	}

	private Date getLastLoginTime(Integer lastLogin, Date createTime) {
		if (lastLogin == null) {
			return DateUtil.dayFirstTime(createTime, -30);
		}
		switch (lastLogin) {
		case 1:
			// 一周内
			return DateUtil.dayFirstTime(createTime, -7);
		case 2:
			// 两周内
			return DateUtil.dayFirstTime(createTime, -14);
		case 4:
			// 两个月内
			return DateUtil.dayFirstTime(createTime, -60);
		default:
			// 一个月内
			return DateUtil.dayFirstTime(createTime, -30);
		}
	}

	/**
	 * 代理线发送
	 *
	 * @param m
	 * @param bo
	 */
	private void adminProxySend(StationMessage m, StationMessageBo bo) {
		if (StringUtils.isEmpty(bo.getProxyName())) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		m.setProxyName(bo.getProxyName());
		m = stationMessageDao.save(m);
		stationMessageReceiveDao.batchSendMessage(m.getId(), m.getStationId(), null, null, m.getSendType(),
				getLastLoginTime(bo.getLastLogin(), m.getCreateTime()), bo.getProxyName(), bo.getPopStatus());
	}

	/**
	 * 管理员按等级发送
	 */
	private void adminDegreeSend(StationMessage m, StationMessageBo bo) {
		if (m.getStationId() == null || m.getDegreeId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		m = stationMessageDao.save(m);
		stationMessageReceiveDao.batchSendMessage(m.getId(), m.getStationId(), m.getDegreeId(), null, m.getSendType(),
				getLastLoginTime(bo.getLastLogin(), m.getCreateTime()), null, bo.getPopStatus());
	}

	/*
	 * 组别发送
	 */
	private void adminGroupSend(StationMessage m, StationMessageBo bo) {
		if (m.getStationId() == null || m.getGroupId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		m = stationMessageDao.save(m);
		stationMessageReceiveDao.batchSendMessage(m.getId(), m.getStationId(), null, m.getGroupId(), m.getSendType(),
				getLastLoginTime(bo.getLastLogin(), m.getCreateTime()), null, bo.getPopStatus());
	}

	/**
	 * 发送给单个会员
	 *
	 * @param m
	 * @param bo
	 */
	private void adminSendToUser(StationMessage m, StationMessageBo bo) {
		if (StringUtils.isEmpty(bo.getReceiveUsername())) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		SysUser user = sysUserDao.findOneByUsername(bo.getReceiveUsername(), m.getStationId(), null);
		if (user == null) {
			throw new ParamException(BaseI18nCode.memberUnExist);
		}
		m = stationMessageDao.save(m);
		StationMessageReceive receiveMessage = new StationMessageReceive();
		receiveMessage.setStationId(m.getStationId());
		receiveMessage.setUsername(user.getUsername());
		receiveMessage.setUserId(user.getId());
		receiveMessage.setMessageId(m.getId());
		receiveMessage.setStatus(StationMessageReceive.STATUS_UNREAD);
		receiveMessage.setSendType(m.getSendType());
		receiveMessage.setPopStatus(m.getPopStatus());
		stationMessageReceiveDao.save(receiveMessage);
	}

	/**
	 * 管理员按多个会员发送
	 */
	private void adminSendToUsers(StationMessage m, StationMessageBo bo) {
		if (StringUtils.isEmpty(bo.getUsernames())) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		m = stationMessageDao.save(m);
		SysUser user = null;
		String[] memarr = bo.getUsernames().split(" |,|，|\n");
		for (String member : memarr) {
			if (StringUtils.isEmpty(member)) {
				continue;
			}
			member = member.trim().toLowerCase();
			user = sysUserDao.findOneByUsername(member, m.getStationId(), null);
			if (user == null) {
				throw new ParamException(BaseI18nCode.memberUnExist);
			}
			StationMessageReceive receiveMessage = new StationMessageReceive();
			receiveMessage.setStationId(m.getStationId());
			receiveMessage.setUsername(user.getUsername());
			receiveMessage.setUserId(user.getId());
			receiveMessage.setMessageId(m.getId());
			receiveMessage.setStatus(StationMessageReceive.STATUS_UNREAD);
			receiveMessage.setSendType(m.getSendType());
			receiveMessage.setPopStatus(m.getPopStatus());
			stationMessageReceiveDao.save(receiveMessage);
		}
	}

	@Override
	public StationMessage findOne(Long id, Long stationId) {
		return stationMessageDao.findOne(id, stationId);
	}

	@Override
	public Page<StationMessage> getReceivePage(Long userId, Long stationId) {
		Page<StationMessage> page = stationMessageDao.getMemberPage(userId, stationId);
		if (page != null && page.getRows() != null) {
			page.getRows().forEach(x -> {
				if (x.getSendType() == StationMessage.SEND_TYPE_ADMIN) {
					//x.setSendUsername(BaseI18nCode.stationWebInfo.getMessage());
					x.setSendUsername(I18nTool.getMessage(BaseI18nCode.stationWebInfo));
				}

				// 站内信 title 和 content 多语言转化
				x.setTitle(I18nTool.convertArrStrToMessage(x.getTitle()));
				x.setContent(I18nTool.convertArrStrToMessage(x.getContent()));

			});
		}
		return page;
	}

	@Override
	public void readMessage2(String rids, String sids, SysUser user) {

		if (StringUtils.isEmpty(rids) || StringUtils.isEmpty(sids)) {
			throw new ParamException(BaseI18nCode.parameterEmpty);
		}
		String[] ridsArr = rids.split(",");
		String[] sidsArr = sids.split(",");
		if (ridsArr.length != sidsArr.length) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		for (int i = 0; i < ridsArr.length; i++) {
			String rid = ridsArr[i];
			String sid = sidsArr[i];
			readMessage(Long.parseLong(rid), Long.parseLong(sid), user);
		}
	}

	@Override
	public void readMessage(Long rid, Long sid, SysUser user) {
		// 如果接收方ID为零，则是系统消息
		if (rid == 0L) {
			StationMessage sendMessage = stationMessageDao.findOneById(sid);
			StationMessageReceive receiveMessage = new StationMessageReceive();
			receiveMessage.setStatus(StationMessageReceive.STATUS_READ);
			receiveMessage.setMessageId(sendMessage.getId());
			receiveMessage.setUserId(user.getId());
			receiveMessage.setUsername(user.getUsername());
			receiveMessage.setStationId(user.getStationId());
			receiveMessage.setSendType(sendMessage.getSendType());
			receiveMessage.setPopStatus(sendMessage.getPopStatus());
			stationMessageReceiveDao.save(receiveMessage);
		} else {
			StationMessageReceive receiveMessage = stationMessageReceiveDao.findOne(rid, user.getStationId());
		//	LoggerFactory.getLogger("a").error("receive message rid = " + rid);
			if (receiveMessage != null && Objects.equals(receiveMessage.getUserId(), user.getId())) {
			//	LoggerFactory.getLogger("a").error("receive message userid = " + receiveMessage.getUserId());
				receiveMessage.setStatus(StationMessageReceive.STATUS_READ);
				stationMessageReceiveDao.update(receiveMessage);
			}
		}
	}

	@Override
	public void receiveDelete(String ids, Long userId, Long stationId) {
		if (StringUtils.isEmpty(ids) || stationId == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		String[] messageId = ids.split(",");
		for (String mes : messageId) {
			stationMessageReceiveDao.delete(Long.parseLong(mes), userId, stationId);
		}
		LogUtils.delLog("删除站内信收发件箱");
	}

	@Override
	public Integer unreadMessageNum(Long userId, Long stationId) {
		int unread = stationMessageReceiveDao.countUnreadMsgNum(userId,stationId);
		if (unread > 0 &&  StationConfigUtil.isOff(stationId, StationConfigEnum.message_pop_up_once)) {
			stationMessageReceiveDao.updateUnReadMsg(userId,stationId);
		}
		return unread;
	}

	@Override
	public Integer unreadMessageNumForApp(Long userId, Long stationId) {
		return stationMessageReceiveDao.countUnreadMsgNumForApp(userId,stationId);
	}

	@Override
	public void clearGenerateData(Date date) {
		Long maxId = stationMessageDao.getMaxMessageId(date);
		// 删除发送记录
		stationMessageDao.clearGenerateData(date);
		// 删除接收记录
		stationMessageReceiveDao.clearGenerateData(maxId);
	}

	@Override
	public List<StationMessage> getMemberSendMessagePopList(Long stationId, String receiveUserName, String sort) {
		return stationMessageDao.getPopMessageList(stationId, receiveUserName,sort);
	}

	@Override
	public List<StationMessage> getMemberSendMessagePopList(Long stationId, String receiveUserName) {
		return getMemberSendMessagePopList(stationId, receiveUserName,null);
	}

	@Override
	public void allReceiveRead(Long userId, Long stationId) {
		stationMessageReceiveDao.allReceiveRead(userId, stationId);
		LogUtils.addLog("一键已读收件箱站内信");
	}

	@Override
	public void allReceiveDelete(Long userId, Long stationId) {
		stationMessageReceiveDao.allReceiveDelete(userId, stationId);
		LogUtils.delLog("一键删除收件箱站内信");
	}

	@Override
	public void sysMessageSend(Long stationId, Long userId, String username, String title, String content,
			Integer popStatus) {
		StationMessage s = new StationMessage();
		s.setTitle(title);
		s.setContent(content);
		s.setCreateTime(new Date());
		s.setStationId(stationId);
        s.setSendUsername(I18nTool.getMessage(BaseI18nCode.stationSysInfo.getCode(), Locale.ENGLISH));

		s.setReceiveType(StationMessage.TYPE_SINGLE);
		s.setSendType(StationMessage.SEND_TYPE_SYS);
		s.setPopStatus(popStatus);
		stationMessageDao.save(s);

		StationMessageReceive r = new StationMessageReceive();
		r.setStatus(StationMessageReceive.STATUS_UNREAD);
		r.setUserId(userId);
		r.setStationId(stationId);
		r.setMessageId(s.getId());
		r.setUsername(username);
		r.setPopStatus(Constants.STATUS_DISABLE);
		stationMessageReceiveDao.save(r);
	}

	@Override
	public String findSingleReceiveUsername(Long messageId, Long stationId) {
		return stationMessageReceiveDao.findSingleReceiveUsername(messageId, stationId);
	}
}
