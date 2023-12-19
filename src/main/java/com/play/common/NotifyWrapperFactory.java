package com.play.common;

/**
 * 支付回调工厂类
 */
public class NotifyWrapperFactory {
  public static NotifyWrapper getNotifyController(String name) throws Exception {
      String NotifyName=name.substring(0,1).toUpperCase()+name.substring(1,name.length()).toLowerCase()+"NotifyController";
      String pathName="com.play.web.controller.notify.online.notify."+NotifyName;
      Class<NotifyWrapper> clazz = (Class<NotifyWrapper>)Class.forName(pathName);
      NotifyWrapper notifyController = clazz.newInstance();
      return notifyController;
  }
}
