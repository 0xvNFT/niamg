package com.play.core;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BankModelEnum {


    private String bankCode; //第三方支付编号
    private List<String> bankJson; //第三方银行卡快捷支付
    private List<String> onlinepayJson; // 第三方扫码支付
    private List<String> onlinepayh5Json; //第三方h5跳转扫码支付

    //private List<String> bankThbJson; //泰国站点银行支付

    private String bankName;  //第三方支付名称


    public static List<BankModelEnum> values() {
        List<BankModelEnum> banks = new ArrayList<>();
        for (BankModelEnumPartOne b1 : BankModelEnumPartOne.values()) {
            BankModelEnum bankModelEnum = new BankModelEnum();
            bankModelEnum.setBankCode(b1.getBankCode());
            bankModelEnum.setBankJson(b1.getBankJson());
            bankModelEnum.setOnlinepayJson(b1.getOnlinepayJson());
            bankModelEnum.setOnlinepayh5Json(b1.getOnlinepayh5Json());
            bankModelEnum.setBankName(b1.getBankName());
            banks.add(bankModelEnum);
        }
        return banks;
    }

    //获取BankJson
    public static List<String> getBankJson(String name) {
        try {
            return BankModelEnumPartOne.getBankInfo(name).getBankJson();
        } catch (Exception e) {
            return BankModelEnumPartOne.getOther().getBankJson();
        }
    }

    //获取OnlinepayJson
    public static List<String> getOnlinepayJson(String name) {
        try {
            return BankModelEnumPartOne.getBankInfo(name).getOnlinepayJson();
        } catch (Exception e) {
            return BankModelEnumPartOne.getOther().getOnlinepayJson();
        }
    }

    //获取Onlinepayh5Json
    public static List<String> getOnlinepayh5Json(String name) {
        try {
            return BankModelEnumPartOne.getBankInfo(name).getOnlinepayh5Json();
        } catch (Exception e) {
            return BankModelEnumPartOne.getOther().getOnlinepayh5Json();
        }
    }

    public static List<String> getChannelList(String channel) {
        if (StringUtils.isEmpty(channel)) {
            return new ArrayList<>();
        }
        return Arrays.asList(StringUtils.split(channel, ",")).stream().filter(x -> StringUtils.isNotEmpty(x)).collect(Collectors.toList());
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public List<String> getBankJson() {
        return bankJson;
    }


    public void setBankJson(List<String> bankJson) {
        this.bankJson = bankJson;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    public void setOnlinepayJson(List<String> onlinepayJson) {
        this.onlinepayJson = onlinepayJson;
    }


    public void setOnlinepayh5Json(List<String> onlinepayh5Json) {
        this.onlinepayh5Json = onlinepayh5Json;
    }

}
