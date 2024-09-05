package com.example.springdemo.service.strategy;

import com.example.springdemo.service.strategy.impl.RuleFactory;
import com.example.springdemo.service.strategy.impl.RuleMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Rule> rules = new ArrayList<>();
        rules.add(RuleFactory.createRule("=", "status", "active"));
//        rules.add(RuleFactory.createRule(">", "age", 18));

        RuleMatcher ruleMatcher = new RuleMatcher(rules);

        List<Map<String, Object>> entities = new ArrayList<>();
        // 添加实体数据

        Map<Boolean, List<Long>> result = ruleMatcher.matchEntities(entities);

        List<Long> matchedIds = result.get(true);
        List<Long> unmatchedIds = result.get(false);

        System.out.println("Matched IDs: " + matchedIds);
        System.out.println("Unmatched IDs: " + unmatchedIds);
    }
}
