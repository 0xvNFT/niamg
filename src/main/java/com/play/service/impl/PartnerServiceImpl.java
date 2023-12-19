package com.play.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.dao.PartnerDao;
import com.play.model.Partner;
import com.play.orm.jdbc.page.Page;
import com.play.service.PartnerService;
import com.play.service.PartnerUserService;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 合作商信息表
 *
 * @author admin
 *
 */
@Service
public class PartnerServiceImpl implements PartnerService {

	@Autowired
	private PartnerDao partnerDao;
	@Autowired
	private PartnerUserService partnerUserService;

	@Override
	public List<Partner> getAll() {
		return partnerDao.getAll();
	}

	@Override
	public Page<Partner> page() {
		return partnerDao.page();
	}

	@Override
	public void save(Partner p) {
		partnerDao.save(p);
		partnerUserService.initUser(p.getId());
		LogUtils.addLog("添加合作商");
	}

	@Override
	public Partner findOne(Long id) {
		return partnerDao.findOne(id);
	}

	@Override
	public void modify(Partner p) {
		Partner old = partnerDao.findOne(p.getId());
		if (old == null) {
			throw new BaseException(BaseI18nCode.partnerOldNotExist);
		}
		partnerDao.update(p);
		LogUtils.addLog("修改合作商 旧名称：" + old.getName() + " 新名称：" + p.getName());
	}
}
