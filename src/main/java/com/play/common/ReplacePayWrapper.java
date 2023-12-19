package com.play.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.service.MnyDrawRecordService;

public interface ReplacePayWrapper {

    /**
     * 参数约定
     * 0 mechCode 商户编码
     * 1 mechKey 商户密匙
     * 2 orderNo 提款订单号
     * 3 orderAmount 提款金额
     * 4 mechDomain 接口域名
     * 5 bankName 银行名称
     * 6 merchantAccount 商户账号
     * 7 customerIp 发起支付客户端IP
     * 8 referer 当前系统域名
     * 9 payType 支付类型 目前为""
     * 10 payGetway 支付网关
     * 11 appid appid
     * 12 domain 主域名
     * 13 account 支付用户 用户名
     * 14 extra 备选参数
     * 15 cardName 持卡人名
     * 16 cardId 持卡人id
     * 17 bankAddress 银行开户行地址
     * 18 cardNo 银行卡卡号
     *
     * @param params
     * @return
     */
	JSONObject wrap(String... params) throws Exception;

    /**
     * 得到支持的银行
     *
     * @return
     */
    List<String> getSupportBankList();

    /**
     * 查询余额
     *
     * @return
     */
    void searchBalance(HttpServletRequest request, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDraw replaceWithDraw, MnyDrawRecord mnyDrawRecord) throws Exception;

    /**
     * 代付查询
     *
     * @param request
     * @param mnyDrawRecordService
     * @param replaceWithDraw      代付出款设置
     * @param mnyDrawRecord        用户提款记录
     * @return
     */
    void search(HttpServletRequest request, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDraw replaceWithDraw, MnyDrawRecord mnyDrawRecord) throws Exception;

}
