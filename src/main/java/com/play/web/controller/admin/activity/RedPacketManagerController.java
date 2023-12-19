package com.play.web.controller.admin.activity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationRedPacket;
import com.play.model.StationRedPacketRecord;
import com.play.model.SysUserDegree;
import com.play.service.StationRedPacketDegreeService;
import com.play.service.StationRedPacketService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

/**
 * 红包管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/redPacket")
public class RedPacketManagerController extends AdminBaseController {

	@Autowired
	private StationRedPacketService stationRedPacketService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;

	@Autowired
	private StationRedPacketDegreeService packetDegreeService;

	/**
	 * 红包管理首页
	 */
	@Permission("admin:redPacket")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		return returnPage("/activity/redPacket/redPacketIndex");
	}

	/**
	 * 红包列表
	 */
	@Permission("admin:redPacket")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("红包列表")
	public void list() {
		renderJSON(stationRedPacketService.getPage(SystemUtil.getStationId()));
	}

	/**
	 * 裂变红包页面
	 */
	@Permission("admin:redPacket:add")
	@RequestMapping("/showFissionAdd")
	public String showFissionAdd(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/activity/redPacket/redPacketFissionAdd");
	}

	/**
	 * 发策略红包页面
	 */
	@Permission("admin:redPacket:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/activity/redPacket/redPacketAdd");
	}

	/**
	 * 保存策略红包
	 */
	@Permission("admin:redPacket:add")
	@RequestMapping("/addSave")
	@ResponseBody
	public void addSave(StationRedPacket redPacket, Long[] degreeIds, Long[] groupIds, String begin, String end) {
		if (degreeIds != null && degreeIds.length > 0) {
			redPacket.setDegreeIds(","+StringUtils.join(degreeIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			redPacket.setGroupIds(","+StringUtils.join(groupIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		redPacket.setStationId(SystemUtil.getStationId());
		redPacket.setBeginDatetime(DateUtil.toDatetime(begin));
		redPacket.setEndDatetime(DateUtil.toDatetime(end));
		stationRedPacketService.saveRedPacket(redPacket, degreeIds);
		renderSuccess();
	}

	/**
	 * 保存裂变红包
	 */
	@Permission("admin:redPacket:add")
	@RequestMapping("/addFissionSave")
	@ResponseBody
	public void addFissionSave(StationRedPacket redPacket, Long[] degreeIds, Long[] groupIds, String begin, String end) {
		if (degreeIds != null && degreeIds.length > 0) {
			redPacket.setDegreeIds(","+StringUtils.join(degreeIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			redPacket.setGroupIds(","+StringUtils.join(groupIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		redPacket.setStationId(SystemUtil.getStationId());
		redPacket.setBeginDatetime(DateUtil.toDatetime(begin));
		redPacket.setEndDatetime(DateUtil.toDatetime(end));
		stationRedPacketService.saveRedPacketFission(redPacket, degreeIds);
		renderSuccess();
	}

	/**
	 * 按层级发红包
	 *
	 * @return
	 */
	@Permission("admin:redPacket:add")
	@RequestMapping("/shoaAddDegree")
	public String shoaAddDegree(Map<String, Object> map) {
		List<SysUserDegree> degrees = userDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE);
		map.put("degrees", degrees);
		map.put("degreesNum", degrees.size());
		return returnPage("/activity/redPacket/redPacketAddDegree");
	}

	/**
	 * 保存按层级发红包
	 *
	 //* @param mrp
	 */
	@Permission("admin:redPacket:add")
	@RequestMapping("/saveDegree")
	@ResponseBody
	public void saveDegree(StationRedPacket redPacket, Long[] redBagDid, Integer[] redBagTmn, Long[] redBagMin,
			Long[] redBagMax, String begin, String end) {
		if (StringUtils.isEmpty(begin) || StringUtils.isEmpty(end)) {
			throw new BaseException(BaseI18nCode.stationActBegTiemMust);
		}
		redPacket.setBeginDatetime(DateUtil.toDatetime(begin));
		redPacket.setEndDatetime(DateUtil.toDatetime(end));
		redPacket.setStationId(SystemUtil.getStationId());
		stationRedPacketService.saveRedPacketDegree(redPacket, redBagDid, redBagTmn, redBagMin, redBagMax);
		renderSuccess();
	}

	/**
	 * 按充值量发红包
	 *
	 * @return
	 */
	@Permission("admin:redPacket:add")
	@RequestMapping("/showAddRecharge")
	public String showAddRecharge(Map<String, Object> map) {
		map.put("degrees", userDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
		map.put("curDate", DateUtil.getCurrentTime());
		map.put("afterYearDate", DateUtil.getRollDay(new Date(), 365));
		return returnPage("/activity/redPacket/redPacketAddRecharge");
	}

	/**
	 * 保存按充值量发红包
	 *
	 */
	@Permission("admin:redPacket:add")
	@RequestMapping("/saveRecharge")
	@ResponseBody
	public void saveRecharge(StationRedPacket redPacket, Long[] degreeIds, Long[] redBagRechargeMin,
			Long[] redBagRechargeMax, Integer[] packetBumber, Integer[] ipNumber, Long[] redBagMin, Long[] redBagMax,
			String begin, String end, String hisBeginStr, String hisEndStr) {
		if (StringUtils.isEmpty(begin) || StringUtils.isEmpty(end)) {
			throw new BaseException(BaseI18nCode.stationActBegTiemMust);
		}
		redPacket.setStationId(SystemUtil.getStationId());
		redPacket.setHisBegin(DateUtil.toDatetime(hisBeginStr));
		redPacket.setHisEnd(DateUtil.toDatetime(hisEndStr));
		redPacket.setBeginDatetime(DateUtil.toDatetime(begin));
		redPacket.setEndDatetime(DateUtil.toDatetime(end));
		stationRedPacketService.saveRedPacketRecharge(redPacket, degreeIds, redBagRechargeMin, redBagRechargeMax,
				packetBumber, ipNumber, redBagMin, redBagMax);
		renderSuccess();
	}

	@Permission("admin:redPacket:add")
	@RequestMapping("/cloneOneRedBag")
	public String cloneOneRedBag(Map<String, Object> map) {
		String date = DateUtil.getRollDay(new Date(), 1);
		map.put("startTime", date + " 00:00:00");
		map.put("endTime", date + " 23:59:59");
		return returnPage("/activity/redPacket/redPacketClone");
	}

	@Permission("admin:redPacket:add")
	@RequestMapping("/doCloneOne")
	@ResponseBody
	public void doCloneOne(Long rid, String begin, String end, Integer addDay) {
		stationRedPacketService.cloneRedBag(rid, SystemUtil.getStationId(), begin, end, addDay);
		renderSuccess();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@Permission("admin:redPacket:add")
	@RequestMapping("/delete")
	public void delete(Long id) {
		stationRedPacketService.delRedPacket(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@Permission("admin:redPacket:add")
	@RequestMapping("/deleteAll")
	public void deleteAll(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ParamException();
		}
		String[] delIds = ids.split(",");
		Long stationId = SystemUtil.getStationId();
		for (String idStr : delIds) {
			stationRedPacketService.delRedPacket(Long.parseLong(idStr), stationId);
		}
		renderSuccess();
	}
	
	 /**
     * 查看
     *
     * @param map
     * @param id
     * @return
     */
    @RequestMapping("/view")
    @NeedLogView("红包详情")
    public String view(Map<String, Object> map, Long id) {
        StationRedPacket redSet = stationRedPacketService.findOne(id, SystemUtil.getStationId());
        map.put("curRedBag", redSet);
        map.put("degreeNames",userDegreeService.getDegreeNames(redSet.getDegreeIds(), redSet.getStationId()));
        map.put("groupNames",userGroupService.getGroupNames(redSet.getGroupIds(), redSet.getStationId()));
        StationRedPacketRecord redPag = stationRedPacketService.getMoneyAndCount(id);
        if (redPag.getNum() != null && redPag.getNum() == redSet.getTotalNumber()) {
            redPag.setMoney(redSet.getTotalMoney());
        }
        map.put("grabMoney", redPag.getMoney() != null ? redPag.getMoney() : BigDecimal.ZERO);
        map.put("grabNum", redPag.getNum() != null ? redPag.getNum() : 0);
        return returnPage("/activity/redPacket/redPacketView");
    }

    @RequestMapping("/viewLevel")
    public String viewLevel(Map<String, Object> map, Long id) {
    	Long stationId=SystemUtil.getStationId();
        map.put("curRedBag", stationRedPacketService.findOne(id, stationId));
        map.put("list", packetDegreeService.list(stationId, id, true));
        return returnPage("/activity/redPacket/redPacketViewDegree");
    }

    @RequestMapping("/viewRecharge")
    public String viewRecharge(Map<String, Object> map, Long id) {
    	Long stationId=SystemUtil.getStationId();
        StationRedPacket mr = stationRedPacketService.findOne (id, stationId );
        map.put("curRedBag", mr);
        map.put("list", packetDegreeService.list(stationId , id, true));
        map.put("degreeNames",userDegreeService.getDegreeNames(mr.getDegreeIds(), stationId));
        return returnPage("/activity/redPacket/redPacketViewRecharge");
    }

    /**
     * 修改红包状态
     */
    @ResponseBody
    @Permission("admin:redPacket:status")
    @RequestMapping("/updStatus")
    public void updStatus(Long id, Integer status) {
        stationRedPacketService.updStatus(id, status, SystemUtil.getStationId());
        renderSuccess();
    }

}
