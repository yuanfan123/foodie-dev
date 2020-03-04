package com.imooc.controller;


import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore//swagger忽略
@RestController
public class StuFooController {
    @Autowired
    private StuService stuService;
    @GetMapping("/getStu")
    public Object getStu(int stuId){
        return stuService.getStuInfo(stuId);
    }
    @PostMapping("/saveStu")
    public Object saveStu(){
        stuService.saveStu();
        return "200";
    }
    @PostMapping("/updateStu")
    public Object updateStu(int id){
        stuService.updateStu(id);
        return "200";
    }
    @PostMapping("/deleteStu")
    public Object deleteStu(int id){
        stuService.deleteStu(id);
        return "200";
    }
}
