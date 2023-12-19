package com.play.web.controller.front.usercenter;

import java.util.*;

import com.play.model.*;
import com.play.service.StationTurntableGiftService;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginMemberUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.utils.DateUtil;

import com.play.service.StationDummyDataService;
import com.play.service.StationTurntableRecordService;
import com.play.service.StationTurntableService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.HidePartUtil;
import com.play.web.var.SystemUtil;

import static com.play.model.StationTurntableRecord.STATUS_SUCCESS;

/**
 * 大转盘
 * 前端展示
 */
@Controller
@RequestMapping("/userTurnlate")
public class UserTurnlateController extends FrontBaseController {

    @Autowired
    private StationTurntableService turntableService;
    @Autowired
    private StationTurntableRecordService turntableRecordService;
    @Autowired
    private StationDummyDataService dummyDataService;
    @Autowired
    private StationTurntableGiftService stationTurntableGiftService;

    @ResponseBody
    @RequestMapping("/award")
    public void award(Map<String, Object> map, Long activeId) {
        StationTurntable ma = turntableService.findOne(activeId, SystemUtil.getStationId());
        if (ma == null) {
            throw new ParamException();
        }
        SysUser user = LoginMemberUtil.currentUser();
		if(GuestTool.isGuest(user)) {
			 throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}
        String playConfig = ma.getPlayConfig();
        StationTurntableAward saa;
        if (StringUtils.isEmpty(playConfig)) {
            saa = turntableService.play(ma);
        } else {
            saa = turntableService.playPay(ma);
        }
        map.put("index", saa.getAwardIndex());
        map.put("awardName", saa.getAwardName());
        if (Objects.nonNull(user)) {
            // 用户积分
            map.put("userScore", userScoreService.getScore(user.getId(), SystemUtil.getStationId()));
        }
        if (saa.getAwardType().compareTo(3) == 0) {
            map.put("awardsType", saa.getAwardType());
            StationTurntableGift stationTurntableGift = stationTurntableGiftService.findOne(saa.getGiftId(), SystemUtil.getStationId());
            if (Objects.nonNull(stationTurntableGift)){
                map.put("productImg", stationTurntableGift.getProductImg());
            }

        }
        renderJSON(map);
    }

    @ResponseBody
    @RequestMapping("/records")
    @NotNeedLogin
    public void records(String startTime, String endTime, String username, Integer awardType, Long activeId, Integer limit) {
        Date begin = DateUtil.toDatetime(startTime);
        if (begin == null) {
            begin = DateUtil.dayFirstTime(new Date(), 0);
        }
        Date end = DateUtil.toDatetime(endTime);
        if (end == null) {
            end = DateUtil.dayFirstTime(new Date(), 1);
        }
        Long stationId = SystemUtil.getStationId();
        List<StationTurntableRecord> sarList = turntableRecordService.getRecoreList(stationId, begin, end, username,
                awardType, activeId, limit, STATUS_SUCCESS,null,null);
        List<StationDummyData> sfList = dummyDataService.getList(stationId, StationDummyData.TRUN_ROUND, new Date());
        if ((sarList == null || sarList.size() == 0) && (sfList == null || sfList.size() == 0)) {
            renderJSON(sfList);
            return;
        }
        sfList.forEach(s -> {
            if (s.getUsername() != null) {
                s.setUsername(HidePartUtil.removeAllKeep2(s.getUsername()));
            }
            if (StringUtils.isEmpty(s.getItemName())) {
                s.setItemName(s.getWinMoney() + "");
            }
        });
        if (!CollectionUtils.isEmpty(sarList)) {
            StationDummyData sfd = null;
            for (StationTurntableRecord s : sarList) {
                sfd = new StationDummyData();
                if (s.getUsername() != null) {
                    sfd.setUsername(HidePartUtil.removeAllKeep2(s.getUsername()));
                }
                sfd.setItemName(s.getGiftName());
                if (StringUtils.isEmpty(sfd.getItemName())) {// 为了配合假数据 转盘数据特殊处理 前端展示统一用itemName
                    sfd.setItemName(s.getAwardValue() + "");
                }
                sfd.setWinMoney(s.getAwardValue());
                sfd.setWinTime(s.getCreateDatetime());
                StationTurntable ma = turntableService.findOne(activeId, SystemUtil.getStationId());
                if (s.getTurntableId().compareTo(ma.getId()) == 0) {
                    sfd.setImgPath(ma.getImgPath());
                }
                sfList.add(sfd);
            }
        }
        sfList.sort((StationDummyData sf1, StationDummyData sf2) -> sf2.getWinTime().compareTo(sf1.getWinTime()));
        if (sfList.size() > 300) {
            sfList = sfList.subList(0, 300);
        }
        renderJSON(sfList);
    }


    @ResponseBody
    @RequestMapping("/turnlateRecordList")
    public void record(String startTime, String endTime, Integer awardType, Long activeId, Integer limit,String productName) {
        Date begin = DateUtil.toDatetime(startTime);
        if (begin == null) {
            begin = DateUtil.dayFirstTime(new Date(), 0);
        }
        Date end = DateUtil.toDatetime(endTime);
        if (end == null) {
            end = DateUtil.dayFirstTime(new Date(), 1);
        }
        Long stationId = SystemUtil.getStationId();
        List<StationTurntableRecord> sarList = turntableRecordService.getRecoreList(stationId, begin, end, LoginMemberUtil.getUsername(),
                awardType, activeId, limit, null, LoginMemberUtil.getUserId(),productName);
        if (sarList.size() > 300) {
            sarList = sarList.subList(0, 300);
        }
        renderJSON(sarList);
    }
}
