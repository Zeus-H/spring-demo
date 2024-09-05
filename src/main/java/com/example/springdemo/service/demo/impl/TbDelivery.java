package com.example.springdemo.service.demo.impl;

import com.example.springdemo.service.demo.Delivery;
import com.example.springdemo.service.demo.ReturnDelivery;
import org.springframework.stereotype.Service;

/**
 * @author Mr.Huang
 * @date 2023/6/6 17:51
 **/
@Service
public class TbDelivery implements Delivery, ReturnDelivery {
    @Override
    public void send() {
        System.out.println("TbDelivery send");
    }

    @Override
    public void returnSend() {
        System.out.println("TbDelivery returnSend");
    }
}
