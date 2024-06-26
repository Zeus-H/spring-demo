package com.example.springdemo.service.demo.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.example.springdemo.entity.demo.Address;
import com.example.springdemo.entity.demo.Demo;
import com.example.springdemo.entity.demo.Rule;
import com.example.springdemo.entity.demo.User;
import com.example.springdemo.service.demo.DemoService;
import com.example.springdemo.service.demo.ReturnDelivery;
import com.example.springdemo.service.demo.impl.util.TestFileUtil;
import javafx.util.Pair;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                idle.offer(Objects.requireNonNull(using.poll()).getValue()); // 维护在 st 时刻空闲的会议室
            }
            int id;
            if (idle.isEmpty()) {
                Pair<Long, Integer> p = using.poll(); // 没有可用的会议室，那么弹出一个最早结束的会议室（若有多个同时结束的，会弹出下标最小的）
                end += Objects.requireNonNull(p).getKey() - st; // 更新当前会议的结束时间
                id = p.getValue();
            } else id = idle.poll();
            ++cnt[id];
            using.offer(new Pair<>(end, id)); // 使用一个会议室
        }
        int ans = 0;
        for (int i = 0; i < n; ++i) if (cnt[i] > cnt[ans]) ans = i;
        System.out.println(ans);
    }

    @Test
    public void seven() {
        BufferedReader myBufferedReader = null;

        try {
            myBufferedReader = new BufferedReader(new FileReader("C:\\Users\\2588\\Desktop\\genneric.txt"));
            String line;
            while ((line = myBufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException error) {
            System.out.println(error.getMessage());
            System.out.println("1");
            return;
        } catch (Exception error) {
            System.out.println(error.getMessage());
            System.out.println("2");
        } finally {
            try {
                if (myBufferedReader != null) {
                    myBufferedReader.close();
                }
                System.out.println("3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done");
    }

    @Test
    public void eight() {
        String doc = "{\"string\":\"ems\"}";
        DemoData data = JSONObject.parseObject(doc, DemoData.class);
        System.out.println(JSONObject.toJSONString(data));
        if (StringUtils.isNotBlank(data.getIgnore()) && "1".equals(data.getIgnore())) {
            System.out.println("没报错");
        }
    }

    @Test
    public void nine() {
        Kryo kryo = new Kryo();
        kryo.register(DemoData.class);

        // 创建对象
        DemoData data = new DemoData();
        data.setString("ems");
        data.setIgnore("1");

        // 序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output, data);
        output.close();

        // 反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Input input = new Input(bais);
        DemoData demoData = kryo.readObject(input, DemoData.class);
        input.close();

        System.out.println(demoData.toString());
    }

    @Test
    public void ten() throws Exception {
        // 获取 内部类 的class 对象 并且创建类的实例
        Class<?> targetClass = Class.forName("com.example.springdemo.service.demo.impl.DemoTest$DemoData");
        DemoData targetDemo = (DemoData) targetClass.newInstance();

        // 获取对象中定义的所有方法
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        System.out.println("=========================================");

        // 获取指定的方法并调用
        Method publicMethod = targetClass.getDeclaredMethod("publicMethod",String.class);
        publicMethod.invoke(targetDemo, "Java");

        // 获取指定参数并对参数进行修改
        Field field = targetClass.getDeclaredField("string");
        // 为了修改类中参数，取消private的安全检查
        field.setAccessible(true);
        field.set(targetDemo, "python");
        System.out.println("=========================================");

        // 调用 private 方法
        Method privateMethod = targetClass.getDeclaredMethod("privateMethod");
        // 为了修改类中参数，取消private的安全检查
        privateMethod.setAccessible(true);
        privateMethod.invoke(targetDemo);
    }

    @Test
    public void eleven() {
        // 根据用户id生成对应的邀请码，gen 的后几位会出现一致，gen2经过了混淆减少相同之处，decode是gen2的反解析的方法
        for (long i = 10000L; i < 10010L; i++) {
            String code = new InviteCode().gen(i);
            System.out.println("Generated Code: " + code);
        }

        System.out.println("=============================");

        for (long i = 10000L; i < 10010L; i++) {
            String code2 = new InviteCode().gen2(i);
            System.out.println("Generated Code2: " + code2);
            Long decode = new InviteCode().decode(code2);
            System.out.println("Generated decode: " + decode);
        }
    }

    @Test
    public void twelve() {

    }

    /**
     * 导出的demo代码
     */
    @Test
    public void repeatedWrite() {
        // 方法1: 如果写到同一个sheet
        String fileName = TestFileUtil.getPath() + "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        // sheet 的页数
        int pageSize = 12;
        // 生成 1-10000的数
        List<Integer> totalNums = IntStream.rangeClosed(1, 10001)
                .boxed()
                .collect(Collectors.toList());
        System.out.println("-------- 开始导出 --------");

        // 计算每页的元素数量
        int elementsPerPage = (int) Math.ceil((double) totalNums.size() / pageSize);

        // 使用 Java 8 流将 totalNums 划分为 pageSize 个子列表
        List<List<Integer>> pages = IntStream.range(0, pageSize)
                .mapToObj(i -> totalNums.subList(i * elementsPerPage, Math.min((i + 1) * elementsPerPage, totalNums.size())))
                .collect(Collectors.toList());

        // 这里 指定文件  .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) 自适应宽度
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build()) {
            // 去调用写入,实际使用时根据数据库分页的总的页数来。这里最终会写到pageSize个sheet里面
            for (int i = 0; i < pageSize; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<DemoData> data = data(pages.get(i));
                excelWriter.write(data, writeSheet);
                System.out.println("-------- 导出中" + i + " --------");
            }
            System.out.println("-------- 导出成功！！！------");
        }
    }

    @Data
    @EqualsAndHashCode
    public static class DemoData {
        @ExcelProperty("字符串标题")
        private String string;
        @ExcelProperty("日期标题")
        @ColumnWidth(20)
        private Date date;
        @ExcelProperty("数字标题")
        private Double doubleData;
        /** 忽略这个字段 */
        @ExcelIgnore
        private String ignore;

        public DemoData() {
            string = "Java";
        }

        public void publicMethod(String s) {
            System.out.println("I love " + s);
        }

        private void privateMethod() {
            System.out.println("string = " + string);
        }
    }

    private List<DemoData> data(List<Integer> totalNums) {
        List<DemoData> list = ListUtils.newArrayList();
        for (Integer nums : totalNums) {
            DemoData data = new DemoData();
            data.setString("字符串" + nums);
            data.setDate(new Date());
            data.setDoubleData(new Random().nextDouble());
            list.add(data);
        }
        return list;
    }

    private static class InviteCode {
        // 定义字符数组，假设为32个不同的字符
        private static final char[] CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();
        // 定义字符数组的长度
        private static final int CHARS_LENGTH = CHARS.length;
        // 定义生成的邀请码长度
        private static final int CODE_LENGTH = 6;
        // 素数用于乘法操作
        private static final long PRIME1 = 31;
        // 盐值用于加法操作
        private static final long SLAT = 1361;
        // 使用的第二个素数，用于索引乱序操作
        private static final int PRIME2 = 29;

        /**
         * 生成邀请码
         *
         * @param id 唯一的id主键
         * @return code
         */
        public String gen(Long id) {
            // 进行补位
            id = id * PRIME1 + SLAT;
            // 将id转换成32进制的值
            long[] b = new long[CODE_LENGTH];
            // 32进制数
            b[0] = id;
            for (int i = 0; i < CODE_LENGTH - 1; i++) {
                b[i + 1] = b[i] / CHARS_LENGTH;
                b[i] = b[i] % CHARS_LENGTH;
            }
            // 增加一个简单的校验位
            b[5] = (b[2] + b[3] + b[4]) * PRIME1 % CHARS_LENGTH;

            StringBuilder buffer = new StringBuilder();
            Arrays.stream(b).boxed().map(Long::intValue).map(t -> CHARS[t]).forEach(buffer::append);
            return buffer.toString();
        }

        /**
         * 生成邀请码
         *
         * @param id 唯一的id主键
         * @return code
         */
        public String gen2(Long id) {
            // 进行补位，并扩大整体
            id = id * PRIME1 + SLAT;
            // 将id转换成32进制的值
            long[] b = new long[CODE_LENGTH];
            // 32进制数
            b[0] = id;
            for (int i = 0; i < CODE_LENGTH - 1; i++) {
                b[i + 1] = b[i] / CHARS_LENGTH;
                // 扩大每一位的差异
                b[i] = (b[i] + i * b[0])% CHARS_LENGTH;
            }
            // 增加一个简单的校验位
            b[5] = (b[0] + b[1] + b[2] + b[3] + b[4]) * PRIME1 % CHARS_LENGTH;
            //进行混淆
            long[] codeIndexArray = new long[CODE_LENGTH];
            for (int i = 0; i < CODE_LENGTH; i++) {
                codeIndexArray[i] = b[i * PRIME2 % CODE_LENGTH];
            }


            StringBuilder buffer = new StringBuilder();
            Arrays.stream(codeIndexArray).boxed().map(Long::intValue).map(t -> CHARS[t]).forEach(buffer::append);
            return buffer.toString();
        }

        /**
         * 将邀请码解密成原来的id
         *
         * @param code 邀请码
         * @return id
         */
        public Long decode(String code) {
            if (code.length() != CODE_LENGTH) {
                return null;
            }
            // 将字符还原成对应数字
            long[] a = new long[CODE_LENGTH];
            for (int i = 0; i < CODE_LENGTH; i++) {
                char c = code.charAt(i);
                int index = findIndex(c);
                if (index == -1) {
                    // 异常字符串
                    return null;
                }
                a[i * PRIME2 % CODE_LENGTH] = index;
            }

            long[] b = new long[CODE_LENGTH];
            for (int i = CODE_LENGTH - 2; i >= 0; i--) {
                b[i] = (a[i] - a[0] * i + (long) CHARS_LENGTH * i) % CHARS_LENGTH;
            }

            long res = 0;
            for (int i = CODE_LENGTH - 2; i >= 0; i--) {
                res += b[i];
                res *= (i > 0 ? CHARS_LENGTH : 1);
            }
            return (res - SLAT) / PRIME1;
        }

        /**
         * 查找对应字符的index
         *
         * @param c 字符
         * @return index
         */
        private static int findIndex(char c) {
            for (int i = 0; i < CHARS_LENGTH; i++) {
                if (CHARS[i] == c) {
                    return i;
                }
            }
            return -1;
        }
    }
}
