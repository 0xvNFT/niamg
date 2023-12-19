package com.play.web.i18n;

public enum BaseI18ErrorCode implements I18nCode{
    unknownError("10000", "未知错误，请联系客服"),
    nullObj("10001", "对象为空"),
    networkError("10002","网络异常"),

    thirdUsernameUnExist("100","第三方游戏账号不存在"),
    userStatusErr("101", "玩家状态异常，请联系客服"),
    usernameExist("102", "账号已经被使用"),
    createUsernameError("103","创建账号发生错误"),
    getBalanceError("104","获取余额发生错误"),
    kickOutError("105","踢下线发生错误"),
    usernameIsEmpty("106","会员不能为空"),
    usernameFormatError("107","会员账号格式错误"),

    currencyUnExist("200", "币种不存在"),
    languageUnExist("201","语种不存在"),
    platformError("202","平台类型错误"),
    serverUnExist("203", "服务器信息不存在"),
    serverDisabled("204", "服务器信息被禁用"),
    serverIpNotOnWhite("205", "您的ip{0}不在白名单内"),
    underMaintenance("206", "{0}，维护中..."),
    platformValError("207","参数platform值有误"),
    loginError("208","登录游戏发生错误"),
    signError("209","签名无效"),
    moneyError("210","无效金额"),
    exchangeTypeError("211","额度转换类型错误"),
    orderIdEmpty("212","缺少订单号"),
    exchangeError("213","转账异常,请及时联系客服人员处理!"),
    exchangeOrderUnExist("214","订单不存在"),
    createSessionIdError("215","创建sessionId发生错误"),

    searchTimeRequired("300","请选择时间范围进行查询"),
    startTimeAfterEndTime("301","开始时间必需小时结束时间！"),
    twoTimeIntervals7Days("302","两个时间间隔必须在7天内"),
    twoTimeIntervals1Month("303","两个时间间隔必须在1个月内"),
    within3Months("304","只能查询3个月内的数据"),
    concurrentRestriction3s("305","由于统计比较耗性能，2次查询需要间隔3秒以上"),
    statTimeRequired("306","请选择统计日期"),
    startDateAfterEndDate("307","结束日期必须大于开始日期"),

    stationQuotaNull("900","没有设置额度"),
    stationQuotaExceed("401","额度超过 {0}"),
    resultDataEmpty("402","缺少data字段"),
    resultDataStatusEmpty("403","缺少data.Status字段"),
    unsupportCurrency("404","该游戏不支持此币种"),
    httpsCertificateFailed("405","https证书校验失败"),
    orderStatusUnknow("406","未知订单状态[{0}]"),
    notEnoughCredit("407","游戏里余额不足"),
    unsupportLogout("408", "不支持退出功能"),
    thirdGameQuotaNotEnough("409","三方额度不足，请联系客服"),
    gameCodeRequired("410","缺少gameCode"),
    moneyGreaterThan1("411","金额必须大于1，汇率{0}"),
    invalidPlayerStatus("1305","无效玩家"),
    transferAmountExceedsLimit("2821","转账金额超出限制"),
    ;


    private String code;
    private String message;

    private BaseI18ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return "base.third" + code;
    }
    @Override
    public String getMessage() {
        return message;
    }
    /*@Override
    public String toString() {
        return "[" + this.code + "]" + this.message;
    }*/


}
