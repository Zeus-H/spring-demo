package com.example.springdemo.entity.demo;

import lombok.Data;

import java.util.List;

@Data
public class Address {
    private String city;
    private String street;
    private List<String> phones;
}