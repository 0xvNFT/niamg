package com.play.model.app;

import com.alibaba.fastjson.JSONObject;
import com.play.model.StationDrawFeeStrategy;
import com.play.model.SysUserBetNum;

import java.math.BigDecimal;

/**
 * @author johnson
 * 提款时帐号等相关数据
 */
public class PickMoneyData {

    int curWnum;//已提款次数
    int wnum;//今日还可提款几次,-1表示不限制
    boolean enablePick=true;//是否能取款
    String drawFlag;//不可提款的原因，可提款时值为"是"
    String startTime;//今日提款有效开始时间
    String endTime;//今天提款有效结束时间
    BigDecimal minPickMoney;//最小提现额度
    BigDecimal maxPickMoney;//最大提现额度
    Long bankId;
    String cardNo;//提款卡号
    String userName;//提款帐户名
    float validBetMoney;//有效投注金额
    int accountStatus;//帐号启用状态
    String bankAddress;//开户行
    String bankName;//银行名称
    float accountBalance;//帐户余额
    float checkBetNum;//出款需达到的投注量,-1表示不限投注量

    SysUserBetNum betNum;//打码量信息

    //手续费相关
    JSONObject strategy;//手续费策略
    boolean canDraw;
    private boolean isUSDT;
    private String usdtRemark;
    private String usdtRate;

    private String withdrawPageIntro;//提款页说明

    public boolean isUSDT() {
        return isUSDT;
    }

    public void setUSDT(boolean USDT) {
        isUSDT = USDT;
    }

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

    public JSONObject getStrategy() {
        return strategy;
    }

    public void setStrategy(JSONObject strategy) {
        this.strategy = strategy;
    }

    public boolean isCanDraw() {
        return canDraw;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public boolean isEnablePick() {
        return enablePick;
    }

    public void setEnablePick(boolean enablePick) {
        this.enablePick = enablePick;
    }

    public int getCurWnum() {
        return curWnum;
    }

    public void setCurWnum(int curWnum) {
        this.curWnum = curWnum;
    }

    public int getWnum() {
        return wnum;
    }

    public void setWnum(int wnum) {
        this.wnum = wnum;
    }

    public String getDrawFlag() {
        return drawFlag;
    }

    public void setDrawFlag(String drawFlag) {
        this.drawFlag = drawFlag;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getMinPickMoney() {
        return minPickMoney;
    }

    public void setMinPickMoney(BigDecimal minPickMoney) {
        this.minPickMoney = minPickMoney;
    }

    public BigDecimal getMaxPickMoney() {
        return maxPickMoney;
    }

    public void setMaxPickMoney(BigDecimal maxPickMoney) {
        this.maxPickMoney = maxPickMoney;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getValidBetMoney() {
        return validBetMoney;
    }

    public void setValidBetMoney(float validBetMoney) {
        this.validBetMoney = validBetMoney;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(float accountBalance) {
        this.accountBalance = accountBalance;
    }

    public float getCheckBetNum() {
        return checkBetNum;
    }

    public void setCheckBetNum(float checkBetNum) {
        this.checkBetNum = checkBetNum;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public SysUserBetNum getBetNum() {
        return betNum;
    }

    public void setBetNum(SysUserBetNum betNum) {
        this.betNum = betNum;
    }

    public String getWithdrawPageIntro() {
        return withdrawPageIntro;
    }

    public void setWithdrawPageIntro(String withdrawPageIntro) {
        this.withdrawPageIntro = withdrawPageIntro;
    }
}
