package com.example.springdemo.service;

import com.alibaba.fastjson.JSON;
import com.example.springdemo.common.annotation.EncryptField;
import com.example.springdemo.entity.demo.Demo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * 加密 注解用于方法上
 */
@Component
@Aspect
public class EncryptAspect {
    @Around("@annotation(com.example.springdemo.common.annotation.Encrypt)")
    public Object encrypt(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        Object[] encrypts = new Object[args.length];
        // 对参数进行加密处理
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Demo) {
                Demo demo = JSON.parseObject(JSON.toJSONBytes(args[i]), Demo.class);
                demo.setName(getHiddenName(demo.getName()));
                encrypts[i] = demo;
            }
        }
        if (ObjectUtils.isEmpty(encrypts)) {
            encrypts = args;
        }
        // 调用目标方法并返回结果
        Object result = joinPoint.proceed(encrypts);
        // 对结果进行加密处理
        if (result instanceof Demo) {
            Demo demo = JSON.parseObject(JSON.toJSONBytes(result), Demo.class);
            demo.setName(getHiddenName(demo.getName()));
            result = demo;
        }
        return result;
    }

//    @Around("@annotation(com.example.springdemo.common.annotation.EncryptName)")
    public Object encryptName(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                Field[] fields = arg.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(EncryptField.class)) {
                        field.setAccessible(true);
                        Object value = field.get(arg);
                        if (value instanceof String) {
                            String encryptedValue = getHiddenName((String) value);
                            field.set(arg, encryptedValue);
                        }
                    }
                }
            }
        }
        Object result = pjp.proceed(args);
        if (result instanceof ResponseBody) {
            String resultJson = new ObjectMapper().writeValueAsString(result);
            ObjectNode rootNode = new ObjectMapper().readValue(resultJson, ObjectNode.class);
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (field.getValue().isTextual() && rootNode.get(field.getKey()).asText().length() > 0) {
                    if (rootNode.get(field.getKey()).has("****")) {
                        continue;
                    }
                    rootNode.put(field.getKey(), getHiddenName(field.getValue().asText()));
                }
            }
            return rootNode;
        }
        return result;
    }

    public static String getHiddenName(String name) {
        return ObjectUtils.isEmpty(name) ? "" :
                name.length() > 2 ? name.charAt(0) + "*" + name.substring(name.length() - 1)
                        : name.charAt(0) + "*";
    }
}
