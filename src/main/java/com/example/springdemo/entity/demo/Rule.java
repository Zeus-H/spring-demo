package com.example.springdemo.entity.demo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rule {
    private String ruleCode;
    private String symbol;
    private String ruleValue;
}