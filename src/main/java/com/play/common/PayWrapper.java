package com.play.common;

import com.alibaba.fastjson.JSONObject;

public interface PayWrapper {

    /**
     * 参数约定
     * 0 mechCode 商户编码
     * 1 mechKey 商户密匙
     * 2 orderNo 订单号
     * 3 orderAmount 订单金额
     * 4 mechDomain 接口域名
     * 5 bankCode 银行代码
     * 6 merchantAccount 商户账号、终端号、卖家邮箱等
     * 7 customerIp 发起支付客户端IP
     * 8 referer 当前系统域名
     * 9 payType 支付类型
     * 10 payGetway 支付网关
     * 11 appid
     * 12 domain
     * 13 支付用户
     * 14 orderTime 订单时间
     * 15 alternative 备选参数
     * 16 memberAccount 会员账号
     * 17 agentUser 用户设备信息
     * @param params
     * @return
     */
   JSONObject wrap(String... params) throws Exception;

}
