package com.example.springdemo.service.demo.impl;

import com.example.springdemo.service.demo.Delivery;
import org.springframework.stereotype.Service;

/**
 * @author Mr.Huang
 * @date 2023/6/6 17:30
 **/
@Service
public class DyDelivery implements Delivery {
    @Override
    public void send() {
        System.out.println("DyDelivery send");
    }
}
