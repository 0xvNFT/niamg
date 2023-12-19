package com.play.model.app;

import java.util.ArrayList;
import java.util.List;

public class PayMethodInfoForRN {

    public static final int ONLINE_PAY_TYPE = 1;
    public static final int FAST_PAY_TYPE = 2;
    public static final int BANK_PAY_TYPE = 3;

    long id;
    long maxFee;//最大充值金额
    long minFee;//最小充值金额
    String icon;//支付图标地址
    String payName;//支付名称
    int status;//开关状态
    String payType;//支付方式,用于确定支付跳转时的支付方式类型
    String iconCss;//支付css,用于跳转支付地址或支付二维码时使用
    String frontDetails;//前台操作说明
    String fixedAmount = "";//可选固定金额，逗号分隔
    String receiveName;//收款人姓名；仅用于快速和银行支付类型
    String qrCodeImg;//扫码二维码地址;仅用于快速和银行支付类型
    Integer depositType;
    List<PayMethodInfoForRN> sub = new ArrayList<>();

    //在线充值-----------------
    String merchantCode;//商户ID
    String payGetway;//支付网关
    String merchantKey;//商户密钥
    String appId;
    Integer isFixedAmount;//是否固定金额
    String bankList;//银行台列表
    String payAlias;//前台显示别名
    String url;
    String showType;

    //快速充值-------------------------
    String receiveAccount;//收款帐号
    String frontLabel;//快速支付前端显示的文字


    //银行充值 --------------------------------

    String uuid;
    String bankCard;//银行帐号
    String bankAddress;//开户地址
    String payBankName;//银行名称
    Integer aliQrcodeStatus;
    String aliQrcodeLink;
    String remark;
    private boolean isUSDT;
    private String usdtRemark;
    private String usdtRate;

    public String getUsdtRemark() {
        return usdtRemark;
    }

    public void setUsdtRemark(String usdtRemark) {
        this.usdtRemark = usdtRemark;
    }

    public String getUsdtRate() {
        return usdtRate;
    }

    public void setUsdtRate(String usdtRate) {
        this.usdtRate = usdtRate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isUSDT() {
        return isUSDT;
    }

    public void setUSDT(boolean USDT) {
        isUSDT = USDT;
    }

    public long getMaxFee() {
        return maxFee;
    }

    public void setMaxFee(long maxFee) {
        this.maxFee = maxFee;
    }

    public long getMinFee() {
        return minFee;
    }

    public void setMinFee(long minFee) {
        this.minFee = minFee;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getIconCss() {
        return iconCss;
    }

    public void setIconCss(String iconCss) {
        this.iconCss = iconCss;
    }

    public String getFrontDetails() {
        return frontDetails;
    }

    public void setFrontDetails(String frontDetails) {
        this.frontDetails = frontDetails;
    }

    public String getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(String fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getQrCodeImg() {
        return qrCodeImg;
    }

    public void setQrCodeImg(String qrCodeImg) {
        this.qrCodeImg = qrCodeImg;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getPayGetway() {
        return payGetway;
    }

    public void setPayGetway(String payGetway) {
        this.payGetway = payGetway;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getFrontLabel() {
        return frontLabel;
    }

    public void setFrontLabel(String frontLabel) {
        this.frontLabel = frontLabel;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getPayBankName() {
        return payBankName;
    }

    public void setPayBankName(String payBankName) {
        this.payBankName = payBankName;
    }

    public Integer getAliQrcodeStatus() {
        return aliQrcodeStatus;
    }

    public void setAliQrcodeStatus(Integer aliQrcodeStatus) {
        this.aliQrcodeStatus = aliQrcodeStatus;
    }

    public String getAliQrcodeLink() {
        return aliQrcodeLink;
    }

    public void setAliQrcodeLink(String aliQrcodeLink) {
        this.aliQrcodeLink = aliQrcodeLink;
    }

    public Integer getDepositType() {
        return depositType;
    }

    public void setDepositType(Integer depositType) {
        this.depositType = depositType;
    }

    public List<PayMethodInfoForRN> getSub() {
        return sub;
    }

    public void setSub(List<PayMethodInfoForRN> sub) {
        this.sub = sub;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getIsFixedAmount() {
        return isFixedAmount;
    }

    public void setIsFixedAmount(Integer isFixedAmount) {
        this.isFixedAmount = isFixedAmount;
    }

    public String getBankList() {
        return bankList;
    }

    public void setBankList(String bankList) {
        this.bankList = bankList;
    }

    public String getPayAlias() {
        return payAlias;
    }

    public void setPayAlias(String payAlias) {
        this.payAlias = payAlias;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public String getUuid(){
        return uuid;
    }

}
