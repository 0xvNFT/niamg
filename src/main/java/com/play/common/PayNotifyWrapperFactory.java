package com.play.common;

/**
 * 支付回调工厂类
 */
public class PayNotifyWrapperFactory {
  public static PayNotifyWrapper getNotifyController(String name) throws Exception {
      String NotifyName=name.substring(0,1).toUpperCase()+name.substring(1,name.length()).toLowerCase()+"NotifyController";
      String pathName="com.play.web.controller.notify.online.notify."+NotifyName;
      Class<PayNotifyWrapper> clazz = (Class<PayNotifyWrapper>)Class.forName(pathName);
      PayNotifyWrapper payNotifyWrapper = clazz.newInstance();
      return payNotifyWrapper;
  }
}
