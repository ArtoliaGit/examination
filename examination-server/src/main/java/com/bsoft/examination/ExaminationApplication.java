package com.bsoft.examination;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 启动类
 * 开启swagger EnableSwagger2Doc
 * 开启异步 EnableAsync
 * 开启定时任务 EnableScheduling
 * 开启webmvc EnableWebMvc
 */
@EnableSwagger2Doc
@EnableAsync
@EnableScheduling
@EnableWebMvc
@SpringBootApplication
public class ExaminationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExaminationApplication.class, args);
    }

}
