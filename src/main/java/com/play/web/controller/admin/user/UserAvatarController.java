package com.play.web.controller.admin.user;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.web.filter.XssShieldUtil;
import com.play.model.SysUserAvatar;
import com.play.service.SysUserAvatarService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.var.SystemUtil;

/**
 * 会员头像管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/userAvatar")
public class UserAvatarController extends AdminBaseController {

	@Autowired
	private SysUserAvatarService userAvatarService;

	/**
	 * 会员头像管理首页
	 */
	@Permission("admin:userAvatar")
	@RequestMapping("/index")
	public String memberAvatarIndex(Map<String, Object> map) {
		return returnPage("/user/avatar/userAvatarIndex");
	}

	/**
	 * 头像列表
	 */
	@Permission("admin:userAvatar")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("会员头像列表")
	public void memberAvatarList() {
		renderJSON(userAvatarService.getPage(SystemUtil.getStationId()));
	}

	/**
	 * 会员头像添加
	 */
	@Permission("admin:userAvatar:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		return returnPage("/user/avatar/userAvatarAdd");
	}

	/**
	 * 会员头像保存
	 */
	@Permission("admin:userAvatar:add")
	@RequestMapping("/doAdd")
	@ResponseBody
	public void doAdd(SysUserAvatar avatar) {
		if (StringUtils.isNotEmpty(avatar.getUrl())) {
			avatar.setUrl(XssShieldUtil.xssEncode(avatar.getUrl()));
		}
		if (StringUtils.isEmpty(avatar.getUrl())) {
			throw new ParamException();
		}
		avatar.setStationId(SystemUtil.getStationId());
		avatar.setPartnerId(SystemUtil.getPartnerId());
		userAvatarService.saveAvatar(avatar);
		renderSuccess();
	}

	/**
	 * 头像删除
	 */
	@Permission("admin:userAvatar:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		userAvatarService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 头像添加
	 */
	@Permission("admin:userAvatar:modify")
	@RequestMapping("/showModify")
	@NeedLogView("会员头像修改详情")
	public String modify(Map<String, Object> map, Long id) {
		map.put("avatar", userAvatarService.findOne(id, SystemUtil.getStationId()));
		return returnPage("/user/avatar/userAvatarModify");
	}
	/**
	 * 会员头像保存
	 */
	@Permission("admin:userAvatar:modify")
	@RequestMapping("/doModify")
	@ResponseBody
	public void doModify(SysUserAvatar avatar) {
		if (StringUtils.isNotEmpty(avatar.getUrl())) {
			avatar.setUrl(XssShieldUtil.xssEncode(avatar.getUrl()));
		}
		if (StringUtils.isEmpty(avatar.getUrl())) {
			throw new ParamException();
		}
		avatar.setStationId(SystemUtil.getStationId());
		userAvatarService.modifyAvatar(avatar);
		renderSuccess();
	}
}
