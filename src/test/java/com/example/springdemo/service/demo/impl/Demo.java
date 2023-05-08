package com.example.springdemo.service.demo.impl;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Mr.Huang
 * @date 2023/4/3 17:44
 **/
public class Demo {
    @Data
    static class User {
        private String name;
        private int age;
        private List<Integer> scores;
        private List<Address> addresses;
    }
    @Data
    static class Address {
        private String city;
        private String street;
        private List<String> phones;
    }

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
        Address address1 = new Address();
        address1.setCity("Beijing");
        address1.setStreet("Wudaokou");
        address1.setPhones(Arrays.asList("123456789", "987654321"));
        Address address2 = new Address();
        address2.setCity("Shanghai");
        address2.setStreet("Jingan");
        address2.setPhones(Arrays.asList("123456789", "987654321"));
        user.setAddresses(Arrays.asList(address1, address2));

        // 定义的值
        Map<String, Object> config = new HashMap<>();
        config.put("name", "Alice");
        config.put("scores", Arrays.asList(90, 70, 80));
        Map<String, Object> address2Config = new HashMap<>();
        address2Config.put("city", "Shanghai");
        // 差异
        address2Config.put("street", "Huangpu");
        address2Config.put("phones", Arrays.asList("222222222", "111111111"));
        config.put("addresses", Arrays.asList(address2Config));

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

}
