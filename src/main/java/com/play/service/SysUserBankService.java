package com.play.service;

import java.util.List;

import com.play.model.SysUserBank;
import com.play.model.vo.BankVo;
import com.play.orm.jdbc.page.Page;

/**
 * 用户银行卡信息表
 *
 * @author admin
 *
 */
public interface SysUserBankService {

	Page<SysUserBank> adminPage(Long stationId, String username, String cardNo, String degreeIds, String bankName,
			String realname, String bankAddress);

	void adminAddBank(SysUserBank bank);

	SysUserBank findOneById(Long id, Long stationId);

	void modifySave(SysUserBank bank);

	void modifyRealName(Long id, Long stationId, String realName);

	void changeStatus(Long id, Long stationId, Integer status);

	void delete(Long id, Long stationId);

	SysUserBank findOneByCardNo(String cardNo, Long stationId);

	SysUserBank getLastBankInfo(Long userId, Long stationId);

	List<SysUserBank> findByUserId(Long userId, Long stationId, Integer status, String type);

	void save(BankVo vo);

	List<SysUserBank> getBanksForUserCenter(Long userId, Long stationId);

	void delUserBank(SysUserBank sysUserBank);

    void delUSDTTimeout(List<Long> userBankIds);

	void updateUSDTInitBind(Long userId, Long stationId, String cardNo);

	List<SysUserBank> getUSDTDisableList();
}
