package com.play.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.play.common.Constants;
import com.play.common.ip.IpUtils;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.common.utils.OrderIdMaker;
import com.play.common.utils.security.MD5Util;
import com.play.core.*;
import com.play.dao.ThirdTransLogDao;
import com.play.dao.ThirdTransferOutLimitDao;
import com.play.model.*;
import com.play.model.bo.MnyMoneyBo;
import com.play.model.vo.ThirdSearchVo;
import com.play.service.*;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.HttpResponseException;
import com.play.web.exception.ParamException;
import com.play.web.exception.ThirdException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.RateUtil;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.ValidateUtil;
import com.play.web.utils.http.HttpClientProxy;
import com.play.web.utils.http.HttpClientProxyForTransMoney;
import com.play.web.utils.http.HttpConnectionManager;
import com.play.web.utils.http.HttpConnectionManagerForOrder;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ThirdCenterServiceImpl implements ThirdCenterService {

    private static final String EXCHANGE_MD5_KEY = "WIPJ@(*#HJlkjfihep2f879[8&G*(){W(*E&GYBUOHOJP(@G&(*@)!)(P@}_!IH0Q289EHGOYP";

    private Logger logger = LoggerFactory.getLogger(ThirdCenterServiceImpl.class);

    private static String realCenterDomain;
    private static String realCenterToken;

    @Autowired
    private ThirdTransLogDao thirdTransLogDao;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysUserMoneyService moneyService;
    @Autowired
    private ThirdTransferOutLimitDao thirdTransferOutLimitDao;
    @Autowired
    private ThirdAutoExchangeService autoExchangeService;
    @Autowired
    private ThirdPlatformService thirdPlatformService;
    @Autowired
    StationService stationService;

    @Override
    public JSONObject login(PlatformType pt, SysUser acc, Station station, Map<String, Object> map) {
  //      logger.error("login method pt={}, acc={},station={} ,map={}", pt, acc, station, JSONObject.toJSONString(map));
        this.checkMaintenance(pt, acc.getStationId());
        String json = new HttpClientProxy() {
            @Override
            public List<NameValuePair> getParameters() {
                List<NameValuePair> params = getBasicParams(pt, acc, station);
                for (String key : map.keySet()) {
                    params.add(new BasicNameValuePair(key, String.valueOf(map.get(key))));
                }
                return params;
            }
        }.doPost(getUrl("/api/member/login.do"));
        JSONObject jsonObj = JSON.parseObject(json);
        if (jsonObj.getBooleanValue("maintain")) {
            throw new BaseException(BaseI18nCode.stationLiveCenter);
        }
        checkError(jsonObj);
        autoExchangeService.autoExchange(acc, pt, station);
        return jsonObj;
    }

    @Override
    public JSONObject loginPgTest(PlatformType pt, SysUser acc, Station station, Map<String, Object> map) {
        //      logger.error("login method pt={}, acc={},station={} ,map={}", pt, acc, station, JSONObject.toJSONString(map));
        this.checkMaintenance(pt, acc.getStationId());
        String json = new HttpClientProxy() {
            @Override
            public List<NameValuePair> getParameters() {
                List<NameValuePair> params = getBasicParams(pt, acc, station);
                for (String key : map.keySet()) {
                    params.add(new BasicNameValuePair(key, String.valueOf(map.get(key))));
                }
                return params;
            }
        }.doPost(getUrl("/api/member/loginPgTest.do"));
        JSONObject jsonObj = JSON.parseObject(json);
        if (jsonObj.getBooleanValue("maintain")) {
            throw new BaseException(BaseI18nCode.stationLiveCenter);
        }
        checkError(jsonObj);
        autoExchangeService.autoExchange(acc, pt, station);
        return jsonObj;
    }


    private List<NameValuePair> getBasicParams(PlatformType pt, SysUser user, Station station) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", getToken()));
        params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
        params.add(new BasicNameValuePair("code", station.getCode()));
        params.add(new BasicNameValuePair("stationName", station.getName()));
        params.add(new BasicNameValuePair("currency", station.getCurrency()));
        params.add(new BasicNameValuePair("isGuest", String.valueOf(GuestTool.isGuest())));
        //params.add(new BasicNameValuePair("language", station.getLanguage()));
        params.add(new BasicNameValuePair("platform", String.valueOf(pt.getValue())));
        params.add(new BasicNameValuePair("userId", String.valueOf(user.getId())));
        params.add(new BasicNameValuePair("username", user.getUsername()));
        params.add(new BasicNameValuePair("parentIds", user.getParentIds()));
        params.add(new BasicNameValuePair("language", SystemUtil.getLanguage()));
        if (user.getRecommendId() != null) {
            params.add(new BasicNameValuePair("recommendId", String.valueOf(user.getRecommendId())));
        }
        params.add(new BasicNameValuePair("userIp", IpUtils.getIp()));
        return params;
    }

    private void checkError(JSONObject json) {
        if (!json.getBooleanValue("success")) {
            logger.error("ThirdCenter checkError, json:{}", json);
            String msg = json.getString("msg");
            if (StringUtils.isNotEmpty(msg) && msg.contains("会员不存在")) {
                msg = I18nTool.getMessage(BaseI18nCode.memberUnExist);
                throw new BaseException(msg);
            }
            throw new BaseException(I18nTool.getMessage(ValidateUtil.isCodeError(json.getString("code"), msg)));
        }
    }

    /**
     * 获取真人中心授权口令
     *
     * @return
     */
    private String getToken() {
        if (StringUtils.isNotEmpty(realCenterToken)) {
            return realCenterToken;
        }
        realCenterToken = sysConfigService.findValue(SysConfigEnum.third_center_token);
        if (StringUtils.isEmpty(realCenterToken)) {
            throw new BaseException(BaseI18nCode.stationAdminContactSetting);
        }
        return realCenterToken;
    }

    /**
     * 获取真人中心网址
     *
     * @param url
     * @return
     */
    private String getUrl(String url) {
        if (StringUtils.isNotEmpty(realCenterDomain)) {
            return new StringBuilder(realCenterDomain).append(url).toString();
        }
        realCenterDomain = sysConfigService.findValue(SysConfigEnum.third_center_url);
        if (StringUtils.isEmpty(realCenterDomain)) {
            throw new BaseException(BaseI18nCode.stationAdminContactLiveAddress);
        }
        return new StringBuilder(realCenterDomain).append(url).toString();
    }

    /**
     * @param pt
     * @param acc
     * @param station
     * @param checkMain
     * @return
     */
    @Override
    public BigDecimal getBalance(PlatformType pt, SysUser acc, Station station, boolean checkMain) {
        if (checkMain) {
            this.checkMaintenance(pt, acc.getStationId());
        }
        try {
            String json = new HttpClientProxy() {
                @Override
                public List<NameValuePair> getParameters() {
                    return getBasicParams(pt, acc, station);
                }
            }.doPost(getUrl("/api/member/getBalance.do"));
            JSONObject obj = JSON.parseObject(json);
            if (obj.getBooleanValue("maintain")) {
           //     logger.error(obj.getString("msg"));
                return BigDecimal.ZERO;
            }
            checkError(obj);
            return BigDecimalUtil.toBigDecimalDefaultZero(obj.getString("balance")).setScale(2, RoundingMode.DOWN);
        } catch (BaseException e) {
            logger.error("获取余额发生错误1, username:{}, stationId:{}, pt:{}", acc.getUsername(), acc.getStationId(), pt.name(), e);
            return BigDecimal.ZERO;
        } catch (JSONException e) {
            logger.error("获取余额发生错误2, username:{}, stationId:{}, pt:{}", acc.getUsername(), acc.getStationId(), pt.name(), e);
            return BigDecimal.ZERO;
        } catch (Exception e) {
            logger.error("获取余额发生错误3, username:{}, stationId:{}, pt:{}", acc.getUsername(), acc.getStationId(), pt.name(), e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public boolean getOrderStatus(Long id, Station station) {
        ThirdTransLog log = thirdTransLogDao.findOne(id, station.getId());
        if (log == null) {
            throw new ParamException(BaseI18nCode.thirdTransLogNotExist);
        }
        return getOrderStatus(log, station);
    }

    @Override
    public boolean getOrderStatus(ThirdTransLog log, Station station) {
        PlatformType pf = PlatformType.getPlatform(log.getPlatform());
        this.checkMaintenance(pf, log.getStationId());
        String json = new HttpClientProxy() {
            @Override
            public List<NameValuePair> getParameters() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", getToken()));
                params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
                params.add(new BasicNameValuePair("code", station.getCode()));
                params.add(new BasicNameValuePair("currency", station.getCurrency()));
                params.add(new BasicNameValuePair("stationName", station.getName()));
                params.add(new BasicNameValuePair("orderId", log.getOrderId()));
                params.add(new BasicNameValuePair("language", SystemUtil.getLanguage()));
                return params;
            }
        }.doPost(getUrl("/api/game/checkTransfer.do"));
        JSONObject jsonObj = JSON.parseObject(json);
        if (jsonObj.getBooleanValue("maintain")) {
            throw new BaseException(BaseI18nCode.stationLiveCenter);
        }
        if (jsonObj.getBooleanValue("success")) {
            Integer status = jsonObj.getIntValue("status");
            if (status == ThirdTransLog.TRANS_STATUS_SUCCESS) {
                return true;
            } else if (status == ThirdTransLog.TRANS_STATUS_FAIL) {
                return false;
            } else {
                throw new BaseException(BaseI18nCode.orderStillHandler);
            }
        } else {
            //throw new BaseException(I18nTool.getMessage(jsonObj.getString("code"), jsonObj.getString("msg")));
            throw new BaseException(I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
        }
    }

    /**
     * 租户后台把钱转出
     */
    @Override
    public JSONObject retrieveBalanceByAgent(PlatformType pt, SysUser user, BigDecimal money, Station station) {
        String orderId = OrderIdMaker.getExchangeOrderId();
        try {
            JSONObject jsonObj = callThirdTrans(pt.getValue(), user, orderId, ThirdTransLog.TYPE_OUT_THIRD, money, station);
            boolean success = jsonObj.getBooleanValue("success");
            if (success) {
                // 第三方转入本平台 接口明确转账成功后,本平台才加钱
                retrieveBalanceByAgentSuccess(orderId, pt, user, money);
                LogUtils.modifyLog("转出用户" + user.getUsername() + " 真人平台：" + pt.name() + "的余额:" + money);
            }
            JSONObject json = new JSONObject();
            json.put("success", success);
            //json.put("msg", jsonObj.getString("msg"));
            json.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
            return json;
        } catch (Exception e) {
            // logger.error("转账异常", e);
            JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("msg", I18nTool.getMessage(BaseI18nCode.stationTransandAccountError));
            return json;
        }
    }

    /**
     * 租户后台把钱转出成功后，给玩家加钱
     *
     * @param orderId
     * @param pt
     * @param acc
     * @param money
     */
    private void retrieveBalanceByAgentSuccess(String orderId, PlatformType pt, SysUser acc, BigDecimal money) {
        Date now = new Date();
        // 保存日志
        ThirdTransLog log = new ThirdTransLog();
        log.setPartnerId(acc.getPartnerId());
        log.setStationId(acc.getStationId());
        log.setUserId(acc.getId());
        log.setUsername(acc.getUsername());
        log.setOrderId(orderId);
        log.setPlatform(pt.getValue());
        log.setType(ThirdTransLog.TYPE_OUT_THIRD);
        log.setMoney(money);
        log.setStatus(ThirdTransLog.TRANS_STATUS_SUCCESS);
        log.setCreateDatetime(now);
        log.setBeforeMoney(money);
        log.setAfterMoney(BigDecimal.ZERO);
        log.setUpdateDatetime(now);
        thirdTransLogDao.save(log);
        // 给会员加余额
        MnyMoneyBo vo = new MnyMoneyBo();
        vo.setUser(acc);
        vo.setMoney(money);
        vo.setMoneyRecordType(MoneyRecordType.REAL_GAME_ADD);
        vo.setOrderId(orderId);
        vo.setStationId(acc.getStationId());
        vo.setBizDatetime(now);
        List <String> remarkList = I18nTool.convertCodeToList(pt.name()," -> ",BaseI18nCode.thisSystemPlat.getCode());
        vo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
        moneyService.updMnyAndRecord(vo);
        autoExchangeService.saveOrUpdate(pt.getValue(), acc.getStationId(), acc.getId(),
                ThirdAutoExchange.type_third_to_sys);
    }

    @Override
    public String findThirdUser(String username, String thirdUsername, Integer platform, Integer pageSize,
                                Integer pageNumber, Station station) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManager.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("token", getToken()));
                    params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
                    params.add(new BasicNameValuePair("code", station.getCode()));
                    params.add(new BasicNameValuePair("stationName", station.getName()));
                    params.add(new BasicNameValuePair("currency", station.getCurrency()));
                    if (platform != null) {
                        params.add(new BasicNameValuePair("platform", String.valueOf(platform)));
                    }
                    params.add(new BasicNameValuePair("pageSize", pageSize == null ? "" : pageSize.toString()));
                    params.add(new BasicNameValuePair("pageNumber", pageNumber == null ? "" : pageNumber.toString()));
                    params.add(new BasicNameValuePair("username", username));
                    params.add(new BasicNameValuePair("thirdUsername", thirdUsername));
                    return params;
                }
            }.doPost(getUrl("/api/game/getMemberList.do"));
            JSONObject obj = JSON.parseObject(json);
            if (obj.getBooleanValue("maintain")) {
                throw new BaseException(BaseI18nCode.stationLiveCenter);
            }
            // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
            if (obj.getBooleanValue("success") && !obj.getBoolean("success")) {
                JSONObject jo = new JSONObject();
                jo.put("success", false);
                jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(obj.getString("code"), obj.getString("arts"))));
                return jo.toJSONString();
            }
            return json;
        } catch (HttpResponseException e) {
            logger.error("findThirdUser exception:{}", e);
            throw new BaseException(BaseI18nCode.stationServiceRead);
        }
    }

    @Override
    public void checkMaintenance(PlatformType pf, Long stationId) {
        ThirdPlatform tp = thirdPlatformService.findOne(stationId, pf.getValue());
        if (tp == null || !Objects.equals(tp.getStatus(), Constants.STATUS_ENABLE)) {
      //      logger.error(pf.name() + "游戏已关闭");
            throw new ThirdException(BaseI18nCode.stationExpectHope);
        }
    }

    @Override
    public String getStationQuota(Integer platform, Station station) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManagerForOrder.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("token", getToken()));
                    params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
                    params.add(new BasicNameValuePair("code", station.getCode()));
                    params.add(new BasicNameValuePair("stationName", station.getName()));
                    params.add(new BasicNameValuePair("currency", station.getCurrency()));
                    if (platform != null) {
                        params.add(new BasicNameValuePair("platform", String.valueOf(platform)));
                    }
                    return params;
                }
            }.doPost(getUrl("/api/game/getStationQuota.do"));
            if (!StringUtils.startsWith(json, "[")) {
                JSONObject jsonObj = JSON.parseObject(json);
                if (jsonObj.getBooleanValue("maintain")) {
                    throw new ThirdException(BaseI18nCode.stationLiveCenter);
                }

                // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
                if (jsonObj.getBooleanValue("success") && !jsonObj.getBoolean("success")) {
                    JSONObject jo = new JSONObject();
                    jo.put("success", false);
                    jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                    return jo.toJSONString();
                }
            }
            return json;
        } catch (ThirdException e) {
            logger.error("getStationQuota ThirdException exception:{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("getStationQuota exception:{}", e);
            throw new BaseException(BaseI18nCode.stationServiceWebCash);
        }
    }

    @Override
    public String getEgamePage(ThirdSearchVo v) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManagerForOrder.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    return getRecordParam(v);

                }
            }.doPost(getUrl("/api/game/getEgameRecord.do"));
            JSONObject obj = JSON.parseObject(json);
            if (obj.getBooleanValue("maintain")) {
                throw new ThirdException(BaseI18nCode.stationLiveCenter);
            }

            // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
            if (obj.getBooleanValue("success") && !obj.getBoolean("success")) {
                JSONObject jo = new JSONObject();
                jo.put("success", false);
                jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(obj.getString("code"), obj.getString("arts"))));
                return jo.toJSONString();
            }
            return json;
        } catch (ThirdException e) {
            logger.error("getEgamePage ThirdException exception:{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("getEgamePage exception:{}", e);
            throw new BaseException(BaseI18nCode.stationNoReadEwebRecord);
        }
    }

    protected List<NameValuePair> getRecordParam(ThirdSearchVo v) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", getToken()));
        Station station = v.getStation();
        params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
        params.add(new BasicNameValuePair("code", station.getCode()));
        params.add(new BasicNameValuePair("stationName", station.getName()));
        params.add(new BasicNameValuePair("currency", station.getCurrency()));
        params.add(new BasicNameValuePair("username", v.getUsername()));
        if (StringUtils.isNotEmpty(v.getPlatform())) {
            params.add(new BasicNameValuePair("platform", v.getPlatform()));
        }
        params.add(new BasicNameValuePair("orderId", v.getOrderId()));
        if (StringUtils.isEmpty(v.getStartTime())) {
            Date startDate = DateUtil.dayFirstTime(new Date(), -7);
            v.setStartTime(DateUtil.toDatetimeStr(startDate));
        }
        if (StringUtils.isEmpty(v.getEndTime())) {
            Date endDate = DateUtil.dayEndTime(new Date(), 0);
            v.setEndTime(DateUtil.toDatetimeStr(endDate));
        }
        params.add(new BasicNameValuePair("beginTime", v.getStartTime()));
        params.add(new BasicNameValuePair("endTime", v.getEndTime()));
        params.add(new BasicNameValuePair("parentIds", v.getParentIds()));
        params.add(new BasicNameValuePair("sortName", v.getSortName()));
        params.add(new BasicNameValuePair("sortOrder", v.getSortOrder()));
        params.add(new BasicNameValuePair("pageNumber", String.valueOf(v.getPageNumber())));
        params.add(new BasicNameValuePair("pageSize", String.valueOf(v.getPageSize())));
        if (v.getRecommendId() != null) {
            params.add(new BasicNameValuePair("recommendId", String.valueOf(v.getRecommendId())));
        }
        if (StringUtils.isNotEmpty(v.getGameName())) {
            params.add(new BasicNameValuePair("gameName", v.getGameName()));
        }
        return params;
    }

    @Override
    public String getLivePage(ThirdSearchVo v) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManagerForOrder.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    return getRecordParam(v);
                }
            }.doPost(getUrl("/api/game/getLiveRecord.do"));
            JSONObject obj = JSON.parseObject(json);
            if (obj.getBooleanValue("maintain")) {
                throw new ThirdException(BaseI18nCode.stationLiveCenter);
            }

            // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
            if (obj.getBooleanValue("success") && !obj.getBoolean("success")) {
                JSONObject jo = new JSONObject();
                jo.put("success", false);
                jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(obj.getString("code"), obj.getString("arts"))));
                return jo.toJSONString();
            }
            return json;
        } catch (ThirdException e) {
            logger.error("getLivePage ThirdException exception:{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("getLivePage exception:{}", e);
            throw new BaseException(BaseI18nCode.stationNoLiveRecord);
        }
    }

    @Override
    public String getChessPage(ThirdSearchVo v) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManagerForOrder.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    return getRecordParam(v);
                }
            }.doPost(getUrl("/api/game/getChessRecord.do"));
            JSONObject obj = JSON.parseObject(json);
            if (obj.getBooleanValue("maintain")) {
                throw new ThirdException(BaseI18nCode.stationLiveCenter);
            }

            // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
            if (obj.getBooleanValue("success") && !obj.getBoolean("success")) {
                JSONObject jo = new JSONObject();
                jo.put("success", false);
                jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(obj.getString("code"), obj.getString("arts"))));
                return jo.toJSONString();
            }
            return json;
        } catch (ThirdException e) {
            logger.error("getChessPage ThirdException exception:{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("getChessPage exception:{}", e);
            throw new BaseException(BaseI18nCode.stationNoChessRecord);
        }
    }

    @Override
    public String getPtPage(ThirdSearchVo v) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManagerForOrder.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    return getRecordParam(v);
                }
            }.doPost(getUrl("/api/game/getPtRecord.do"));
            JSONObject jsonObj = JSON.parseObject(json);
            if (jsonObj.getBooleanValue("maintain")) {
                throw new ThirdException(BaseI18nCode.stationLiveCenter);
            }
            // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
            if (jsonObj.getBooleanValue("success") && !jsonObj.getBoolean("success")) {
                JSONObject jo = new JSONObject();
                jo.put("success", false);
                jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                return jo.toJSONString();
            }
            return json;
        } catch (ThirdException e) {
            logger.error("getPtPage ThirdException exception:{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("getPtPage exception:{}", e);
            throw new BaseException(BaseI18nCode.stationNoPTRecord);
        }
    }

    @Override
    public String getSportPage(ThirdSearchVo v) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManagerForOrder.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    return getRecordParam(v);
                }
            }.doPost(getUrl("/api/game/getSportRecord.do"));
            JSONObject jsonObj = JSON.parseObject(json);
            if (jsonObj.getBooleanValue("maintain")) {
                throw new ThirdException(BaseI18nCode.stationLiveCenter);
            }

            // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
            if (jsonObj.getBooleanValue("success") && !jsonObj.getBoolean("success")) {
                JSONObject jo = new JSONObject();
                jo.put("success", false);
                jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                return jo.toJSONString();
            }

            return json;
        } catch (ThirdException e) {
            logger.error("getSportPage ThirdException exception:{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("getSportPage exception:{}", e);
            throw new BaseException(BaseI18nCode.stationNoSportRecord);
        }
    }

    @Override
    public String getEsportPage(ThirdSearchVo v) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManagerForOrder.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    return getRecordParam(v);
                }
            }.doPost(getUrl("/api/game/getEsportRecord.do"));
            JSONObject jsonObj = JSON.parseObject(json);
            if (jsonObj.getBooleanValue("maintain")) {
                throw new ThirdException(BaseI18nCode.stationLiveCenter);
            }

            // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
            if (jsonObj.getBooleanValue("success") && !jsonObj.getBoolean("success")) {
                JSONObject jo = new JSONObject();
                jo.put("success", false);
                jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                return jo.toJSONString();
            }
            return json;
        } catch (ThirdException e) {
            logger.error("getEsportPage ThirdException exception:{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("getEsportPage exception:{}", e);
            throw new BaseException(BaseI18nCode.stationNoEgameRecord);
        }
    }

    @Override
    public String getFishingPage(ThirdSearchVo v) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManagerForOrder.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    return getRecordParam(v);
                }
            }.doPost(getUrl("/api/game/getFishRecord.do"));
            JSONObject jsonObj = JSON.parseObject(json);
            if (jsonObj.getBooleanValue("maintain")) {
                throw new ThirdException(BaseI18nCode.stationLiveCenter);
            }

            // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
            if (jsonObj.getBooleanValue("success") && !jsonObj.getBoolean("success")) {
                JSONObject jo = new JSONObject();
                jo.put("success", false);
                jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                return jo.toJSONString();
            }
            return json;
        } catch (ThirdException e) {
            logger.error("getFishingPage ThirdException exception:{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("getFishingPage exception:{}", e);
            throw new BaseException(BaseI18nCode.stationNoFishRecord);
        }
    }

    @Override
    public String getLotteryPage(ThirdSearchVo v) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManagerForOrder.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    return getRecordParam(v);
                }
            }.doPost(getUrl("/api/game/getLotRecord.do"));
            JSONObject jsonObj = JSON.parseObject(json);
            if (jsonObj.getBooleanValue("maintain")) {
                throw new ThirdException(BaseI18nCode.stationLiveCenter);
            }
            // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
            if (jsonObj.getBooleanValue("success") && !jsonObj.getBoolean("success")) {
                JSONObject jo = new JSONObject();
                jo.put("success", false);
                jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                return jo.toJSONString();
            }
            return json;
        } catch (ThirdException e) {
            logger.error("getLotteryPage ThirdException exception:{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("getLotteryPage exception:{}", e);
            throw new BaseException(BaseI18nCode.stationNoLotRecord);
        }
    }

    /**
     * 第三方报表
     */
    @Override
    public String thirdReport(String startDate, String endDate, Station station) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public org.apache.http.client.HttpClient getClient() {
                    return HttpConnectionManagerForOrder.getHttpClient();
                }

                @Override
                public List<NameValuePair> getParameters() {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("token", getToken()));
                    params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
                    params.add(new BasicNameValuePair("code", station.getCode()));
                    params.add(new BasicNameValuePair("stationName", station.getName()));
                    params.add(new BasicNameValuePair("currency", station.getCurrency()));
                    params.add(new BasicNameValuePair("startDate", startDate));
                    params.add(new BasicNameValuePair("endDate", endDate));
                    return params;
                }
            }.doPost(getUrl("/api/game/getThirdRecord.do"));
            if (!StringUtils.startsWith(json, "[")) {
                JSONObject jsonObj = JSON.parseObject(json);
                if (jsonObj.getBooleanValue("maintain")) {
                    throw new ThirdException(BaseI18nCode.stationLiveCenter);
                }

                // mlangthird接口若正常返回，则无需获取异常信息，即没有返回"success"
                if (jsonObj.getBooleanValue("success") && !jsonObj.getBoolean("success")) {
                    JSONObject jo = new JSONObject();
                    jo.put("success", false);
                    jo.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                    return jo.toJSONString();
                }
            }
            return json;
        } catch (ThirdException e) {
            logger.error("thirdReport ThirdException exception:{}", e);
            throw e;
        } catch (Exception e) {
            logger.error("thirdReport exception:{}", e);
            throw new BaseException(BaseI18nCode.stationNoReportRecord);
        }
    }

    /**
     * 转入第三方第一步，扣钱并记录转账日志
     */
    @Override
    public ThirdTransLog transToThirdStep1(PlatformType pt, SysUser acc, BigDecimal money, Station station) {
        logger.error("transToThirdStep1 username:{}, pt:{}, money:{}", acc.getUsername(), pt.name(), money);
        this.checkMaintenance(pt, acc.getStationId());
        // 判断转入金额
        ThirdTransferOutLimit limit = thirdTransferOutLimitDao.findByPlatformStationId(acc.getStationId(),
                pt.getValue());
        boolean autoExchange = StationConfigUtil.isOn(acc.getStationId(), StationConfigEnum.third_auto_exchange);
        if (limit != null && !autoExchange) {
            String limitAccounts = limit.getLimitAccounts();
            if (StringUtils.isNotEmpty(limitAccounts) && limitAccounts.indexOf(",") != -1) {
                for (String limitAccount : limitAccounts.split(",")) {
                    if (acc.getUsername().equals(limitAccount)) {
                        if (limit.getMinMoney() != null && limit.getMinMoney().compareTo(BigDecimal.ZERO) > 0
                                && limit.getMinMoney().compareTo(money) > 0) {
                            throw new BaseException(BaseI18nCode.stationTransendMoneyMax,
                                    new Object[]{limit.getMinMoney()});
                        }
                        if (limit.getMaxMoney() != null && limit.getMaxMoney().compareTo(BigDecimal.ZERO) > 0
                                && limit.getMaxMoney().compareTo(money) < 0) {
                            throw new BaseException(BaseI18nCode.stationTransendMoenyMin,
                                    new Object[]{limit.getMaxMoney()});
                        }
                    }
                }
            } else {
                if (limit.getMinMoney() != null && limit.getMinMoney().compareTo(BigDecimal.ZERO) > 0
                        && limit.getMinMoney().compareTo(money) > 0) {
                    throw new BaseException(BaseI18nCode.stationTransendMoneyMax, new Object[]{limit.getMinMoney()});
                }
                if (limit.getMaxMoney() != null && limit.getMaxMoney().compareTo(BigDecimal.ZERO) > 0
                        && limit.getMaxMoney().compareTo(money) < 0) {
                    throw new BaseException(BaseI18nCode.stationTransendMoenyMin, new Object[]{limit.getMaxMoney()});
                }
            }
        }

        BigDecimal beforeMoney = getBalanceForTrans(pt, acc, station);// 先获取账户金额
        // 扣除金额并记录账变
        String orderId = OrderIdMaker.getExchangeOrderId();
        MnyMoneyBo vo = new MnyMoneyBo();
        vo.setUnusedMoney(BigDecimal.ZERO);
        vo.setMoney(money);
        //如果系统金额是VND金额,不转换百位以下金额,防止金额丢失
       // if(SystemUtil.getCurrency().name().equals("VND")){
        //如果系统金额是VND金额,不转换百位以下金额,防止转入三方金额丢失
        RateUtil.setUnusedMoney(money, vo, SystemUtil.getCurrency().name());
        vo.setUser(acc);
        vo.setMoneyRecordType(MoneyRecordType.REAL_GAME_SUB);
        vo.setOrderId(orderId);
        vo.setStationId(acc.getStationId());
        List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.thisSystemPlat.getCode()," -> ",pt.name());
        vo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
        //vo.setRemark(I18nTool.getMessage(BaseI18nCode.thisSystemPlat) + " -> " + pt.getTitle());
        logger.error("transToThirdStep1 username:{}, pt:{}, moneyBo:{}", acc.getUsername(), pt.name(), JSON.toJSON(vo));
        moneyService.updMnyAndRecord(vo);
        // 记录转账日志
        return saveTransferLog(acc, orderId, pt.getValue(), ThirdTransLog.TYPE_INTO_THIRD, money.subtract(vo.getUnusedMoney()), beforeMoney);
    }

    /**
     * 转账时查询余额
     *
     * @param pt
     * @param acc
     * @return
     */
    @Override
    public BigDecimal getBalanceForTrans(PlatformType pt, SysUser acc, Station station) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public List<NameValuePair> getParameters() {
                    return getBasicParams(pt, acc, station);
                }
            }.doPost(getUrl("/api/member/getBalance.do"));
            JSONObject obj = JSON.parseObject(json);
            if (obj.getBooleanValue("maintain")) {
                throw new BaseException(BaseI18nCode.stationLiveCenter);
            }
            if (obj.getBooleanValue("success") == false) {
           // 	 logger.error("obj.getBooleanValue == false, pt={}, obj={}", pt, JSON.toJSON(obj));
                throw new BaseException(I18nTool.getMessage(ValidateUtil.isCodeError(obj.getString("code"), obj.getString("arts"))));
                //throw new BaseException(I18nTool.getMessage(obj.getString("code"), obj.getString("msg")));

            }
            return BigDecimalUtil.toBigDecimalDefaultZero(obj.getString("balance"));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            logger.error("获取余额发生错误3", e);
            throw new BaseException(BaseI18nCode.stationNetError);
        }
    }

    /**
     * 保存第三方转账记录，状态未知
     *
     * @param acc
     * @param orderId
     * @param type
     * @param money
     * @param beforeMoney
     * @return
     */
    private ThirdTransLog saveTransferLog(SysUser acc, String orderId, int platform, int type, BigDecimal money,
                                          BigDecimal beforeMoney) {
        ThirdTransLog log = new ThirdTransLog();
        log.setPartnerId(acc.getPartnerId());
        log.setUserId(acc.getId());
        log.setOrderId(orderId);
        log.setPlatform(platform);
        log.setType(type);
        log.setMoney(money);
        log.setStatus(ThirdTransLog.TRANS_STATUS_UNKNOW);
        log.setCreateDatetime(new Date());
        log.setUsername(acc.getUsername());
        log.setStationId(acc.getStationId());
        log.setBeforeMoney(beforeMoney);
        return thirdTransLogDao.save(log);
    }

    /**
     * 转入第三方第二部，调用真人中心接口，并修改日志信息，如果失败则加钱
     */
    @Override
    public JSONObject transToThirdStep2(SysUser acc, ThirdTransLog log, PlatformType pt, Station station) {
        JSONObject json = new JSONObject();
        try {
            JSONObject jsonObj = callThirdTrans(log.getPlatform(), acc, log.getOrderId(), log.getType(), log.getMoney(),
                    station);
            boolean success = jsonObj.getBooleanValue("success");
            Boolean rollback = jsonObj.getBoolean("rollback");
            if (rollback == null || rollback) {
                // 没有rollback 信息 或者 rollback=true，说明转入时失败，需要回滚
                //log.setMsg(jsonObj.getString("msg"));
//                log.setMsg(I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                List <String> msgList = I18nTool.convertCodeToList(ValidateUtil.isCodeErrorGetCode(jsonObj.getString("code"), jsonObj.getString("arts")));
                String msgString = I18nTool.convertCodeToArrStr(msgList);
                log.setMsg(msgString);
              //  logger.error("tranout limit step2 fail,msg = " + log.getMsg());
                transToThirdRollback(log, acc);
                json.put("success", false);
                //json.put("msg", jsonObj.getString("msg"));
                json.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                return json;
            }
            if (success) {
                transToThirdSuccess(acc, log, pt, station);
                json.put("success", true);
                json.put("msg", I18nTool.getMessage(BaseI18nCode.stationTranSucc));
                return json;
            }
            json.put("success", false);
            //json.put("msg", jsonObj.getString("msg"));
            json.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
            return json;
        } catch (Exception e) {
            logger.error("transToThirdStep2 exception:", e);
            json.put("success", false);
            json.put("msg", I18nTool.getMessage(BaseI18nCode.stationTransandAccountError));
            return json;
        }
    }

    /**
     * 转账日志修改成转账失败，并退还额度
     *
     * @param log
     */
    private void transToThirdRollback(ThirdTransLog log, SysUser acc) {
        if (thirdTransLogDao.updateFailed(log.getId(), log.getStationId(), log.getMsg())) {
            MnyMoneyBo vo = new MnyMoneyBo();
            vo.setUser(acc);
            vo.setMoney(log.getMoney());
            vo.setOrderId(log.getOrderId());
            vo.setStationId(log.getStationId());
            vo.setBizDatetime(log.getCreateDatetime());
            List <String> remarkList = I18nTool.convertCodeToList();
            remarkList.add(BaseI18nCode.thisSystemPlat.getCode());
            remarkList.add(" -> ");
            remarkList.add(PlatformType.getPlatform(log.getPlatform()).getTitle());
            remarkList.add(BaseI18nCode.thirdTurnSeedError.getCode());
            vo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
            vo.setMoneyRecordType(MoneyRecordType.REAL_GAME_FAILED);
            moneyService.updMnyAndRecord(vo);
        }
    }

    /**
     * 转账日志修改成成功
     *
     * @param log
     */
    private void transToThirdSuccess(SysUser acc, ThirdTransLog log, PlatformType pt, Station station) {
        // 获取转账后的额度
        // log.setAfterMoney(log.getMoney().add(log.getBeforeMoney()));
        log.setAfterMoney(getBalanceForTrans(pt, acc, station));
        log.setUpdateDatetime(new Date());
        thirdTransLogDao.updateSuccess(log);
        autoExchangeService.saveOrUpdate(pt.getValue(), station.getId(), acc.getId(),
                ThirdAutoExchange.type_sys_to_third);
    }

    /**
     * 从第三方转出到本系统第一步
     */
    @Override
    public ThirdTransLog takeOutToSysStep1(PlatformType pt, SysUser acc, BigDecimal money, Station station) {
        this.checkMaintenance(pt, acc.getStationId());
        BigDecimal beforeMoney = getBalanceForTrans(pt, acc, station);// 先获取账户金额
        if (beforeMoney.compareTo(money) < 0) {
            throw new BaseException(BaseI18nCode.stationCashNotEnah);
        }
        String orderId = OrderIdMaker.getExchangeOrderId();
        // 记录转账日志
        return saveTransferLog(acc, orderId, pt.getValue(), ThirdTransLog.TYPE_OUT_THIRD, money, beforeMoney);
    }

    /**
     * 从第三方转出到本系统第一步
     */
    @Override
    public ThirdTransLog takeOutToSys(PlatformType pt, SysUser acc, Station station) {
        this.checkMaintenance(pt, acc.getStationId());
        // 先获取账户金额
        BigDecimal beforeMoney = getBalanceForTrans(pt, acc, station);
        long roundDown = BigDecimalUtil.roundDown(beforeMoney);
//        if (roundDown < 1) {
//            throw new BaseException(BaseI18nCode.stationValueMinOne);
//        }
        String orderId = OrderIdMaker.getExchangeOrderId();
        // 记录转账日志
        return saveTransferLog(acc, orderId, pt.getValue(), ThirdTransLog.TYPE_OUT_THIRD, new BigDecimal(roundDown), beforeMoney);
    }

    /**
     * 从第三方转出到本系统第二步，成功就加余额
     */
    @Override
    public JSONObject takeOutToSysStep2(SysUser acc, ThirdTransLog log, PlatformType pt, Station station) {
        JSONObject json = new JSONObject();
        try {
            JSONObject jsonObj = callThirdTrans(log.getPlatform(), acc, log.getOrderId(), log.getType(), log.getMoney(),
                    station);
            boolean success = jsonObj.getBooleanValue("success");
            Boolean rollback = jsonObj.getBoolean("rollback");
            if (rollback == null || rollback) {
                // 没有rollback 信息 或者 rollback=true，说明转出到本系统失败，需要修改成失败
                //thirdTransLogDao.updateFailed(log.getId(), log.getStationId(), jsonObj.getString("msg"));
                //thirdTransLogDao.updateFailed(log.getId(), log.getStationId(), I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                List <String> msgList = I18nTool.convertCodeToList(ValidateUtil.isCodeErrorGetCode(jsonObj.getString("code"), jsonObj.getString("arts")));
                String msgString = I18nTool.convertCodeToArrStr(msgList);
                thirdTransLogDao.updateFailed(log.getId(), log.getStationId(),msgString);
                json.put("success", false);
                //json.put("msg", jsonObj.getString("msg"));
                json.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                return json;
            }
            if (success) {
                takeOutToSysSuccess(acc, log, pt, station);
                json.put("success", true);
                json.put("msg", I18nTool.getMessage(BaseI18nCode.stationTranSucc));
                return json;
            }
            json.put("success", false);
            json.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
            //json.put("msg", jsonObj.getString("msg"));
            return json;
        } catch (Exception e) {
            // logger.error("转账异常,请联系客服", e);
            json.put("success", false);
            json.put("msg", I18nTool.getMessage(BaseI18nCode.stationTransandAccountError));
            return json;
        }
    }

    /**
     * 从第三方转出到本系统 转出成功处理
     *
     * @param acc
     * @param log
     * @param pt
     */
    private void takeOutToSysSuccess(SysUser acc, ThirdTransLog log, PlatformType pt, Station station) {
        // 获取转账后的额度
        log.setAfterMoney(getBalanceForTrans(pt, acc, station));
        log.setUpdateDatetime(new Date());
        if (thirdTransLogDao.updateSuccess(log)) {
            MnyMoneyBo vo = new MnyMoneyBo();
            vo.setUser(acc);
            vo.setMoney(log.getMoney());
            vo.setMoneyRecordType(MoneyRecordType.REAL_GAME_ADD);
            vo.setOrderId(log.getOrderId());
            vo.setStationId(acc.getStationId());
            vo.setBizDatetime(log.getCreateDatetime());
            List <String> remarkList = I18nTool.convertCodeToList(pt.name()," -> ",BaseI18nCode.thisSystemPlat.getCode());
            vo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
            moneyService.updMnyAndRecord(vo);
        }
    }

    /**
     * 调用真人中心转账接口
     *
     * @param platform
     * @param acc
     * @param orderId
     * @param type
     * @param money
     * @return
     */
    private JSONObject callThirdTrans(int platform, SysUser acc, String orderId, int type, BigDecimal money,
                                      Station station) {
        String token = getToken();
        String json = new HttpClientProxyForTransMoney() {
            @Override
            public List<NameValuePair> getParameters() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", token));
                params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
                params.add(new BasicNameValuePair("code", station.getCode()));
                params.add(new BasicNameValuePair("stationName", station.getName()));
                params.add(new BasicNameValuePair("currency", station.getCurrency()));
                //params.add(new BasicNameValuePair("language", station.getLanguage()));
                params.add(new BasicNameValuePair("language", SystemUtil.getLanguage()));
                params.add(new BasicNameValuePair("platform", String.valueOf(platform)));
                params.add(new BasicNameValuePair("userId", String.valueOf(acc.getId())));
                params.add(new BasicNameValuePair("username", acc.getUsername()));
                params.add(new BasicNameValuePair("orderId", orderId));
                params.add(new BasicNameValuePair("money", money.toString()));
                params.add(new BasicNameValuePair("type", String.valueOf(type)));
                params.add(new BasicNameValuePair("parentIds", acc.getParentIds()));
                params.add(new BasicNameValuePair("isGuest", String.valueOf(GuestTool.isGuest(acc))));
                params.add(new BasicNameValuePair("userIp", IpUtils.getIp()));
                StringBuilder signsb = new StringBuilder();
                signsb.append(station.getCode()).append("_");
                signsb.append(station.getId()).append("_");
                signsb.append(station.getName()).append("_");
                signsb.append(station.getCurrency()).append("_");
                signsb.append(acc.getUsername()).append("_");
                signsb.append(acc.getId()).append("_");
                signsb.append(platform).append("_");
                signsb.append(token).append("_");
                signsb.append(type).append("_");
                signsb.append(money).append("_");
                signsb.append(orderId).append("_").append(EXCHANGE_MD5_KEY);
                params.add(new BasicNameValuePair("sign", MD5Util.md5(signsb.toString())));
                return params;
            }
        }.doPost(getUrl("/api/member/exchange.do"));
        JSONObject obj = JSON.parseObject(json);
        if (obj.getBooleanValue("maintain")) {
            throw new BaseException(BaseI18nCode.stationLiveCenter);
        }
        return obj;
    }

    /**
     * @param recordId
     * @param platform
     * @param type     1=live,2=egame,3=chess,4=sport,5=esport,6=fishing,7=lottery
     * @param station
     * @return
     */
    @Override
    public String getDetailUrl(Long recordId, Integer platform, Integer type, Station station) {
        // Platform pf = Platform.getPlatform(platform);
        // this.checkMaintenance(pf, station.getStationId());

        String json = new HttpClientProxy() {
            @Override
            public List<NameValuePair> getParameters() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("token", getToken()));
                params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
                params.add(new BasicNameValuePair("code", station.getCode()));
                params.add(new BasicNameValuePair("stationName", station.getName()));
                params.add(new BasicNameValuePair("currency", station.getCurrency()));
                //params.add(new BasicNameValuePair("language", station.getLanguage()));
                params.add(new BasicNameValuePair("language", SystemUtil.getLanguage()));
                params.add(new BasicNameValuePair("platform", String.valueOf(platform)));
                params.add(new BasicNameValuePair("recordId", String.valueOf(recordId)));
                params.add(new BasicNameValuePair("type", String.valueOf(type)));
                return params;
            }
        }.doPost(getUrl("/api/game/getDetailUrl.do"));
        JSONObject jsonObj = JSON.parseObject(json);
        if (jsonObj.getBooleanValue("maintain")) {
            throw new BaseException(BaseI18nCode.stationLiveCenter);
        }
        if (jsonObj.getBooleanValue("success")) {
            return jsonObj.getString("url");
        } else {
            throw new BaseException(I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
            //throw new BaseException(jsonObj.getString("msg"));
        }
    }

    @Override
    public JSONObject getThirdAdminUrl(Map<String, Object> map,Station station,Integer platform) {
        AdminUser user = LoginAdminUtil.currentUser();
        try {
            String json = new HttpClientProxy() {
                @Override
                public List<NameValuePair> getParameters() {

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("token", getToken()));
                    params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
                    params.add(new BasicNameValuePair("code", station.getCode()));
                    params.add(new BasicNameValuePair("stationName", station.getName()));
                    params.add(new BasicNameValuePair("currency", station.getCurrency()));
                    params.add(new BasicNameValuePair("language", SystemUtil.getLanguage()));
                    params.add(new BasicNameValuePair("platform", String.valueOf(platform)));
                    JSONObject d = new JSONObject();
                    d.put("username", map.get("username"));
                    d.put("password", map.get("password"));
                    d.put("oriIp", IpUtils.getIp());
                    return params;
                }
            }.doPost(getUrl("/api/game/getAdminLoginUrl.do"));
            JSONObject jsonObj = JSON.parseObject(json);
            if (jsonObj.getBooleanValue("maintain")) {
                throw new BaseException(BaseI18nCode.stationLiveCenter);
            }
            checkError(jsonObj);
            return jsonObj;
        } catch (Exception e) {
            logger.error("get admin login url error = ", e);
        }
        return new JSONObject();
    }

    @Override
    public JSONObject getLotteryList(Integer platform, Long stationId,Integer lotVersion) {
        try{
            Station station = stationService.findOneById(stationId);
            String json = new HttpClientProxy() {
                @Override
                public List<NameValuePair> getParameters() {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("token", getToken()));
                    params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
                    params.add(new BasicNameValuePair("code", station.getCode()));
                    params.add(new BasicNameValuePair("stationName", station.getName()));
                    params.add(new BasicNameValuePair("currency", station.getCurrency()));
                    params.add(new BasicNameValuePair("language", SystemUtil.getLanguage()));
                    params.add(new BasicNameValuePair("platform", String.valueOf(platform)));
                    params.add(new BasicNameValuePair("lotVersion", String.valueOf(lotVersion)));
                    return params;
                }
            }.doPost(getUrl("/api/game/getlotteryList.do"));
            JSONObject jsonObj = JSON.parseObject(json);
           // logger.error("get iyg lottery data jsonob === ", jsonObj.toJSONString());
            if (jsonObj.getBooleanValue("maintain")){
                throw new BaseException(BaseI18nCode.stationLiveCenter);
            }
            checkError(jsonObj);
            return jsonObj;
        }catch (Exception e){
            logger.error("getLottery list error = ", e);
        }
        return null;
    }

}
