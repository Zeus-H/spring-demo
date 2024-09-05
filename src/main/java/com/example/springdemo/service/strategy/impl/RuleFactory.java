package com.example.springdemo.service.strategy.impl;

import com.example.springdemo.service.strategy.Rule;

public class RuleFactory {
    public static Rule createRule(String condition, String field, Object value) {
        switch (condition) {
            case "=":
                return new EqualRule(field, value);
//            case "!=":
//                return new NotEqualRule(field, value);
//            case ">":
//                return new GreaterThanRule(field, value);
            // 添加其他条件对应的规则创建
            default:
                throw new IllegalArgumentException("Unsupported condition: " + condition);
        }
    }
}
