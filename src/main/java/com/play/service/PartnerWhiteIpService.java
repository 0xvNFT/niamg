package com.play.service;

import java.util.List;

import com.play.model.PartnerWhiteIp;
import com.play.model.vo.WhiteIpVo;
import com.play.orm.jdbc.page.Page;

/**
 * 合作商后台ip白名单
 *
 * @author admin
 *
 */
public interface PartnerWhiteIpService {

	Page<PartnerWhiteIp> page(Long partnerId, String ip, Integer type);

	void delete(Long id, Long partnerId);

	String save(PartnerWhiteIp ip);

	List<WhiteIpVo> getIps(Long partnerId);

}