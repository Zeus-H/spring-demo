package com.example.springdemo.service.demo.impl;

import com.example.springdemo.entity.demo.DanmakuEntity;
import com.example.springdemo.service.demo.DanmakuService;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DanmakuServiceImplTest {

    public static void main(String[] args) {
        DanmakuService danmakuService = new DanmakuServiceImpl();

        // 发送弹幕
        DanmakuEntity danmaku1 = DanmakuEntity
                .builder()
                .color("red")
                .content("Hello world!")
                .position(1)
                .build();
        DanmakuEntity danmaku2 = DanmakuEntity
                .builder()
                .color("blue")
                .content("Hello world!")
                .position(2)
                .build();

        danmakuService.sendDanmaku(danmaku1);
        danmakuService.sendDanmaku(danmaku2);

        // 获取所有弹幕
        List<DanmakuEntity> allDanmakus = danmakuService.getAllDanmakus();
        for (DanmakuEntity danmaku : allDanmakus) {
            System.out.println("Content: " + danmaku.getContent());
            System.out.println("Position: " + danmaku.getPosition());
            System.out.println("Color: " + danmaku.getColor());
        }
    }
}