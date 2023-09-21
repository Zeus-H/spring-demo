package com.example.springdemo.service.strategy;

import java.util.Map;

public interface Rule {
    boolean matches(Map<String, Object> entity);
}
