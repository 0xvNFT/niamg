package com.play.web.controller.admin.activity;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.cache.redis.DistributedLockUtil;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.ScoreRecordTypeEnum;
import com.play.model.SysUser;
import com.play.model.SysUserScoreHistory;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserScoreHistoryService;
import com.play.service.SysUserScoreService;
import com.play.service.SysUserService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

/**
 * 会员积分记录
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/scoreHistory")
public class ScoreHistoryController extends AdminBaseController {

	@Autowired
	private SysUserScoreHistoryService userScoreHistoryService;
	@Autowired
	private SysUserScoreService userScoreService;
	@Autowired
	private SysUserService userService;

	/**
	 * 会员积分记录首页
	 */
	@Permission("admin:scoreHistory")
	@RequestMapping("/index")
	public String index(Map<Object, Object> map, String startTime, String endTime) {
		map.put("startTime",
				StringUtils.isEmpty(startTime) ? DateUtil.formatDate(new Date(), "yyyy-MM-dd") + " 00:00:00"
						: startTime);
		map.put("endTime",
				StringUtils.isEmpty(endTime) ? DateUtil.formatDate(new Date(), "yyyy-MM-dd" + " 23:59:59") : endTime);
		JSONArray jsonArray = ScoreRecordTypeEnum.getTypeArray();
		for(int i=0;i<jsonArray.size();i++){
		    JSONObject jsonObject =  jsonArray.getJSONObject(i);
			//jsonObject.put("type","type"+jsonObject.getInteger("type")+"value");
			jsonObject.put("type",jsonObject.getInteger("type"));
		}
		map.put("scoreTypes", jsonArray);
		return returnPage("/activity/score/scoreHistoryIndex");
	}

	@Permission("admin:scoreHistory")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("会员积分列表")
	public void list(String startTime, String endTime, Integer type, String username) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			renderJSON(new Page<SysUserScoreHistory>());
		} else {
			Date begin = DateUtil.toDatetime(startTime);
			Date end = DateUtil.toDatetime(endTime);
			renderJSON(userScoreHistoryService.getPage(SystemUtil.getStationId(), username, begin, end, type, null));
		}
	}

	/**
	 * 添加积分
	 */
	@Permission("admin:scoreHistory:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<Object, Object> map) {
		return returnPage("/activity/score/scoreAdd");
	}

	@Permission("admin:scoreHistory:add")
	@RequestMapping("/doSave")
	@ResponseBody
	public void doSave(Integer type, String username, Long score, String remark) {
		if (type == null) {
			renderError(BaseI18nCode.operatingTypeError.getMessage());
			return;
		}
		if (score == null || score <= 0) {
			renderError(BaseI18nCode.insufficientPoints.getMessage());
			return;
		}
		ScoreRecordTypeEnum rt = ScoreRecordTypeEnum.getScoreRecordType(type);
		if (rt == null || (rt != ScoreRecordTypeEnum.ADD_ARTIFICIAL && rt != ScoreRecordTypeEnum.SUB_ARTIFICIAL)) {
			renderError(BaseI18nCode.stationRigTypeInput.getMessage());
			return;
		}
		SysUser user = userService.findOneByUsername(username, SystemUtil.getStationId(), null);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		userScoreService.operateScore(rt, user, score, remark);
		renderSuccess();
	}

	/**
	 * 批量加积分
	 */
	@Permission("admin:scoreHistory:add")
	@RequestMapping("/showBatchAdd")
	public String showBatchAdd(Map<Object, Object> map) {
		return returnPage("/activity/score/scoreBatchAdd");
	}

	@ResponseBody
	@Permission("admin:scoreHistory:add")
	@RequestMapping("/doBatchAdd")
	synchronized public void doBatchAdd(String usernames, String remark, Integer modelType, Long score) {
		if (!DistributedLockUtil.tryGetDistributedLock("batchAddScore:" + SystemUtil.getStationId(), 3)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit);
		}
		try {
			userScoreService.batchAddScore(modelType, score, usernames, SystemUtil.getStationId(), remark);
		} catch (Exception e) {
			renderError(e.getMessage());
			return;
		}
		renderSuccess();
	}
}
