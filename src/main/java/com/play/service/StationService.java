package com.play.service;

import java.util.List;

import com.play.model.Station;
import com.play.model.vo.StationComboVo;
import com.play.model.vo.StationOnlineNumVo;
import com.play.orm.jdbc.page.Page;

public interface StationService {

	List<StationComboVo> getCombo(Long partnerId);

	void modify(Long id, Long partnerId, String name, String language,String currency);

	/**
	 * 所有启用状态的站点id
	 *
	 * @return
	 */
	List<Long> getActiveIds();

	Station findOneByCode(String code);

	Page<Station> getPage(Long partnerId, String code, String name);

	void changeStatus(Long id, Long partnerId, Integer status);

	void changeBgStatus(Long id, Long partnerId, Integer status);

	Station findOneById(Long id, Long partnerId);

	Station findOneById(Long id);

	List<Station> getAll();

	/**
	 * 获取总控在线统计列表
	 *
	 * @return
	 */
	List<StationOnlineNumVo> getManagerOnlineNum();

	Station findByDomain(String domainName);

	void modifyCode(Long id, String code);

}
