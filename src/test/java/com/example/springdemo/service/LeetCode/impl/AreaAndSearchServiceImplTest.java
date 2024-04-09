package com.example.springdemo.service.LeetCode.impl;

import com.example.springdemo.service.LeetCode.AreaAndSearchService;
import com.example.springdemo.util.TimeLogUtil;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;


@SpringBootTest
public class AreaAndSearchServiceImplTest {

    public static void main(String[] args) {
        int[] nums = {-2, 0, 3, -5, 2, -1};
        System.out.println("输入数组:" + Arrays.toString(nums));

        TimeLogUtil timeLog = new TimeLogUtil("测试numArray：");
        AreaAndSearchService numArray = new AreaAndSearchServiceImpl(nums);
        numArray.NumArray(0, 5);
        System.out.println(timeLog.end());

        TimeLogUtil timeLog2 = new TimeLogUtil("测试numArray2：");
        AreaAndSearchService numArray2 = new AreaAndSearchServiceImpl(nums);
        numArray2.NumArray2(0, 5);
        System.out.println(timeLog2.end());
    }
}