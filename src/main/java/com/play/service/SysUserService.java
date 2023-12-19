package com.play.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.play.model.SysUser;
import com.play.model.bo.UserRegisterBo;
import com.play.model.so.UserSo;
import com.play.model.vo.UserVo;
import com.play.orm.jdbc.page.Page;

/**
 * 会员账号信息
 *
 * @author admin
 *
 */
public interface SysUserService {

	SysUser findOneByUsername(String username, Long stationId, Integer type);

	SysUser findOneById(Long userId, Long stationId);

	/**
	 * 获取站点最低的会员层级
	 *
	 * @param stationId
	 * @return
	 */
	int getLowestLevel(Long stationId, Long proxyId);

	Page<UserVo> getPageForAdmin(UserSo so, boolean viewContact);

	boolean exist(String username, Long stationId, Long escapeId);
	boolean existByEmail(String email, Long stationId, Long escapeId);

	SysUser save(SysUser user);

	void adminPwdModify(Long id, Long stationId, String pwd, String rpwd);

	void modifyProxy(Long id, UserRegisterBo rbo, Long stationId);

	void changeStatus(Long id, Long stationId, Integer status);

	void modifyRemark(Long id, Long stationId, String remark);

	void freezeMoney(Long id, Long stationId, Integer status);

	void export(UserSo so);

	void batchChangeProxy(UserRegisterBo rbo, String usernames, Long stationId);

	void batchDisableStatus(String usernames, Long stationId);

	void batchEnableStatus(String usernames, Long stationId);

	void errorPwdDisableAccount(SysUser user);

	void resetLoginPwd(String password, String password2, Long id, Long stationId, String username);

	int countRegisterMemberByIp(Long stationId, Date begin, Date end, String ip, Integer userType);
	int countRegisterMemberByOs(Long stationId, Date begin, Date end, String os, Integer userType);

	Integer countEffectiveNum(Long stationId, boolean effective);

	void modifyLoginPwd(String oldPwd, String newPwd, String okPwd, Long userId, Long stationId, String username);

	/**
	 * 获取用户下级用户分页数据
	 * 
	 * @param user
	 * @param username
	 * @param maxBalance
	 * @param minBalance
	 * @param beginTime
	 * @param endTime
	 * @param depositTotal
	 * @param include
	 * @param level
	 * @param sortName
	 * @param sortOrder
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	Page<UserVo> getSubordinatePage(SysUser user, String username, BigDecimal maxBalance, BigDecimal minBalance,
			Date beginTime, Date endTime, BigDecimal depositTotal, Boolean include, Integer level, String sortName,
			String sortOrder, Integer pageSize, Integer pageNumber);

	/**
	 * 获取用户分页数据
	 * 
	 * @param user
	 * @param proxyName
	 * @param beginTime
	 * @param endTime
	 * @param type
	 * @param include
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	Page<UserVo> getUserTeamDataPage(SysUser user, String proxyName, Date beginTime, Date endTime, Integer type,
			Boolean include, Integer pageSize, Integer pageNumber);

	/**
	 * 代理修改下级代理返点
	 * 
	 * @param proxy
	 * @param userId
	 * @param liveRebate
	 * @param sportRebate
	 * @param egameRebate
	 * @param chessRebate
	 * @param esportRebate
	 * @param fishingRebate
	 * @param lotRebate
	 */
	void modifyUserRebate(SysUser proxy, Long userId, BigDecimal liveRebate, BigDecimal sportRebate,
			BigDecimal egameRebate, BigDecimal chessRebate, BigDecimal esportRebate, BigDecimal fishingRebate,
			BigDecimal lotRebate);

	/**
	 * 后台系统风险运营分析 登录情况
	 *
	 * @param begin
	 * @param end
	 * @param proxyName
	 * @param username
	 * @param degreeIds
	 * @param agentName
	 * @return
	 */
	Page<UserVo> adminRiskReport(Long stationId, Date begin, Date end, String proxyName, String username,
			String degreeIds, String agentName);

	Page<UserVo> adminCheatReport(Long stationId, Date begin, Date end, String username,Integer cheatType);

	/**
	 * 批量禁用长时间未登陆账号
	 *
	 * @param lastLoginDate 最后登陆时间
	 * @param noLoginDay    未登陆时间
	 * @param stationId
	 */
	void batchDisableNoLogin(Date lastLoginDate, int noLoginDay, Long stationId);

	void changeOnlineWarnStatus(Long id, Long stationId, Integer status);

	/**
	 * 获取游客列表
	 * @param userSo
	 * @return
	 */
	Page<UserVo> userGuestPage(UserSo userSo);

	/**
	 * 设置游客账户余额
	 * @param ids
	 * @param money
	 * @param operator
	 */
	void resetGuestMoney(String ids, BigDecimal money, String operator);

	/**
	 * 会员转代理
	 * @param stationId
	 * @param userId
	 * @param liveRebate
	 * @param sportRebate
	 * @param egameRebate
	 * @param chessRebate
	 * @param esportRebate
	 * @param fishingRebate
	 * @param lotteryRebate
	 */
	void changeUserToProxy(Long userId, Long stationId, BigDecimal liveRebate, BigDecimal sportRebate, BigDecimal egameRebate, BigDecimal chessRebate, BigDecimal esportRebate, BigDecimal fishingRebate, BigDecimal lotteryRebate);

	/**
	 * 删除试玩账户
	 * @param id
	 * @param username
	 */
    void deleteGuest(Long id, String username);

	/**
	 * 批量删除试玩账户
	 * @param ids
	 */
	void deleteBatchGuest(List<Long> ids);

	/**
	 * 清理用户注册时初始化的信息表
	 * @param userId
	 * @param stationId
	 * @param userType
	 */
	void cleanUserRegisterInfo(Long userId, Long stationId, Integer userType);
}