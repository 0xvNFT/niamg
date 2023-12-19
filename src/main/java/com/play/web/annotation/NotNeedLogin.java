package com.play.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * Springmvc controller类方法头部追加此注解，无需登陆即有权限可访问 若没加此注解调用方法先，系统将会检测当前用户是否登陆
 * 若没有登陆将抛出NoLoginException
 * 
 * @author admin
 *
 */
public @interface NotNeedLogin {

}