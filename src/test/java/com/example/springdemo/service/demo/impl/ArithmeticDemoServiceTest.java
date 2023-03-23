package com.example.springdemo.service.demo.impl;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * @author Mr.Huang
 * @date 2023/2/1 9:04
 **/
@SpringBootTest
public class ArithmeticDemoServiceTest {
    /**
     * 动态数组之和
     * 输入：nums = [1,2,3,4]
     * 输出：[1,3,6,10]
     * 解释：动态和计算过程为 [1, 1+2, 1+2+3, 1+2+3+4] 。
     */
    @Test
    public void one() {
        int[] nums = {1, 2, 3, 4, 5};

        for (int i = 1; i < nums.length; i++) {
            nums[i] += nums[i - 1];
        }
        System.out.println(Arrays.toString(nums));
    }

    /**
     * <a href="https://leetcode.cn/problems/number-of-steps-to-reduce-a-number-to-zero">将数字变成 0 的操作次数</a>
     * 给你一个非负整数 num ，请你返回将它变成 0 所需要的步数。 如果当前数字是偶数，你需要把它除以 2 ；否则，减去 1 。
     * 输入：num = 8
     * 输出总步骤：4
     * 解释：
     * 步骤 1） 8 是偶数，除以 2 得到 4 。
     * 步骤 2） 4 是偶数，除以 2 得到 2 。
     * 步骤 3） 2 是偶数，除以 2 得到 1 。
     * 步骤 4） 1 是奇数，减 1 得到 0 。
     */
    @Test
    public void two() {
        int num = 10;
        int step = 0;
        while (num > 0) {
            num = num % 2 == 0 ? num / 2 : num - 1;
            step++;
        }
        System.out.println("总步骤为:" + step);
    }

    /**
     * 最富有客户的资产总量
     * 输入：accounts = [[1,2,3],[3,2,1]]
     * 输出：6
     * 解释：
     * 第 1 位客户的资产总量 = 1 + 2 + 3 = 6
     * 第 2 位客户的资产总量 = 3 + 2 + 1 = 6
     * 两位客户都是最富有的，资产总量都是 6 ，所以返回 6
     */
    @Test
    public void three() {
        int[][] accounts = {{1, 2, 3}, {3, 4, 5}};
        int max = 0;
        // 原始解法
        for (int[] account : accounts) {
            int num = 0;
            for (int i : account) {
                num += i;
            }
            max = Math.max(num, max);
        }
        System.out.println("最大值为:" + max);

        //官方解法
        int maxWealth = Integer.MIN_VALUE;
        for (int[] account : accounts) {
            maxWealth = Math.max(maxWealth, Arrays.stream(account).sum());
        }
        System.out.println("最大值为:" + maxWealth);
    }

