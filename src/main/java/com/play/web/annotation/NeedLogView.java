package com.play.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.play.core.LogTypeEnum;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * Springmvc controller类方法头部追加此注解，记入数据访问日志
 *
 * @author admin
 *
 */
public @interface NeedLogView {

	String value();

	LogTypeEnum type() default LogTypeEnum.VIEW_LIST;
}
