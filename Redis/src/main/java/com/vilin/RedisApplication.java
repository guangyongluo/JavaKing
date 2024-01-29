package com.vilin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.vilin.mapper")
@EnableSwagger2
public class RedisApplication {

  public static void main(String[] args) {
    SpringApplication.run(RedisApplication.class, args);
  }
}