    /**
     * 给你一个整数 n ，找出从 1 到 n 各个整数的 Fizz Buzz 表示，并用字符串数组 answer（下标从 1 开始）返回结果，其中：
     * answer[i] == "FizzBuzz" 如果 i 同时是 3 和 5 的倍数。
     * answer[i] == "Fizz" 如果 i 是 3 的倍数。
     * answer[i] == "Buzz" 如果 i 是 5 的倍数。
     * answer[i] == i （以字符串形式）如果上述条件全不满足。
     */
    @Test
    public void four() {
        int n = 15;
        List<String> answer = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            StringBuilder param = new StringBuilder();
            if (i % 3 == 0) {
                param.append("Fizz");
            }
            if (i % 5 == 0) {
                param.append("Buzz");
            }
            answer.add(param.length() == 0 ? String.valueOf(i) : param.toString());
        }
        System.out.println(answer);
    }

    /**
     * 链表的中间结点
     * 给定一个头结点为 head 的非空单链表，返回链表的中间结点。
     * 如果有两个中间结点，则返回第二个中间结点。
     * 解：<a href="https://leetcode.cn/problems/middle-of-the-linked-list/solution/lian-biao-de-zhong-jian-jie-dian-by-leetcode-solut/">...</a>
     */
    @Test
    public void five() {
        // 数组:链表的缺点在于不能通过下标访问对应的元素。因此我们可以考虑对链表进行遍历，同时将遍历到的元素依次放入数组 A 中。如果我们遍历到了 N 个元素，那么链表以及数组的长度也为 N，对应的中间节点即为 A[N/2]。
        ListNode head = getListNode();
        ListNode[] A = new ListNode[100];
        int t = 0;
        while (head != null) {
            A[t++] = head;
            head = head.next;
        }
        System.out.println(A[t / 2].val);

        // 单指针:我们可以对方法一进行空间优化，省去数组 A。我们可以对链表进行两次遍历。第一次遍历时，我们统计链表中的元素个数 N；第二次遍历时，我们遍历到第 N/2 个元素（链表的首节点为第 0 个元素）时，将该元素返回即可。
        head = getListNode();
        int n = 0;
        ListNode cur = head;
        while (cur != null) {
            ++n;
            cur = cur.next;
        }
        int k = 0;
        cur = head;
        while (k < n / 2) {
            ++k;
            cur = cur.next;
        }
        System.out.println(cur.val);

        // 快慢指针:用两个指针 slow 与 fast 一起遍历链表。slow 一次走一步，fast 一次走两步。那么当 fast 到达链表的末尾时，slow 必然位于中间。
        head = getListNode();
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        System.out.println(slow.val);
    }

    /**
     * 给你两个字符串：ransomNote 和 magazine ，判断 ransomNote 能不能由 magazine 里面的字符构成。
     * 如果可以，返回 true ；否则返回 false 。
     * magazine 中的每个字符只能在 ransomNote 中使用一次。
     * 输入：ransomNote = "aa", magazine = "aab"
     * 输出：true
     * 输入：ransomNote = "a", magazine = "b"
     * 输出：false
     */
    @Test
    public void six() {
        String ransomNote = "a", magazine = "aab";
        System.out.println(canConstruct(ransomNote, magazine));
    }

    /**
     * 移动零:给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
     */
    @Test
    public void seven() {
        int[] nums = {1, 2, 0, 3, 0};
        int index = 0;
        // index 是记录有多少个不为 0 参数的数，nums.length - index = 为0的数
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[index++] = nums[i];
            }
        }

        while (index < nums.length) {
            nums[index++] = 0;
        }
        System.out.println(Arrays.toString(nums));
    }

    /**
     * 移除元素:给你一个数组 nums和一个值 val，你需要 原地 移除所有数值等于val的元素，并返回移除后数组的新长度。
     * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
     * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
     */
    @Test
    public void eight() {
        int[] nums = {3, 2, 2, 3, 0};
        int val = 2;

        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[index++] = nums[i];
            }
        }

        while (index < nums.length) {
            nums[index++] = val;
        }
        System.out.println("输出:" + index + ", nums = " + Arrays.toString(nums));
    }

    /**
     * 删除排序数组中的重复项:给你一个 升序排列 的数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。元素的 相对顺序 应该保持 一致 。
     */
    @Test
    public void nine() {
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        System.out.println("输入:nums = " + Arrays.toString(nums));
        int index = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != nums[index]) {
                nums[++index] = nums[i];
            }
        }

        System.out.println("输出:" + (index + 1) + ", nums = " + Arrays.toString(nums));

    }

    /**
     * 删除排序数组中的重复项 II:给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使得出现次数超过两次的元素只出现两次 ，返回删除后数组的新长度。
     * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
     */
    @Test
    public void ten() {
        int[] nums = {1, 1, 1, 2, 2, 2, 3};
        System.out.println("输入:nums = " + Arrays.toString(nums));

        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (index < 2 || nums[i] != nums[index - 2]) {
                nums[index++] = nums[i];
            }
        }

        System.out.println("输出:" + index + ", nums = " + Arrays.toString(nums));
    }

    /**
     * 颜色分类:给定一个包含红色、白色和蓝色、共n 个元素的数组nums,原地对它们进行排序,使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
     * 我们使用整数 0、1 和 2 分别表示红色、白色和蓝色。
     * 必须在不使用库内置的 sort 函数的情况下解决这个问题。
     */
    @Test
    public void eleven() {
        int[] nums = {2, 4, 5, 7, 0, 3, 1};
        System.out.println("输入:nums = " + Arrays.toString(nums));

        for (int i = 0; i < nums.length; i++) {
            int index = nums[i];
            // 记录最小的位置
            int site = 0;
            for (int j = nums.length - 1; j > i; j--) {
                if (index > nums[j]) {
                    index = nums[j];
                    site = j;
                }
            }
            if (site != 0) {
                nums[site] = nums[i];
                nums[i] = index;
            }
        }

        System.out.println("输出:nums = " + Arrays.toString(nums));
    }

    /**
     * 数组中的第K个最大元素:给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
     * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
     * 你必须设计并实现时间复杂度为 O(n) 的算法解决此问题。
     */
    @Test
    public void twelve() {
        int[] nums = {2, 4, 5, 7, 0, 3, 1, 0};
        int k = 3;
        System.out.println("输入:nums = " + Arrays.toString(nums) + ", k = " + k);
        Arrays.sort(nums);
        System.out.println(nums[nums.length - k]);
        System.out.println("输出:nums = " + Arrays.toString(nums));
    }

    public static void main(String[] args) {
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        System.out.println("输入:nums = " + Arrays.toString(nums) + ", k = " + k);

        System.out.println("输出:nums = " + Arrays.toString(nums));
    }

    private static Boolean canConstruct(String ransomNote, String magazine) {
        // 什么都可以匹配
        Map<Character, Integer> map = new HashMap<>();
        char[] one = ransomNote.toCharArray();
        char[] two = magazine.toCharArray();
        if (one.length > two.length) {
            return Boolean.FALSE;
        }
        for (int i = 0; i < two.length; i++) {
            if (map.get(two[i]) != null) {
                map.put(two[i], map.get(two[i]) + 1);
                continue;
            }
            map.put(two[i], 1);
        }

        for (int i = 0; i < one.length; i++) {
            if (map.get(one[i]) == null || map.get(one[i]) == 0) {
                return Boolean.FALSE;
            }
            map.put(one[i], map.get(one[i]) - 1);
        }
        return Boolean.TRUE;
        // 只局限与 26个小字母
//        if (ransomNote.length() > magazine.length()) {
//            return false;
//        }
//        // 设置26个因为字母的坑，char c值 - ’a‘ = 26位字母的坑位 依次++1
//        int[] cnt = new int[26];
//        for (char c : magazine.toCharArray()) {
//            cnt[c - 'a']++;
//        }
//        // 先-- 在判断如果小于0就是不存在的
//        for (char c : ransomNote.toCharArray()) {
//            cnt[c - 'a']--;
//            if(cnt[c - 'a'] < 0) {
//                return false;
//            }
//        }
//        return true;
    }

    @NotNull
    private static ListNode getListNode() {
        ListNode listNode = new ListNode(1);    // 创建首节点，节点的val=0
        ListNode nextNode;                          // 声明一个变量用来在移动过程中指向当前节点
        nextNode = listNode;                        // 指向首节点，这样两个节点的指针就指向同一个节点

        for (int i = 2; i <= 10; i++) {
            nextNode.next = new ListNode(i);        // 生成新的节点，并把新节点连接起来
            nextNode = nextNode.next;               // 当前节点往后移动
        }                                           // 当for 循环结束之后，nextNode 指向最后一个节点

        nextNode = listNode;                        // 重新赋值让它指向首节点
//        while (nextNode != null) {
//            System.out.println("节点:" + nextNode.val);
//            nextNode = nextNode.next;
//        }
        return nextNode;
    }

    @Data
    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

}
