package com.play.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.ParameterException;
import com.play.common.Constants;
import com.play.common.PayWrapper;
import com.play.common.PayWrapperFactory;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.LogUtils;
import com.play.core.PayPlatformEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.StationDepositOnlineDao;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositOnline;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.service.SysUserService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author admin
 */
@Service
public class StationDepositOnlineServiceImpl implements StationDepositOnlineService {
    private static Logger logger = LoggerFactory.getLogger(StationDepositOnlineServiceImpl.class);
    @Autowired
    private StationDepositOnlineDao stationDepositOnlineDao;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private MnyDepositRecordService mnyDepositRecordService;

    @Override
    public Page<StationDepositOnline> getPage(String name, Integer status, Long stationId) {
        List<String> enumlist = PayPlatformEnum.onlinePaymentBlurry(name);
        Page<StationDepositOnline> page = stationDepositOnlineDao.getPage(enumlist, status, stationId);
        List<StationDepositOnline> payPlatformEnumList = new ArrayList<>();
        page.getRows().stream().forEach(online -> {
            Optional.of(online.getPayPlatformCode()).ifPresent(x -> {
            	/**
            	 * 如果把不必要的 三方渠道屏蔽掉 这里会报错（如果已经配了这个第三方）
            	 * </p>
            	 * 暂时先这么解决一下
            	 * */
            	try {
            		online.setPayName(PayPlatformEnum.valueOfPayName(x));
				} catch (Exception e) {
					// TODO: handle exception
					online.setPayName("");
					logger.error("catch an exception, e=", e);
				}
                payPlatformEnumList.add(online);
            });
        });
        page.setRows(payPlatformEnumList);
        return page;
    }


