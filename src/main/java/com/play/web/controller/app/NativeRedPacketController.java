package com.play.web.controller.app;


import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.ArticleTypeEnum;
import com.play.model.*;

import com.play.model.bo.RedPacketBo;
import com.play.service.*;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.HidePartUtil;
import com.play.web.utils.ServletUtils;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE + "/v2")
public class NativeRedPacketController extends FrontBaseController {

    @Autowired
    private StationRedPacketService stationRedPacketService;
    @Autowired
    private StationArticleService stationArticleService;
    @Autowired
    private StationDummyDataService dummyDataService;
    @Autowired
    private StationTurntableService turntableService;
    @Autowired
    private StationTurntableRecordService turntableRecordService;


    /**
     * 红包规则
     */
    @NotNeedLogin
    @RequestMapping("/redBagRule")
    @ResponseBody
    public void redBagRule(HttpServletRequest request) {
//        String lang = (String) request.getSession().getAttribute(Constants.SESSION_KEY_LANGUAGE);
        //        String lang = (String) request.getSession().getAttribute(Constants.SESSION_KEY_LANGUAGE);
        String lang = SystemUtil.getStation().getLanguage();//需要使用站点默认的语种，不可使用前后台已经变更后的语种
        List<StationArticle> stationArticles = stationArticleService.frontNews(SystemUtil.getStationId(),
                ArticleTypeEnum.RE_role.getCode(), lang, new Date(), null);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", stationArticles);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     * 抢红包记录
     */
    @RequestMapping("/redpacketRecord")
    @ResponseBody
    public void redpacketRecord(String username, Long redPacketId, Integer limit) {
//        Page<StationRedPacketRecord> recordPage = stationRedPacketService.getRecordPage(null,
//                SystemUtil.getStationId(), null, null);
        SysUser user = LoginMemberUtil.currentUser();
        Long stationId = SystemUtil.getStationId();
        List<StationRedPacketRecord> mrrList = new ArrayList<>();
        if (redPacketId != null) {
            mrrList.addAll(stationRedPacketService.getListBysidAndRid(stationId, redPacketId,
                    username != null ? username : (user != null ? user.getUsername() : null), limit));
        }
        List<StationDummyData> dataList = dummyDataService.getList(stationId, StationDummyData.REG_BAG, new Date());
        int idx = 0;
        if (dataList != null && dataList.size() > 0) {
            for (StationDummyData f : dataList) {
                if (idx < 200) {// 防止红包页面卡顿，控制假数据条数
                    StationRedPacketRecord mrr = new StationRedPacketRecord();
                    mrr.setUsername(f.getUsername());
                    if (f.getWinTime() != null) {
                        mrr.setCreateDatetime(f.getWinTime());
                    }
                    if (f.getWinMoney() != null) {
                        mrr.setMoney(f.getWinMoney());
                    }
                    mrrList.add(mrr);
                }
                idx++;
            }
        }
        Collections.sort(mrrList, new Comparator<StationRedPacketRecord>() {
            @Override
            public int compare(StationRedPacketRecord o1, StationRedPacketRecord o2) {
                return o2.getCreateDatetime().compareTo(o1.getCreateDatetime());
            }
        });
        for (int i = 0; i < mrrList.size(); i++) {
            mrrList.get(i).setUsername(HidePartUtil.removeAllKeep2(mrrList.get(i).getUsername()));
        }
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", /*recordPage*/mrrList);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     * 抢红包
     * App抢红包接口
     * App与Wap入参方式有问题，先临时分别用两个接口
     * @param packetId
     */
    @RequestMapping(value = "/actionRedPacket")
    @ResponseBody
    public void actionRedPacket(Long packetId) {
//        Long id = packetId != null ? packetId : bo.getPacketId();
//        if (packetId == null) {
//            throw new ParamException(BaseI18nCode.parameterEmpty);
//        }
        BigDecimal bigDecimal = stationRedPacketService.grabRedPacketPlan(packetId);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", bigDecimal);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     * 抢红包
     * Wap抢红包接口
     * App与Wap入参方式有问题，先临时分别用两个接口
     * @param bo
     */
    @RequestMapping(value = "/actionRedPacket2")
    @ResponseBody
    public void actionRedPacket2(@RequestBody RedPacketBo bo) {
//        Long id = packetId != null ? packetId : bo.getPacketId();
//        if (bo.getPacketId() == null) {
//            throw new ParamException(BaseI18nCode.parameterEmpty);
//        }

        BigDecimal bigDecimal = stationRedPacketService.grabRedPacketPlan(bo.getPacketId());
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", bigDecimal);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     * 获取可抢的红包
     */
    @RequestMapping("/getValidRedPacket")
    @ResponseBody
    public void getValidRedPacket() {
        StationRedPacket stationRedPacket = stationRedPacketService.getCurrentRedPacketNewForApp();
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", stationRedPacket);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     * @param startTime 真实中奖记录开始时间
     * @param endTime   真实中奖记录结束时间
     * @param username  真实中奖用户名
     * @param awardType 奖项类型
     * @param activeId  真实记录活动id
     * @param limit     限制多少条
     */
    @ResponseBody
    @RequestMapping("/turnlateFakeData")
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
                awardType, activeId, limit, null, null,null);
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
        if (sfList.size() > 150) {
            sfList = sfList.subList(0, 150);
        }
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", sfList);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    @ResponseBody
    @RequestMapping("/inviteBonusFakeData")
    @NotNeedLogin
    public void inviteBonusFakeData(String startTime, String endTime, Integer limit) {
        Date begin = DateUtil.toDatetime(startTime);
        if (begin == null) {
            begin = DateUtil.dayFirstTime(new Date(), 0);
        }
        Date end = DateUtil.toDatetime(endTime);
        if (end == null) {
            end = DateUtil.dayFirstTime(new Date(), 1);
        }
        Long stationId = SystemUtil.getStationId();
        List<StationDummyData> sfList = dummyDataService.getList(stationId, StationDummyData.INVITE_DEPOSIT, new Date());
        if (sfList == null || sfList.size() == 0) {
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
        sfList.sort((StationDummyData sf1, StationDummyData sf2) -> sf2.getWinTime().compareTo(sf1.getWinTime()));
        if (limit == null) {
            limit = 150;
        }
        if (sfList.size() > limit) {
            sfList = sfList.subList(0, limit);
        }
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", sfList);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     * 获取用户红包记录
     *
     * @param packetId
     * @param startTime
     * @param endTime
     */
    @RequestMapping("/getUserRedPacketRecordList")
    @ResponseBody
    public void getUserRedPacketRecordList(Long packetId, String startTime, String endTime) {

        Date startDate = DateUtil.toDatetime(startTime);
        if (startDate == null) {
            startDate = DateUtil.dayFirstTime(new Date(), 0);
        }
        Date endDate = DateUtil.toDatetime(endTime);
        if (endDate == null) {
            endDate = DateUtil.dayFirstTime(new Date(), 1);
        }
        try{
            SysUser sysUser = LoginMemberUtil.currentUser();
            List<StationRedPacketRecord> list = stationRedPacketService.getUserRedPacketRecordList(sysUser.getId(), sysUser.getStationId(),
                    packetId, StationRedPacketRecord.STATUS_SUCCESS, startDate, endDate);

            Map<String, Object> json = new HashMap<>();
            json.put("success", true);
            json.put("content", list);
            json.put("accessToken", ServletUtils.getSession().getId());
            renderJSON(json);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("get user red packet record error = ", e);
        }
    }
}
