package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Classname Swagger2
 * @Description Swagger2
 * @Date 2020/3/3 0:17
 * @Created by lyf
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    //配置swagger2核心配置（）
    //http://localhost:8088/swagger-ui.html 原始
    //http://localhost:8088/doc.html
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)//执行 api Swagger 2
                .apiInfo(apiInfo())                   //定义Api文档汇总
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.imooc.controller")) //制定controller包
                .paths(PathSelectors.any())                                       //所有controller
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台api接口")           //文档页标题
                .contact(new Contact("IMOOC",
                        "www.imooc.com",
                        "abc@imooc.com "))              //联系人
                .description("API文档")                     //详细信息
                .version("1.0.1")
                .termsOfServiceUrl("http://www,imooc.com")  //网站信息
                .build();
    }



}