    @Override
    public void addSave(StationDepositOnline online) {
        if (online == null) {
            throw new ParamException(BaseI18nCode.dataError);
        }
        online.setMerchantKey(clearEmptyStr(online.getMerchantKey()));
        online.setMerchantCode(clearEmptyStr(online.getMerchantCode()));
        online.setUrl(clearEmptyStr(online.getUrl()));
        online.setIcon(clearEmptyStr(online.getIcon()));
        online.setPayGetway(clearEmptyStr(online.getPayGetway()));
        online.setPayType(clearEmptyStr(online.getPayType()));
        online.setAccount(clearEmptyStr(online.getAccount()));
        online.setAppid(clearEmptyStr(online.getAppid()));
        online.setFixedAmount(clearEmptyStr(online.getFixedAmount()));
        online.setBankList(clearEmptyStr(spliceBankData(online.getBankList())));
        online.setPayAlias(clearEmptyStr(online.getPayAlias()));
        online.setAlterNative(clearEmptyStr(online.getAlterNative()));
        online.setWhiteListIp(clearEmptyStr(online.getWhiteListIp()));
        online.setBgRemark(clearEmptyStr(online.getBgRemark()));
        online.setQueryGateway(clearEmptyStr(online.getQueryGateway()));
        online.setPcRemark(clearEmptyStr(online.getPcRemark()));
        online.setWapRemark(clearEmptyStr(online.getWapRemark()));
        online.setAppRemark(clearEmptyStr(online.getAppRemark()));
        online.setDegreeIds(clearEmptyStr(online.getDegreeIds()));
        online.setGroupIds(clearEmptyStr(online.getGroupIds()));

        Long stationId = SystemUtil.getStationId();

        //修改
        if (online.getId() != null && online.getId() > 0) {
            StationDepositOnline updateOnline = stationDepositOnlineDao.findOneById(online.getId());
            String beforeBanklist = updateOnline.getBankList();
            if (!online.getMerchantCode().contains("*")) {
                updateOnline.setMerchantCode(online.getMerchantCode());
            }
            if (!online.getMerchantKey().contains("*")) {
                updateOnline.setMerchantKey(online.getMerchantKey());
            }
            if (!online.getAccount().contains("*")) {
                updateOnline.setAccount(online.getAccount());
            }
            if (!online.getBgRemark().contains("*")) {
                updateOnline.setBgRemark(online.getBgRemark());
            }
            if (!online.getQueryGateway().contains("*")) {
                updateOnline.setQueryGateway(online.getQueryGateway());
            }

            updateOnline.setUrl(online.getUrl());
            updateOnline.setMin(online.getMin());
            updateOnline.setMax(online.getMax());
            // 默认禁用
            updateOnline.setStatus(online.getStatus());
            updateOnline.setDef(online.getDef());
            updateOnline.setIcon(online.getIcon());
            updateOnline.setPayType(online.getPayType());
            updateOnline.setPayGetway(online.getPayGetway());
            updateOnline.setAppid(online.getAppid());
            updateOnline.setSortNo(online.getSortNo());
            updateOnline.setShowType(online.getShowType());
            updateOnline.setIsFixedAmount(online.getIsFixedAmount());
            updateOnline.setFixedAmount(online.getFixedAmount());
            updateOnline.setBankList(online.getBankList());
            updateOnline.setPayAlias(online.getPayAlias());
            updateOnline.setAlterNative(online.getAlterNative());
            updateOnline.setWhiteListIp(online.getWhiteListIp());
            updateOnline.setBgRemark(online.getBgRemark());
            updateOnline.setQueryGateway(online.getQueryGateway());
            updateOnline.setCreateUser(LoginAdminUtil.getUserId());
            updateOnline.setOpenUser(null);
            updateOnline.setDepositDailyStartTime(online.getDepositDailyStartTime());
            updateOnline.setDepositDailyEndTime(online.getDepositDailyEndTime());
            updateOnline.setVersion(online.getVersion());
            updateOnline.setPcRemark(online.getPcRemark());
            updateOnline.setWapRemark(online.getWapRemark());
            updateOnline.setAppRemark(online.getAppRemark());
            updateOnline.setSystemType(online.getSystemType());
            updateOnline.setGroupIds(online.getGroupIds());
            updateOnline.setDegreeIds(online.getDegreeIds());
            if (online.getRandomAdd() == null || online.getRandomAdd() < 0) {
                updateOnline.setRandomAdd(0);
            } else {
                updateOnline.setRandomAdd(online.getRandomAdd());
            }
            if (StringUtils.isEmpty(updateOnline.getWhiteListIp()) && updateOnline.getStatus() == Constants.STATUS_ENABLE) {
                throw new ParamException(BaseI18nCode.whiteIpWarning);
            }
            LogUtils.modifyLog("修改在线充值配置：" + updateOnline.getPayPlatformCode() + " 商户编号："
                    + updateOnline.getMerchantCode() + "  白名单ip：" + updateOnline.getWhiteListIp() + " 备注:" + updateOnline.getBgRemark() + " 勾选支付:" + updateOnline.getBankList() + " 修改前勾选支付:" + beforeBanklist);
            stationDepositOnlineDao.update(updateOnline, false);
        } else {
            if (StringUtils.isEmpty(online.getWhiteListIp()) && online.getStatus() == Constants.STATUS_ENABLE) {
                throw new ParamException(BaseI18nCode.whiteIpWarning);
            }
            online.setCreateUser(LoginAdminUtil.getUserId());
            online.setStationId(stationId);
            LogUtils.addLog("新增在线充值配置：" + online.getPayPlatformCode() + " 商户编号：" + online.getMerchantCode()
                    + "  白名单ip：" + online.getWhiteListIp());
            stationDepositOnlineDao.save(online);
        }
    }

    private String clearEmptyStr(String str) {
        if (str == null) {
            return "";
        }
        str = str.trim();
        str = str.replaceAll(" ", "");
        str = str.replaceAll("\n", "");
        str = str.replaceAll("\r", "");
        return str;
    }

