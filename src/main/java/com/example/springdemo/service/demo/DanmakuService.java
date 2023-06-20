package com.example.springdemo.service.demo;

import com.example.springdemo.entity.demo.DanmakuEntity;

import java.util.List;

/**
 * @author Mr.Huang
 * @date 2023/6/19 9:43
 **/
public interface DanmakuService {

    void sendDanmaku(DanmakuEntity danmaku);

    List<DanmakuEntity> getAllDanmakus();
}
