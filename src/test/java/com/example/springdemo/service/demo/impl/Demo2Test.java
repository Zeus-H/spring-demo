package com.example.springdemo.service.demo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mr.Huang
 * @date 2023/8/4 17:26
 **/
public class Demo2Test {
    public static String findFirstLocationKeyword(String input) {
        List<String> locationKeywords = new ArrayList<>(Arrays.asList("区", "镇", "县", "市", "州", "盟", "旗"));

        // 修改：同时存在 "市" 和 "区" 的情况下，将 "市" 替换为 "市区" 进行匹配
        if (locationKeywords.contains("市") && locationKeywords.contains("区")) {
            locationKeywords.remove("市");
        }

        int minIndex = Integer.MAX_VALUE;
        String firstKeyword = null;

        for (String keyword : locationKeywords) {
            int index = input.indexOf(keyword);
            if (index != -1 && index < minIndex) {
                minIndex = index;
                firstKeyword = keyword;
            }
        }

        return firstKeyword;
    }

    public static void main(String[] args) {
        String input = "在某市中某市区的某区的某旗的某市有一座古镇";
        String firstKeyword = findFirstLocationKeyword(input);

        if (firstKeyword != null) {
            System.out.println("找到位置关键词：" + firstKeyword);
        } else {
            System.out.println("未找到位置关键词");
        }
    }
}