    /**
     * 拼接 收银台列表
     *
     * @param bankData
     * @return
     */
    public String spliceBankData(String bankData) {
        String data = "";
        String Gateway = "";
        String bankH5 = "";
        String bankScan = "";
        String mobileH5 = "";
        String pc = "";
        if (bankData != null) {
            String[] str = bankData.split(",");
            for (int i = 0; i < str.length; i++) {
                if (str[i].equals("bankcodes")) {// 判断是否为 网关支付
                    Gateway += str[i];
                } else {
                    if (str[i].contains("H5") || str[i].contains("REVERSE")) {// 判断是否为H5
                        bankH5 += str[i] + ",";
                    } else if (str[i].contains("pc")) {// pc端显示
                        pc += str[i] + ",";
                    } else if (str[i].contains("h5")) {// 移动端显示
                        mobileH5 += str[i] + ",";
                    } else {// 判断是否为扫码
                        bankScan += str[i] + ",";
                    }
                }

            }
        }

        if (Gateway != "") {
            data += Gateway + "|";
        } else {
            data += "n" + "|";
        }
        if (bankScan != "") {
            data += bankScan + "|";
        } else {
            data += "n" + "|";
        }
        if (bankH5 != "") {
            data += bankH5 + "|";
        } else {
            data += "n" + "|";
        }
        if (pc != "") {
            data += pc + "|";
        } else {
            data += "n" + "|";
        }
        if (mobileH5 != "") {
            data += mobileH5 + "|";
        } else {
            data += "n" + "|";
        }
        String bankdata = "";
        if (data != "") {
            String str = data.replaceAll(",\\|", "|");
            bankdata = str.substring(0, str.length() - 1);
        }
        return bankdata;
    }

    @Override
    public void modifySave(StationDepositOnline online) {

    }

    @Override
    public void delOnline(long id) {
        StationDepositOnline online = stationDepositOnlineDao.findOneById(id);
        if (online == null) {
            throw new ParamException(BaseI18nCode.orderNotExist);
        }
        LogUtils.delLog("删除在线充值：" + online.getPayPlatformCode() + " 商户编号：" + online.getMerchantCode()
                + "  持卡人：" + online.getAccount());
        stationDepositOnlineDao.deleteById(id);
    }

    @Override
    public StationDepositOnline getOne(Long id, Long stationId) {
        StationDepositOnline online = stationDepositOnlineDao.findOne(id, stationId);
        if (online != null) {
            online.setPayName(PayPlatformEnum.valueOfPayName(online.getPayPlatformCode()));
            online.setMerchantCode(removePart(online.getMerchantCode()));
            online.setMerchantKey(removePart(online.getMerchantKey()));
            online.setAccount(removePart(online.getAccount()));
        }
        return online;
    }

    @Override
    public StationDepositOnline getOneNoHide(Long id, Long stationId) {
        return stationDepositOnlineDao.findOne(id, stationId);
    }

    /**
     * 隐藏部分数据
     *
     * @param text
     * @return
     */
    public static String removePart(String text) {
        if (text == null) {
            return "";
        }
        int len = text.length();
        if (len > 8) {
            return new StringBuilder(text.substring(0, 3)).append("***").append(text.substring(len - 4)).toString();
        }
        if (len > 4) {
            return new StringBuilder("***").append(text.substring(len - 4)).toString();
        }
        if (len > 2) {
            return new StringBuilder("***").append(text.substring(len - 2)).toString();
        }
        return text;
    }

