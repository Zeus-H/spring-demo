package com.example.springdemo.service;

import com.example.springdemo.common.annotation.EncryptField;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

@Component
@Aspect
public class EncryptionAspect {
    @Around("@annotation(com.example.springdemo.common.annotation.EncryptField)")
    public Object encryptName(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (Object arg : args) {
                Field[] fields = arg.getClass().getDeclaredFields();
                for (Field field : fields) {
                    // 获取有标签的字段，进行加密处理
                    if (field.isAnnotationPresent(EncryptField.class)) {
                        field.setAccessible(true);
                        Object value = field.get(arg);
                        if (value instanceof String) {
                            field.set(arg, encrypt((String) value));
                            setEncryptedName(arg, field.getName(), getHiddenField((String) value));
                        }
                    }
                }
            }
        }
        return joinPoint.proceed();
    }

    // 对字段进行加密处理
    private String encrypt(String field) {
        return field + "_encrypted";
    }

    // 获取对应存储密文的字段
    private void setEncryptedName(Object target, String hiddenNamee, String encryptedName) {
        try {
            Field encryptedNameField = target.getClass().getDeclaredField("encrypted" + StringUtils.capitalize(hiddenNamee));
            encryptedNameField.setAccessible(true);
            encryptedNameField.set(target, encryptedName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 进行脱敏操作
    public static String getHiddenField(String field) {
        if (ObjectUtils.isEmpty(field)) {
            return "";
        }

        if (field.length() <= 3) {
            return field.length() > 2 ? field.charAt(0) + "*" + field.substring(field.length() - 1)
                    : field.charAt(0) + "*";
        } else {
            return field.length() < 11 ? StringUtils.repeat("*", field.length()) :
                    field.substring(0, 3) + "****" + field.substring(7);
        }
    }
}
