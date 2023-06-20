package com.example.springdemo.entity.demo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Mr.Huang
 * @date 2023/6/19 9:43
 **/
@Data
@Builder
public class DanmakuEntity {
    private String content;
    private int position;
    private String color;
}