    @Override
    public void updateStatus(Integer status, Long id, Long stationId) {
        if (id == null || id <= 0 || status == null || stationId == null) {
            throw new ParamException(BaseI18nCode.parameterError);
        }
        StationDepositOnline saveOnline = stationDepositOnlineDao.findOneById(id);
        Long loginUser = LoginAdminUtil.getUserId();
        if (saveOnline == null) {
            throw new ParamException(BaseI18nCode.depositMethodNotExist);
        }
        if (StringUtils.isEmpty(saveOnline.getWhiteListIp()) && status == Constants.STATUS_ENABLE) {
            throw new ParamException(BaseI18nCode.whiteIpNullNotOn);
        }
        stationDepositOnlineDao.updateStatus(status, id, stationId, loginUser);
        LogUtils.modifyStatusLog("修改在线充值：" + saveOnline.getMerchantCode() + "  修改网站入款设置状态："
                + ((status.equals(1)) ? "禁用" : "启用") + "    站点：" + stationId);
    }

    @Override
    public List<StationDepositOnline> getOnlineListByStation(Long stationId) {
        List<StationDepositOnline> onlineList = stationDepositOnlineDao.getDistinctOnlineListByStationId(stationId);
        onlineList.forEach(x -> {
            x.setPayName(PayPlatformEnum.valueOfPayName(x.getPayPlatformCode()));
        });
        return onlineList;
    }

    @Override
    public List<StationDepositOnline> getStationOnlineList(Long stationId, Long degreeId, Long groupId, Integer status) {
        return stationDepositOnlineDao.getStationOnlineList(stationId, degreeId, groupId, status);
    }

    @Override
    public void remarkModify(Long id, String remark) {
        Long stationId = SystemUtil.getStationId();
        StationDepositOnline ado = stationDepositOnlineDao.findOne(id, stationId);
        if (ado == null) {
            throw new ParamException(BaseI18nCode.depositMethodNotExist);
        }
        stationDepositOnlineDao.changeRemark(id, stationId, remark);
        LogUtils.modifyLog("用户:" + ado.getAccount() + " 的备注从 " + ado.getBgRemark() + " 修改成 " + remark);
    }

    @Override
    public StationDepositOnline getOneBySidAndId(Long stationId, Long id) {
        return stationDepositOnlineDao.getOneBySidAndId(stationId, id, Constants.STATUS_ENABLE);
    }

    @Override
    public StationDepositOnline findOneById(Long payId, Long stationId) {
        StationDepositOnline online = stationDepositOnlineDao.findOne(payId, stationId);
        if (online != null) {
            online.setPayName(PayPlatformEnum.valueOfPayName(online.getPayPlatformCode()));
            online.setMerchantCode(removePart(online.getMerchantCode()));
            online.setMerchantKey(removePart(online.getMerchantKey()));
            online.setAccount(removePart(online.getAccount()));
        }
        return online;
    }

