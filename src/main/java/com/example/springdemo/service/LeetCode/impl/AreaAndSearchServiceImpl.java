package com.example.springdemo.service.LeetCode.impl;

import com.example.springdemo.service.LeetCode.AreaAndSearchService;

import java.util.Arrays;

/**
 * @author Mr.Huang
 * @date 2024/3/18 17:33
 **/
public class AreaAndSearchServiceImpl implements AreaAndSearchService {

    int[] NUMS;

    public AreaAndSearchServiceImpl(int[] nums) {
        NUMS = nums;
    }

    @Override
    public void NumArray(int left, int right) {
        int num = sumRange(left, right);
        System.out.println("区间之和:" + num);
    }

    public int sumRange(int left, int right) {
        int sum = 0;
        if (left <= right) {
            for (int i = right; i >= left; i--) {
                sum += NUMS[i];
            }
            return sum;
        }
        return 0;
    }

    int[] sums;
    @Override
    public void NumArray2(int left, int right) {
        int n = NUMS.length;
        sums = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + NUMS[i];
        }

        int num = sumRange2(left, right);
        System.out.println("区间之和:" + num);
    }

    private int sumRange2(int left, int right) {
        return sums[right + 1] - sums[left];
    }
}
