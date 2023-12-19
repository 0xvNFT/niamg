package com.play.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.cache.CacheKey;
import com.play.cache.redis.RedisAPI;
import com.play.dao.StationRedPacketDao;
import com.play.dao.StationRedPacketDegreeDao;
import com.play.dao.SysUserDegreeDao;
import com.play.model.StationRedPacket;
import com.play.model.StationRedPacketDegree;
import com.play.model.SysUserDegree;
import com.play.service.StationRedPacketDegreeService;

/**
 * 
 *
 * @author admin
 *
 */
@Service
public class StationRedPacketDegreeServiceImpl implements StationRedPacketDegreeService {

	@Autowired
	private StationRedPacketDegreeDao redpacketDegreeDao;
	@Autowired
	private StationRedPacketDao stationRedPacketDao;
	@Autowired
	private SysUserDegreeDao userDegreeDao;

	@Override
	public void deleteByRedPacketId(Long redPacketId) {
		redpacketDegreeDao.deleteByRedPacketId(redPacketId);

	}

	@Override
	public Set<Long> findDegrees(Long redPacketId) {
		List<Long> list = redpacketDegreeDao.findByRedPacketId(redPacketId);
		if (list != null) {
			return new HashSet<>(list);
		}
		return new HashSet<>();
	}

	@Override
	public List<StationRedPacketDegree> list(Long stationId, Long redBagId, boolean redBagNum) {
		List<StationRedPacketDegree> list = redpacketDegreeDao.listBySidAndRid(stationId, redBagId, redBagNum);
		if (list != null && list.size() > 0) {
			SysUserDegree d = null;
			for (StationRedPacketDegree mrl : list) {
				d = userDegreeDao.findOne(mrl.getDegreeId(), mrl.getStationId());
				if (d != null) {
					mrl.setDegreeName(d.getName());
				}
				setRemainNumber(redBagId, mrl);
			}
		}
		return list;
	}

	private void setRemainNumber(Long redBagId, StationRedPacketDegree mrl) {
		StationRedPacket packet = stationRedPacketDao.findOne(redBagId, mrl.getStationId());
		String redBagSumCountKey = null;
		int redBagSumCount = 0;
		switch (packet.getRedBagType()) {
		case StationRedPacket.RED_BAG_TYPE_1:
			break;
		case StationRedPacket.RED_BAG_TYPE_3:
			// 按充值量
			redBagSumCountKey = ":MEMBER_RED_BAG_NUM_" + packet.getId() + "_USER_" + mrl.getRedBagRechargeMin()
					+ "_NUM_" + mrl.getRedBagRechargeMax();// 活动红包总个数
			redBagSumCount = NumberUtils.toInt(RedisAPI.getCache(redBagSumCountKey, CacheKey.RED_PACKET.getDb()));
			mrl.setRemainNumber(mrl.getPacketNumber() - redBagSumCount);
			break;
		case StationRedPacket.RED_BAG_TYPE_2:
			// 按层级
			redBagSumCountKey = ":MEMBER_RED_BAG_NUM_" + redBagId + "_USER_ID_NUM_" + mrl.getDegreeId();// 每个层级红包总个数
			redBagSumCount = NumberUtils.toInt(RedisAPI.getCache(redBagSumCountKey, CacheKey.RED_PACKET.getDb()));
			mrl.setRemainNumber(mrl.getPacketNumber() - redBagSumCount);
			break;
		}
	}
}
