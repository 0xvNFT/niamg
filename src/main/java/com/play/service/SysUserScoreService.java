package com.play.service;

import com.play.core.ScoreRecordTypeEnum;
import com.play.model.SysUser;
import com.play.model.SysUserScore;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会员积分信息
 *
 * @author admin
 */
public interface SysUserScoreService {
	void init(Long id, Long stationId);

	/**
	 * 加减积分
	 *
	 * @param type
	 * @param user
	 * @param score
	 * @param remark
	 */
	void operateScore(ScoreRecordTypeEnum type, SysUser user, Long score, String remark);

	/**
	 * 会员积分清零
	 *
	 * @param stationId
	 */
	void scoreToZero(Long stationId);

	/**
	 * 获取单个
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	SysUserScore findOne(Long id, Long stationId);

	/**
	 * 组装签到数据格式
	 * 
	 * @param userId
	 * @return
	 */
	List<Map<String, String>> signMobileCalList(Long userId);

	List<Integer> signPcCalList(Long userId);

	public List<Map<String, String>> signList(Long userId);

	/**
	 * 会员签到更新积分
	 * 
	 * @param user
	 * @param opeScore        签到获得积分
	 * @param signCount       签到次数
	 * @param oldLastSignDate
	 */
	void signInUpdateScore(SysUser user, Long opeScore, Integer signCount, Date oldLastSignDate);
	void signInUpdateScore(SysUser user, Long opeScore, Integer signCount, Date oldLastSignDate,Date newSignDate);

	void batchAddScore(Integer modelType, Long score, String usernames, Long stationId, String remark);

	Long getScore(Long id, Long stationId);

}
