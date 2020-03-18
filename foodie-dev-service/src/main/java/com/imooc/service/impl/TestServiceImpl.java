package com.imooc.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * @Classname TestServiceImpl
 * @Description
 * @Date 2020/3/5 23:42
 * @Created by lyf
 */
@Component
public class TestServiceImpl implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    public String test(){
        return applicationContext.getEnvironment().getProperty("key3");
    }
}
