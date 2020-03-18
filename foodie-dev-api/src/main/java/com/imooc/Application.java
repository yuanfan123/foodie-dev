package com.imooc;

import com.imooc.initializer.SecondInItializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//扫描所有包
@ComponentScan(basePackages = {"org.n3r","com.imooc"})
//扫描通用mapper所在的包
@MapperScan(basePackages = "com.imooc.mapper")
@EnableScheduling  //开启定时任务
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        //第二类初始化器实现方式
//        SpringApplication springApplication = new SpringApplication(Application.class);
//        springApplication.addInitializers(new SecondInItializer());
//        springApplication.run();
    }
}