    @Override
    public JSONObject doPay(BigDecimal amount, Long payId, String username, String bankCode, String remark, String ip, String domain, String payPlatformCode, String agentUser) {
        SysUser acc = LoginMemberUtil.currentUser();
        if(GuestTool.isGuest(acc)) {
            throw new BaseException(BaseI18nCode.gusetPleaseRegister);
        }
        if (StringUtils.isNotEmpty(username)) {
            acc = sysUserService.findOneByUsername(username, SystemUtil.getStationId(), null);
        }
        if (acc == null || GuestTool.isGuest(acc)) {
            throw new ParamException(BaseI18nCode.memberUnExist);
        }
        StationDepositOnline online = stationDepositOnlineDao.findOne(payId, acc.getStationId());
        if (online == null) {
            throw new ParamException(BaseI18nCode.depositMethodNotExist);
        }
        if (online.getStatus() != Constants.STATUS_ENABLE) {
            throw new ParamException(BaseI18nCode.depositMethodClosed);
        }
        if (online.getMin() != null && amount != null && amount.compareTo(online.getMin()) == -1) {
            throw new ParamException(BaseI18nCode.depositMoneyMustGt, online.getMin().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
        }
        if (online.getMax() != null && amount != null && amount.compareTo(online.getMax()) == 1) {
            throw new ParamException(BaseI18nCode.depositMoneyMustLt, online.getMax().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
        }

        String onlinePayType = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());

        if (StringUtils.isEmpty(onlinePayType)) {
            throw new ParameterException(I18nTool.getMessage(BaseI18nCode.thisPayWayNull));
        }
        if (StringUtils.isEmpty(bankCode)) {
            throw new ParameterException(I18nTool.getMessage(BaseI18nCode.bankcodeCanntEmpty));
        }
        Long degreeId = acc.getDegreeId();
        Long groupId = acc.getGroupId();
        if (degreeId != null) {
            Set<Long> degreeIds = getSet(online.getDegreeIds());
            if (!degreeIds.contains(degreeId)) {
                throw new ParamException(BaseI18nCode.degreeCanntDeposit);
            }
        }
        if (groupId != null) {
            Set<Long> groupIds = getSet(online.getGroupIds());
            if (!groupIds.contains(groupId)) {
                throw new ParamException(BaseI18nCode.degreeCanntDeposit);
            }
        }
       // amount = addOrSubRandom(amount, online);
        // 保存充值记录
        MnyDepositRecord record = mnyDepositRecordService.depositSave(amount, MnyDepositRecord.PAY_CODE_ONLINE, payId,
                null, null, null, acc, bankCode, "", payPlatformCode, null, null);
        if (record == null) {
            throw new ParamException(BaseI18nCode.saveDepositOrderException);
        }

        //如果是后台新增的试玩账号，直接跳过请求第三方
        if (GuestTool.isGuest(acc)) {
            JSONObject result = new JSONObject();
            result.put("success", true);
            result.put("msg", BaseI18nCode.operateSuccess);
            return result;
        }

        String payDomain = online.getUrl();
        String merchantCode = online.getMerchantCode();
        String merchantKey = online.getMerchantKey();
        String merchantAccount = online.getAccount();
//        BigDecimal min = online.getMin();
//        BigDecimal max = online.getMax();
        String payType = online.getPayType();
        String payGetway = online.getPayGetway();
        String appid = online.getAppid();
        String alterNative = online.getAlterNative();
        String orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(record.getCreateDatetime());
    //    logger.error("online pay fff result 3333=== " );
        try {
            switch (onlinePayType) {
                default:
                    PayWrapper wrapper = PayWrapperFactory.getWrapper(PayPlatformEnum.getEnumName(online.getPayPlatformCode()));
                    JSONObject result = wrapper.wrap(merchantCode, merchantKey, record.getOrderId(), amount.toString(), payDomain, bankCode, merchantAccount, ip, payDomain, payType, payGetway, appid, domain, username, orderTime, alterNative, record.getUsername(), agentUser);
                //    logger.error("online pay fff result 4444=== " + result.toJSONString());
                    if(result.getString("thirdOrderId") != null) {
                    	mnyDepositRecordService.bgRemarkModify(record.getId(), result.getString("thirdOrderId") );
                    }
                    return result;
            }
        } catch (Exception e) {
            logger.error("在线存款发生错误", e);
            throw new ParamException(BaseI18nCode.depositSaveFail, e.getMessage());
        }
    }

    private Set<Long> getSet(String ids) {
        Set<Long> set = new HashSet<>();
        if (ids != null) {
            for (String id : ids.split(",")) {
                if (StringUtils.isNotEmpty(id)) {
                    set.add(NumberUtils.toLong(id));
                }
            }
        }
        return set;
    }

    private static final Random RANDOM = new Random();

    /**
     * 根据
     *
     * @param amount
     * @param online
     * @return
     */
    private BigDecimal addOrSubRandom(BigDecimal amount, StationDepositOnline online) {
        if (online.getRandomAdd() != null && online.getRandomAdd() > 0) {
            int i = RANDOM.nextInt(online.getRandomAdd());
            return amount.add(new BigDecimal(i).divide(BigDecimalUtil.HUNDRED));
        }
        return amount;
    }
}
