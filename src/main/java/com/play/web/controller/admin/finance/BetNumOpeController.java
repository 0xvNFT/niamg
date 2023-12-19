package com.play.web.controller.admin.finance;

import java.math.BigDecimal;
import java.util.Map;

import com.play.model.SysUserBetNumHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.cache.redis.DistributedLockUtil;
import com.play.common.SystemConfig;
import com.play.core.StationConfigEnum;
import com.play.service.SysUserBetNumService;
import com.play.web.annotation.AdminReceiptPwdVerify;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

/**
 * 打码量加减管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/finance/betNumOpe")
public class BetNumOpeController extends AdminBaseController {

	@Autowired
	private SysUserBetNumService userBetNumService;

	@Permission("admin:betNumOpe")
	@RequestMapping("index")
	public String index(Map<String, Object> map, String username) {
		map.put("username", username);
		map.put("isReceiptPwd",
				StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.admin_re_pwd_bet_num_ope));
		return returnPage("/finance/betNum/betNumOpe");
	}

	@ResponseBody
	@Permission("admin:betNumOpe")
	@RequestMapping("getBetInfo")
	@NeedLogView("会员打码量信息")
	public void getBetInfo(String username) {
		renderJSON(userBetNumService.addOrGetBetInfo(SystemUtil.getStationId(), username));
	}

	@ResponseBody
	@Permission("admin:betNumOpe")
	@RequestMapping("/save")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_bet_num_ope)
	public void save(Long id, BigDecimal betNum, String remark, Integer type) {
		LoginAdminUtil.checkPerm();
		Long stationId = SystemUtil.getStationId();
		if (!DistributedLockUtil.tryGetDistributedLock("betNumOpe:" + id + ":" + betNum + ":" + stationId, 3)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit);
		}
		userBetNumService.adminUpdateBetNum(id, stationId, betNum, remark, type);
		renderSuccess();
	}

	@ResponseBody
	@RequestMapping("/clearbetNumOpe")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_bet_num_ope)
	public void clearbetNumOpe(Long id, String remark) {
		LoginAdminUtil.checkPerm();
		if (!DistributedLockUtil.tryGetDistributedLock("betNumOpe:" + id + ":"  + SystemUtil.getStationId(),
				3000)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit);
		}
		userBetNumService.clearbetNumOpe( id, SystemUtil.getStationId(), remark,SysUserBetNumHistory.TYPE_DRAWNEED_CLEAR);
		renderSuccess();
	}
}
