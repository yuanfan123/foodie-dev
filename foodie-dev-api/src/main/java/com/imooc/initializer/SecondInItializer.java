package com.imooc.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname FirstInItializer
 * @Description
 * @Date 2020/3/5 22:22
 * @Created by lyf
 */
@Order(2)
public class SecondInItializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();
        Map<String, Object> map = new HashMap<>();
        map.put("key2","value2");
        MapPropertySource mapPropertySource = new MapPropertySource("secondInItializer", map);
        environment.getPropertySources().addLast(mapPropertySource);
        System.out.println("自定义初始化2");

    }
}
