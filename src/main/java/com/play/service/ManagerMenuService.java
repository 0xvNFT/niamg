package com.play.service;

import java.util.List;
import java.util.Set;

import com.play.model.vo.MenuVo;

/**
 * 总控菜单
 *
 * @author admin
 *
 */
public interface ManagerMenuService {
	Set<String> getPermissionSet(Long id);

	List<MenuVo> getNavMenuVo();

	List<MenuVo> getAllMenuVo();

	Set<Long> getPermissionIdSet(Long id);

	void setAuth(Long userId, Long[] menuIds);

}