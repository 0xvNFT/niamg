package com.play.common;

public class PayWrapperFactory {
    public static PayWrapper getWrapper(String payName) throws Exception {
        if (PayListEnum.isInclude(payName)) {
            payName = PayListEnum.getPaylistByPayName(payName);
        }
        String wrapperName = payName.substring(0, 1).toUpperCase() + payName.substring(1, payName.length()).toLowerCase();
        String className = "com.play.pay.online.wrapper." + wrapperName + "PayWrapper";
        @SuppressWarnings("unchecked")
        Class<PayWrapper> clazz = (Class<PayWrapper>) Class.forName(className);

        PayWrapper wrapper = clazz.newInstance();

        return wrapper;
    }
}
