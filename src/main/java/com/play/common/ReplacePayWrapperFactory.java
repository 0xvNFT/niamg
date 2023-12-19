package com.play.common;

public class ReplacePayWrapperFactory {
    public static ReplacePayWrapper getWrapper(String payName) throws Exception {
        if (PayListEnum.isInclude(payName)) {
            payName = PayListEnum.getPaylistByPayName(payName);
        }
        String wrapperName = payName.substring(0, 1).toUpperCase() + payName.substring(1, payName.length()).toLowerCase();
        String className = "com.play.pay.withdraw.wrapper." + wrapperName + "ReplacePayWrapper";
        @SuppressWarnings("unchecked")
        Class<ReplacePayWrapper> clazz = (Class<ReplacePayWrapper>) Class.forName(className);
        ReplacePayWrapper wrapper = clazz.newInstance();
        return wrapper;
    }
}
