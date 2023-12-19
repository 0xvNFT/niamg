package com.play.service;

import java.util.List;
import java.util.Set;

import com.play.model.vo.MenuVo;

/**
 * 合作商后台菜单
 *
 * @author admin
 *
 */
public interface PartnerMenuService {

	Set<String> getPermissionSet(Long id);

	List<MenuVo> getNavMenuVo();

	List<MenuVo> getAllMenuVo(Long userId);

	Set<Long> getPermissionIdSet(Long id);

	void setAuth(Long userId, Long currentUserId, Long partnerId, Long[] menuIds);

}