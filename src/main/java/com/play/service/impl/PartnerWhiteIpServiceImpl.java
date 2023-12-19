package com.play.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.dao.PartnerDao;
import com.play.dao.PartnerWhiteIpDao;
import com.play.model.Partner;
import com.play.model.PartnerWhiteIp;
import com.play.model.vo.WhiteIpVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.PartnerWhiteIpService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ValidateUtil;

/**
 * 合作商后台ip白名单
 *
 * @author admin
 *
 */
@Service
public class PartnerWhiteIpServiceImpl implements PartnerWhiteIpService {

	@Autowired
	private PartnerWhiteIpDao partnerWhiteIpDao;
	@Autowired
	private PartnerDao partnerDao;

	@Override
	public Page<PartnerWhiteIp> page(Long partnerId, String ip, Integer type) {
		return partnerWhiteIpDao.page(partnerId, ip, type);
	}

	@Override
	public void delete(Long id, Long partnerId) {
		PartnerWhiteIp ip = partnerWhiteIpDao.findOne(id, partnerId);
		if (ip == null) {
			throw new BaseException(BaseI18nCode.ipNotExist);
		}
		partnerWhiteIpDao.deleteById(id);
		LogUtils.delLog("删除合作商后台IP白名单" + ip.getIp());
	}

	@Override
	public String save(PartnerWhiteIp wip) {
		if (StringUtils.isEmpty(wip.getIp())) {
			throw new ParamException(BaseI18nCode.inputIp);
		}
		String allIps = StringUtils.trim(wip.getIp());
		if (StringUtils.isEmpty(allIps)) {
			throw new ParamException(BaseI18nCode.inputCorrectIp);
		}
		Partner p = partnerDao.findOne(wip.getPartnerId());
		if (p == null) {
			throw new ParamException(BaseI18nCode.selectPartnerPlease);
		}
		String[] ips = allIps.split(",|，|\n");
		StringBuilder sb = new StringBuilder();
		wip.setCreateDatetime(new Date());
		wip.setPartnerId(p.getId());
		for (String ip : ips) {
			ip = StringUtils.trim(ip);
			if (!ValidateUtil.isIPAddr(ip)) {
				sb.append(ip).append(I18nTool.getMessage(BaseI18nCode.formatError));
				continue;
			}
			if (partnerWhiteIpDao.exist(ip, wip.getPartnerId())) {
				sb.append(ip).append(BaseI18nCode.stationBound.getMessage());
				continue;
			}
			wip.setId(null);
			wip.setIp(ip);
			partnerWhiteIpDao.save(wip);
		}
		LogUtils.addLog("添加合作商后台IP白名单 " + allIps);
		return sb.toString();
	}

	@Override
	public List<WhiteIpVo> getIps(Long partnerId) {
		return partnerWhiteIpDao.getIps(partnerId);
	}
}
