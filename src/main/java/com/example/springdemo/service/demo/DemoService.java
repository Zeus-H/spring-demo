package com.example.springdemo.service.demo;

import com.example.springdemo.entity.demo.Demo;

import java.util.List;

/**
 * DemoService
 * @author generator
 * @date 2022年09月02日
 */
public interface DemoService {

	void save(Demo entity);

	void update(Demo entity);

	Demo selectById(String id);

	List<Demo> query(Demo demo);
}
