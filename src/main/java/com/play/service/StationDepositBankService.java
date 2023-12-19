package com.play.service;

import java.util.List;

import com.play.model.StationDepositBank;
import com.play.model.dto.ExchangeUSDTDto;
import com.play.orm.jdbc.page.Page;

/**
 * 网站入款：银行入款
 *
 * @author admin
 */
public interface StationDepositBankService {

	/**
	 * 获取列表数据
	 *
	 * @param stationId
	 * @return
	 */
	Page<StationDepositBank> getPage(String name, Integer status, Long stationId);

	/**
	 * 保存
	 *
	 * @param bank
	 */
	void addSave(StationDepositBank bank);

	void modifySave(StationDepositBank bank);

	/**
	 * 删除
	 *
	 * @param id
	 */
	void delBank(long id);

    /**
     * 获取单个
     *
     * @param id
     * @param stationId
     * @param groupId
     * @param degreeId
     * @return
     */
    StationDepositBank getOne(Long id, Long stationId, Long groupId, Long degreeId,String bankName);


    /**
	 * 获取单个
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	StationDepositBank getOne(Long id, Long stationId);

	/**
	 * 更新状态
	 *
	 * @param status
	 * @param id
	 * @param stationId
	 */
	void updateStatus(Integer status, Long id, Long stationId);

	/**
	 * 获取用户能使用的银行入款
	 *
	 * @param stationId
	 * @param degreeId  会员等级ID
	 * @return
	 */
	List<StationDepositBank> getStationBankList(Long stationId, Long degreeId, Long groupId, Integer status,String bankName);

	/**
	 * 根据站查询租户银行入款
	 *
	 * @param stationId
	 * @return
	 */
	List<StationDepositBank> getBankListByStation(Long stationId);

	/**
	 * 备注修改
	 *
	 * @param
	 * @param remark
	 */
	void remarkModify(Long id, String remark);


	/**
	 * 获取单个
	 *
	 * @param  bankCard
	 * @param stationId
	 * @param bankName
	 * @return
	 */
	StationDepositBank getUsdtBank(Long stationId,String bankName,String bankCard);

	/**
	 * 获取存款银行卡
	 * @param stationId
	 * @param bankName
	 * @param bankCard
	 * @param status
	 * @return
	 */
	List<StationDepositBank> getStationDepositBankList(Long stationId, String bankName, String bankCard, Integer status);

	/**
	 * 获取所有
	 *
	 * @return
	 */
	List<StationDepositBank> getAllUsdtBank();

	/**
	 * 获取USDT存取款汇率
	 * @param stationId
	 * @return
	 */
	ExchangeUSDTDto getUsdtRate(Long stationId);
}
