package com.play.service;

import java.util.List;
import java.util.Set;

import com.play.model.vo.MenuVo;

public interface AdminAuthorityService {

	Set<String> getPermissionSet(Long groupId, Long stationId);

	List<MenuVo> getNavMenuVo(Long groupId, Long stationId);

}
