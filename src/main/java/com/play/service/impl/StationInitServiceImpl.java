package com.play.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.play.common.utils.security.MD5Util;
import com.play.model.AdminUser;
import com.play.model.vo.ThirdPlatformVo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.CurrencyEnum;
import com.play.core.LanguageEnum;
import com.play.core.PlatformType;
import com.play.core.UserTypeEnum;
import com.play.dao.AdminMenuDao;
import com.play.dao.StationDao;
import com.play.model.AdminUserGroup;
import com.play.model.Partner;
import com.play.model.Station;
import com.play.service.AdminUserGroupService;
import com.play.service.AdminUserService;
import com.play.service.PartnerService;
import com.play.service.StationConfigService;
import com.play.service.StationInitService;
import com.play.service.StationRebateService;
import com.play.service.StationRegisterConfigService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.service.ThirdGameService;
import com.play.service.ThirdPlatformService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

@Service
public class StationInitServiceImpl implements StationInitService {
	private Logger logger = LoggerFactory.getLogger(StationInitServiceImpl.class);
	@Autowired
	private StationDao stationDao;
	@Autowired
	private StationConfigService configService;
	@Autowired
	private AdminMenuDao adminMenuDao;
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private AdminUserGroupService adminUserGroupService;
	@Autowired
	private PartnerService partnerService;
	@Autowired
	private ThirdGameService thirdGameService;
	@Autowired
	private ThirdPlatformService thirdPlatformService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;
	@Autowired
	private StationRebateService rebateService;
	@Autowired
	private StationRegisterConfigService registerConfigService;

	@Override
	public void initEvolution(Integer p) {
		// TODO Auto-generated method stub
		stationDao.findAll().forEach(t -> {
			List<ThirdPlatformVo> list = thirdPlatformService.find(t.getId());
			boolean flag = true;
			for(int i=0;i<list.size();i++){
				if(list.get(i).getPlatform()!=null && list.get(i).getPlatform().equals(p)){
					flag = false;
				}
			}
		    if(flag){
				thirdPlatformService.initEvolution(t.getId(), t.getPartnerId(),p);
			}
		});
	}
	
	@Override
	public void init(Station station) {
		if (station.getPartnerId() == null) {
			station.setPartnerId(0L);
		}
		validData(station);
		if (station.getPartnerId() != null && station.getPartnerId() > 0) {
			Partner p = partnerService.findOne(station.getPartnerId());
			if (p == null) {
				throw new ParamException(BaseI18nCode.partnerOldNotExist);
			}
		}
		Long stationId = stationDao.getNextId();
		station.setCreateDatetime(new Date());
		station.setStatus(Constants.STATUS_ENABLE);
		station.setBgStatus(Constants.STATUS_ENABLE);
		station.setId(stationId);
		stationDao.save(station);
		initAdmin(stationId, station.getPartnerId());
		initThird(stationId, station.getPartnerId());
		configService.initStation(stationId, station.getPartnerId());
		userDegreeService.init(stationId, station.getPartnerId());
		userGroupService.init(stationId, station.getPartnerId());
		rebateService.init(stationId);
		// 注册选项
		registerConfigService.init(stationId);
		LogUtils.addLog("添加站点" + station.getName() + "[" + station.getCode() + "]");
	}

	private void initThird(Long stationId, Long partnerId) {
		thirdGameService.init(stationId, partnerId);
		thirdPlatformService.init(stationId, partnerId);
	}

	/**
	 * 验证数据是否正确
	 * 
	 * @param station
	 */
	private void validData(Station station) {
		station.setName(StringUtils.trim(station.getName()));
		if (StringUtils.isEmpty(station.getName())) {
			throw new ParamException(BaseI18nCode.stationNameNotEmpty);
		}
		station.setCode(StringUtils.trim(station.getCode()));
		if (StringUtils.isEmpty(station.getCode())) {
			throw new ParamException(BaseI18nCode.stationCodeNotEmpty);
		}
		LanguageEnum le = null;
		try {
			le = LanguageEnum.valueOf(station.getLanguage());
		} catch (Exception e) {
			throw new ParamException(BaseI18nCode.stationSelectLanguage);
		}
		if (le == null) {
			throw new ParamException(BaseI18nCode.stationSelectLanguage);
		}
		CurrencyEnum ce = null;
		try {
			ce = CurrencyEnum.valueOf(station.getCurrency());
		} catch (Exception e) {
			throw new ParamException(BaseI18nCode.stationSelectCurrency);
		}
		if (ce == null) {
			throw new ParamException(BaseI18nCode.stationSelectCurrency);
		}
		if (stationDao.exist(station.getCode())) {
			throw new ParamException(BaseI18nCode.stationCodeExist);
		}
	}

