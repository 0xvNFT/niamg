package com.play.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.play.common.Constants;
import com.play.core.BankInfo;
import com.play.core.TronLinkStatusEnum;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.play.common.utils.security.EncryptDataUtil;
import com.play.core.UserTypeEnum;
import com.play.model.AdminUser;
import com.play.model.SysUserBank;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.HidePartUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;
import org.springframework.util.CollectionUtils;

/**
 * 用户银行卡信息表
 *
 * @author admin
 *
 */
@Repository
public class SysUserBankDao extends JdbcRepository<SysUserBank> {

	public Page<SysUserBank> page(Long stationId, Long userId, String cardNo, String degreeIds, String bankName,
			String realname, String bankAddress) {
		StringBuilder sb = new StringBuilder("select a.id,a.user_id,a.username,a.card_no,a.real_name,a.bank_name,");
		sb.append("a.bank_address,a.status,a.remarks,a.create_time,c.name as degree_name from sys_user_bank a");
		sb.append(" left join sys_user b on a.user_id = b.id left join sys_user_degree c on b.degree_id = c.id");
		sb.append(" where a.station_id =:stationId ");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		if (userId != null) {
			sb.append(" and a.user_id=:userId");
			params.put("userId", userId);
		}
		if (StringUtils.isNotEmpty(cardNo)) {
			sb.append(" and a.card_no =:cardNo");
			params.put("cardNo", EncryptDataUtil.encryptData(cardNo));
		}
		if (StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" AND b.degree_id in(");
			for (String lId : degreeIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (StringUtils.isNotEmpty(bankName)) {
			sb.append(" and a.bank_name like :bankName");
			params.put("bankName", "%" + bankName + "%");
		}
		if (StringUtils.isNotEmpty(bankAddress)) {
			sb.append(" and a.bank_address like :bankAddress");
			params.put("bankAddress", "%" + bankAddress + "%");
		}
		if (StringUtils.isNotEmpty(realname)) {
			sb.append(" and a.real_name =:realname");
			params.put("realname", EncryptDataUtil.encryptData(realname));
		}
		sb.append(" order by a.id desc");
		Page<SysUserBank> page = queryByPage(sb.toString(), params);
		if (page.hasRows()) {
			boolean isAdmin = false;// 站点管理员，不是总控或者合作商管理员
			AdminUser adminUser = LoginAdminUtil.currentUser();
			if (adminUser != null) {
				isAdmin = (SystemUtil.getUserType() == UserTypeEnum.ADMIN);
			}
			for (SysUserBank b : page.getRows()) {
				b.setCardNo(EncryptDataUtil.decryptData(b.getCardNo()));
				b.setRealName(EncryptDataUtil.decryptData(b.getRealName()));

				BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(b.getDegreeName());
				if (baseI18nCode != null) {
					b.setDegreeName(I18nTool.getMessage(baseI18nCode));
				}
				if (!isAdmin) {
					b.setCardNo(HidePartUtil.removePart(b.getCardNo()));
				}
			}
		}
		return page;
	}

	public boolean exist(Long userId, Long stationId, String cardNo) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(*) from sys_user_bank where 1=1 ");
		Map<String, Object> params = new HashMap<>();
		if (userId != null) {
			sb.append(" and user_id =:userId ");
			params.put("userId", userId);
		}
		if (stationId != null) {
			sb.append(" and station_id =:stationId ");
			params.put("stationId", stationId);
		}
		if (StringUtils.isNotEmpty(cardNo)) {
			sb.append(" and card_no =:cardNo ");
			params.put("cardNo", EncryptDataUtil.encryptData(cardNo));
		}
		return count(sb.toString(), params) > 0;
	}

	@Override
	public SysUserBank findOne(Long id, Long stationId) {
		SysUserBank b = super.findOne(id, stationId);
		if (b != null) {
			b.setCardNo(EncryptDataUtil.decryptData(b.getCardNo()));
			b.setRealName(EncryptDataUtil.decryptData(b.getRealName()));
		}
		return b;
	}

	@Override
	public SysUserBank save(SysUserBank t) {
		t.setCardNo(EncryptDataUtil.encryptData(t.getCardNo()));
		t.setRealName(EncryptDataUtil.encryptData(t.getRealName()));
		return super.save(t);
	}

	@Override
	public int update(SysUserBank t) {
		t.setCardNo(EncryptDataUtil.encryptData(t.getCardNo()));
		t.setRealName(EncryptDataUtil.encryptData(t.getRealName()));
		return super.update(t);
	}

	/**
	 * 根据用户ID获取用户银行卡列表
	 */
	public List<SysUserBank> getListByAccountId(Long userId, Long stationId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select id,real_name,bank_name,card_no,remarks,status");
		sb.append(",create_time,bank_code,bank_address from sys_user_bank where 1=1 ");
		Map<String, Object> params = new HashMap<>();
		if (userId != null) {
			sb.append(" and user_id =:userId ");
			params.put("userId", userId);
		}
		if (stationId != null) {
			sb.append(" and station_id =:stationId ");
			params.put("stationId", stationId);
		}
		sb.append(" order By id DESC");
		List<SysUserBank> list = find(sb.toString(), params);
		if (list != null && !list.isEmpty()) {
			for (SysUserBank b : list) {
				b.setCardNo(EncryptDataUtil.decryptData(b.getCardNo()));
				b.setRealName(EncryptDataUtil.decryptData(b.getRealName()));
			}
		}
		return list;
	}

