package com.play.service;

import com.alibaba.fastjson.JSONObject;
import com.play.core.PlatformType;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.ThirdTransLog;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface YGCenterService {

    public JSONObject createUser(SysUser sysUser);

    public JSONObject deposit(String data,String sign);

    public JSONObject withdraw(String data,String sign);

    public JSONObject getLoginUrl(Map<String, Object> map);

    public JSONObject getAdminLoginUrl(Map<String, Object> map);

    public JSONObject findAccountInfo(String data);

    public JSONObject getLotteryOrder(String orderId,String username,String qiHao,String gameCode,
                                      String startTime,String endTime,Integer pageNumber,Integer pageSize);


    public boolean getOrderStatus(ThirdTransLog log, Station station);

    public boolean getOrderStatus(Long id, Station station);

    public JSONObject getLotteryList();
    public JSONObject guestRegister(String username);

    public ThirdTransLog transToThirdStep1(PlatformType pt, SysUser acc, BigDecimal money, Station station);

    public JSONObject transToThirdStep2(SysUser acc, ThirdTransLog log, PlatformType pt, Station station);

    public ThirdTransLog takeOutToSysStep1(PlatformType pt, SysUser acc, BigDecimal money, Station station);

    public JSONObject takeOutToSysStep2(SysUser acc, ThirdTransLog log, PlatformType pt, Station station);

    public BigDecimal getBalanceForTrans(PlatformType pt, SysUser acc, Station station);

    public void checkMaintenance(PlatformType pf, Long stationId);

    public JSONObject convertYGData(JSONObject json);

}