	private void initAdmin(Long stationId, Long partnerId) {
		adminMenuDao.initStation(stationId, partnerId, 0L, null);
		// 添加组别和权限
		AdminUserGroup zg = adminUserGroupService.saveDefaultGroup(stationId, partnerId,
				BaseI18nCode.stationOwnerLeader.getMessage(), AdminUserGroup.TYPE_MASTER);
		adminMenuDao.initStation(stationId, partnerId, zg.getId(), adminMenuDao.getExcludeMenuIdForZg());

		AdminUserGroup kf = adminUserGroupService.saveDefaultGroup(stationId, partnerId,
				BaseI18nCode.stationClientService.getMessage(), AdminUserGroup.TYPE_MASTER);
		adminMenuDao.initStation(stationId, partnerId, kf.getId(), adminMenuDao.getExcludeMenuIdForKf());

		AdminUserGroup js = adminUserGroupService.saveDefaultGroup(stationId, partnerId,
				BaseI18nCode.stationTechService.getMessage(), AdminUserGroup.TYPE_MASTER);
		adminMenuDao.initStation(stationId, partnerId, js.getId(), adminMenuDao.getExcludeMenuIdForJs());

		if (partnerId != null && partnerId > 0) {// 有选择合作商，才需要添加
			AdminUserGroup partner = adminUserGroupService.saveDefaultGroup(stationId, partnerId,
					BaseI18nCode.stationPartnerAdmin.getMessage(), AdminUserGroup.TYPE_PARTNER_VIEW);
			adminMenuDao.initStation(stationId, partnerId, partner.getId(), adminMenuDao.getExcludeMenuIdForPartner());

			adminUserService.saveDefaultAdminUser(stationId, partnerId, SystemConfig.ADMIN_PARTNER,
					SystemConfig.ADMIN_PARTNER_PWD, partner.getId(), UserTypeEnum.ADMIN_PARTNER_SUPER.getType());
		}
		AdminUserGroup ug = adminUserGroupService.saveDefaultGroup(stationId, partnerId,
				BaseI18nCode.stationDefAdmin.getMessage(), AdminUserGroup.TYPE_ADMIN_VIEW);
		adminMenuDao.initStation(stationId, partnerId, ug.getId(), adminMenuDao.getExcludeMenuIdForStation());
		// 添加站点后台管理员
		adminUserService.saveDefaultAdminUser(stationId, partnerId, SystemConfig.ADMIN_ROOT,
				SystemConfig.ADMIN_ROOT_PWD, 0L, UserTypeEnum.ADMIN_MASTER_SUPER.getType());

		adminUserService.saveDefaultAdminUser(stationId, partnerId, SystemConfig.ADMIN, SystemConfig.ADMIN_PWD,
				ug.getId(), UserTypeEnum.ADMIN.getType());

		batchInsertAdminUser(js.getId(), kf.getId(), zg.getId(), stationId, partnerId);
	}

	private void batchInsertAdminUser(Long jsGroupId, Long kfGroupId, Long zgGroupId, Long stationId, Long partnerId) {
		String text = readAdminUserFile("empty-station-admin-user.txt");
		if (StringUtils.isNotEmpty(text)) {
			text = text.replace("${jsGroupId}", jsGroupId.toString()).replace("${zgGroupId}", zgGroupId.toString())
					.replace("${kfGroupId}", kfGroupId.toString()).replace("${stationId}", stationId.toString())
					.replace("${createDatetime}", DateUtil.getCurrentTime())
					.replace("${partnerId}", partnerId.toString());
			stationDao.update(text);
		}
	}

	public void initAdminPwd(String newPwd,Long stationId) {
		List<AdminUser> users = adminUserService.findByStationId(stationId);
		for (AdminUser admin : users) {
			if (admin.getUsername().equals("admin") || admin.getUsername().equals("root")) {
				continue;
			}
			adminUserService.updatePwd(admin.getId(), admin.getStationId(), newPwd, newPwd);
		}
	}

	/**
	 * 读取生成管理员文件
	 */
	private String readAdminUserFile(String fileName) {
		try {
			File file = ResourceUtils.getFile("classpath:" + fileName);
			if (!file.exists()) {
				return null;
			}
			return FileUtils.readFileToString(file, "UTF-8");
		} catch (Exception e) {
			logger.error("读取文件发生错误", e);
		}
		return null;
	}
}
