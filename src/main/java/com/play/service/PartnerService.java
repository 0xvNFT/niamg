package com.play.service;

import java.util.List;

import com.play.model.Partner;
import com.play.orm.jdbc.page.Page;

/**
 * 合作商信息表 
 *
 * @author admin
 *
 */
public interface PartnerService {

	List<Partner> getAll();

	Page<Partner> page();

	void save(Partner p);

	Partner findOne(Long id);

	void modify(Partner p);

}