package com.vilin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class RedisDistributedLockApp8888 {

  public static void main(String[] args) {
    SpringApplication.run(RedisDistributedLockApp8888.class, args);
  }
}