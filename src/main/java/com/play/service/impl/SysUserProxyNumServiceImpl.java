package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.SysUserProxyNumDao;
import com.play.model.SysUserProxyNum;
import com.play.service.SysUserProxyNumService;

/**
 * 代理下级人数记录表
 *
 * @author admin
 *
 */
@Service
public class SysUserProxyNumServiceImpl implements SysUserProxyNumService {

	@Autowired
	private SysUserProxyNumDao sysUserProxyNumDao;

	@Override
	public void init(Long proxyId) {
		SysUserProxyNum p = new SysUserProxyNum();
		p.setUserId(proxyId);
		p.setMemberNum(0L);
		p.setProxyNum(0L);
		sysUserProxyNumDao.save(p);
	}
	@Override
	public void updateProxyNum(boolean isProxy, Integer num, Long proxyId) {
		sysUserProxyNumDao.updateProxyNum(isProxy, num, proxyId);
	}
}
