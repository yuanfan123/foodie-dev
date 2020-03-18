package com.imooc.controller;


import com.imooc.service.impl.TestServiceImpl;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
//@ApiIgnore//swagger忽略
public class HelloController {
    @Autowired
    private TestServiceImpl testService;
    final static Logger logger = LoggerFactory.getLogger(HelloController.class);
    @GetMapping("/hello")
    public Object hello(){
        logger.debug("ingo:hello-");
        logger.info("ingo:hello-");
        logger.warn("ingo:hello-");
        logger.error("ingo:hello-");
        return "hello world-";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("userInfo","new user");
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");
       // session.removeAttribute("userInfo");
        return "ok";
    }
    @GetMapping("/test")
    public String test(){
        return testService.test();
    }
}
