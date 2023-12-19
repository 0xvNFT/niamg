package com.play.service;

import com.alibaba.fastjson.JSONObject;
import com.play.core.PlatformType;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.ThirdTransLog;
import com.play.model.vo.ThirdSearchVo;

import java.math.BigDecimal;
import java.util.Map;

public interface ThirdCenterService {

    public JSONObject login(PlatformType pt, SysUser acc, Station station, Map<String, Object> map);
    public JSONObject loginPgTest(PlatformType pt, SysUser acc, Station station, Map<String, Object> map);

    public BigDecimal getBalance(PlatformType pt, SysUser acc, Station station, boolean checkMain);

    public boolean getOrderStatus(ThirdTransLog log, Station station);

    public boolean getOrderStatus(Long id, Station station);

    public JSONObject retrieveBalanceByAgent(PlatformType pt, SysUser account, BigDecimal money, Station station);

    public String findThirdUser(String username, String thirdUsername, Integer platform, Integer pageSize,
                                Integer pageNumber, Station station);

    public String getStationQuota(Integer platform, Station station);

    public String getEgamePage(ThirdSearchVo v);

    public String getLivePage(ThirdSearchVo v);

    public String getChessPage(ThirdSearchVo v);

    public String getPtPage(ThirdSearchVo v);

    public String getSportPage(ThirdSearchVo v);

    public String getEsportPage(ThirdSearchVo v);

    public String getFishingPage(ThirdSearchVo v);

    public String getLotteryPage(ThirdSearchVo v);

    public String thirdReport(String startDate, String endDate, Station station);

    public ThirdTransLog transToThirdStep1(PlatformType pt, SysUser acc, BigDecimal money, Station station);

    public JSONObject transToThirdStep2(SysUser acc, ThirdTransLog log, PlatformType pt, Station station);

    public ThirdTransLog takeOutToSysStep1(PlatformType pt, SysUser acc, BigDecimal money, Station station);

    public ThirdTransLog takeOutToSys(PlatformType pt, SysUser acc, Station station);

    public JSONObject takeOutToSysStep2(SysUser acc, ThirdTransLog log, PlatformType pt, Station station);

    /**
     * @param recordId
     * @param platform
     * @param type     1=live,2=egame,3=chess,4=sport,5=esport,6=fishing,7=lottery
     * @param station
     * @return
     */
    public String getDetailUrl(Long recordId, Integer platform, Integer type, Station station);
    public JSONObject getThirdAdminUrl(Map<String, Object> map,Station station,Integer platform);

    public void checkMaintenance(PlatformType pf, Long stationId);

    public BigDecimal getBalanceForTrans(PlatformType pt, SysUser acc, Station station);

    public JSONObject getLotteryList(Integer platform, Long stationId,Integer lotVersion);

}