	/**
	 * 获取用户最新绑定的银行卡信息
	 */
	public SysUserBank getLastBankInfo(Long userId, Long stationId, String cardNo) {
		StringBuilder sb = new StringBuilder("select * from sys_user_bank where 1=1 ");
		Map<String, Object> params = new HashMap<>();
		if (userId != null) {
			sb.append(" and user_id =:userId ");
			params.put("userId", userId);
		}
		if (stationId != null) {
			sb.append(" and station_id =:stationId ");
			params.put("stationId", stationId);
		}
		if (StringUtils.isNotEmpty(cardNo)) {
			sb.append(" and card_no =:cardNo ");
			params.put("cardNo", EncryptDataUtil.encryptData(cardNo));
		}
		sb.append(" order By create_time DESC limit 1");
		SysUserBank b = findOne(sb.toString(), params);
		if (b != null) {
			b.setCardNo(EncryptDataUtil.decryptData(b.getCardNo()));
			b.setRealName(EncryptDataUtil.decryptData(b.getRealName()));
		}
		return b;
	}

	public void updateRealName(Long id, String realName) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("realName", EncryptDataUtil.decryptData(realName));
		update("update sys_user_bank set real_name=:realName where id=:id", params);
	}

	public void updateStatus(Long id, Integer status, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", status);
		map.put("stationId", stationId);
		map.put("id", id);
		update("UPDATE sys_user_bank SET status=:status WHERE id =:id and station_id=:stationId", map);
	}

	public List<SysUserBank> findByUserId(Long userId, Long stationId, Integer status, String type) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("stationId", stationId);
		StringBuilder sql = new StringBuilder("select * from sys_user_bank where");
		sql.append(" user_id=:userId and station_id=:stationId");
		if (status != null) {
			params.put("status", status);
			sql.append(" and status=:status");
		}
		if (StringUtils.isNotBlank(type)) {
			if (BankInfo.USDT.getBankName().equalsIgnoreCase(type)) {
				sql.append(" and (bank_name = :type or bank_code = :type)");
			} else {
				sql.append(" and (bank_name <> :type and bank_code <> :type)");
			}
			params.put("type", BankInfo.USDT.getBankName());
		}
		List<SysUserBank> list = find(sql.toString(), params);
		if (list != null && !list.isEmpty()) {
			for (SysUserBank b : list) {
				b.setRemarks(I18nTool.convertArrStrToMessage(b.getRemarks()));
				b.setCardNo(EncryptDataUtil.decryptData(b.getCardNo()));
				b.setRealName(EncryptDataUtil.decryptData(b.getRealName()));
			}
		}
		return list;
	}

	public List<SysUserBank> getBanksForUserCenter(Long userId, Long stationId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("stationId", stationId);
		StringBuilder sb = new StringBuilder("select id,card_no,real_name,bank_code,");
		sb.append("bank_address,bank_name from sys_user_bank where user_id=:userId");
		sb.append(" and station_id=:stationId and status=2");
		List<SysUserBank> list = find(sb.toString(), params);
		if (list != null && !list.isEmpty()) {
			for (SysUserBank b : list) {
				b.setCardNo(EncryptDataUtil.decryptData(b.getCardNo()));
				b.setRealName(EncryptDataUtil.decryptData(b.getRealName()));
			}
		}
		return list;
	}

	public void delUSDTTimeout(List<Long> userBankIds) {
		if (CollectionUtils.isEmpty(userBankIds)) {
			return;
		}
		StringBuilder sb = new StringBuilder("delete from sys_user_bank where id in (");
		for (Long ids : userBankIds) {
			sb.append(ids).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");

		update(sb.toString());
	}

	public void updateUSDTInitBind(Long userId, Long stationId, String cardNo) {
		StringBuilder sb = new StringBuilder("update sys_user_bank set status = :status ");
		Map<String, Object> params = new HashMap<>();
		params.put("status", TronLinkStatusEnum.bind.getType());

		sb.append(" where user_id = :userId");
		params.put("userId", userId);

		sb.append(" and station_id = :stationId");
		params.put("stationId", stationId);

		// 当前业务逻辑：一个会员只能绑定一个Tron链，故该条件可不用加
//		sb.append(" and card_no = :cardNo");
//		params.put("cardNo", EncryptDataUtil.encryptData(cardNo));

		sb.append(" and bank_name = :bankName");
		params.put("bankName", BankInfo.USDT.getBankName());

		update(sb.toString(), params);
	}

	public List<SysUserBank> getUSDTDisableList() {
		StringBuilder sql = new StringBuilder("select * from sys_user_bank where status = :status and (bank_name = :bankName or bank_code = :bankCode)");
		Map<String, Object> params = new HashMap<>();
		params.put("status", Constants.STATUS_DISABLE);
		params.put("bankName", BankInfo.USDT.getBankName());
		params.put("bankCode", BankInfo.USDT.getBankCode());
		return find(sql.toString(), params);
	}
}
