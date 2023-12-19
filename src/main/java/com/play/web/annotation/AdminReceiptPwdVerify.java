package com.play.web.annotation;

import com.play.core.StationConfigEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * Springmvc controller类方法头部追加此注解，有该注解时，会进行ADMIN二级密码验证
 *
 * @author admin
 *
 */
public @interface AdminReceiptPwdVerify {


    StationConfigEnum value();
}
