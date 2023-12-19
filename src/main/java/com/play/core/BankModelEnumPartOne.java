package com.play.core;

import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BankModelEnumPartOne {
	DAMSONDAIFU("DAMSONDAIFU", "PIX", "", "", "damson pay"),
	DAMSONZHIFU("DAMSONZHIFU", "PIX", "", "", "damson collect"),
	GOOPAGODAIFU("GOOPAGODAIFU", "PIX", "", "", "goopago pay"),
	GOOPAGOZHIFU("GOOPAGOZHIFU", "PIX", "", "", "goopago collect"),
	TOPAYDAIFU("TOPAYDAIFU", "PIX", "", "", "topay pay"),
	TOPAYZHIFU("TOPAYZHIFU", "PIX", "", "", "topay collect"),
    OKKPAYZHIFU("OKKPAYZHIFU", "BNIB,CANARN,PAYTM,UPI", "", "", "okk collect"),
    OKKPAYDAIFU("OKKPAYDAIFU", "BNIB,CANARN,PAYTM,UPI", "", "", "okk pay"),
    UZPAYZHIFU("UZPAYZHIFU", "CANARN", "", "", "uz collect"),
    UZPAYDAIFU("UZPAYDAIFU", "CANARN", "", "", "uz pay"),
    WOWPAYZHIFU("WOWPAYZHIFU", "CANARN", "", "", "wow collect"),
    WOWPAYDAIFU("WOWPAYDAIFU", "CANARN", "", "", "wow pay"),
    TRUSTPAYDAIFU("TRUSTPAYDAIFU", "PIX", "", "", "trust pay"),
    TRUSTPAYZHIFU("TRUSTPAYZHIFU", "PIX", "", "", "trust collect"),
    SUNPAYDAIFU("SUNPAYDAIFU", "USDT", "", "", "sun pay"),
    SUNPAYZHIFU("SUNPAYZHIFU", "USDT", "", "", "sun collect"),
    CASHPAYDAIFU("CASHPAYDAIFU", "PIX", "", "", "cash pay"),
    CASHPAYZHIFU("CASHPAYZHIFU", "PIX", "", "", "cash collect"),
    OWENPAYDAIFU("OWENPAYDAIFU", "PIX", "", "", "owen pay"),
    OWENPAYZHIFU("OWENPAYZHIFU", "PIX", "", "", "owen collect"),
    BOSSDAIFU("BOSSDAIFU", "", "", "", "boss代付"),
    BOSSPAYZHIFU("BOSSPAYZHIFU", "OTHER", "UNION,SCB", "SCBREVERSE", "boss支付"),
    YESPAYDAIFU("YESPAYDAIFU", "VCB", "", "", "yes pay"),
    YESPAYZHIFU("YESPAYZHIFU", "VCB", "ZALO,MOMO,VTPAY", "", "yes collect"),
    SPEEDLYDAIFU("SPEEDLYDAIFU", "PIX", "", "", "speedly pay"),
    SPEEDLYZHIFU("SPEEDLYZHIFU", "PIX", "", "", "speedly collect"),

//    BOSSPAYZHIFU("BOSSPAYZHIFU", "OTHER", "UNION,SCB,KBANK,KTB,BBL,TMB", "SCBREVERSE,KBANKREVERSE,KTBREVERSE,BBLREVERSE,TMBREVERSE", "boss支付"),
//    UPIDAIFU("UPIDAIFU", "", "", "", "upi代付"),
//    UPIZHIFU("UPIZHIFU", "OTHER", "UNION,SCB,KBANK,KTB,BBL,TMB", "SCBREVERSE,KBANKREVERSE,KTBREVERSE,BBLREVERSE,TMBREVERSE", "upi支付"),
//    UPIZHIFUV2("UPIZHIFUV2", "OTHER", "UNION,SCB,KBANK,KTB,BBL,TMB", "SCBREVERSE,KBANKREVERSE,KTBREVERSE,BBLREVERSE,TMBREVERSE", "upi支付v2"),
//    UPIDAIFUV2("UPIDAIFUV2", "", "", "", "upi代付v2"),
//    PAYTMDAIFU("PAYTMDAIFU", "", "", "", "paytm代付"),
//    PAYTMZHIFU("PAYTMZHIFU", "OTHER", "UNION,SCB,KBANK,KTB,BBL,TMB", "SCBREVERSE,KBANKREVERSE,KTBREVERSE,BBLREVERSE,TMBREVERSE", "paytm支付"),
//    TPAYDAIFU("TPAYDAIFU", "", "", "", "tpay代付"),
//    IPAYZHIFU("IPAYZHIFU", "OTHER", "UNION,SCB,KBANK,KTB,BBL,TMB", "SCBREVERSE,KBANKREVERSE,KTBREVERSE,BBLREVERSE,TMBREVERSE", "ipay支付"),
//    EASYPAY2("EASYPAY2", "", "ALIPAY", "WEIXINH5,ALIPAYH5,JDPAYH5,QQPAYH5,UNIONH5", "快支付2"),
//    QUICKPAY("QUICKPAY", "ZGGSYX,ZGNYYX,ZGJSYX,ZGYX,ZGZSYX,BJYX,ZGJTYX,ZGXYYX,NJYX,ZGMSYX,ZGGDYX,PAYX,BHYX,BEADYYX,NBYX,ZXYX,GFYX,SHYX,PFYX,ZGYZYX", "WEIXIN,ALIPAY,JDPAY,QQPAY,UNION,QUICKPAY", "WEIXINH5,ALIPAYH5,UNIONH5,QQPAYH5,WEIXINREVERSE,ALIPAYREVERSE", "快捷支付"),
//    MIPAYZHIFU("MIPAYZHIFU", "OTHER", "UNION,YP_SBIN,YP_ICICI,YP_PNJB,BBL,TMB", "SCBREVERSE,KBANKREVERSE,KTBREVERSE,BBLREVERSE,TMBREVERSE", "mipay支付"),
//    MIPAYZHIFUV2("MIPAYZHIFUV2", "OTHER", "UNION,SCB,KBANK,KTB,BBL,TMB", "SCBREVERSE,KBANKREVERSE,KTBREVERSE,BBLREVERSE,TMBREVERSE", "mipay支付v2"),
//    //JINXIANGZHIFUTWO("JINXIANGZHIFUTWO","SCB,KBANK,KTB,BBL,TMB", "SCB,KBANK,KTB,BBL,TMB","SCBH5,KBANKH5,KTBH5,BBLH5,TMBH5","金象支付2"),
//    CROWNZHIFU("CROWNZHIFU", "OTHER", "UNION,SCB,KBANK,KTB,BBL,TMB", "SCBREVERSE,KBANKREVERSE,KTBREVERSE,BBLREVERSE,TMBREVERSE", "tpay支付"),
//    JINXIANGZHIFU("JINXIANGZHIFU", "OTHER", "UNION,SCB,KBANK,KTB,BBL,TMB", "SCBREVERSE,KBANKREVERSE,KTBREVERSE,BBLREVERSE,TMBREVERSE", I18nTool.getMessage(BaseI18nCode.jinXiangOnlinePay, Locale.ENGLISH)),
//    //JINXIANGZHIFU("JINXIANGZHIFU", "OTHER", "WEIXIN,ALIPAY,JDPAY,QQPAY,UNION,QUICKPAY", "WEIXINH5,ALIPAYH5,UNIONH5,QQPAYH5,WEIXINREVERSE,ALIPAYREVERSE", "金象支付"),
//    YARLUNGZHIFU("YARLUNGZHIFU", "OTHER", "WEIXIN,ALIPAY,JDPAY,QQPAY,UNION,QUICKPAY", "WEIXINH5,ALIPAYH5,UNIONH5,QQPAYH5,WEIXINREVERSE,ALIPAYREVERSE", "yarlung支付"),
//    LIUGZHIFU("LIUGZHIFU", "OTHER", "WEIXIN,ALIPAY,JDPAY,QQPAY,UNION,QUICKPAY", "WEIXINH5,ALIPAYH5,UNIONH5,QQPAYH5,WEIXINREVERSE,ALIPAYREVERSE", "6G支付"),
//    QUICKDAIFU("QUICKDAIFU", "", "", "", "快捷代付"),
//    JINXIANGDAIFU("JINXIANGDAIFU", "", "", "", "金象代付"),
//    YARLUNGDAIFU("YARLUNGDAIFU", "", "", "", "yarlung代付"),
//    LIUGDAIFU("LIUGDAIFU", "", "", "", "6G代付"),
    PAYNULL("","","","","");
    //OTHER("OTHER", "OTHER", "OTHER", "", "不匹配时返回");


    private String bankCode; //第三方支付编号
    private String bankJson; //第三方银行卡快捷支付
    private String onlinepayJson; // 第三方扫码支付
    private String onlinepayh5Json; //第三方h5跳转扫码支付

    private String bankName;  //第三方支付名称

    private BankModelEnumPartOne(String bankCode, String bankJson, String onlinepayJson, String onlinepayh5Json, String bankName) {
        this.bankCode = bankCode;
        this.bankJson = bankJson;
        this.onlinepayJson = onlinepayJson;
        this.onlinepayh5Json = onlinepayh5Json;
        this.bankName = bankName;
    }

    public static BankModelEnumPartOne getBankInfo(String name) {
        try {
            return BankModelEnumPartOne.valueOf(BankModelEnumPartOne.class, name);
        } catch (Exception e) {
            return PAYNULL;
        }
    }

    //返回other
    public static BankModelEnumPartOne getOther(){
        return PAYNULL;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public List<String> getBankJson() {
        return Arrays.asList(this.bankJson.split(",")).stream().filter(x -> StringUtils.isNotEmpty(x)).collect(Collectors.toList());
    }

    public void setBankJson(String bankJson) {
        this.bankJson = bankJson;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<String> getOnlinepayJson() {
        return Arrays.asList(this.onlinepayJson.split(",")).stream().filter(x -> StringUtils.isNotEmpty(x)).collect(Collectors.toList());
    }

    public void setOnlinepayJson(String onlinepayJson) {
        this.onlinepayJson = onlinepayJson;
    }

    public List<String> getOnlinepayh5Json() {
        return Arrays.asList(this.onlinepayh5Json.split(",")).stream().filter(x -> StringUtils.isNotEmpty(x)).collect(Collectors.toList());
    }

    public void setOnlinepayh5Json(String onlinepayh5Json) {
        this.onlinepayh5Json = onlinepayh5Json;
    }


}
