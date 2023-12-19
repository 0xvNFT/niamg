package com.play.web.var;

import com.play.core.LanguageEnum;
import com.play.core.UserTypeEnum;
import com.play.model.AdminUser;
import com.play.model.AgentUser;
import com.play.model.ManagerUser;
import com.play.model.PartnerUser;
import com.play.model.Station;
import com.play.model.StationDomain;
import com.play.model.SysUser;

public class SysThreadObject {
	private UserTypeEnum userType = UserTypeEnum.DEFAULT_TYPE;
	private StationType stationType;

	private ManagerUser managerUser;
	private PartnerUser partnerUser;
	private AdminUser adminUser;
	private AgentUser agentUser;
	private SysUser user;

	private StationDomain domain;
	private Station station;
	private String ip;

	private LanguageEnum language;
	
	/** 是否是前端站点 用来解决语言切换的相互影响问题（管理后台和用户前端） */
	private boolean isFrontStation = false;

	public boolean isFrontStation() {
		return isFrontStation;
	}

	public void setFrontStation(boolean isFrontStation) {
		this.isFrontStation = isFrontStation;
	}

	public UserTypeEnum getUserType() {
		return userType;
	}

	public void setUserType(UserTypeEnum userType) {
		this.userType = userType;
	}

	public StationType getStationType() {
		return stationType;
	}

	public void setStationType(StationType stationType) {
		this.stationType = stationType;
	}

	public ManagerUser getManagerUser() {
		return managerUser;
	}

	public void setManagerUser(ManagerUser managerUser) {
		this.managerUser = managerUser;
	}

	public PartnerUser getPartnerUser() {
		return partnerUser;
	}

	public void setPartnerUser(PartnerUser partnerUser) {
		this.partnerUser = partnerUser;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public AgentUser getAgentUser() {
		return agentUser;
	}

	public void setAgentUser(AgentUser agentUser) {
		this.agentUser = agentUser;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public StationDomain getDomain() {
		return domain;
	}

	public void setDomain(StationDomain domain) {
		this.domain = domain;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public LanguageEnum getLanguage() {
		return language;
	}

	public void setLanguage(LanguageEnum language) {
		this.language = language;
	}

}
