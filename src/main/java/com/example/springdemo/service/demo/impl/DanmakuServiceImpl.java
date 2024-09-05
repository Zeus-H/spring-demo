package com.example.springdemo.service.demo.impl;

import com.example.springdemo.entity.demo.DanmakuEntity;
import com.example.springdemo.service.demo.DanmakuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.Huang
 * @date 2023/6/19 9:47
 **/
@Service
public class DanmakuServiceImpl implements DanmakuService {

    private final List<DanmakuEntity> danmakuList = new ArrayList<>();

    @Override
    public void sendDanmaku(DanmakuEntity danmaku) {
        danmakuList.add(danmaku);
    }

    @Override
    public List<DanmakuEntity> getAllDanmakus() {
        return danmakuList;
    }
}
