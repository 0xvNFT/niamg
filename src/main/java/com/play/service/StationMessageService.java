package com.play.service;

import com.play.model.StationMessage;
import com.play.model.SysUser;
import com.play.model.bo.StationMessageBo;
import com.play.orm.jdbc.page.Page;

import java.util.Date;
import java.util.List;

/**
 * 站点站内信发送表
 *
 * @author admin
 *
 */
public interface StationMessageService {
	/**
	 * 获取站内信发送列表
	 *
	 * @param stationId
	 * @param sendType
	 * @param receiveType
	 * @param title
	 * @param sendUsername
	 * @param receiveUsername
	 * @param begin
	 * @param end
	 * @return
	 */
	Page<StationMessage> getAdminPage(Long stationId, Integer sendType, Integer receiveType, String title,
			String sendUsername, String receiveUsername, Date begin, Date end);

	/**
	 * 发送站内信
	 *
	 * @param message
	 */
	void    sendBo(StationMessageBo message);

	/**
	 * 发送删除
	 *
	 * @param ids
	 * @param stationId
	 */
	void sendDelete(String ids, Long stationId);

	/**
	 * 收件删除
	 *
	 * @param ids
	 * @param stationId
	 */
	void receiveDelete(String ids, Long userId, Long stationId);

	/**
	 * 系统消息发送
	 *
	 * @param stationId
	 * @param userId
	 * @param username
	 * @param title
	 * @param content
	 */
	void sysMessageSend(Long stationId, Long userId, String username, String title, String content, Integer popStatus);

	/**
	 * 获取单个
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	StationMessage findOne(Long id, Long stationId);

	/**
	 * 获取站内信接收列表
	 *
	 * @param userId
	 * @param stationId
	 * @return
	 */
	Page<StationMessage> getReceivePage(Long userId, Long stationId);

	/**
	 * 消息读取
	 *
	 * @param rid 接收方ID
	 * @param sid 发送方ID
	 */
	void readMessage(Long rid, Long sid, SysUser user);

	void readMessage2(String rids, String sids, SysUser user);


	/**
	 * 获取用户未读站内信列表
	 *
	 * @param userId
	 * @return
	 */
	Integer unreadMessageNum(Long userId, Long stationId);

	Integer unreadMessageNumForApp(Long userId, Long stationId);

	/**
	 * 清除一个月前的站内信记录
	 *
	 * @param date
	 */
	void clearGenerateData(Date date);

	/**
	 * 获取用户弹窗站内信
	 *
	 * @param
	 */
	List<StationMessage> getMemberSendMessagePopList(Long stationId, String receiveAccount);
	List<StationMessage> getMemberSendMessagePopList(Long stationId, String receiveAccount,String sort);

	/**
	 * 一键已读收件箱站内信
	 *
	 * @param userId
	 */
	void allReceiveRead(Long userId, Long stationId);

	/**
	 * 一键删除收件箱站内信
	 *
	 * @param userId
	 */
	void allReceiveDelete(Long userId, Long stationId);

	/**
	 * 获取发送到个人的接受者账号
	 */
	String findSingleReceiveUsername(Long messageId, Long stationId);

}
