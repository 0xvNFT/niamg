package com.play.service;

import java.util.List;

import com.play.model.StationTurntableGift;
import com.play.model.vo.StaionTurntableImageVo;
import com.play.orm.jdbc.page.Page;

/**
 * 转盘奖品表
 *
 * @author admin
 *
 */
public interface StationTurntableGiftService {

	Page<StationTurntableGift> getPage(Long stationId);

	void save(StationTurntableGift t);

	void modify(StationTurntableGift t);

	void delete(Long id, Long stationId);

	List<StationTurntableGift> getCombo(Long stationId);

	StationTurntableGift findOne(Long id, Long stationId);

	List<StaionTurntableImageVo> findListImages(Long id, Long stationId);

	String getName(Long id,Long stationId);
}