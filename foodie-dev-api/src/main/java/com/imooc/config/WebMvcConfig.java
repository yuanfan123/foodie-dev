package com.imooc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Classname WebMvcConfig
 * @Description
 * @Date 2020/3/12 22:23
 * @Created by lyf
 */
@Configuration
public class WebMvcConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
       return   restTemplateBuilder.build();
    }
}
