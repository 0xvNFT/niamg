package com.play.web.controller.admin.user;


import com.play.cache.redis.DistributedLockUtil;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.StationConfigEnum;
import com.play.model.SysUser;
import com.play.model.bo.UserRegisterBo;
import com.play.model.so.UserSo;
import com.play.service.*;
import com.play.web.annotation.AdminReceiptPwdVerify;

import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;

import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.ValidateUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 账户管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/accGuest")
public class UserGuestManageController extends AdminBaseController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;
	@Autowired
	private SysUserRegisterService userRegisterService;
	@Autowired
	private SysUserInfoService sysUserInfoService;

	@Permission("admin:accGuest")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/user/guest/guestIndex");
	}

	@ResponseBody
	@Permission("admin:accGuest")
	@RequestMapping("/list")
	//@NeedLogView("游客账号列表")
	public void list(UserSo userSo, String startDate, String endDate) {
		userSo.setStationId(SystemUtil.getStationId());
		userSo.setBegin(StringUtils.isEmpty(startDate) ? null : DateUtil.toDatetime(startDate));
		userSo.setEnd(StringUtils.isEmpty(endDate) ? null : DateUtil.toDatetime(endDate));
		renderJSON(sysUserService.userGuestPage(userSo));
	}

	/**
	 * 后台新增游客账号
	 */
	@Permission("admin:accGuest:add")
	@RequestMapping("/addGuest")
	public String addGuest(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/user/guest/guestAdd");
	}

	@ResponseBody
	@Permission("admin:accGuest:add")
	@RequestMapping("/addGuestSave")
	public void addGuestSave(UserRegisterBo rbo) {
		userRegisterService.adminSaveGuest(SystemUtil.getStationId(), rbo);
		renderSuccess();
	}

	/**
	 * 后台批量新增游客账号页面
	 */
//	@Permission("admin:accGuest:add")
//	@RequestMapping("/batchAddGuest")
//	public String batchAddGuest(Map<String, Object> map) {
//		Long stationId = SystemUtil.getStationId();
//		map.put("levels", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
//		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
//		return returnPage("/user/guest/guestBatchAdd");
//	}

	/**
	 * 后台批量新增游客账号
	 */
//	@ResponseBody
//	@Permission("admin:accGuest:add")
//	@RequestMapping(value = "/batchAddGuestSave", method = RequestMethod.POST)
//	public void batchAddGuestSave(String guestUsernames, UserRegisterBo rbo) {
//		Long stationId = SystemUtil.getStationId();
//		// 锁定 30 秒，避免重复操作
//		if (!DistributedLockUtil.tryGetDistributedLock("accGuest:add:" + stationId, 30)) {
//			throw new BaseException(BaseI18nCode.concurrencyLimit30);
//		}
//		//sysUserService.batchAddGuestSave(guestUsernames, rbo);
//		renderSuccess();
//	}

	/**
	 * 密码修改页面
	 */
	@Permission("admin:accGuest:modify")
	@RequestMapping("/pwdModify")
	public String pwdModify(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		SysUser sysUser = sysUserService.findOneById(id, stationId);
		if (sysUser == null) {
			throw new ParamException(BaseI18nCode.memberUnExist);
		}
		map.put("userId", id);
		map.put("username", sysUser.getUsername());
		map.put("onOffReceiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_acc_pwd_update));
		return returnPage("/user/guest/guestPwdModify");
	}

	/**
	 * 密码修改保存
	 */
	@ResponseBody
	@Permission("admin:accGuest:modify")
	@RequestMapping("pwdModifySave")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_acc_pwd_update)
	public void pwdModifySave(Integer type, Long userId, String newPwd, String okPwd) {
		Long stationId = SystemUtil.getStationId();
		if (type == 1) {
			// 修改登陆密码
			sysUserService.adminPwdModify(userId, stationId, newPwd, okPwd);
		} else if (type == 2) {
			// 修改提款密码
			sysUserInfoService.adminPayPwdModify(userId, stationId, newPwd, okPwd);
		}
		renderSuccess();
	}

	/**
     * 游客状态修改
     */
    @ResponseBody
    @RequestMapping("/changeStatus")
    public void changeStatus(Long userId, Integer status) {
		sysUserService.changeStatus(userId, SystemUtil.getStationId(), status);
        renderSuccess();
    }

	/**
	 * 游客状态批量修改页面
	 */
//	@RequestMapping("/batchChangeGuestStatusPage")
//	public String batchChangeStatusPage(Map<String, Object> map) {
//		return returnPage("/user/guest/guestStatusBatchModify");
//	}

//	@ResponseBody
//	@RequestMapping("/batchChangeStatus")
//	public void batchChangeStatus(String accounts, Integer status) {
//		try {
//			//sysUserService.batchChangeStatus(accounts, status);
//		} catch (Exception e) {
//			renderError(e.getMessage());
//			return;
//		}
//		renderSuccess();
//	}

	/**
	 * 设置游客账户余额
	 * @param ids
	 * @param money
	 */
    @ResponseBody
    @RequestMapping("/resetGuestMoney")
	@Permission("admin:accGuest:modify:sensitive")
    public void resetGuestMoney(String ids , BigDecimal money){
		sysUserService.resetGuestMoney(ids, money, LoginAdminUtil.getUsername());
    	renderSuccess();
	}

	/**
	 * 删除试玩账户
	 * @param id
	 */
	@ResponseBody
	@RequestMapping("/deleteGuest")
	@Permission("admin:accGuest:delete")
	public void deleteGuest(Long id, String username){
		sysUserService.deleteGuest(id, username);
		renderSuccess();
	}

	/**
	 * 批量删除试玩账户
	 * @param ids
	 */
	@ResponseBody
	@RequestMapping("/deleteBatchGuest")
	@Permission("admin:accGuest:delete")
	public void deleteBatchGuest(String ids) {
		if (org.springframework.util.StringUtils.isEmpty(ids)) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		List<Long> list = Arrays.stream(ids.split(","))
				// 过滤非数字
				.filter(e -> ValidateUtil.isNumber(e))
				// 转成Long型
				.map(Long::new)
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(list)) {
			throw new ParamException(BaseI18nCode.parameterError);
		}

		Long stationId = SystemUtil.getStationId();
		// 锁定 6 秒，避免重复操作
		if (!DistributedLockUtil.tryGetDistributedLock("accGuest:delete:" + stationId, 6)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit6);
		}

		sysUserService.deleteBatchGuest(list);
		renderSuccess();
	}

}
