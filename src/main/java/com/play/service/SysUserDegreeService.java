package com.play.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.play.model.SysUser;
import com.play.model.SysUserDegree;
import com.play.orm.jdbc.page.Page;

/**
 * 会员层级信息
 *
 * @author admin
 *
 */
public interface SysUserDegreeService {

	void init(Long stationId, Long partnerId);

	List<SysUserDegree> find(Long stationId, Integer status);

	SysUserDegree getDefault(Long stationId);

	Long getDefaultId(Long stationId);

	Page<SysUserDegree> page(Long stationId);

	void save(SysUserDegree degree);

	SysUserDegree findOne(Long id, Long stationId);

	String findDegreeName(Long id, Long stationId);

	boolean findOneByName(Long stationId, String name);

	void update(SysUserDegree degree);

	void updateStatus(Long id, Integer status, Long stationId);

	void reStatMemberAccount(Long stationId);

	void delete(Long id, Long stationId);

	void transfer(Long curId, Long nextId, Long stationId);

	void updLevelModel(Integer type, Long stationId, Map<String, String[]> parameterMap);

	void modifyUpgrade(String configs, Long stationId);

	void changeUserDegree(SysUser user, BigDecimal totalBetNum, String remark);

	void changeMemberDegree(Long userId, Long stationId, Long degreeId, Integer lockDegree, String remark,
			boolean isHandle);

	void batchChangeDegree(String usernames, Long stationId, Long degreeId, Integer lockDegree);

	String getDegreeNames(String degreeIds, Long stationId);

	String getName(Long id, Long stationId);

}