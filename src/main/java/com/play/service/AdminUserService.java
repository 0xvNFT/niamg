package com.play.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.play.model.AdminUser;
import com.play.model.vo.AdminUserVo;
import com.play.orm.jdbc.page.Page;

/**
 * 站点管理员信息
 *
 * @author admin
 *
 */
public interface AdminUserService {
	void saveDefaultAdminUser(Long stationId, Long partnerId, String username, String password, Long groupId,
			Integer type);

	void save(AdminUser user);

	Page<AdminUserVo> pageForManager(Long stationId, Long groupId, String username, Integer type);

	Page<AdminUserVo> pageForPartner(Long partnerId, Long stationId, Long groupId, String username, Integer type);

	Page<AdminUserVo> adminPage(Long stationId, Integer minType, String username, Long groupId, Boolean onlineStatus);

	void changeStatus(Long id, Integer status, Long stationId);

	AdminUser findById(Long id, Long stationId);

	List<AdminUser> findByStationId(Long stationId);

	void update(AdminUser user);

	void delete(Long id, Long stationId);

	void updatePwd(Long id, Long stationId, String pwd, String rpwd);

	void doLogin(String username, String password, String verifyCode, Long stationId);

	void updpwd(Long id, String opwd, String pwd, String rpwd, Long stationId);

	void changeAddMoneyLimit(Long id, Long stationId, BigDecimal moneyLimit);

	void resetReceiptPwd(Long id, Long stationId, String pwd);

	void changeDepositLimit(Long id, Long stationId, BigDecimal depositLimit);

	void changeWithdrawLimit(Long id, Long stationId, BigDecimal withdrawLimit);

	Map<String, List<String>> getNormalUsernamesByGroup(Long stationId);

}