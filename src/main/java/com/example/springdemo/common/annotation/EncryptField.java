package com.example.springdemo.common.annotation;

import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.annotation.*;

/**
 * 加密字段注解,方法触发
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
public @interface EncryptField {

}