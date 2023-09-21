package com.example.springdemo.service.demo.impl;

import java.util.Arrays;
import java.util.List;

public class LocationFinder {

    public static String findFirstLocationKeyword(String input) {
        List<String> locationKeywords = Arrays.asList("区", "镇", "县", "市", "州", "盟", "旗","市区");
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
        String input = "在某市区的某区的某旗的某市有一座古镇";
        String firstKeyword = findFirstLocationKeyword(input);

        if (firstKeyword != null) {
            System.out.println("找到位置关键词：" + firstKeyword);
        } else {
            System.out.println("未找到位置关键词");
        }
    }
}
