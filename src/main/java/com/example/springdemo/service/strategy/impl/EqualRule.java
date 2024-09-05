package com.example.springdemo.service.strategy.impl;

import com.example.springdemo.service.strategy.Rule;

import java.util.Map;

public class EqualRule implements Rule {
    private String field;
    private Object value;

    public EqualRule(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public boolean matches(Map<String, Object> entity) {
        return entity.containsKey(field) && entity.get(field).equals(value);
    }
}

// 创建其他规则类，如 NotEqualRule, GreaterThanRule, InRule, NotInRule 等
