package com.example.springdemo.service.demo.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class DemoServiceImplTest {
    // 计时器
    private static final TreeMap<String, Integer> englishMaps = new TreeMap<>();
    private static final TreeMap<String, Integer> chinaseMaps = new TreeMap<>();
    private static final String path = "C:\\Users\\2588\\Desktop\\坚持\\2022年11月14日-Monday.md";
    private static final List<String> englishs = new ArrayList<>();
    private static final List<String> chinase = new ArrayList<>();
    private static final List<String> chars = new ArrayList<>();
    private static final Integer nums = 2;
    private static Integer sum;

    /**
     * 需求概要用于-复习-当天所学的单词：
     * 存储学过的英文单词与汉语 （存储在文本里？用某个分隔符中英文分开）
     * 随机输出中文/英文，输入对应的答案
     * 一个单词反答对三次后就算通过，
     * 当所有单词都对了就结束。
     * v1.0 随机获取数组有问题
     * v2.0 随机获取一个单词/中文、校验次数，>0的就进行输出并比对，=0时就清理掉（remove）列表的 单词/数字
     */
    public static void main(String[] args) {
        //初始化、赋值
        getData();
        //取随机
        //输入、校验、对比
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入对应的中文/英文:");
        while (sum != 0) {
            // 获取 英文+中文的长度 的随机数
            int randomNum = (int) (Math.random() * chars.size());
            int whetherToDelete;
            String param = chars.get(randomNum);
            //随机给出的中英文
            System.out.print(param + ":");
            String parameter = scan.next();
            //结束复习
            if ("END".equals(parameter) || "end".equals(parameter)) {
                System.out.println("复习结束祝学有所成!");
                break;
            }
            if (englishs.contains(param)) {
                // 输出英文 对比输入的中文
                String chinaseParam = chinase.get(englishs.indexOf(param));
                if (!chinaseParam.equals(parameter)) {
                    System.out.println("X");
                    continue;
                }
                whetherToDelete = chinaseMaps.get(chinaseParam) - 1;
                chinaseMaps.put(chinaseParam, whetherToDelete);
                sum--;
            } else {
                // 输出中文 对比输入的英文
                String englishsParam = englishs.get(chinase.indexOf(param));
                if (!englishsParam.equals(parameter)) {
                    System.out.println("X");
                    continue;
                }
                whetherToDelete = englishMaps.get(englishsParam) - 1;
                englishMaps.put(englishsParam, whetherToDelete);
                sum--;
            }
            System.out.println("√");

            //当答对了3次以后 就从列表中删除
            if (whetherToDelete == 0) {
                chars.remove(param);
            }
        }
    }

    /**
     * 读取文本
     * 把中英文进行赋值
     * 文本中英文格式：
     * 1. habitat n.栖息地
     * 2. ideal adj.理想的、完美的
     * <p>
     * 截取规则：
     * 一行有2个空格;第一个空格后是英文,第二个空格后是中文
     */
    public static void getData() {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader in = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            int b, i = 0;
            try {
                StringBuilder englishKey = new StringBuilder();
                StringBuilder chinaseValue = new StringBuilder();
                while ((b = in.read()) != -1) {
                    //是空格
                    if (Character.isSpaceChar((char) b)) {
                        i++;
                        continue;
                    }

                    // 是否换行
                    if ((char) b == '\n') {
                        i = 0;
                        // 中文不能为空
                        if (StringUtils.isNotBlank(chinaseValue)) {
                            String key = englishKey.toString().replaceAll("\\p{C}|\\s", "");
                            String value = chinaseValue.toString().replaceAll("\\p{C}|\\s", "");
                            englishMaps.put(key, nums);
                            chinaseMaps.put(value, nums);
                            englishs.add(key);
                            chinase.add(value);
                        }

                        englishKey = new StringBuilder();
                        chinaseValue = new StringBuilder();
                    }

                    // 空格判断,过了第一个空格判定为英文, 过了第二个空格 判定为中文
                    switch (i) {
                        case 1:
                            englishKey.append((char) b);
                            break;
                        case 2:
                            chinaseValue.append((char) b);
                            break;
                    }
                }
                in.close();

                chars.addAll(chinase);
                chars.addAll(englishs);
                sum = chars.size() * nums;
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(JSON.toJSONString(englishMaps));
            System.out.println(JSON.toJSONString(chinaseMaps));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}