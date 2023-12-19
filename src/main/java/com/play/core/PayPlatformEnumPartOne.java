package com.play.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.web.i18n.I18nCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 支付平台快速入款类
 *
 * @author admin
 */
public enum PayPlatformEnumPartOne {
    /********************* 在线入款 *********************/

    /******* 巴西 *******/
	DAMSONDAIFU(3, "damson pay", 2L, true, LanguageEnum.br),
	DAMSONZHIFU(3, "damson collect", 2L, true, LanguageEnum.br),
	GOOPAGODAIFU(3, "goopago pay", 2L, true, LanguageEnum.br),
	GOOPAGOZHIFU(3, "goopago collect", 2L, true, LanguageEnum.br),
	TOPAYDAIFU(3, "topay pay", 2L, true, LanguageEnum.br),
	TOPAYZHIFU(3, "topay collect", 2L, true, LanguageEnum.br),
    TRUSTPAYZHIFU(3, "trust collect", 2L, true, LanguageEnum.br),
    TRUSTPAYDAIFU(3, "trust pay", 2L, true, LanguageEnum.br),
    OKKPAYZHIFU(3, "okk collect", 2L, true, LanguageEnum.br),
    OKKPAYDAIFU(3, "okk pay", 2L, true, LanguageEnum.br),
    UZPAYZHIFU(3, "uz collect", 2L, true, LanguageEnum.br),
    UZPAYDAIFU(3, "uz pay", 2L, true, LanguageEnum.br),
    SUNPAYZHIFU(3, "sun collect", 2L, true, LanguageEnum.br),
    SUNPAYDAIFU(3, "sun pay", 2L, true, LanguageEnum.br),
    OWENPAYZHIFU(3, "owen collect", 2L, true, LanguageEnum.br),
    OWENPAYDAIFU(3, "owen pay", 2L, true, LanguageEnum.br),
    WOWPAYZHIFU(3, "wow collect", 2L, true, LanguageEnum.br),
    WOWPAYDAIFU(3, "wow pay", 2L, true, LanguageEnum.br),
    CASHPAYZHIFU(3, "cash collect", 2L, true, LanguageEnum.br),
    CASHPAYDAIFU(3, "cash pay", 2L, true, LanguageEnum.br),
    SPEEDLYZHIFU(3, "speedly collect", 2L, true, LanguageEnum.br),
    SPEEDLYDAIFU(3, "speedly pay", 2L, true, LanguageEnum.br),


    /******* 越南 *******/
    YESPAYDAIFU(3, "yes pay", 2L, true, LanguageEnum.vi),
    YESPAYZHIFU(3, "yes collect", 2L, true, LanguageEnum.vi),




    /*在线入款*/
//    BOSSDAIFU(3, "boss代付", 2L, true, LanguageEnum.cn),
//    BOSSPAYZHIFU(3, "boss支付", 2L, true, LanguageEnum.cn),
//    UPIDAIFU(3, "upi代付", 2L, true, LanguageEnum.cn),
//    UPIZHIFU(3, "upi支付", 2L, true, LanguageEnum.cn),
//    UPIZHIFUV2(3, "upi支付v2", 2L, true, LanguageEnum.cn),
//    UPIDAIFUV2(3, "upi代付v2", 2L, true, LanguageEnum.cn),
//    PAYTMDAIFU(3, "paytm代付", 2L, true, LanguageEnum.cn),
//    PAYTMZHIFU(3, "paytm支付", 2L, true, LanguageEnum.cn),
//    TPAYDAIFU(3, "tpay代付", 2L, true, LanguageEnum.cn),
//    MIPAYDAIFUV2(3, "mipay代付v2", 2L, true, LanguageEnum.cn),
//    IPAYZHIFU(3, "ipay支付", 2L, true, LanguageEnum.cn),
//    MIPAYZHIFUV2(3, "mipay支付V2", 2L, true, LanguageEnum.cn),
//    MIPAYZHIFU(3, "mipay支付", 2L, true, LanguageEnum.cn),
//    CROWNZHIFU(3, "tpay支付", 2L, true, LanguageEnum.cn),
//    YARLUNGZHIFU(3, "yarlung支付", 2L, true, LanguageEnum.cn),
//    YARLUNGDAIFU(3, "yarlung代付", 2L, true, LanguageEnum.cn),
//    EASYPAY2(3, "快支付2", 2L, true, LanguageEnum.cn),
//    QUICKPAY(3, "快捷支付", 2L, true, LanguageEnum.cn),
//    QUICKDAIFU(3, "快捷代付", 2L, true, LanguageEnum.cn),
//    JINXIANGZHIFU(3, "金象支付", 2L, true, LanguageEnum.th),
//    //JINXIANGZHIFUTWO(3,"金象支付2",2L,true, LanguageEnum.th),
//    JINXIANGDAIFU(3, "金象代付", 2L, true, LanguageEnum.cn),
//    LIUGZHIFU(3, "6G支付", 2L, true, LanguageEnum.cn),
//    LIUGDAIFU(3, "6G代付", 2L, true, LanguageEnum.cn),
    OTHER(3, "其他", 560L, true, LanguageEnum.cn);


    PayPlatformEnumPartOne(Integer type, String payName, Long sortNo, Boolean isAble, LanguageEnum code) {
        this.type = type;
        this.payName = payName;
        this.sortNo = sortNo;
        this.isAble = isAble;
        this.code = code;
    }

    /**
     * 支付类型啊 3：在线支付
     */
    private Integer type;
    /**
     * 支付名称
     */
    private String payName;
    /**
     * 排序
     */
    private Long sortNo;
    /**
     * 是否可用
     */
    private boolean isAble;

    /**
     * 国际化code
     */
    private LanguageEnum code;

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public Long getSortNo() {
        return sortNo;
    }

    public void setSortNo(Long sortNo) {
        this.sortNo = sortNo;
    }

    public boolean isAble() {
        return isAble;
    }

    public void setAble(boolean able) {
        isAble = able;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public LanguageEnum getCode() {
        return code;
    }

    public void setCode(LanguageEnum code) {
        this.code = code;
    }
}
