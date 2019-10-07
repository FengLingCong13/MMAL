package com.xhhp.mmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.xhhp.mmall.dao")
@ComponentScan(basePackages = {"com.xhhp.mmall.*"})
public class MmallApplication {

    public static void main(String[] args) {
         SpringApplication.run(MmallApplication.class, args);
    }

}
