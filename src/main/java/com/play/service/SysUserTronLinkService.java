package com.play.service;

import com.play.model.SysUser;
import com.play.model.SysUserTronLink;
import com.play.orm.jdbc.page.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 *
 */
public interface SysUserTronLinkService {

	SysUserTronLink findOne(Long userId, Long stationId);

	SysUserTronLink findOne(String tronLinkAddr);

	SysUserTronLink save(SysUserTronLink t);

	void updateInitValue(String tronLinkAddr, double initTRX);

	void updateInitSuccess(String tronLinkAddr);

	void addUserTronLink(SysUser user, Long stationId, String addr);

	void deleteTimeoutInit(List<Long> tronLinkIds);

	void deleteUserTronLink(Long userId, Long stationId);

	void changeBindTronLink(SysUser user, Long stationId, String addr);

	Page<SysUserTronLink> getUserTronLinkListPage(Long stationId, String userName, Integer bindStatus, Date startTime, Date endTime, Integer pageSize, Integer pageNumber);

    List<SysUserTronLink> getUnBindList();
}