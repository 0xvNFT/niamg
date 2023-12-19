package com.play.service.impl;

import com.play.common.utils.LogUtils;
import com.play.core.TronLinkStatusEnum;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.SysUserTronLinkDao;
import com.play.model.SysUserTronLink;
import com.play.service.SysUserTronLinkService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 *
 */
@Service
public class SysUserTronLinkServiceImpl implements SysUserTronLinkService {
	@Autowired
	private SysUserTronLinkDao sysUserTronLinkDao;

	@Override
	public SysUserTronLink findOne(Long userId, Long stationId) {
		// TODO Auto-generated method stub
		return sysUserTronLinkDao.findOne(userId, stationId);
	}

	@Override
	public SysUserTronLink findOne(String tronLinkAddr) {
		// TODO Auto-generated method stub
		return sysUserTronLinkDao.findOne(tronLinkAddr);
	}

	@Override
	public SysUserTronLink save(SysUserTronLink model) {
		// TODO Auto-generated method stub
		return sysUserTronLinkDao.save(model);
	}

	@Override
	public void updateInitValue(String tronLinkAddr, double initTRX) {
		// TODO Auto-generated method stub
		sysUserTronLinkDao.updateInitValue(tronLinkAddr, initTRX);
	}

	@Override
	public void updateInitSuccess(String tronLinkAddr) {
		// TODO Auto-generated method stub
		sysUserTronLinkDao.updateInitSuccess(tronLinkAddr);
	}

	@Override
	public void deleteTimeoutInit(List<Long> tronLinkIds) {
		// TODO Auto-generated method stub
		sysUserTronLinkDao.deleteTimeoutInit(tronLinkIds);
	}

	@Override
	public void addUserTronLink(SysUser user, Long stationId, String addr) {
		SysUserTronLink sysUserTronLink = new SysUserTronLink();
		sysUserTronLink.setStationId(stationId);
		sysUserTronLink.setUserId(user.getId());
		sysUserTronLink.setUserName(user.getUsername());
		sysUserTronLink.setTronLinkAddr(addr);
		sysUserTronLink.setInitTrx(this.genRandom());
		sysUserTronLink.setCreateDatetime(new Date());
		sysUserTronLink.setInitValidDatetime(new Date());
		sysUserTronLink.setBindStatus(TronLinkStatusEnum.unBind.getType());
		this.save(sysUserTronLink);
		LogUtils.addLog("新增用户Tron链地址：userId=" + user.getId() + ", stationId=" + stationId + ", addr=" + addr);
	}

	@Override
	public void deleteUserTronLink(Long userId, Long stationId) {
		sysUserTronLinkDao.deleteUserTronLink(userId, stationId);
		LogUtils.delLog("删除用户Tron链地址：userId=" + userId+ ", stationId=" + stationId);
	}

	@Transactional
	@Override
	public void changeBindTronLink(SysUser user, Long stationId, String addr) {
		this.deleteUserTronLink(user.getId(), stationId);
		this.addUserTronLink(user, stationId, addr);
		LogUtils.modifyLog("换绑用户Tron链地址：userId=" + user.getId()+ ", stationId=" + stationId + ", addr=" + addr);
	}

	@Override
	public Page<SysUserTronLink> getUserTronLinkListPage(Long stationId, String userName, Integer bindStatus, Date startTime, Date endTime, Integer pageSize, Integer pageNumber) {
		return sysUserTronLinkDao.getUserTronLinkListPage(stationId, userName, bindStatus, startTime, endTime);
	}

	@Override
	public List<SysUserTronLink> getUnBindList() {
		return sysUserTronLinkDao.getUnBindList();
	}

	/**
	 * 生成一个范围在0.1001 - 0.1999的小数
	 * @return
	 */
	private BigDecimal genRandom() {
		int num = (int)(Math.random() * 1001) + 1000;
		return BigDecimal.valueOf(num).divide(new BigDecimal(10000));
	}
}
