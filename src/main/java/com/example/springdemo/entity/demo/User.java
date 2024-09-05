package com.example.springdemo.entity.demo;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private String name;
    private int age;
    private List<Integer> scores;
    private List<Address> addresses;
}