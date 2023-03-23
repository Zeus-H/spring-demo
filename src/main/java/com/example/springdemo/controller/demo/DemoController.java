package com.example.springdemo.controller.demo;

import com.example.springdemo.entity.demo.Demo;
import com.example.springdemo.service.demo.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/demo")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @RequestMapping("/update")
    public void update(@RequestBody Demo demo){
        demoService.update(demo);
    }

    @GetMapping("/byId")
    @Cacheable(value = "demoCache", key = "#id")
    public Demo selectById(String id){
       return demoService.selectById(id);
    }

    @RequestMapping("/save")
    public void save(@RequestBody Demo demo){
        demoService.save(demo);
    }

    @RequestMapping("/query")
    public List<Demo> query(@RequestBody Demo demo){
        return demoService.query(demo);
    }


}
