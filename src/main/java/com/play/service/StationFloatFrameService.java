package com.play.service;

import java.util.List;

import com.play.model.StationFloatFrame;
import com.play.model.StationFloatFrameSetting;
import com.play.model.vo.FloatFrameVo;
import com.play.orm.jdbc.page.Page;

/**
 * 
 *
 * @author admin
 *
 */
public interface StationFloatFrameService {

	Page<StationFloatFrame> page(Long stationId, String language, Integer showPage, Integer platform);

	void addSave(StationFloatFrame ff, String imgUrl, String imgHoverUrl, String imgSort, String linkType,
			String linkUrl);

	void modify(StationFloatFrame ff, String imgUrl, String imgHoverUrl, String imgSort, String linkType,
			String linkUrl);

	void delete(Long id, Long stationId);

	StationFloatFrame findOne(Long id, Long stationId);

	List<StationFloatFrame> find(Long stationId, Integer status);

	List<FloatFrameVo> find(Long stationId, Long degreeId, Long groupId, Integer status, Integer showPage,
			Integer platform);

	List<StationFloatFrameSetting> findSettingByFfId(Long ffId);

	void changeStatus(Long id, Long stationId, Integer status);

}