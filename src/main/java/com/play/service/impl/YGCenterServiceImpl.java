package com.play.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.common.Constants;
import com.play.common.ip.IpUtils;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.CurrencyUtils;
import com.play.common.utils.DateUtil;
import com.play.common.utils.OrderIdMaker;
import com.play.common.utils.security.AES;
import com.play.common.utils.security.MD5Util;
import com.play.core.MoneyRecordType;
import com.play.core.PlatformType;
import com.play.core.StationConfigEnum;
import com.play.dao.ThirdTransLogDao;
import com.play.dao.ThirdTransferOutLimitDao;
import com.play.model.*;
import com.play.model.bo.BcLotteryOrderBo;
import com.play.model.bo.MnyMoneyBo;
import com.play.service.*;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ThirdException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.ValidateUtil;
import com.play.web.utils.http.HttpClientProxy;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class YGCenterServiceImpl implements YGCenterService {
    private Logger logger = LoggerFactory.getLogger(YGCenterServiceImpl.class);
    private static String ygApiKey;
    private static String ygApiAesKey;
    private static String ygCenterDomain;
    @Autowired
    private ThirdPlatformService thirdPlatformService;
    @Autowired
    private SysUserMoneyService moneyService;
    @Autowired
    private ThirdTransferOutLimitDao thirdTransferOutLimitDao;
    @Autowired
    private ThirdTransLogDao thirdTransLogDao;
    @Autowired
    private ThirdAutoExchangeService autoExchangeService;
    @Autowired
    private YGCenterService ygCenterService;
    @Override
    public JSONObject createUser(SysUser sysUser) {
        this.checkMaintenance(PlatformType.YG, sysUser.getStationId());
        String json = new HttpClientProxy() {
            @Override
            public List<NameValuePair> getParameters() {
                List<NameValuePair> params = getBasicParams(PlatformType.YG, sysUser, SystemUtil.getStation());
                JSONObject d = new JSONObject();
                String finalUsername = sysUser.getUsername();
                if (StringUtils.isNotEmpty(finalUsername) && finalUsername.length() > 6) {
                    finalUsername = finalUsername.substring(0, 6);
                }
                String userName = finalUsername + MD5Util.md5(sysUser.getUsername()).substring(5,15);//10位的随机数
                d.put("username", userName);
                d.put("thirdUserName", sysUser.getUsername());
                d.put("registerIp", IpUtils.getIp());
                d.put("password", MD5Util.md5(sysUser.getUsername() + "111111").substring(5,25));
                String agentAccount = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.sys_api_agent_acount);
                d.put("parentNames", agentAccount);
                String dataOri = JSONObject.toJSONString(d);
                String data = AES.encrypt(dataOri, getYgApiAesKey());
                String sign = MD5Util.md5(data + getYGKey());
                params.add(new BasicNameValuePair("data", data));
                params.add(new BasicNameValuePair("sign", sign));
                return params;
            }
        }.doPost(getYGUrl("/api/account/register.do"));
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj = JSON.parseObject(json);
            if (jsonObj.getBooleanValue("maintain")) {
                throw new BaseException(BaseI18nCode.stationLiveCenter);
            }
            checkError(jsonObj);
        }catch (Exception e){
            logger.error("create user error = ",e);
        }
        if (jsonObj.getBooleanValue("maintain")) {
            throw new BaseException(BaseI18nCode.stationLiveCenter);
        }
        return jsonObj;
    }

    @Override
    public JSONObject deposit(String data, String sign) {
        return null;
    }
    @Override
    public JSONObject withdraw(String data, String sign) {
        return null;
    }
    @Override
    public JSONObject getLotteryList() {
        this.checkMaintenance(PlatformType.YG, SystemUtil.getStationId());
        try{
            String json = new HttpClientProxy() {
                @Override
                public List<NameValuePair> getParameters() {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    return params;
                }
            }.doPost(getYGUrl("/api/lottery/getlotteryList.do"));
            JSONObject jsonObj = JSON.parseObject(json);
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

    @Override
    public JSONObject guestRegister(String username) {
        this.checkMaintenance(PlatformType.YG, SystemUtil.getStationId());
        String json = new HttpClientProxy() {
            @Override
            public List<NameValuePair> getParameters() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                if (StringUtils.isNotEmpty(username)) {
                    params.add(new BasicNameValuePair("username", username));
                }
                return params;
            }
        }.doGet(getYGUrl("/api/account/guestRegister.do"));
        JSONObject jsonObj = JSON.parseObject(json);
        if (jsonObj.getBooleanValue("maintain")){
            throw new BaseException(BaseI18nCode.stationLiveCenter);
        }
        checkError(jsonObj);
        return jsonObj;
    }
    @Override
    public JSONObject getLotteryOrder(String orderId,String username,String qiHao,String lotCode,
                                      String startTime,String endTime,Integer pageNumber,Integer pageSize) {
        this.checkMaintenance(PlatformType.YG, SystemUtil.getStationId());
        String json = new HttpClientProxy() {
            @Override
            public List<NameValuePair> getParameters() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject d = new JSONObject();
                d.put("startTime", startTime);
                d.put("endTime", endTime);
                d.put("orderId", orderId);
                if (StringUtils.isNotBlank(username)) {
                   if (username.length() > 6) {
                       d.put("username", username.substring(0, 6) + MD5Util.md5(username).substring(5,15));
                   } else {
                       d.put("username", username + MD5Util.md5(username).substring(5,15));
                   }
                }
                d.put("qiHao", qiHao);
                String agentAccount = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.sys_api_agent_acount);
                d.put("parentNames", agentAccount);
                d.put("gameCode", lotCode);
//                d.put("pageNumber", pageNumber);
//                d.put("pageSize", pageSize);
                String dataOri = JSONObject.toJSONString(d);
                String data = AES.encrypt(dataOri, getYgApiAesKey());
                String sign = MD5Util.md5(data + getYGKey());
                params.add(new BasicNameValuePair("data", data));
                params.add(new BasicNameValuePair("sign", sign));
                return params;
            }
        }.doPost(getYGUrl("/api/lottery/getLotteryOrder.do"));
        JSONObject jsonObj = JSON.parseObject(json);
        if (jsonObj.getBooleanValue("maintain")) {
            throw new BaseException(BaseI18nCode.stationLiveCenter);
        }
        checkError(jsonObj);
        return jsonObj;
    }
    @Override
    public JSONObject getAdminLoginUrl(Map<String, Object> map) {
        AdminUser user = LoginAdminUtil.currentUser();
        this.checkMaintenance(PlatformType.YG, user.getStationId());
        try {
            String json = new HttpClientProxy() {
                @Override
                public List<NameValuePair> getParameters() {
//                List<NameValuePair> params = getBasicParams(PlatformType.YG, user, SystemUtil.getStation());
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    JSONObject d = new JSONObject();
                    d.put("backendUrl", map.get("backendUrl"));
                    d.put("username", map.get("username"));
                    d.put("password", map.get("password"));
                    String dataOri = JSONObject.toJSONString(d);
                    String data = AES.encrypt(dataOri, getYgApiAesKey());
                    String sign = MD5Util.md5(data + getYGKey());
                    params.add(new BasicNameValuePair("data", data));
                    params.add(new BasicNameValuePair("sign", sign));
                    params.add(new BasicNameValuePair("oriIp", IpUtils.getIp()));
                    return params;
                }
            }.doPost(getYGUrl("/api/account/getAdminLoginUrl.do"));
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
    public JSONObject getLoginUrl(Map<String, Object> map) {
        SysUser user = LoginMemberUtil.currentUser();
        this.checkMaintenance(PlatformType.YG, user.getStationId());
        String json = new HttpClientProxy() {
            @Override
            public List<NameValuePair> getParameters() {
                List<NameValuePair> params = getBasicParams(PlatformType.YG, user, SystemUtil.getStation());
                JSONObject d = new JSONObject();
                String md5 = MD5Util.md5(user.getUsername());
                String finalUsername = user.getUsername();
                if (StringUtils.isNotEmpty(finalUsername) && finalUsername.length() > 6) {
                    finalUsername = finalUsername.substring(0, 6);
                }
                String userName = finalUsername + md5.substring(5,15);
                d.put("username", userName);
                d.put("backUrl", map.get("backUrl"));
                d.put("ttlotdata", map.get("ttLotdata"));
                d.put("ttver", map.get("ttver"));
                d.put("lotCode", map.get("lotCode"));
                d.put("curVersion", map.get("curVersion"));
                String dataOri = JSONObject.toJSONString(d);
                String data = AES.encrypt(dataOri, getYgApiAesKey());
                String sign = MD5Util.md5(data + getYGKey());
                params.add(new BasicNameValuePair("data", data));
                params.add(new BasicNameValuePair("sign", sign));
                params.add(new BasicNameValuePair("oriIp", IpUtils.getIp()));
                return params;
            }
        }.doPost(getYGUrl("/api/account/getLoginUrl.do"));
        JSONObject jsonObj = JSON.parseObject(json);
        if (jsonObj.getBooleanValue("maintain")) {
            throw new BaseException(BaseI18nCode.stationLiveCenter);
        }
        checkError(jsonObj);
        //自动转入系统额度到YG
        autoExchangeService.ygAutoExchange(user, PlatformType.YG, SystemUtil.getStation());
        jsonObj.forEach((k,v)->{
            String usernameYG =  jsonObj.getString("username");
            if(StringUtils.isNotEmpty(usernameYG)&&usernameYG.length() > 6){
                //字符串去掉后10位随机字符串
                jsonObj.put("username",usernameYG.substring(0, usernameYG.length() - 10));
            }
        });
        return jsonObj;
    }
    @Override
    public JSONObject findAccountInfo(String data) {
        return null;
    }

    @Override
    public boolean getOrderStatus(ThirdTransLog log, Station station) {
        return false;
    }

    @Override
    public boolean getOrderStatus(Long id, Station station) {
        return false;
    }

    private void checkError(JSONObject json) {
        if (!json.getBooleanValue("success")) {
            String msg = json.getString("msg");
            if ("5s内，不允许重复调用api".equals(msg)){
                throw new BaseException("Within 5s, it is not allowed to call the API repeatedly");
            } else if ("开始时间与结束时间最大范围需在14天内".equals(msg)) {
                throw new BaseException("Search start <-> end time interval need within 14 days");
            }
            throw new BaseException(I18nTool.getMessage(ValidateUtil.isCodeError(json.getString("code"), json.getString("msg"))));
        }
    }

    @Override
    public JSONObject convertYGData(JSONObject json) {
        List<BcLotteryOrderBo> list = null;
        try {
            list = JSONArray.parseArray(json.getString("rows"), BcLotteryOrderBo.class);
        } catch (Exception e) {
         //   logger.error("YG彩票注单数据转换异常,e:{}", e.getMessage(), e);
            throw new BaseException(e);
        }
        List<Map<String, Object>> resList = new ArrayList<>();
        list.stream().forEach(e -> {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", e.getOrderId()); //订单号
            map.put("platformType", e.getPlatformType());
            map.put("username", e.getUsername());
            map.put("thirdUserName", e.getThirdUserName());
            map.put("gameName", e.getLotName());
            map.put("playName", e.getPlayName());
            map.put("qiHao", e.getQiHao());
            map.put("content", e.getContent());
            map.put("bettingMoney", e.getBettingMoney());
            map.put("realBettingMoney", e.getRealBettingMoney());
            map.put("winMoney", e.getWinMoney());
            map.put("createDatetime", e.getBettingTime());
            map.put("createDatetimeStr", DateUtil.toDatetimeStr(e.getBettingTime()));
            map.put("status", getBetStatusTypeStr(e.getStatus()));
            resList.add(map);
        });
        JSONObject obj = new JSONObject();
        obj.put("rows", resList);
        obj.put("total", json.get("total"));
        obj.put("aggsData", json.getString("aggsData"));
        obj.put("success", json.getBooleanValue("success"));
        return obj;
    }

    public static int getBetStatusTypeStr(String betState) {
        //1未开奖 2已中奖 3未中奖 4撤单 5派奖回滚成功 6和局
        if (StringUtils.isEmpty(betState)) {
            return 1;
        }
        if (betState.equals("UNOPEN")) {
            return 1;
        }else if (betState.equals("WIN")) {
            return 2;
        }else if (betState.equals("LOSE")) {
            return 3;
        }else if (betState.equals("CANCELED")) {
            return 4;
        }else if (betState.equals("ROLLBACK")) {
            return 5;
        }else if (betState.equals("TIE")) {
            return 6;
        }
        return 1;
    }

    private String getYGUrl(String url) {
//        if (StringUtils.isNotEmpty(ygCenterDomain)) {
//            return new StringBuilder(ygCenterDomain).append(url).toString();
//        }
        ygCenterDomain = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.yg_api_entry_url);
        if (StringUtils.isEmpty(ygCenterDomain)) {
            throw new BaseException(BaseI18nCode.ygstationAdminContactLiveAddress);
        }
        return new StringBuilder(ygCenterDomain).append(url).toString();
    }

    private List<NameValuePair> getBasicParams(PlatformType pt, SysUser user, Station station) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (station != null) {
            params.add(new BasicNameValuePair("stationId", String.valueOf(station.getId())));
            params.add(new BasicNameValuePair("code", station.getCode()));
            params.add(new BasicNameValuePair("stationName", station.getName()));
            params.add(new BasicNameValuePair("currency", station.getCurrency()));
        }
        //params.add(new BasicNameValuePair("language", station.getLanguage()));
        params.add(new BasicNameValuePair("platform", String.valueOf(pt.getValue())));
        params.add(new BasicNameValuePair("userId", String.valueOf(user.getId())));
        params.add(new BasicNameValuePair("username", user.getUsername()));
        params.add(new BasicNameValuePair("parentIds", user.getParentIds()));
        params.add(new BasicNameValuePair("language", SystemUtil.getLanguage()));
        if (user.getRecommendId() != null) {
            params.add(new BasicNameValuePair("recommendId", String.valueOf(user.getRecommendId())));
        }
        return params;
    }

    /**
     * 获取云迹API授权KEY及DES
     *
     * @return
     */
    private String getYGKey() {
        if (StringUtils.isNotEmpty(ygApiKey)) {
            return ygApiKey;
        }
        ygApiKey = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.yg_api_key);
        if (StringUtils.isEmpty(ygApiKey)) {
            throw new BaseException(BaseI18nCode.ygstationAdminContactSetting);
        }
        return ygApiKey;
    }

    private String getYgApiAesKey() {
        if (StringUtils.isNotEmpty(ygApiAesKey)) {
            return ygApiAesKey;
        }
        ygApiAesKey = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.yg_api_aes_key);
        if (StringUtils.isEmpty(ygApiAesKey)) {
            throw new BaseException(BaseI18nCode.stationAdminContactSetting);
        }
        return ygApiAesKey;
    }

    @Override
    public void checkMaintenance(PlatformType pf, Long stationId) {
        ThirdPlatform tp = thirdPlatformService.findOne(stationId, pf.getValue());
        if (tp == null || !Objects.equals(tp.getStatus(), Constants.STATUS_ENABLE)) {
            logger.error(pf.name() + "游戏已关闭");
            throw new ThirdException(BaseI18nCode.stationExpectHope);
        }
    }

    /**
     * 转入第三方第一步，扣钱并记录转账日志
     */
    @Override
    public ThirdTransLog transToThirdStep1(PlatformType pt, SysUser acc, BigDecimal money, Station station) {
        this.checkMaintenance(pt, acc.getStationId());
        // 判断转入金额
        ThirdTransferOutLimit limit = thirdTransferOutLimitDao.findByPlatformStationId(acc.getStationId(),
                pt.getValue());
        if (limit != null) {
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
        try{
         //   JSONObject obj = null;
            if (GuestTool.isGuest(acc)) {
                ygCenterService.guestRegister(acc.getUsername());
            } else {
                ygCenterService.createUser(acc);
            }
         //   logger.error("YG...transToThirdStep1:"+obj);
        }catch (Exception e){
            logger.error("create user e = ",e);
        }
        BigDecimal beforeMoney = getBalanceForTrans(pt, acc, station);// 先获取账户金额
        // 扣除金额并记录账变
        String orderId = OrderIdMaker.getExchangeOrderId();
        MnyMoneyBo vo = new MnyMoneyBo();
        vo.setUser(acc);
        vo.setMoney(money);
        vo.setMoneyRecordType(MoneyRecordType.REAL_GAME_SUB);
        vo.setOrderId(orderId);
        vo.setStationId(acc.getStationId());
        //vo.setRemark(I18nTool.getMessage(BaseI18nCode.thisSystemPlat) + " -> " + pt.getTitle());

        List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.thisSystemPlat.getCode(), pt.name());
        String remarkString = I18nTool.convertCodeToArrStr(remarkList);
        vo.setRemark(remarkString);

        moneyService.updMnyAndRecord(vo);
        // 记录转账日志
        return saveTransferLog(acc, orderId, pt.getValue(), ThirdTransLog.TYPE_INTO_THIRD, money, beforeMoney);
    }

    @Override
    public JSONObject transToThirdStep2(SysUser acc, ThirdTransLog log, PlatformType pt, Station station) {
        JSONObject json = new JSONObject();
        try {
            // 转出金额汇率转换
            log.setMoney(log.getMoney().multiply(CurrencyUtils.getTransOutRate(station.getCurrency())));

            JSONObject jsonObj = callYgTrans(log.getPlatform(), acc, log.getOrderId(), log.getType(), log.getMoney(),
                    station);
            boolean success = jsonObj.getBooleanValue("success");
           // logger.error("jsonobj when transto thhird stepw === " + jsonObj.toJSONString());
//            Boolean rollback = jsonObj.getBoolean("rollback");
            if (!success) {//调用充值接口不成功则回滚
                List <String> msgList = I18nTool.convertCodeToList(ValidateUtil.isCodeErrorGetCode(jsonObj.getString("code"), jsonObj.getString("arts")));
                String msgString = I18nTool.convertCodeToArrStr(msgList);
                log.setMsg(msgString);
                transToThirdRollback(log, acc);
                json.put("success", false);
                String msg = jsonObj.getString("msg");
                if (StringUtils.isNotEmpty(msg) && msg.contains("会员不存在")) {
                    msg = I18nTool.getMessage(BaseI18nCode.memberUnExist);
                }
//                json.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
                json.put("msg", msg);
                return json;
            }
            if (success) {
                transToThirdSuccess(acc, log, pt, station);
                json.put("success", true);
                json.put("msg", I18nTool.getMessage(BaseI18nCode.stationTranSucc));
                return json;
            }
            json.put("success", false);
            json.put("msg", I18nTool.getMessage(ValidateUtil.isCodeError(jsonObj.getString("code"), jsonObj.getString("arts"))));
            return json;
        } catch (Exception e) {
            logger.error("转账异常,请联系客服", e);
            transToThirdRollback(log, acc);//异常时将金额回滚
            json.put("success", false);
            json.put("msg", I18nTool.getMessage(BaseI18nCode.stationTransandAccountError));
            return json;
        }
    }

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
     * 从第三方转出到本系统第二步，成功就加余额
     */
    @Override
    public JSONObject takeOutToSysStep2(SysUser acc, ThirdTransLog log, PlatformType pt, Station station) {
        JSONObject json = new JSONObject();
        try {
            JSONObject jsonObj = callYgTrans(log.getPlatform(), acc, log.getOrderId(), log.getType(), log.getMoney(),
                    station);
            boolean success = jsonObj.getBooleanValue("success");
         //   logger.error("auto exchange history takeOutToSysStep2: jsonObj:{}", jsonObj);
            if (!success) {//提款失败则回滚
                List <String> msgList = I18nTool.convertCodeToList(ValidateUtil.isCodeErrorGetCode(jsonObj.getString("code"), jsonObj.getString("arts")));
                String msgString = I18nTool.convertCodeToArrStr(msgList);
                thirdTransLogDao.updateFailed(log.getId(), log.getStationId(),msgString);
                json.put("success", false);
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
        // 转入金额汇率转换
        log.setMoney(log.getMoney().multiply(CurrencyUtils.getTransInRate(station.getCurrency())));
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
            //vo.setRemark(pt.getTitle() + " -> " + I18nTool.getMessage(BaseI18nCode.thisSystemPlat));
            List <String> remarkString = I18nTool.convertCodeToList(pt.name()," -> ",BaseI18nCode.thisSystemPlat.getCode());
            vo.setRemark(I18nTool.convertCodeToArrStr(remarkString));
            moneyService.updMnyAndRecord(vo);
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
//            vo.setRemark(I18nTool.getMessage(BaseI18nCode.thisSystemPlat) + " -> "
//                    + PlatformType.getPlatform(log.getPlatform()).getTitle()
//                    + I18nTool.getMessage(BaseI18nCode.thirdTurnSeedError));
            List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.thisSystemPlat.getCode()," -> ",
                    PlatformType.getPlatform(log.getPlatform()).getTitle(),BaseI18nCode.thirdTurnSeedError.getCode());
            vo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
            vo.setMoneyRecordType(MoneyRecordType.REAL_GAME_FAILED);
            moneyService.updMnyAndRecord(vo);
        }
    }

    private void transToThirdSuccess(SysUser acc, ThirdTransLog log, PlatformType pt, Station station) {
        // 获取转账后的额度
        // log.setAfterMoney(log.getMoney().add(log.getBeforeMoney()));
        log.setAfterMoney(getBalanceForTrans(pt, acc, station));
        log.setUpdateDatetime(new Date());
        thirdTransLogDao.updateSuccess(log);
        autoExchangeService.saveOrUpdate(pt.getValue(), station.getId(), acc.getId(),
                ThirdAutoExchange.type_sys_to_third);
    }

    private JSONObject callYgTrans(int platform, SysUser acc, String orderId, int type, BigDecimal money, Station station) {
      //  logger.error("callYgTrans: user:{}, orderId:{}, type:{}, money:{}, currency:{}", acc.getUsername(), orderId,
         //       type, money, station.getCurrency());

        String json = new HttpClientProxy() {
            @Override
            public List<NameValuePair> getParameters() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                try{
                    JSONObject d = new JSONObject();
                    String md5 = MD5Util.md5(acc.getUsername());
                    String finalUsername = acc.getUsername();
                    if (StringUtils.isNotEmpty(finalUsername) && finalUsername.length() > 6) {
                        finalUsername = finalUsername.substring(0, 6);
                    }
                    String userName = finalUsername + md5.substring(5,15);
                    d.put("username", userName);
                    d.put("money", money.toString());
                    d.put("orderId", orderId);
                    String dataOri = JSONObject.toJSONString(d);
                    String data = AES.encrypt(dataOri, getYgApiAesKey());
                    String sign = MD5Util.md5(data + getYGKey());
                    params.add(new BasicNameValuePair("data", data));
                    params.add(new BasicNameValuePair("sign", sign));
                    return params;
                }catch (Exception e){
                    logger.error("eee error = ", e);
                }
                return params;
            }
        }.doPost(getYGUrl(type == ThirdTransLog.TYPE_INTO_THIRD ? "/api/money/deposit.do" : "/api/money/withdraw.do"));
        JSONObject obj = JSON.parseObject(json);
        if (obj.getBooleanValue("maintain")) {
            throw new BaseException(BaseI18nCode.stationLiveCenter);
        }
        return obj;
    }

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
     * 转账时查询余额
     */
    @Override
    public BigDecimal getBalanceForTrans(PlatformType pt, SysUser acc, Station station) {
        try {
            String json = new HttpClientProxy() {
                @Override
                public List<NameValuePair> getParameters() {
                    List<NameValuePair> params = getBasicParams(PlatformType.YG, acc, station);
                    try{
                        JSONObject d = new JSONObject();
                        String md5 = MD5Util.md5(acc.getUsername());
                        String finalUsername = acc.getUsername();
                        if (StringUtils.isNotEmpty(finalUsername) && finalUsername.length() > 6) {
                            finalUsername = finalUsername.substring(0, 6);
                        }
                        String userName = finalUsername + md5.substring(5,15);
                        d.put("username", userName);
                        String dataOri = JSONObject.toJSONString(d);
                        String data = AES.encrypt(dataOri, getYgApiAesKey());
                        String sign = MD5Util.md5(data + getYGKey());
                        params.add(new BasicNameValuePair("data", data));
                        params.add(new BasicNameValuePair("sign", sign));
                        params.add(new BasicNameValuePair("oriIp", IpUtils.getIp()));
                    }catch (Exception e){
                        logger.error("getBalanceForTrans exception: ", e);
                    }
                    return params;
                }
            }.doPost(getYGUrl("/api/money/getBalance.do"));
            JSONObject obj = JSON.parseObject(json);
        //    logger.error("getBalanceForTrans obj:{} ", obj.toJSONString());
            if (obj.getBooleanValue("maintain")) {
                throw new BaseException(BaseI18nCode.stationLiveCenter);
            }
            if (obj.getBooleanValue("success") == false) {
                String msg = obj.getString("msg");
                if (StringUtils.isNotEmpty(msg) && msg.contains("会员不存在")) {
                    msg = I18nTool.getMessage(BaseI18nCode.memberUnExist);
                }
//                throw new BaseException(msg);
                return BigDecimal.ZERO;
            }
            return BigDecimalUtil.toBigDecimalDefaultZero(obj.getString("balance"));
        } catch (BaseException e) {
            logger.error("getBalanceForTrans...error:"+ ExceptionUtils.getStackTrace(e));
            return BigDecimal.ZERO;
        } catch (Exception e) {
            logger.error("获取余额发生错误3", e);
            throw new BaseException(BaseI18nCode.stationNetError);
        }
    }


}
