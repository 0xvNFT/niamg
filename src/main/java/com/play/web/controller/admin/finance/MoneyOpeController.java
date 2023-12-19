package com.play.web.controller.admin.finance;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.SystemConfig;
import com.play.core.MoneyRecordType;
import com.play.core.StationConfigEnum;
import com.play.model.SysUser;
import com.play.model.SysUserDegree;
import com.play.model.SysUserInfo;
import com.play.service.BatchOptMoneyService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserInfoService;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserService;
import com.play.web.annotation.AdminReceiptPwdVerify;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

/**
 * 会员加扣款管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/finance/moneyOpe")
public class MoneyOpeController extends AdminBaseController {

	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserMoneyService userMoneyService;
	@Autowired
	private SysUserInfoService userInfoService;
	@Autowired
	private BatchOptMoneyService batchOptMoneyService;
	@Autowired
	private SysUserDegreeService userDegreeService;

	@Permission("admin:moneyOpe")
	@RequestMapping("/index")
	public String index(Map<String, Object> map, String username) {
		map.put("isReceiptPwd",
				StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.admin_re_pwd_money_ope));
		map.put("username", username);
		return returnPage("/finance/money/moneyOpeIndex");
	}

	@ResponseBody
	@Permission("admin:moneyOpe")
	@RequestMapping("/getMoney")
	@NeedLogView("会员余额信息")
	public void getMoney(String username) {
		LoginAdminUtil.checkPerm();
		if (StringUtils.isEmpty(username)) {
			throw new BaseException(BaseI18nCode.inputUsername);
		}
		username = username.toLowerCase().trim();
		Long stationId = SystemUtil.getStationId();
		JSONObject object = new JSONObject();
		SysUser user = userService.findOneByUsername(username, stationId, null);
		if (user == null || GuestTool.isGuest(user)) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		SysUserInfo info = userInfoService.findOne(user.getId(), stationId);
		object.put("username", username);
		object.put("remark", user.getRemark());
		object.put("userId", user.getId());
		object.put("money", userMoneyService.getMoney(user.getId()));
		object.put("userType", user.getType());
		object.put("realName", info.getRealName());
		object.put("parentNames", StringUtils.isEmpty(user.getParentNames()) ? ""
				: user.getParentNames().substring(1, user.getParentNames().length() - 1));
		SysUserDegree degree = userDegreeService.findOne(user.getDegreeId(), user.getStationId());
		if (null == degree) {
			object.put("degreeName", "");
		} else {
			object.put("degreeName", degree.getName());
		}
		renderJSON(object);
	}

	@ResponseBody
	@Permission("admin:moneyOpe")
	@RequestMapping("/save")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_money_ope)
	synchronized public void save(Long userId, Integer type, BigDecimal money, BigDecimal betNumMultiple, String remark,
			BigDecimal giftMoney, BigDecimal giftBetNumMultiple, Long score, String bgRemark) {
		LoginAdminUtil.checkPerm();
		if (score != null && score < 0) {
			throw new BaseException(BaseI18nCode.scoreCannotBeNegative);
		}
		Long stationId = SystemUtil.getStationId();
		if (!DistributedLockUtil.tryGetDistributedLock("artificial_" + userId + "_" + money + stationId, 3)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit);
		}
		// 手动加款策略
		boolean useStrategy = false;
		if (type == 999) {
			type = MoneyRecordType.DEPOSIT_ARTIFICIAL.getType();
			useStrategy = true;
		}
		userMoneyService.moneyOpe(userId, stationId, MoneyRecordType.getMoneyRecordType(type), money, betNumMultiple,
				remark, giftMoney, giftBetNumMultiple, score, useStrategy, bgRemark);
		renderSuccess();
	}

	@Permission("admin:moneyOpe")
	@RequestMapping("/showBatchAdd")
	public String showBatchAdd(Map<String, Object> map) {
		map.put("idReceiptPwd",
				StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.admin_re_pwd_money_ope));
		return returnPage("/finance/money/moneyBatchAdd");
	}

	@Permission("admin:moneyOpe")
	@RequestMapping("/showBatchSub")
	public String showBatchSub(Map<String, Object> map) {
		map.put("isReceiptPwd",
				StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.admin_re_pwd_money_ope));
		return returnPage("/finance/money/moneyBatchSub");
	}

	@ResponseBody
	@Permission("admin:moneyOpe")
	@RequestMapping("/batchAddMoney")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_money_ope)
	synchronized public void batchAddSave(String usernames, BigDecimal betNumMultiple, BigDecimal giftBetNumMultiple,
			String remark, Integer modelType, BigDecimal money, BigDecimal giftMoney, String bgRemark) {
		LoginAdminUtil.checkPerm();
		Long stationId = SystemUtil.getStationId();
		if (!DistributedLockUtil.tryGetDistributedLock("batchAddOrSubMoney:" + stationId, 60)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit60);
		}
		try {
			batchOptMoneyService.batchAddMoney(stationId, modelType, money, giftMoney, usernames, betNumMultiple,
					giftBetNumMultiple, remark, bgRemark);
		} catch (Exception e) {
			renderSuccess(e.getMessage());
			return;
		}
		renderSuccess();
	}

	@ResponseBody
	@Permission("admin:moneyOpe")
	@RequestMapping("/batchSubMoney")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_money_ope)
	synchronized public void batchSubMoney(String usernames, BigDecimal money, String remark, String bgRemark,
			Integer type) {
		LoginAdminUtil.checkPerm();
		Long stationId = SystemUtil.getStationId();
		if (!DistributedLockUtil.tryGetDistributedLock("batchAddOrSubMoney:" + stationId, 60000)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit60);
		}
		try {
			batchOptMoneyService.batchSubMoney(stationId, usernames, money, remark, bgRemark,
					MoneyRecordType.getMoneyRecordType(type));
		} catch (Exception e) {
			renderError(e.getMessage());
			return;
		}
		renderSuccess();
	}
}
