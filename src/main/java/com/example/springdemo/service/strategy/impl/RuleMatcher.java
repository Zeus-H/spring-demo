package com.example.springdemo.service.strategy.impl;

import com.example.springdemo.service.strategy.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleMatcher {
    private List<Rule> rules;

    public RuleMatcher(List<Rule> rules) {
        this.rules = rules;
    }

    public Map<Boolean, List<Long>> matchEntities(List<Map<String, Object>> entities) {
        Map<Boolean, List<Long>> result = new HashMap<>();
        result.put(true, new ArrayList<>());
        result.put(false, new ArrayList<>());

        for (Map<String, Object> entity : entities) {
            boolean isMatch = true;
            for (Rule rule : rules) {
                if (!rule.matches(entity)) {
                    isMatch = false;
                    break;
                }
            }
            if (isMatch) {
                result.get(true).add((Long) entity.get("id")); // 假设实体中有id字段
            } else {
                result.get(false).add((Long) entity.get("id"));
            }
        }

        return result;
    }
}
