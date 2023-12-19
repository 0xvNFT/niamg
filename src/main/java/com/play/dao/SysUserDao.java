package com.play.dao;

import java.math.BigDecimal;
import java.util.*;

import com.play.core.CheatAnalyzeEnum;
import com.play.orm.jdbc.support.Aggregation;
import com.play.web.var.GuestTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.common.utils.security.EncryptDataUtil;
import com.play.core.UserTypeEnum;
import com.play.model.SysUser;
import com.play.model.SysUserLogin;
import com.play.model.so.UserSo;
import com.play.model.vo.StationDailyRegisterVo;
import com.play.model.vo.UserVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 会员账号信息
 *
 * @author admin
 *
 */
@Repository
public class SysUserDao extends JdbcRepository<SysUser> {

	public SysUser findOneByUsername(String username, Long stationId, Integer type) {
		if (StringUtils.isEmpty(username) || stationId == null) {
			return null;
		}
		username = StringUtils.lowerCase(StringUtils.trim(username));
		if (StringUtils.isEmpty(username)) {
			return null;
		}
		Long id = CacheUtil.getCache(CacheKey.USER_INFO, getCacheKey(username, stationId, type), Long.class);
		if (id != null) {
			return findOneById(id, stationId);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("stationId", stationId);
		StringBuilder sql = new StringBuilder("select * from sys_user");
		sql.append(" where username=:username and station_id=:stationId");
		if (type != null) {
			map.put("type", type);
			sql.append(" and type=:type");
		}
		SysUser u = findOne(sql.toString(), map);
		addCache(u);
		return u;
	}

	public SysUser findOneById(Long id, Long stationId) {
		SysUser u = CacheUtil.getCache(CacheKey.USER_INFO, getCacheKey(id, stationId), SysUser.class);
		if (u == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("stationId", stationId);
			u = findOne("select * from sys_user where id=:id and station_id=:stationId", map);
			addCache(u);
		}
		return u;
	}

	private String getCacheKey(Long id, Long stationId) {
		return new StringBuffer("uid:").append(id).append(":").append(stationId).toString();
	}

	private String getCacheKey(String username, Long stationId, Integer type) {
		return new StringBuffer("u:username:").append(username).append(":").append(stationId).append(":").append(type)
				.toString();
	}

	private void addCache(SysUser u) {
		if (u != null) {
			CacheUtil.addCache(CacheKey.USER_INFO, getCacheKey(u.getId(), u.getStationId()), u);
			CacheUtil.addCache(CacheKey.USER_INFO, getCacheKey(u.getUsername(), u.getStationId(), u.getType()),
					u.getId());
		}
	}

	/**
	 * 修改会员等级
	 * 
	 * @param userId
	 * @param stationId
	 * @param degreeId
	 * @param lockDegree
	 */
	public void changeDegree(Long userId, Long stationId, Long degreeId, Integer lockDegree) {
		StringBuilder sb = new StringBuilder("update sys_user set degree_id =:degreeId");
		sb.append(",lock_degree=:lockDegree where station_id =:stationId and id =:userId");
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("stationId", stationId);
		params.put("degreeId", degreeId);
		params.put("lockDegree", lockDegree);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}

	public int getLowestLevelIndex(Long stationId, Long proxyId) {
		StringBuilder sb = new StringBuilder("select max(level) from sys_user where station_id=:stationId");
		sb.append(" and level is not null and (type != :guest and type != :guestBack)");
		Map<String, Object> params = new HashMap<>();
		if (proxyId != null) {
			sb.append(" and parent_ids like :proxyIds");
			params.put("proxyIds", "%," + proxyId + ",%");
		}
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		params.put("stationId", stationId);
		Integer i = findObject(sb.toString(), params, Integer.class);
		if (i == null) {
			return 0;
		}
		return i;
	}

	public Page<UserVo> pageForAdmin(UserSo so, boolean viewContact) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("SELECT a.id,a.username,a.create_datetime,a.status,a.type,a.register_os,");
		sql.append("a.remark,a.agent_name,a.money_status,a.degree_id,a.group_id,a.create_username,");
		sql.append("a.create_user_id,a.lock_degree,a.level,a.online_warn,a.update_pwd_datetime,");
		sql.append("a.proxy_id,a.proxy_name,a.recommend_id,a.recommend_username,a.register_ip,");
		sql.append("m.money,l.last_login_datetime,l.online_status,l.last_login_ip,l.terminal,");
		if (viewContact) {
			sql.append("i.real_name,i.phone,i.email,i.qq,i.wechat,i.facebook,");
		}
		sql.append("s.score,(num.proxy_num + num.member_num) as under_num");
		sql.append(" FROM sys_user a LEFT JOIN sys_user_money m ON a.id = m.user_id");
		sql.append(" LEFT JOIN sys_user_login l ON a.id = l.user_id");
		sql.append(" LEFT JOIN sys_user_info i ON a.id = i.user_id");
		sql.append(" LEFT JOIN sys_user_score s ON a.id = s.user_id");
		sql.append(" left join sys_user_proxy_num num on num.user_id =a.id");
		sql.append(" WHERE a.station_id = :stationId");
		params.put("stationId", so.getStationId());
		// 试玩账号不显示
		if (so.getUserType() != null) {
			if(GuestTool.isGuest(so.getUserType())) {
				// 只要为试玩类型，都查前后台试玩账户
				sql.append(" AND (a.type = :guest OR a.type = :guestBack) ");
				params.put("guest", UserTypeEnum.GUEST.getType());
				params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
			} else {
				sql.append(" AND a.type =:userType");
				params.put("userType", so.getUserType());
			}
		} else {
			sql.append(" AND (a.type != :guest AND a.type != :guestBack) ");
			params.put("guest", UserTypeEnum.GUEST.getType());
			params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		}
		if (so.getBegin() != null) {
			sql.append(" AND a.create_datetime >=:startTime");
			params.put("startTime", so.getBegin());
		}
		if (so.getEnd() != null) {
			sql.append(" AND a.create_datetime <:endTime");
			params.put("endTime", so.getEnd());
		}
		if (StringUtils.isNotEmpty(so.getUsername())) {
			String username = so.getUsername().trim().toLowerCase();
			if (username.contains(",")) {
				sql.append(" AND a.username in (");
				for (String ss : username.split(",")) {
					ss = ss.trim();
					sql.append(":u").append(ss).append(",");
					params.put("u" + ss, ss);
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(")");
			} else {
				sql.append(" AND a.username=:username");
				params.put("username", username);
			}
		}
		if (StringUtils.isNotEmpty(so.getUsernameLike())) {
			sql.append(" AND a.username like :username");
			params.put("username", "%" + so.getUsernameLike().trim().toLowerCase() + "%");
		}
		if (StringUtils.isNotEmpty(so.getProxyName())) {
			sql.append(" AND a.parent_names like :proxyName");
			params.put("proxyName", "%," + so.getProxyName().trim().toLowerCase() + ",%");
		}
		if (StringUtils.isNotEmpty(so.getAgentName())) {
			sql.append(" AND a.agent_name=:agentName");
			params.put("agentName", so.getAgentName().trim().toLowerCase());
		}
		if(StringUtils.isNotEmpty(so.getRecommendName())) {
			sql.append(" AND a.recommend_username=:recommend");
			params.put("recommend", so.getRecommendName().trim().toLowerCase());
		}
		if (StringUtils.isNotEmpty(so.getDegreeIds())) {
			sql.append(" AND a.degree_id in(");
			for (String lId : so.getDegreeIds().split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sql.append(id).append(",");
				}
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}
		if (StringUtils.isNotEmpty(so.getGroupIds())) {
			sql.append(" AND a.group_id in(");
			for (String lId : so.getGroupIds().split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sql.append(id).append(",");
				}
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}
		if (so.getDepositStatus() != null) {
			switch (so.getDepositStatus()) {
			case UserSo.DEPOSIT_STATUS_YES:
				sql.append(" AND (select count(*) from sys_user_deposit");
				sql.append(" where total_money>0 and user_id=a.id)>0");
				break;
			case UserSo.DEPOSIT_STATUS_NO:
				sql.append(" AND (select count(*) from sys_user_deposit");
				sql.append(" where total_money=0 and user_id=a.id)>0");
				break;
			case UserSo.DEPOSIT_STATUS_FIRST:
				sql.append(" AND (select account_id from sys_user_deposit where first_time ");
				sql.append("notnull and sec_time isnull and user_id=a.id)>0");
				break;
			}
		}
		if (so.getDrawStatus() != null) {
			switch (so.getDrawStatus()) {
			case UserSo.DRAW_STATUS_YES:
				sql.append(" AND (select count(*) from sys_user_withdraw where");
				sql.append(" user_id=a.id and total_money>0)>0");
				break;
			case UserSo.DRAW_STATUS_NO:
				sql.append(" AND (select count(*) from sys_user_withdraw where");
				sql.append(" user_id=a.id and total_money=0)>0");
				break;
			}
		}
		if (so.getMinMoney() != null) {
			sql.append(" AND m.money >:money");
			params.put("money", so.getMinMoney());
		}
		if (so.getLastLoginTime() != null) {
			sql.append(" AND l.last_login_datetime <=:lastLoginTime");
			params.put("lastLoginTime", so.getLastLoginTime());
		}
		if (StringUtils.isNotEmpty(so.getKeyword())) {
			keyWordHandle(sql, so.getKeywordType(), so.getKeyword(), params);
		}
		if (StringUtils.isNotEmpty(so.getProxyPromoCode())) {
			sql.append(" and a.promo_code = :promoCode");
			params.put("promoCode", so.getProxyPromoCode());
		}
		if (so.getLevel() != null) {
			sql.append(" and a.level =:level");
			params.put("level", so.getLevel());
		}
		if (StringUtils.isNotEmpty(so.getLastLoginIp())) {
			sql.append(" AND l.last_login_ip =:lastLoginIp");
			params.put("lastLoginIp", so.getLastLoginIp());
		}
		if (!StringUtils.equalsAnyIgnoreCase("asc", so.getSortOrder())) {
			so.setSortOrder("desc");
		}
		if (StringUtils.equals(so.getSortName(), "remark")) {
			sql.append(" ORDER BY CHAR_LENGTH(COALESCE(a.remark,'')) ").append(so.getSortOrder());
		} else if (StringUtils.equals(so.getSortName(), "onlineStatus")) {
			sql.append(" order by l.online_status ").append(so.getSortOrder()).append(",a.online_warn desc,a.id desc");
		} else if (StringUtils.equals(so.getSortName(), "type") && StringUtils.isNotEmpty(so.getSortOrder())) {
			if (StringUtils.equals(so.getSortOrder(), "desc")) {
				sql.append(" ORDER BY under_num desc nulls last");
			} else {
				sql.append(" ORDER BY under_num asc nulls first");
			}
		} else if (so.getKeywordType() != null && so.getKeywordType() == UserSo.KEYWORD_REGISTER_IP) {
			// 关键字属性是注册IP时，按注册IP排序
			sql.append(" ORDER BY a.register_ip DESC");
		} else {
			sql.append(" ORDER BY a.id DESC");
		}
		Page<UserVo> page = queryByPage(sql.toString(), new BeanPropertyRowMapper<UserVo>(UserVo.class), params);
		if (page.hasRows() && viewContact) {
			for (UserVo a : page.getRows()) {
				a.setRealName(EncryptDataUtil.decryptData(a.getRealName()));
				a.setQq(EncryptDataUtil.decryptData(a.getQq()));
				a.setWechat(EncryptDataUtil.decryptData(a.getWechat()));
				a.setEmail(EncryptDataUtil.decryptData(a.getEmail()));
				a.setFacebook(EncryptDataUtil.decryptData(a.getFacebook()));
				a.setPhone(EncryptDataUtil.decryptData(a.getPhone()));
			}
		}
		return page;
	}
	public String getSysUserSumMoney(UserSo so){
        SysUser sumMoney =  this.findOne("SELECT sum(m.money) as proxyName FROM sys_user a LEFT JOIN sys_user_money m ON a.id = m.user_id " );
		return sumMoney.getProxyName();
	}

	private void keyWordHandle(StringBuilder sql, Integer keywordType, String keyword, Map<String, Object> params) {
		if (keywordType.equals(UserSo.KEYWORD_REAL_NAME)) {
			params.put("keyword", EncryptDataUtil.encryptData(keyword));
			sql.append(" AND i.real_name = :keyword");
		} else if (keywordType.equals(UserSo.KEYWORD_PHONE)) {
			params.put("keyword", EncryptDataUtil.encryptData(keyword));
			sql.append(" AND i.phone=:keyword");
		} else if (keywordType.equals(UserSo.KEYWORD_QQ)) {
			params.put("keyword", EncryptDataUtil.encryptData(keyword));
			sql.append(" AND i.qq=:keyword");
		} else if (keywordType.equals(UserSo.KEYWORD_MAIL)) {
			params.put("keyword", EncryptDataUtil.encryptData(keyword));
			sql.append(" AND i.email=:keyword");
		} else if (keywordType.equals(UserSo.KEYWORD_WEICHAT)) {
			params.put("keyword", EncryptDataUtil.encryptData(keyword));
			sql.append(" AND i.wechat=:keyword");
		} else if (keywordType.equals(UserSo.KEYWORD_FACEBOOK)) {
			params.put("keyword", EncryptDataUtil.encryptData(keyword));
			sql.append(" AND i.facebook=:keyword");
		} else if (keywordType.equals(UserSo.KEYWORD_REGISTER_IP)) {
			params.put("keyword", "%" + keyword + "%");
			sql.append(" AND a.register_ip LIKE :keyword");
		} else if (keywordType.equals(UserSo.KEYWORD_REGISTER_OS)) {
			params.put("keyword", "%" + keyword + "%");
			sql.append(" AND a.register_url LIKE :keyword");
		} else if (keywordType.equals(UserSo.KEYWORD_REMARK)) {
			params.put("keyword", "%" + keyword + "%");
			sql.append(" AND a.remark LIKE :keyword");
		}
	}

	public boolean exist(String username, Long stationId, Long escapeId) {
		StringBuilder sql = new StringBuilder("select count(*) from sys_user ");
		sql.append(" where username=:username and station_id=:stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("stationId", stationId);
		if (escapeId != null) {
			map.put("escapeId", escapeId);
			sql.append(" and id!=:escapeId");
		}
		return queryForLong(sql.toString(), map) > 0;
	}

	public boolean existEmail(String email, Long stationId, Long escapeId) {
		StringBuilder sql = new StringBuilder("select count(*) from sys_user ");
		sql.append(" where station_id=:stationId");
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotEmpty(email)) {
			sql.append(" and email=:email ");
			map.put("email", email);
		}
		map.put("stationId", stationId);
		if (escapeId != null) {
			map.put("escapeId", escapeId);
			sql.append(" and id!=:escapeId");
		}
		return queryForLong(sql.toString(), map) > 0;
	}

	public void changeLoginPwd(Long id, Long stationId, String newPwdMd5, String newSalt) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append("update sys_user set password =:newPwdMd5,salt =:newSalt");
		sb.append(",update_pwd_datetime=:updatePwdDatetime where station_id =:stationId");
		sb.append(" and id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("newPwdMd5", newPwdMd5);
		params.put("newSalt", newSalt);
		params.put("updatePwdDatetime", new Date());
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(id, stationId));
		SysUser user = findOneById(id, stationId);
		if (user != null) {
			CacheUtil.delCache(CacheKey.DEFAULT, EncryptDataUtil.getAutoLoginKey(user.getUsername(), stationId));
		}
	}

