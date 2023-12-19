package com.play.service;

import java.util.List;
import java.util.Set;

import com.play.model.StationRedPacketDegree;

/**
 * 
 *
 * @author admin
 *
 */
public interface StationRedPacketDegreeService {

	void deleteByRedPacketId(Long redPacketId);

	Set<Long> findDegrees(Long redPacketId);

	List<StationRedPacketDegree> list(Long stationId, Long redBagId, boolean redBagNum);

}