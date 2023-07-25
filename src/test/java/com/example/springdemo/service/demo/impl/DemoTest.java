package com.example.springdemo.service.demo.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.springdemo.entity.demo.Address;
import com.example.springdemo.entity.demo.Demo;
import com.example.springdemo.entity.demo.Rule;
import com.example.springdemo.entity.demo.User;
import com.example.springdemo.service.demo.DemoService;
import com.example.springdemo.service.demo.ReturnDelivery;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Mr.Huang
 * @date 2023/4/3 17:44
 **/
@SpringBootTest
public class DemoTest {
    public static void main(String[] args) throws Exception {
//        User user = new User();
//        user.setName("Alice");
//        user.setAge(20);
//
//        Map<String, Object> config = new HashMap<>();
//        config.put("name", "Alice");
//        config.put("age", 18);
//
//        Class<?> clazz = User.class;
//        for (Map.Entry<String, Object> entry : config.entrySet()) {
//            String fieldName = entry.getKey();
//            Object expectedValue = entry.getValue();
//            Field field = clazz.getDeclaredField(fieldName);
//            field.setAccessible(true);
//            Object actualValue = field.get(user);
//
//            System.out.println(fieldName + " == " + Objects.equals(expectedValue, actualValue));
//        }

        String[] folderNames = {"1","2","3"};
        String directoryPath = ""; // 指定目录路径

        File directory = new File(directoryPath);

        if (directory.exists()) {
            for (String folderName : folderNames) {
                directory = new File(directoryPath + "\\" + folderName);
                if (!directory.exists()) {
                    boolean created = directory.mkdirs(); // 创建文件夹
                    if (created) {
                        System.out.println("文件夹创建成功");
                    } else {
                        System.out.println("文件夹创建失败");
                    }
                } else {
                    System.out.println("文件夹已存在");
                }
            }
        } else {
            System.out.println("文件夹已存在");
        }
    }

    /**
     * 数据库动态设置key-value实体字段和值，然后进行比对。 递归的方式进行处理
     */
    @Test
    public void one() throws IllegalAccessException {
        // 赋值
        User user = new User();
        user.setName("Alice");
        user.setScores(Arrays.asList(90, 80, 70));
        List<Address> addressList = new ArrayList<>();
        Address address1 = new Address();
        address1.setCity("Beijing");
        address1.setStreet("Wudaokou");
        address1.setPhones(Arrays.asList("123456789", "987654321"));
        addressList.add(address1);
        Address address2 = new Address();
        address2.setCity("Shanghai");
        address2.setStreet("Jingan");
        address2.setPhones(Arrays.asList("123456789", "987654321"));
        addressList.add(address2);
        user.setAddresses(addressList);

        // 定义的值
        Map<String, Object> config = new HashMap<>();
        config.put("name", "Alice");
        config.put("scores", Arrays.asList(90, 70, 80));
        Map<String, Object> address2Config = new HashMap<>();
        address2Config.put("city", "Shanghai");
        // 差异
        address2Config.put("street", "Huangpu");
        address2Config.put("phones", Arrays.asList("222222222", "111111111"));
        config.put("addresses", Collections.singletonList(address2Config));

        // 进行递归
        compare(user, config);
    }

