package com.example.springdemo.service.demo.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <a href="https://www.cnblogs.com/skywang12345/p/3603935.html">排序算法</a>
 *
 * @author Mr.Huang
 * @date 2023/2/10 9:57
 **/
@SpringBootTest
public class OverviewServiceTest {
    /**
     * 冒泡算法
     */
    @Test
    public void bubbleSort() {
        int[] nums = {30, 40, 60, 10, 20, 50};
        System.out.println("输入:nums = " + Arrays.toString(nums));
        for (int i = nums.length - 1; i > 0; i--) {
            int flag = 0;
            for (int j = 0; j < i; j++) {
                if (nums[j] > nums[j + 1]) {
                    int tmp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = tmp;
                    flag = 1;
                }
            }
            if (flag == 0) {
                break;
            }
        }
        System.out.println("输出:nums = " + Arrays.toString(nums));
    }

    /**
     * 快速排序
     */
    @Test
    public void quickSort() {
        int[] nums = {3, 4, 6, 1, 2, 5, 9, 7, 8, 0};
        System.out.println("输入:nums = " + Arrays.toString(nums));
        quickSort(nums, 0, nums.length - 1);
        System.out.println("输出:nums = " + Arrays.toString(nums));
    }

    /**
     * 插入排序
     */
    @Test
    public void insertionSort() {
        int[] nums = {3, 4, 6, 1, 2, 5, 9, 7, 8, 0};
        System.out.println("插入排序输入:nums = " + Arrays.toString(nums));
        int i, j, k;
        for (i = 1; i < nums.length; i++) {
            // 为 nums[i]在前面的 nums[0...i-1]有序区间中找一个合适的位置
            for (j = i - 1; j >= 0; j--) {
                if (nums[j] < nums[i]) {
                    break;
                }
            }
            // 如找到了一个合适的位置
            if (j != i - 1) {
                // 将比nums[i]大的数据向后移
                int temp = nums[i];
                for (k = i - 1; k > j; k--) {
                    nums[k + 1] = nums[k];
                }
                // 将nums[i] 放到正确位置上
                nums[k + 1] = temp;
            }
        }
        System.out.println("输出:nums = " + Arrays.toString(nums));
    }

    private void quickSort(int[] nums, int start, int end) {
        if (start >= end) {
            return;
        }
        int i = start, j = end, k = nums[start];
        while (i < j) {
            while (i < j && nums[j] > k) {
                // 从右到左找第一个小于K的 j坐标
                j--;
            }
            if (i < j) {
                // 然后把 小于k 的nums[j]放到nums[i]中,然后 i+1往右移一位
                nums[i++] = nums[j];
            }
            while (i < j && nums[i] < k) {
                // 从左到右找第一个大于K的数
                i++;
            }
            if (i < j) {
                nums[j--] = nums[i];
            }
        }
        nums[i] = k;
        quickSort(nums, start, i - 1);
        quickSort(nums, i + 1, end);
    }
}
