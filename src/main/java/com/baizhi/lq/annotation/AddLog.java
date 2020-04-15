package com.baizhi.lq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author 李瓊
 * @Date 2020/4/6 14:04
 */

//在方法上使用
@Target({ElementType.METHOD})
//在运行时生效
@Retention(RetentionPolicy.RUNTIME)
public @interface AddLog {
    String value();

    String name() default "";
}