    private static void compare(Object obj, Map<String, Object> config) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        // 定义的
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            String fieldName = entry.getKey();
            Object expectedValue = entry.getValue();
            Field field;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                continue;
            }
            field.setAccessible(true);
            Object actualValue = field.get(obj);
            if (expectedValue instanceof List) {
                List<?> expectedList = (List<?>) expectedValue;
                List<?> actualList = (List<?>) actualValue;
                if (actualList.containsAll(expectedList)) {
                    System.out.println("Field " + fieldName + " does match!" + " expectedList: " + expectedValue + "; actualList: " + actualValue);
                } else {
                    for (int i = 0; i < expectedList.size(); i++) {
                        Object expectedElement = expectedList.get(i);
                        Object actualElement = actualList.get(i);
                        if (expectedElement instanceof Map) {
                            // 实体的list
                            compare(actualElement, (Map<String, Object>) expectedElement);
                        } else if (Objects.equals(expectedElement, actualElement)) {
                            System.out.println("Field " + fieldName + " does not match!" + " expectedElement: " + expectedElement + "; actualElement: " + actualElement);
                            break;
                        }

                    }
                }
            } else if (!Objects.equals(expectedValue, actualValue)) {
                System.out.println("Field " + fieldName + " does not match!" + " expectedValue: " + expectedValue + "; actualValue: " + actualValue);
            }
        }
    }

    @Autowired
    private DemoService demoService;

    @Test
    public void two() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = Rule.builder()
                .ruleCode("name")
                .ruleValue("2")
                .symbol(">")
                .build();
        rules.add(rule);
        Rule rule1 = Rule.builder()
                .ruleCode("name")
                .ruleValue("7")
                .symbol("<")
                .build();
        rules.add(rule1);
        Rule rule2 = Rule.builder()
                .ruleCode("name")
                .ruleValue("4, 5, 6")
                .symbol("in")
                .build();
        rules.add(rule2);
        Rule rule3 = Rule.builder()
                .ruleCode("name")
                .ruleValue("5")
                .symbol("not in")
                .build();
        rules.add(rule3);
        Rule rule4 = Rule.builder()
                .ruleCode("name")
                .ruleValue("6")
                .symbol("!=")
                .build();
        rules.add(rule4);
        List<Demo> demos = demoService.queryByRules(rules);
        System.out.println("-----------------------------------------------------------");
        for (Demo demo : demos) {
            System.out.println(JSONObject.toJSONString(demo));
        }
    }

    @Autowired
    private ApplicationContext context;

    @Test
    public void three() {
        ReturnDelivery tbDelivery = context.getBean("tbDelivery", ReturnDelivery.class);
        tbDelivery.returnSend();

        if (context.containsBean("dyDelivery")) {
            ReturnDelivery delivery = context.getBean("dyDelivery", ReturnDelivery.class);
            delivery.returnSend();
        } else {
            System.out.println("没有 DyDelivery bean");
        }
    }

    @Test
    public void four() {
        Map<String, Integer> order = new HashMap<>();
        order.put("1", 1);
        order.put("2", 2);
        order.put("4", 1);
        Map<String, Integer> Result = new HashMap<>();
        Result.put("1", 1);
        Result.put("2", 1);
        Result.put("3", 1);

        boolean flag = true;

        // 比较键值对
        for (Map.Entry<String, Integer> entry : Result.entrySet()) {
            // 检查键是否存在于第二个Map，并比较值是否相等
            if (order.size() != Result.size() ||
                    !order.containsKey(entry.getKey()) ||
                    !Objects.equals(order.get(entry.getKey()), entry.getValue())) {
                // 键值对不完全匹配
                flag = false;
            }
        }

        if (flag) {
            System.out.println("所有键值对完全匹配");
        } else {
            System.out.println("键值对不完全匹配");
        }
    }

    @Test
    public void five() {
        // 读取指定目录的目录名
        File file = new File("");
        File[] files = file.listFiles();
        if (files != null) {
            for (File file1 : files) {
                System.out.println(file1.getName());
            }
        }
    }

    @Test
    public void six() {
        int n = 3;  // 2个会议室
        int[][] meetings = new int[][]{{1,20},{2,10},{3,5},{4,9},{6,8}};    // 会议场次时间 [开始时间, 结束时间]

        int[] cnt = new int[n];
        PriorityQueue<Integer> idle = new PriorityQueue<>();
        for (int i = 0; i < n; ++i) idle.offer(i);
        PriorityQueue<Pair<Long, Integer>> using = new PriorityQueue<>((a, b) -> !Objects.equals(a.getKey(), b.getKey()) ? Long.compare(a.getKey(), b.getKey()) : Integer.compare(a.getValue(), b.getValue()));
        Arrays.sort(meetings, Comparator.comparingInt(a -> a[0]));
        for (int[] m : meetings) {
            long st = m[0], end = m[1];
            while (!using.isEmpty() && using.peek().getKey() <= st) {
                idle.offer(using.poll().getValue()); // 维护在 st 时刻空闲的会议室
            }
            int id;
            if (idle.isEmpty()) {
                Pair<Long, Integer> p = using.poll(); // 没有可用的会议室，那么弹出一个最早结束的会议室（若有多个同时结束的，会弹出下标最小的）
                end += p.getKey() - st; // 更新当前会议的结束时间
                id = p.getValue();
            } else id = idle.poll();
            ++cnt[id];
            using.offer(new Pair<>(end, id)); // 使用一个会议室
        }
        int ans = 0;
        for (int i = 0; i < n; ++i) if (cnt[i] > cnt[ans]) ans = i;
        System.out.println(ans);
    }
}
