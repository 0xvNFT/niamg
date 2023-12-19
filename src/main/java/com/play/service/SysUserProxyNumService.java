package com.play.service;

/**
 * 代理下级人数记录表
 *
 * @author admin
 *
 */
public interface SysUserProxyNumService {

	void init(Long proxyId);

	void updateProxyNum(boolean isProxy, Integer num, Long proxyId);

}