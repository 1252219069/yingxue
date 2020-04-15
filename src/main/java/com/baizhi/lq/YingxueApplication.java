package com.baizhi.lq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.baizhi.lq.dao")
@SpringBootApplication
public class YingxueApplication {

    public static void main(String[] args) {
        SpringApplication.run(YingxueApplication.class, args);
    }

}
