package com.play.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.common.utils.RandomStringUtils;
import com.play.core.LogTypeEnum;
import com.play.core.StationConfigEnum;
import com.play.dao.SystemSmsConfigDao;
import com.play.model.Station;
import com.play.model.SystemSmsConfig;
import com.play.orm.jdbc.page.Page;
import com.play.service.SystemSmsConfigService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.ValidateUtil;
import com.play.web.utils.http.HttpClientProxy;
import com.play.web.var.SystemUtil;
import com.play.web.vcode.VerifyCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SystemSmsConfigServiceImpl implements SystemSmsConfigService {

    private Logger logger = LoggerFactory.getLogger(SystemSmsConfigServiceImpl.class);

    @Autowired
    private SystemSmsConfigDao systemSmsConfigDao;

    @Override
    public void saveConfig(SystemSmsConfig config) {
        if (config.getId() != null) {
            SystemSmsConfig old = systemSmsConfigDao.findOneById(config.getId());
            if (old == null) {
                throw new BaseException("配置不存在");
            }
            old.setAccount(config.getAccount());
            old.setPassword(config.getPassword());
            old.setCountryCode(config.getCountryCode());
            old.setCountry(config.getCountry());
            old.setLanguage(config.getLanguage());
            old.setAppKey(config.getAppKey());
            old.setAppSecret(config.getAppSecret());
            old.setAppCode(config.getAppCode());
            old.setGatewayUrl(config.getGatewayUrl());
            old.setStatus(config.getStatus());
            old.setContent(config.getContent());
            systemSmsConfigDao.update(old);
            LogUtils.log("修改短信验证平台配置", LogTypeEnum.DEFAULT_TYPE);
        } else {
            config.setStatus(Constants.STATUS_ENABLE);
            systemSmsConfigDao.save(config);
            LogUtils.log("新增短信验证平台配置", LogTypeEnum.DEFAULT_TYPE);
        }
    }

    @Override
    public Page<SystemSmsConfig> adminPage(Long stationId) {
        Page<SystemSmsConfig> page = systemSmsConfigDao.adminPage();
        return page;
    }


    @Override
    public SystemSmsConfig findOne(Long stationId, Long id) {
        return systemSmsConfigDao.findOneById(id);
    }

    @Override
    public void smsVerifySend(String countryCode,String phone,String vertifyCode, Long stationId) {
        if (StringUtils.isEmpty(phone)) {
            throw new BaseException(I18nTool.getMessage(BaseI18nCode.stationMobileNotNull));
        }
        if (StringUtils.isNotEmpty(vertifyCode)) {
            VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_SEND_SMS_KEY, vertifyCode);
        }
        if (phone.startsWith("+") || phone.startsWith("0")) {
            phone = phone.substring(1, phone.length());
        }
        if (!ValidateUtil.isPhoneNumber(phone)) {
            throw new BaseException(I18nTool.getMessage(BaseI18nCode.stationMobileFormatError));
        }
        SystemSmsConfig config = systemSmsConfigDao.findByCountry(countryCode);
        if (config == null) {
            throw new BaseException("短信通道未配置，请联系客服");
        }
//        if (!DistributedLockUtil.tryGetDistributedLock("smsSend:" + IpUtils.getIp(), 60000)) {
//            throw new BaseException("请勿重复发送");
//        }
        sendSmsCode(config, phone, null);
    }


    public void smsVerify(String phone, String verifyCode, Long stationId) {
//        if (StationConfigUtil.isOff(stationId, StationConfigEnum.on_off_register_sms_verify)) {
//            return;
//        }
//        String key = "sid:" + ServletUtils.getSession().getId() + ":phone:" + phone;
//        String value = CacheUtil.getCache(RedisKey.DEFAULT, key);
//        if (StringUtils.isEmpty(value) || !StringUtils.equals(value, verifyCode)) {
//            throw new UserException("请输入正确的短信验证码");
//        }
    }


    @Override
    public void delete(Long id, Long stationId) {
        SystemSmsConfig config = systemSmsConfigDao.findOneById(id);
        if (config == null) {
            throw new BaseException(I18nTool.getMessage(BaseI18nCode.illegalRequest));
        }
        systemSmsConfigDao.deleteById(id);
        LogUtils.log("删除短信验证配置",LogTypeEnum.DEFAULT_TYPE);
    }

    @Override
    public void updStatus(Long id, Long stationId, Integer status) {
        systemSmsConfigDao.changeStatus(id, stationId, status);
        LogUtils.log("修改短信验证配置状态",LogTypeEnum.DEFAULT_TYPE);
    }

    private void sendSmsCode(SystemSmsConfig config,String phone, String extend) {
        if (config == null) {
            return;
        }
        try {
            String smsCode = RandomStringUtils.randomInt(6);
            String content = config.getContent();
            if (StringUtils.isNotEmpty(content)) {
                content = config.getContent().replace("{{}}", smsCode);
//                Station bo = SystemUtil.getStation();
//                String webTitle = StationConfigUtil.get(bo.getId(), StationConfigEnum.station_name);
//                webTitle = StringUtils.isNotEmpty(webTitle) ? webTitle : bo.getName();
//                content = content.replace("[{}]", webTitle);
                config.setContent(content);
            }
            String json = new HttpClientProxy() {
                @Override
                public List<NameValuePair> getParameters() {
                    return getBasicParams(config,phone, extend);
                }
            }.doGet(config.getGatewayUrl());
            JSONObject obj = JSON.parseObject(json);
            if (obj != null && obj.getJSONArray("result") != null && obj.getJSONArray("result").size() > 0) {
                String code = obj.getString("code");
                if (StringUtils.isEmpty(code) || !code.equals("00000")) {
                    throw new BaseException(obj.getString("desc"));
                }
                String key = new StringBuilder("reqSmsCode:phone:").append(phone).append(":").append("sid:").append(SystemUtil.getStationId()).toString();
                RedisAPI.addCache(key, smsCode, 300);//验证码缓存300秒
            } else {
                String desc = (String) obj.get("desc");
                if (StringUtils.isEmpty(desc)) {
                    desc = I18nTool.getMessage(BaseI18nCode.operateError);
                }
                throw new BaseException(desc);
            }
        } catch (BaseException e) {
            logger.error("发送短信验证码发生错误1", e);
            throw e;
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private String fillSmsContent(){
        Station bo = SystemUtil.getStation();
        String webTitle = StationConfigUtil.get(bo.getId(), StationConfigEnum.station_name);
        webTitle = StringUtils.isNotEmpty(webTitle) ? webTitle : bo.getName();
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(webTitle).append("]").append("Your verification code is:");
        return sb.toString();
    }

    private List<NameValuePair> getBasicParams(SystemSmsConfig config,String phone,String extend) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("appkey", config.getAppKey()));
        params.add(new BasicNameValuePair("appcode", config.getAppCode()));
        params.add(new BasicNameValuePair("appsecret", config.getAppSecret()));
        params.add(new BasicNameValuePair("msg", config.getContent()));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("extend", extend));
        return params;
    }
}
