package com.play.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.core.MoneyRecordType;
import com.play.core.PlatformType;
import com.play.dao.ThirdTransLogDao;
import com.play.model.SysUser;
import com.play.model.ThirdTransLog;
import com.play.model.bo.MnyMoneyBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserService;
import com.play.service.ThirdCenterService;
import com.play.service.ThirdTransLogService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

/**
 * 
 *
 * @author admin
 *
 */
@Service
public class ThirdTransLogServiceImpl implements ThirdTransLogService {

	@Autowired
	private ThirdTransLogDao thirdTransLogDao;

	@Autowired
	private SysUserMoneyService sysUserMoneyService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private ThirdCenterService thirdCenterService;

	// TODO
	// 第三方中心服务

	@Override
	public ThirdTransLog findOne(Long id, Long stationId) {
		return thirdTransLogDao.findOne(id, stationId);
	}

	@Override
	public Page<ThirdTransLog> page(String username, Long userId, Integer platform, Long stationId, Integer status,
			Integer type, Date start, Date end) {
		Page<ThirdTransLog> page = thirdTransLogDao.page(username, userId, platform, stationId, status, type, start, end);
		page.getRows().forEach(x -> {
			x.setMsg(I18nTool.convertArrStrToMessage(x.getMsg()));
		});
		return page;
	}

	/**
	 * 手动修改日志状态
	 */
	@Override
	public void updateOrderStatus(Long id, Integer status, Long stationId) {
		ThirdTransLog log = thirdTransLogDao.findOne(id, stationId);
		if (log == null) {
			throw new ParamException(BaseI18nCode.thirdTransLogNotExist);
		}
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -5);
		if (log.getCreateDatetime().before(c.getTime())) {
			throw new ParamException(BaseI18nCode.fiveTransLogRecord);
		}
		String msg = I18nTool.getMessage(BaseI18nCode.stationSucc);
		if (status == ThirdTransLog.TRANS_STATUS_SUCCESS) {
			log.setMsg("手动处理成功");
			transferCompleteSuccess(log);
		} else {
			msg = I18nTool.getMessage(BaseI18nCode.stationFail);
			List <String> msgList = I18nTool.convertCodeToList(BaseI18nCode.manualProcessFail.getCode());
			log.setMsg(I18nTool.convertCodeToArrStr(msgList));
			transferCompleteFailed(log);
		}
		LogUtils.modifyLog("手动处理第三方转账：orderId=" + log.getOrderId() + " 状态为：" + msg);

	}

	/**
	 * 手动处理成功
	 *
	 * @param log
	 */
	private void transferCompleteSuccess(ThirdTransLog log) {
		SysUser acc = sysUserService.findOneById(log.getUserId(), log.getStationId());
		if (acc == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}
		PlatformType pt = PlatformType.getPlatform(log.getPlatform());
		if (pt == null) {
			throw new ParamException(BaseI18nCode.thirdPlatformUnExist);
		}
		if (log.getType() == ThirdTransLog.TYPE_INTO_THIRD) {
			// 如果是转入第三方，则只需修改转入日志为成功
			log.setAfterMoney(thirdCenterService.getBalanceForTrans(pt, acc, SystemUtil.getStation()));
			log.setUpdateDatetime(new Date());
			thirdTransLogDao.updateSuccess(log);
		} else {
			// 如果是转出到本系统，则修改成成功，并加钱
			log.setAfterMoney(thirdCenterService.getBalanceForTrans(pt, acc, SystemUtil.getStation()));
			log.setUpdateDatetime(new Date());
			if (thirdTransLogDao.updateSuccess(log)) {
				MnyMoneyBo vo = new MnyMoneyBo();
				// vo.setAccountId(acc.getId());
				vo.setUser(acc);
				vo.setMoney(log.getMoney());
				vo.setMoneyRecordType(MoneyRecordType.REAL_GAME_ADD);
				vo.setOrderId(log.getOrderId());
				vo.setStationId(acc.getStationId());
				vo.setBizDatetime(log.getCreateDatetime());
				//vo.setRemark(pt.getTitle() + " -> "+I18nTool.getMessage(BaseI18nCode.thisSystemPlat));
				List <String> remarkList = I18nTool.convertCodeToList(pt.name()," -> ",BaseI18nCode.thisSystemPlat.getCode());
				vo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
				sysUserMoneyService.updMnyAndRecord(vo);
			}
		}
	}

	/**
	 * 处理成失败
	 *
	 * @param log
	 */
	private void transferCompleteFailed(ThirdTransLog log) {
		SysUser acc = sysUserService.findOneById(log.getUserId(), log.getStationId());
		if (acc == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}
		if (log.getType() == ThirdTransLog.TYPE_INTO_THIRD) {
			// 如果是转入第三方失败，则修改日志为失败，并加钱
			if (thirdTransLogDao.updateFailed(log.getId(), log.getStationId(), log.getMsg())) {
				MnyMoneyBo vo = new MnyMoneyBo();
				vo.setUser(acc);
				vo.setMoney(log.getMoney());
				vo.setOrderId(log.getOrderId());
				vo.setStationId(log.getStationId());
				vo.setBizDatetime(log.getCreateDatetime());
				//vo.setRemark(I18nTool.getMessage(BaseI18nCode.thisSystemPlat)+" -> " + PlatformType.getPlatform(log.getPlatform()).getTitle() + I18nTool.getMessage(BaseI18nCode.thirdTurnSeedError));
				List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.thisSystemPlat.getCode()," -> ",PlatformType.getPlatform(log.getPlatform()).getTitle(),BaseI18nCode.thirdTurnSeedError.getCode());
				vo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
				vo.setMoneyRecordType(MoneyRecordType.REAL_GAME_FAILED);
				sysUserMoneyService.updMnyAndRecord(vo);
			}
		} else {
			// 如果是转出到本系统，则修改成失败
			thirdTransLogDao.updateFailed(log.getId(), log.getStationId(), log.getMsg());
		}
	}

	@Override
	public List<ThirdTransLog> findNeedCheck(Date startTime) {
		return thirdTransLogDao.findNeedCheck(startTime);
	}

	@Override
	public void checkStatus(ThirdTransLog log) {
		if (thirdCenterService.getOrderStatus(log, SystemUtil.getStation())) {
			transferCompleteSuccess(log);
		} else {
			transferCompleteFailed(log);
		}
	}
}
