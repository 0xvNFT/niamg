package com.play.service;

import java.util.List;
import java.util.Set;

import com.play.model.vo.AdminMenuVo;

/**
 * admin user 权限表 
 *
 * @author admin
 *
 */
public interface AdminMenuService {

	List<AdminMenuVo> getAll();

	List<AdminMenuVo> getStationMenu(Long stationId , Long groupId);

	Set<Long> getPermissionIdSet(Long groupId,Long StationId);

    void adminMenuRefresh(Long stationId, Long partnerId, Long[] adminMenuIds);
}