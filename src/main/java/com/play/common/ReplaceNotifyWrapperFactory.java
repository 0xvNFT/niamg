package com.play.common;

/**
 * 支付回调工厂类
 */
public class ReplaceNotifyWrapperFactory {
  public static ReplaceNotifyWrapper getReplaceNotifyController(String name) throws Exception {
      String replaceNotifyName=name.substring(0,1).toUpperCase()+name.substring(1,name.length()).toLowerCase()+"ReplaceNotifyController";
      String pathName="com.play.web.controller.notify.withdraw.notify."+replaceNotifyName;
      Class<ReplaceNotifyWrapper> clazz = (Class<ReplaceNotifyWrapper>)Class.forName(pathName);
      ReplaceNotifyWrapper replaceNotifyWrapper = clazz.newInstance();
      return replaceNotifyWrapper;
  }

    public static void main(String[] args) {
      String name = "trustpaydaifu.do";
        String replaceNotifyName=name.substring(0,1).toUpperCase()+name.substring(1,name.length()).toLowerCase()+"ReplaceNotifyController";
        String pathName="com.play.web.controller.notify.withdraw.notify."+replaceNotifyName;
        System.out.println(pathName);
    }
}
