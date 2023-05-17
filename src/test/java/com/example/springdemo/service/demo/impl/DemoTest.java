package com.example.springdemo.service.demo.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.springdemo.entity.demo.Address;
import com.example.springdemo.entity.demo.Demo;
import com.example.springdemo.entity.demo.Rule;
import com.example.springdemo.entity.demo.User;
import com.example.springdemo.service.demo.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Mr.Huang
 * @date 2023/4/3 17:44
 **/
@SpringBootTest
public class DemoTest {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setName("Alice");
        user.setAge(20);

        Map<String, Object> config = new HashMap<>();
        config.put("name", "Alice");
        config.put("age", 18);

        Class<?> clazz = User.class;
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            String fieldName = entry.getKey();
            Object expectedValue = entry.getValue();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object actualValue = field.get(user);

            System.out.println(fieldName + " == " + Objects.equals(expectedValue, actualValue));
        }
    }

    /** 数据库动态设置key-value实体字段和值，然后进行比对。 递归的方式进行处理 */
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

}
