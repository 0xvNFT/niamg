package com.play.service;

import java.util.List;

import com.play.model.StationBank;
import com.play.orm.jdbc.page.Page;

/**
 * 站点银行信息表
 *
 * @author admin
 *
 */
public interface StationBankService {

	public Page<StationBank> adminPage(Long stationId, String bankName, String bankCode);

	void adminAddBank(StationBank bank);

	void deleteStationBank(Long stationId, Long id);

	boolean modifySave(StationBank bank);

	StationBank findOneById(Long id, Long stationId);

	public List<StationBank> findAll(Long stationId);

	public String getNameByCode(String code,Long stationId);

	public StationBank getStationBankByCode(String code,Long stationId);

}