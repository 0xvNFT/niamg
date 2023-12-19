package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 代理下级人数记录表 
 *
 * @author admin
 *
 */
@Table(name = "sys_user_proxy_num")
public class SysUserProxyNum {
	/**
	 * 会员ID
	 */
	@Column(name = "user_id", primarykey = true, generator = Column.PK_BY_HAND)
	private Long userId;
	/**
	 * 下级代理数量
	 */
	@Column(name = "proxy_num")
	private Long proxyNum;
	/**
	 * 下级会员数量
	 */
	@Column(name = "member_num")
	private Long memberNum;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProxyNum() {
		return proxyNum;
	}

	public void setProxyNum(Long proxyNum) {
		this.proxyNum = proxyNum;
	}

	public Long getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(Long memberNum) {
		this.memberNum = memberNum;
	}

}
