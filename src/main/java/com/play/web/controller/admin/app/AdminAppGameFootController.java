package com.play.web.controller.admin.app;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.HotGameTypeEnum;
import com.play.model.AppGameFoot;
import com.play.model.vo.AgentFootPrintVo;
import com.play.model.vo.AppThirdGameVo;
import com.play.service.AppGameFootService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.exception.ExistedException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.SystemUtil;

/**
 * 足迹管理
 * 
 * @author johnson
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/appGameFoot")
public class AdminAppGameFootController extends AdminBaseController {

	Logger logger = LoggerFactory.getLogger(AdminAppGameFootController.class.getSimpleName());

	@Autowired
	private AppGameFootService appGameFootService;

	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
	//	logger.error("footprint index -------");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		map.put("startDate", DateUtil.toDateStr(c.getTime()));
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);
		map.put("endDate", DateUtil.toDateStr(c.getTime()));
		return returnPage("/app/footprint/index");
	}

	@RequestMapping("/list")
	@ResponseBody
	public void list(String startDate, String endDate, String account) {
		AgentFootPrintVo vo = new AgentFootPrintVo();
		vo.setStationId(SystemUtil.getStationId());
		vo.setBegin(DateUtil.toDate(startDate));
		vo.setEnd(DateUtil.toDate(endDate));
		vo.setAccount(account);
		renderJSON(appGameFootService.page(vo));
	}

	@RequestMapping("/updStatus")
	@ResponseBody
	public void updStatus(Integer status, Long id) {
		Long stationId = SystemUtil.getStationId();
		appGameFootService.openCloseH(status, id, stationId);
		renderSuccess();
	}

	@Permission("admin:appGameFoot:modify")
	@RequestMapping("/modify")
	public String modify(Long id, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		AppGameFoot activity = appGameFootService.getOne(id, stationId);
		if (activity.getType() == 0) {
			throw new BaseException(BaseI18nCode.stationNoPermOnlyUserUpdate);
		}
		map.put("typeList", HotGameTypeEnum.getList());
		map.put("hotGame", activity);
		return super.returnPage("/app/footprint/modify");
	}

	@Permission("admin:appGameFoot:add")
	@RequestMapping("/add")
	public String add(Map<String, Object> map) {
		map.put("typeList", HotGameTypeEnum.getList());
		return super.returnPage("/app/footprint/add");
	}

	/**
     * 修改
     */
    @RequestMapping("/modifySave")
    @ResponseBody
    public void eidtSaveNew(AppGameFoot aaty) {
        if (StringUtils.isEmpty(aaty.getGameCode())) {
            throw new BaseException(BaseI18nCode.stationNoSelectedGame);
        }
        Long stationId = SystemUtil.getStationId();
        aaty.setType(1);
        aaty.setCreateDatetime(new Date());
        aaty.setStationId(stationId);
        appGameFootService.editSave(appendIconForFootPrint(aaty));
        renderSuccess();
    }

	/**
	 */
	@RequestMapping("/addSave")
	@ResponseBody
	public void addSave(AppGameFoot agentFootPrint) {
		try {
			if (StringUtils.isEmpty(agentFootPrint.getGameCode())) {
				throw new ParamException(BaseI18nCode.parameterError);
			}
			AppGameFoot foot = appGameFootService.getOneByGameCode(agentFootPrint.getGameCode(),
					SystemUtil.getStationId());
			if (foot != null) {
				throw new ExistedException(BaseI18nCode.hasExistGameError);
			}
			Long stationId = SystemUtil.getStationId();
			agentFootPrint.setType(1);
			agentFootPrint.setCreateDatetime(new Date());
			agentFootPrint.setStationId(stationId);
			agentFootPrint = appendIconForFootPrint(agentFootPrint);
			appGameFootService.addSave(agentFootPrint);
			renderSuccess();
		} catch (Exception e) {
			renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
		}
	}

	private AppGameFoot appendIconForFootPrint(AppGameFoot footPrint) {
		if (footPrint == null) {
			return footPrint;
		}
		if (footPrint.getIsSubGame() == null) {
			return footPrint;
		}
		if (footPrint.getIsSubGame() == 1 || footPrint.getIsSubGame() == 2) {// 是二级子游戏情况下，已经有游戏图标和跳转链接了
			return footPrint;
		}
		Integer type = NativeUtils.convertTabGameType2HotGameType(footPrint.getGameType());
		List<AppThirdGameVo> otherGame = NativeUtils.getOtherGame(type);
		if (otherGame != null && !otherGame.isEmpty()) {
			for (AppThirdGameVo game : otherGame) {
				if (game != null && game.getGameType().equalsIgnoreCase(footPrint.getGameCode())) {
					footPrint.setCustomIcon(game.getImgUrl());
					footPrint.setCustomLink(game.getForwardUrl());
					break;
				}
			}
		}
		return footPrint;
	}

	/**
	 * 删除
	 */
	@Permission("admin:appGameFoot:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		Long stationId = SystemUtil.getStationId();
		appGameFootService.delete(id, stationId);
		renderSuccess();
	}

}
