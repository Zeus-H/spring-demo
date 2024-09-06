
package com.example.springdemo.service.demo.impl;

/**
 * 冒泡排序
 *
 * @author Mr.Huang
 * @date 2023/4/3 17:44
 **/
public class Demo {
    public static void main(String[] args) {
        // 冒泡排序
        int[] arr = {1, 3, 2, 5, 4};
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        for (int i : arr) {
            System.out.println(i);
        }

        // 测试merge
    }
}