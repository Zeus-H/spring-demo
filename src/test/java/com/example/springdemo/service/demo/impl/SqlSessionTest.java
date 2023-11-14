package com.example.springdemo.service.demo.impl;

import com.alibaba.fastjson.JSON;
import com.example.springdemo.entity.demo.Demo;
import com.example.springdemo.mapper.demo.DemoMapper;
import com.example.springdemo.service.task.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Mr.Huang
 * @date 2023/11/14 11:05
 **/
@SpringBootTest
public class SqlSessionTest {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void one() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            DemoMapper mapper = sqlSession.getMapper(DemoMapper.class);
            Demo demo = mapper.selectByPrimaryKey("1636634665640546306");
            System.out.println(JSON.toJSONString(demo));
        } catch (Exception e) {
            throw new CommonException(90900000, "报错了！");
        }
    }

    @Test
    public void two() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            String prefix = "com.example.springdemo.mapper.demo.DemoMapper.";
            Demo demo = new Demo();
            int count = sqlSession.selectOne(prefix + "selectCountByCondition", demo);
            Cursor<Demo> cursor = sqlSession.selectCursor(prefix + "selectByCondition", demo);
            System.out.println("数据总数：" + count);
            for (Demo doc : cursor) {
                System.out.println(JSON.toJSONString(doc));
            }
        } catch (Exception e) {
            throw new CommonException(90900000, "报错了！" + e);
        }
    }

}
