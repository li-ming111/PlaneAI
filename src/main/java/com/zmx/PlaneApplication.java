package com.zmx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zmx.mapper")
public class PlaneApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaneApplication.class, args);
    }

}