	public void updateChildrenProxy(String oldParentIds, String newParentIds, String oldParentName,
			String newParentName, Long userId) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append("update sys_user");
		if (StringUtils.isEmpty(oldParentIds)) {
			sb.append(" set parent_ids = :newProxyId||parent_ids");
			params.put("newProxyId", newParentIds.substring(0, newParentIds.length() - 1));
		} else if (StringUtils.isEmpty(newParentIds)) {
			sb.append(" set parent_ids = replace(parent_ids,:oldProxyId,',')");
			params.put("oldProxyId", oldParentIds);
		} else {
			sb.append(" set parent_ids = replace(parent_ids,:oldProxyId,:newProxyId)");
			params.put("oldProxyId", oldParentIds);
			params.put("newProxyId", newParentIds);
		}
		if (StringUtils.isEmpty(oldParentName)) {
			sb.append(" ,parent_names = :newProxyName||parent_names");
			sb.append(",level=(array_length(REGEXP_SPLIT_TO_ARRAY(:newProxyName||parent_names, ','), 1) - 1)");
			params.put("newProxyName", newParentName.substring(0, newParentName.length() - 1));
		} else if (StringUtils.isEmpty(newParentName)) {
			sb.append(" ,parent_names = replace(parent_names,:oldProxyName,',')");
			sb.append(",level=(array_length(REGEXP_SPLIT_TO_ARRAY");
			sb.append("(replace(parent_names,:oldProxyName,','), ','), 1) - 1)");
			params.put("oldProxyName", oldParentName);
		} else {
			sb.append(",parent_names = replace(parent_names,:oldProxyName,:newProxyName)");
			sb.append(",level=(array_length(REGEXP_SPLIT_TO_ARRAY");
			sb.append("(replace(parent_names,:oldProxyName,:newProxyName), ','), 1) - 1)");
			params.put("oldProxyName", oldParentName);
			params.put("newProxyName", newParentName);
		}
		sb.append(" where parent_ids like :userId");
		params.put("userId", "%," + userId + ",%");
		update(sb.toString(), params);
		CacheUtil.delCacheByPrefix(CacheKey.USER_INFO);
	}

	/**
	 * 修改会员代理
	 */
	public void changeProxy(SysUser user) {
		StringBuilder sb = new StringBuilder("update sys_user set proxy_id =:proxyId");
		sb.append(",parent_ids =:parentIds,parent_names=:parentNames,proxy_name=:proxyName,");
		sb.append("level=:level where station_id =:stationId and id =:id");
		Map<String, Object> params = new HashMap<>();
		params.put("id", user.getId());
		params.put("stationId", user.getStationId());
		params.put("proxyId", user.getProxyId());
		params.put("parentIds", user.getParentIds());
		params.put("parentNames", user.getParentNames());
		params.put("proxyName", user.getProxyName());
		params.put("level", user.getLevel());
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(user.getId(), user.getStationId()));
	}

	public void changeStatus(Long id, Long stationId, Integer status) {
		StringBuilder sb = new StringBuilder("update sys_user set status =:status");
		sb.append(" where station_id =:stationId and id =:id");
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("status", status);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(id, stationId));
	}

	public void modifyRemark(Long id, Long stationId, String remark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append("update sys_user set remark =:remark");
		sb.append(" where station_id =:stationId");
		sb.append(" and id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("remark", remark);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(id, stationId));
	}

	public void freezeMoney(Long id, Long stationId, Integer status) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" update sys_user set money_status =:status");
		sb.append(" where station_id =:stationId");
		sb.append(" and id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("status", status);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(id, stationId));
	}

	public List<UserVo> exportList(UserSo so) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append("SELECT a.username,a.type,a.proxy_name,a.remark,i.real_name,i.email");
		sql.append(",m.money,s.score,d.name as degree_name,a.parent_names,i.phone");
		sql.append(" FROM sys_user a LEFT JOIN sys_user_money m ON a.id = m.user_id");
		sql.append(" LEFT JOIN sys_user_info i ON a.id = i.user_id");
		sql.append(" LEFT JOIN sys_user_score s ON a.id = s.user_id");
		sql.append(" left join sys_user_degree d on d.id =a.degree_id");
		sql.append(" WHERE a.station_id = :stationId");
		params.put("stationId", so.getStationId());

		// 试玩账号不显示
		if (so.getUserType() != null) {
			if(GuestTool.isGuest(so.getUserType())) {
				// 只要为试玩类型，都查前后台试玩账户
				sql.append(" AND (a.type = :guest OR a.type = :guestBack) ");
				params.put("guest", UserTypeEnum.GUEST.getType());
				params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
			} else {
				sql.append(" AND a.type =:userType");
				params.put("userType", so.getUserType());
			}
		} else {
			sql.append(" AND (a.type != :guest AND a.type != :guestBack) ");
			params.put("guest", UserTypeEnum.GUEST.getType());
			params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		}

		if (so.getBegin() != null) {
			sql.append(" AND a.create_datetime >=:startTime");
			params.put("startTime", so.getBegin());
		}
		if (so.getEnd() != null) {
			sql.append(" AND a.create_datetime <:endTime");
			params.put("endTime", so.getEnd());
		}
		if (StringUtils.isNotEmpty(so.getUsername())) {
			String username = so.getUsername().trim().toLowerCase();
			if (username.contains(",")) {
				sql.append(" AND a.username in (");
				for (String ss : username.split(",")) {
					ss = ss.trim();
					sql.append(":u").append(ss).append(",");
					params.put("u" + ss, ss);
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(")");
			} else {
				sql.append(" AND a.username=:username");
				params.put("username", username);
			}
		}
		if (StringUtils.isNotEmpty(so.getUsernameLike())) {
			sql.append(" AND a.username like :username");
			params.put("username", "%" + so.getUsernameLike().trim().toLowerCase() + "%");
		}
		if (StringUtils.isNotEmpty(so.getProxyName())) {
			sql.append(" AND a.parent_names like :proxyName");
			params.put("proxyName", "%," + so.getProxyName().trim().toLowerCase() + ",%");
		}
		if (StringUtils.isNotEmpty(so.getAgentName())) {
			sql.append(" AND a.agent_name=:agentName");
			params.put("agentName", so.getAgentName().trim().toLowerCase());
		}
		if (StringUtils.isNotEmpty(so.getDegreeIds())) {
			sql.append(" AND a.degree_id in(");
			for (String lId : so.getDegreeIds().split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sql.append(id).append(",");
				}
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}
		if (StringUtils.isNotEmpty(so.getGroupIds())) {
			sql.append(" AND a.group_id in(");
			for (String lId : so.getGroupIds().split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sql.append(id).append(",");
				}
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}
		if (so.getDepositStatus() != null) {
			switch (so.getDepositStatus()) {
			case UserSo.DEPOSIT_STATUS_YES:
				sql.append(" AND (select count(*) from sys_user_deposit");
				sql.append(" where total_money>0 and user_id=a.id)>0");
				break;
			case UserSo.DEPOSIT_STATUS_NO:
				sql.append(" AND (select count(*) from sys_user_deposit");
				sql.append(" where total_money=0 and user_id=a.id)>0");
				break;
			case UserSo.DEPOSIT_STATUS_FIRST:
				sql.append(" AND (select account_id from sys_user_deposit where first_time ");
				sql.append("notnull and sec_time isnull and user_id=a.id)>0");
				break;
			}
		}
		if (so.getDrawStatus() != null) {
			switch (so.getDrawStatus()) {
			case UserSo.DRAW_STATUS_YES:
				sql.append(" AND (select count(*) from sys_user_withdraw where");
				sql.append(" user_id=a.id and total_money>0)>0");
				break;
			case UserSo.DRAW_STATUS_NO:
				sql.append(" AND (select count(*) from sys_user_withdraw where");
				sql.append(" user_id=a.id and total_money=0)>0");
				break;
			}
		}
		if (so.getMinMoney() != null) {
			sql.append(" AND m.money >:money");
			params.put("money", so.getMinMoney());
		}
		if (so.getLastLoginTime() != null) {
			sql.append(" AND l.last_login_datetime <=:lastLoginTime");
			params.put("lastLoginTime", so.getLastLoginTime());
		}
		if (StringUtils.isNotEmpty(so.getKeyword())) {
			keyWordHandle(sql, so.getKeywordType(), so.getKeyword(), params);
		}
		if (StringUtils.isNotEmpty(so.getProxyPromoCode())) {
			sql.append(" and a.promo_code = :promoCode");
			params.put("promoCode", so.getProxyPromoCode());
		}
		if (so.getLevel() != null) {
			sql.append(" and a.level =:level");
			params.put("level", so.getLevel());
		}
		if (StringUtils.isNotEmpty(so.getLastLoginIp())) {
			sql.append(" AND l.last_login_ip =:lastLoginIp");
			params.put("lastLoginIp", so.getLastLoginIp());
		}
		sql.append(" ORDER BY a.id DESC");
		List<UserVo> list = find(sql.toString(), new BeanPropertyRowMapper<UserVo>(UserVo.class), params);
		if (list != null && !list.isEmpty()) {
			for (UserVo a : list) {
				a.setRealName(EncryptDataUtil.decryptData(a.getRealName()));
			}
		}
		return list;
	}

	public void changeGroup(Long userId, Long stationId, Long groupId) {
		StringBuilder sb = new StringBuilder("update sys_user set group_id =:groupId");
		sb.append(" where station_id =:stationId and id =:userId");
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("stationId", stationId);
		params.put("groupId", groupId);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}

	public void changeGroupUser(Long stationId, Long beforeGroupId,Long groupId) {
		StringBuilder sb = new StringBuilder("update sys_user set group_id =:groupId");
		sb.append(" where station_id =:stationId and group_id =:beforeGroupId");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("groupId", groupId);
		update(sb.toString(), params);
		CacheUtil.delCacheByPrefix(CacheKey.USER_INFO);
	}

	public int countRegisterMemberByIp(Long stationId, Date begin, Date end, String ip, Integer type) {
		StringBuilder sb = new StringBuilder("select count(id) from sys_user where station_id =:stationId");
		sb.append(" and register_ip =:ip and register_ip is not null");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
//		params.put("begin", begin);
//		params.put("end", end);
		params.put("ip", ip);
		if (begin != null) {
			sb.append(" AND create_datetime >= :start ");
			params.put("start", begin);
		}
		if (end != null) {
			sb.append(" AND create_datetime <= :end ");
			params.put("end", end);
		}
		if (type != null) {
			if(GuestTool.isGuest(type)) {
				// 只要为试玩类型，都查前后台试玩账户
				sb.append(" AND (type = :guest OR type = :guestBack) ");
				params.put("guest", UserTypeEnum.GUEST.getType());
				params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
			} else {
				sb.append(" AND type =:userType");
				params.put("userType", type);
			}
		} else {
			sb.append(" AND (type != :guest AND type != :guestBack) ");
			params.put("guest", UserTypeEnum.GUEST.getType());
			params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		}
		return count(sb.toString(), params);
	}

	public int countRegisterMemberByOs(Long stationId, Date begin, Date end, String os, Integer type) {
		StringBuilder sb = new StringBuilder("select count(id) from sys_user where station_id =:stationId");
		sb.append(" and register_os =:os and register_os is not null");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
//		params.put("begin", begin);
//		params.put("end", end);
		params.put("os", os);
		if (begin != null) {
			sb.append(" AND create_datetime >= :start ");
			params.put("start", begin);
		}
		if (end != null) {
			sb.append(" AND create_datetime <= :end ");
			params.put("end", end);
		}
		if (type != null) {
			sb.append(" and type =:type");
			params.put("type", type);
		} else {
			sb.append(" and (type != :guest AND type != :guestBack)");
			params.put("guest", UserTypeEnum.GUEST.getType());
			params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		}
		if (type != null) {
			if(GuestTool.isGuest(type)) {
				// 只要为试玩类型，都查前后台试玩账户
				sb.append(" AND (type = :guest OR type = :guestBack) ");
				params.put("guest", UserTypeEnum.GUEST.getType());
				params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
			} else {
				sb.append(" AND type =:userType");
				params.put("userType", type);
			}
		} else {
			sb.append(" AND (type != :guest AND type != :guestBack) ");
			params.put("guest", UserTypeEnum.GUEST.getType());
			params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		}
		return count(sb.toString(), params);
	}

	public Page<UserVo> getSubordinatePage(Long stationId, Long userId, BigDecimal maxBalance, BigDecimal minBalance,
			Date start, Date end, BigDecimal depositTotal, Boolean include, boolean isMember, Integer level) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("SELECT a.id,a.username,a.create_datetime,a.status,a.type,");
		sb.append("a.degree_id,a.level,m.money,l.last_login_datetime,l.online_status,");
		sb.append("r.live,r.egame,r.chess,r.sport,r.esport,r.fishing,r.lottery");
		sb.append(" FROM sys_user a LEFT JOIN sys_user_money m ON a.id = m.user_id");
		sb.append(" LEFT JOIN sys_user_login l ON a.id = l.user_id");
		sb.append(" LEFT JOIN sys_user_rebate r ON a.id = r.user_id");
		sb.append(" WHERE a.station_id = :stationId");
		params.put("stationId", stationId);
		params.put("userId", userId);
		if (include != null && include) {
			if (isMember) {
				sb.append(" and (a.id=:userId OR a.recommend_id=:userId)");
			} else {
				sb.append(" and (a.id=:userId OR a.parent_ids like :userIdCn )");
				params.put("userIdCn", "%," + userId + ",%");
			}
		} else {
			sb.append(" and a.id=:userId");
		}

		if (maxBalance != null) {
			sb.append(" AND m.money <= :maxBalance ");
			params.put("maxBalance", maxBalance);
		}
		if (minBalance != null) {
			sb.append(" AND m.money >= :minBalance ");
			params.put("minBalance", minBalance);
		}
		if (start != null) {
			sb.append(" AND l.last_login_datetime >= :start ");
			params.put("start", start);
		}
		if (end != null) {
			sb.append(" AND l.last_login_datetime <= :end ");
			params.put("end", end);
		}
		if (depositTotal != null) {
			sb.append(" AND (select count(*) from sys_user_deposit");
			sb.append(" where total_money>:depositTotal and user_id=a.id)>0");
			params.put("depositTotal", depositTotal);
		}
		if (level != null) {
			sb.append(" and a.level = :level");
			params.put("level", level);
		}
		sb.append(" ORDER BY l.online_status desc,l.last_login_datetime IS NULL, l.last_login_datetime DESC,a.id desc");
		return queryByPage(sb.toString(), new BeanPropertyRowMapper<UserVo>(UserVo.class), params);
	}

	public Page<UserVo> getUserTeamPage(Long stationId, Long userId, Date start, Date end, Integer type,
			Boolean include, boolean isMember) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("SELECT a.id,a.username,a.create_datetime,a.status,a.type,");
		sb.append("a.degree_id,a.level,m.money,l.last_login_datetime,l.online_status");
		sb.append(" FROM sys_user a LEFT JOIN sys_user_money m ON a.id = m.user_id");
		sb.append(" LEFT JOIN sys_user_login l ON a.id = l.user_id");
		sb.append(" WHERE a.station_id = :stationId");
		params.put("stationId", stationId);
		params.put("userId", userId);
		if (include != null && include) {
			if (isMember) {
				sb.append(" and (a.id=:userId OR a.recommend_id=:userId)");
			} else {
				sb.append(" and (a.id=:userId OR a.parent_ids like :userIdCn )");
				params.put("userIdCn", "%," + userId + ",%");
			}
		} else {
			sb.append(" and a.id=:userId");
		}
		if (type == 3) {// 团队总数
		} else if (type == 4) {// 新增会员人数
			if (start != null) {
				sb.append(" and a.create_datetime >= :begin");
				params.put("begin", start);
			}
			if (end != null) {
				sb.append(" and a.create_datetime <= :end");
				params.put("end", end);
			}
		} else if (type == 5) {// 当前在线
			sb.append(" and l.online_status = :onlineStatus");
			params.put("onlineStatus", SysUserLogin.STATUS_ONLINE_ON);
		} else if (type == 6) {// 三天未登录
			sb.append(" and l.online_status=:onlineStatus");
			params.put("onlineStatus", SysUserLogin.STATUS_ONLINE_OFF);
			sb.append(" and (l.last_login_datetime is null or l.last_login_datetime <= :lastLoginTime)");
			params.put("lastLoginTime", DateUtils.addDays(new Date(), -3));
		} else if (type == 8) {// 团队首充
			if (start != null && end != null) {
				sb.append(" AND (select count(*) from sys_user_deposit");
				sb.append(" where first_time>=:start and first_time<=:end and user_id=a.id)>0");
				params.put("start", start);
				params.put("end", end);
			} else {
				if (start != null) {
					sb.append(" AND (select count(*) from sys_user_deposit");
					sb.append(" where first_time>=:start and user_id=a.id)>0");
					params.put("start", start);
				}
				if (end != null) {
					sb.append(" AND (select count(*) from sys_user_deposit");
					sb.append(" where first_time<=:end and user_id=a.id)>0");
					params.put("end", end);
				}
			}
		}
		sb.append(" ORDER BY l.online_status desc,l.last_login_datetime IS NULL,l.last_login_datetime DESC,a.id desc");
		return queryByPage(sb.toString(), new BeanPropertyRowMapper<UserVo>(UserVo.class), params);
	}

	/**
	 * 统计团队人数
	 */
	public Integer countTeamNum(Long stationId, Long userId, Date begin, Date end, Integer type, boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select count(*) from sys_user where station_id =:stationId");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		if (userId != null) {
			params.put("userId", userId);
			if (isRecommend) {// 会员推荐
				sb.append(" and (id=:userId or recommend_id=:userId)");
			} else {
				sb.append(" and (id=:userId or parent_ids like :userIdCn)");
				params.put("userIdCn", "%," + userId + ",%");
			}
		}
		// 试玩不统计
		if (type != null) {
			if(GuestTool.isGuest(type)) {
				// 只要为试玩类型，都查前后台试玩账户
				sb.append(" AND (type = :guest OR type = :guestBack) ");
				params.put("guest", UserTypeEnum.GUEST.getType());
				params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
			} else {
				sb.append(" AND type =:userType");
				params.put("userType", type);
			}
		} else {
			sb.append(" AND (type != :guest AND type != :guestBack) ");
			params.put("guest", UserTypeEnum.GUEST.getType());
			params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		}

		if (begin != null) {
			sb.append(" and create_datetime >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and create_datetime <= :end");
			params.put("end", end);
		}
		return queryForInt(sb.toString(), params);
	}

	/**
	 * 统计某会员推荐的所有会员数
	 * @param userId 推荐人/上级代理userId
	 */
	public List<SysUser> findRecommendUsers(Long stationId, Long userId, Date begin, Date end, Integer type, boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select * from sys_user where station_id =:stationId");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		if (userId != null) {
			if (isRecommend) {// 会员推荐
				sb.append(" and recommend_id=:userId");
			} else {//代理
				sb.append(" and parent_ids like :userIdCn");
				params.put("userIdCn", "%," + userId + ",%");
			}
		}
		// 试玩不统计
		if (type != null) {
			if(GuestTool.isGuest(type)) {
				// 只要为试玩类型，都查前后台试玩账户
				sb.append(" AND (type = :guest OR type = :guestBack) ");
				params.put("guest", UserTypeEnum.GUEST.getType());
				params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
			} else {
				sb.append(" AND type =:userType");
				params.put("userType", type);
			}
		} else {
			sb.append(" AND (type != :guest AND type != :guestBack) ");
			params.put("guest", UserTypeEnum.GUEST.getType());
			params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		}
		if (begin != null) {
			sb.append(" and create_datetime >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and create_datetime <= :end");
			params.put("end", end);
		}
		return find(sb.toString(), params);
	}

	/**
	 * 统计人数
	 * params
	 * effective 是否有效会员
	 */
	public Integer countEffectiveNum(Long stationId, boolean effective) {
		StringBuilder sb = new StringBuilder("select count(*) from sys_user where station_id =:stationId and status = 2");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		if (effective) {
			sb.append(" and (type != :guest and type != :guestBack)");
			params.put("guest", UserTypeEnum.GUEST.getType());
			params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		}
		return queryForInt(sb.toString(), params);
	}

	public Page<StationDailyRegisterVo> getPageByProxy(Long stationId, Date begin, Date end, Long proxyId,
			String agentName, boolean isRecommend) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		sb.append("select aa.stat_date,aa.proxy_num,aa.member_num,");
		sb.append("aa.register_num,aa.deposit_money,aa.deposit from (");
		sb.append("select to_char(a.create_datetime, 'yyyy-MM-dd') as stat_date,");
		sb.append("COUNT(DISTINCT CASE WHEN a.type=120 THEN a.username ELSE NULL END) as proxy_num,");
		sb.append("COUNT(DISTINCT CASE WHEN a.type=130 THEN a.username ELSE NULL END) as member_num,");

		sb.append("(COUNT(DISTINCT CASE WHEN a.type=120 THEN a.username ELSE NULL END)");
		sb.append("+COUNT(DISTINCT CASE WHEN a.type=130 THEN a.username ELSE NULL END)) as register_num,");

		sb.append("(sum(case when al.first_money is not null and to_char(a.create_datetime, 'yyyy-MM-dd')");
		sb.append("=to_char(al.first_time, 'yyyy-MM-dd') then al.first_money else 0 end)+");
		sb.append("sum(case when al.sec_money is not null and to_char(a.create_datetime, 'yyyy-MM-dd')=");
		sb.append("to_char(al.sec_time, 'yyyy-MM-dd') then al.sec_money else 0 end)) as deposit_money,");

		sb.append("(COUNT(DISTINCT CASE WHEN al.first_time is not null and ");
		sb.append("to_char(a.create_datetime, 'yyyy-MM-dd')=to_char(al.first_time, 'yyyy-MM-dd')");
		sb.append(" THEN al.user_id ELSE NULL END)+COUNT(DISTINCT CASE WHEN al.sec_time is not null");
		sb.append(" and to_char(a.create_datetime, 'yyyy-MM-dd')=to_char(al.sec_time, 'yyyy-MM-dd')");
		sb.append(" THEN al.user_id ELSE NULL END)) as deposit");

		sb.append(" from sys_user a left join sys_user_deposit al on a.id = al.user_id ");
		sb.append(" where a.station_id =:stationId ");
		params.put("stationId", stationId);
		if (proxyId != null) {
			if (isRecommend) {
				sb.append(" and a.recommend_id=:proxyId");
				params.put("proxyId", proxyId);
			} else {
				sb.append(" and a.parent_ids like :proxyIds");
				params.put("proxyIds", "%," + proxyId + ",%");
			}
		}
		if (StringUtils.isNotEmpty(agentName)) {
			sb.append(" and a.agent_name=:agentName");
			params.put("agentName", "agentName");
		}
		if (begin != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(begin);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);

			sb.append(" AND a.create_datetime>=:start");
			params.put("start", c.getTime());
		}
		if (end != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(begin);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.DAY_OF_MONTH, 1);
			sb.append(" AND a.create_datetime<:end");
			params.put("end", c.getTime());
		}
		sb.append(" GROUP BY stat_date ");
		sb.append(" ) as aa  ORDER BY aa.stat_date desc");
		return queryByPage(sb.toString(),
				new BeanPropertyRowMapper<StationDailyRegisterVo>(StationDailyRegisterVo.class), params);
	}

	/**
	 * 代理报表
	 *
	 * @param stationId
	 * @return
	 */
	public Page<SysUser> getProxyReport(Long stationId, Integer level, String proxyName,boolean directSub) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sb = new StringBuilder("select a.id,a.username,a.proxy_name ");
		sb.append(" from sys_user a left join sys_user_proxy_num n on a.id = n.user_id");
		if (directSub) {//如果是代理会员身份，只查看直属下级，则不用去过滤下级是否有下级代理或会员
			sb.append(" where a.station_id =:stationId and a.level=:level ");
		}else{//如果是admin后台查看，可查看下级的下级，如果直属下级没有下级代理或会员，则不提取该下级
			sb.append(" where a.station_id =:stationId and a.level=:level and (n.proxy_num >0 or n.member_num>0)");
		}
		map.put("stationId", stationId);
		map.put("level", level);
		if (StringUtils.isNotEmpty(proxyName)) {
			sb.append(" and a.proxy_name = :proxyName");
			map.put("proxyName", proxyName);
		}
		sb.append(" order by a.id asc");
		List<Aggregation> aggregations = new ArrayList<>();
		return queryByPage(sb.toString(), map, aggregations);
	}

	/**
	 * 获取系统风险分析运营分析列表
	 */
	public Page<UserVo> getUserRiskPage(Long stationId, Date begin, Date end, Long userId, Long proxyId,
			String degreeIds, String agentName, boolean isMember) {
		StringBuilder sb = new StringBuilder("select a.id,a.username,a.type,a.create_datetime");
		sb.append(",b.last_login_datetime,b.last_login_ip,a.register_ip,a.degree_id");
		sb.append(" from sys_user a left join sys_user_login b on a.id = b.user_id");
		sb.append(" where a.station_id =:stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		sb.append(" and (a.type != :guest and a.type != :guestBack)");
		map.put("guest", UserTypeEnum.GUEST.getType());
		map.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		if (begin != null) {
			sb.append(" and b.last_login_datetime>=:begin");
			map.put("begin", begin);
		}
		if (end != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(end);
			c.add(Calendar.DAY_OF_MONTH, 1);
			sb.append(" and b.last_login_datetime<=:end");
			map.put("end", end);
		}
		if (StringUtils.isNotEmpty(agentName)) {
			sb.append(" and a.agent_name = :agentName");
			map.put("agentName", agentName);
		}
		if (userId != null) {
			sb.append(" and a.id =:userId");
			map.put("userId", userId);
		}
		if (proxyId != null) {
			if (isMember) {
				sb.append(" and (a.id=:proxyId OR a.recommend_id=:proxyId)");
			} else {
				sb.append(" and (a.id=:proxyId OR a.parent_ids like :proxyIdCn )");
				map.put("proxyIdCn", "%," + proxyId + ",%");
			}
			map.put("proxyId", proxyId);
		}
		if (StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" and a.degree_id in(");
			for (String lId : degreeIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		sb.append(" order by a.id ASC");
		return queryByPage(sb.toString(), new BeanPropertyRowMapper<UserVo>(UserVo.class), map);
	}

	/**
	 * 获取用户欺骗分析列表
	 */
	public Page<UserVo> getUserCheatPage(Long stationId, Date begin, Date end, String username,Integer cheatType) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("SELECT a.register_os,a.id,a.username,a.create_datetime,a.status,a.type,");
		sql.append("a.remark,a.agent_name,a.money_status,a.degree_id,a.group_id,a.create_username,");
		sql.append("a.create_user_id,a.lock_degree,a.level,a.online_warn,a.update_pwd_datetime,");
		sql.append("a.proxy_id,a.proxy_name,a.recommend_id,a.recommend_username,a.register_ip,");
		sql.append("m.money,l.last_login_datetime,l.online_status,l.last_login_ip,l.terminal,");
		sql.append("s.score,(num.proxy_num + num.member_num) as under_num");
		sql.append(" FROM sys_user a LEFT JOIN sys_user_money m ON a.id = m.user_id");
		sql.append(" LEFT JOIN sys_user_login l ON a.id = l.user_id");
		sql.append(" LEFT JOIN sys_user_info i ON a.id = i.user_id");
		sql.append(" LEFT JOIN sys_user_score s ON a.id = s.user_id");
		sql.append(" left join sys_user_proxy_num num on num.user_id =a.id");
		sql.append(" WHERE a.station_id = :stationId");
		params.put("stationId", stationId);

		SysUser user = findOneByUsername(username, stationId, null);
		if (user == null) {
			return new Page<>();
		}
		// 试玩账号不显示
		if (user.getType() != null) {
			if(GuestTool.isGuest(user)) {
				// 只要为试玩类型，都查前后台试玩账户
				sql.append(" AND (a.type = :guest OR a.type = :guestBack) ");
				params.put("guest", UserTypeEnum.GUEST.getType());
				params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
			} else {
				sql.append(" AND a.type =:userType");
				params.put("userType", user.getType());
			}
		} else {
			sql.append(" AND (a.type != :guest AND a.type != :guestBack) ");
			params.put("guest", UserTypeEnum.GUEST.getType());
			params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		}
		if (begin != null) {
			sql.append(" AND a.create_datetime >=:startTime");
			params.put("startTime", begin);
		}
		if (end != null) {
			sql.append(" AND a.create_datetime <:endTime");
			params.put("endTime", end);
		}

		if (cheatType == CheatAnalyzeEnum.sameIp.getType()) {
			if (StringUtils.isNotEmpty(user.getRegisterIp())) {
				sql.append(" AND a.register_ip=:registerIp and a.register_ip is not null");
				params.put("registerIp", user.getRegisterIp());
			}
		}else if (cheatType == CheatAnalyzeEnum.sameOs.getType()) {
			if (StringUtils.isNotEmpty(user.getRegisterIp())) {
				sql.append(" AND a.register_os=:registerOs and a.register_os is not null");
				params.put("registerOs", user.getRegisterOs());
			}
		}else if (cheatType == CheatAnalyzeEnum.sameIpAndOs.getType()) {
			if (StringUtils.isNotEmpty(user.getRegisterIp())) {
				sql.append(" AND a.register_ip=:registerIp and a.register_ip is not null");
				params.put("registerIp", user.getRegisterIp());
				sql.append(" AND a.register_os=:registerOs and a.register_os is not null");
				params.put("registerOs", user.getRegisterOs());
			}
		}
		sql.append(" ORDER BY a.create_datetime DESC");
		Page<UserVo> page = queryByPage(sql.toString(), new BeanPropertyRowMapper<UserVo>(UserVo.class), params);
		return page;
	}


	public void batchDisableNoLogin(Date lastLoginDate, int noLoginDay, Long stationId) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("update sys_user a");
		sb.append(" set status=:status,remark=(COALESCE(remark,'')||';'||:remark)");
		sb.append(" from sys_user_login b");
		sb.append(" where a.id=b.user_id and b.last_login_datetime notnull");
		sb.append(" and b.last_login_datetime <:lastLoginDate");
		sb.append(" and a.station_id=:stationId  and a.status =:oldStatus");
		params.put("status", Constants.STATUS_DISABLE);
		params.put("oldStatus", Constants.STATUS_ENABLE);
		params.put("remark", "超过" + noLoginDay + "天未登陆，系统自动禁用");
		params.put("lastLoginDate", lastLoginDate);
		params.put("stationId", stationId);
		update(sb.toString(), params);
		CacheUtil.delCacheByPrefix(CacheKey.USER_INFO);
	}

	public void changeOnlineWarnStatus(Long id, Long stationId, Integer status) {
		StringBuilder sb = new StringBuilder("update sys_user set online_warn =:status");
		sb.append(" where id=:id and station_id=:stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("stationId", stationId);
		map.put("status", status);
		update(sb.toString(), map);
		CacheUtil.delCacheByPrefix(CacheKey.USER_INFO);
	}

	public boolean findOneByGroupId(Long id, Long stationId, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("stationId", stationId);
		map.put("status", status);
		return count("select count(1) from sys_user where station_id=:stationId and group_id=:id and status=:status",
				map) > 0;
	}

	public boolean findOneByDegreeId(Long id, Long stationId, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("stationId", stationId);
		map.put("status", status);
		return count("select count(1) from sys_user where station_id=:stationId and degree_id=:id and status=:status",
				map) > 0;
	}

    public void deleteBatchGuest(List<Long> ids) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from sys_user where id in (");
		for (Long id : ids) {
			sql.append(id).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		super.update(sql.toString());
    }
}
