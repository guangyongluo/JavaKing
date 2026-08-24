package com.vilin.my.spring;

import com.vilin.my.spring.service.UserService;

public class MyApplication {

  public static void main(String[] args) {
    // spring容器的创建
    MyApplicationContext applicationContext = new MyApplicationContext(MyConfig.class);
    UserService userService = (UserService) applicationContext.getBean("userService");
    System.out.println(userService);
    System.out.println(userService);
    System.out.println(userService);
    System.out.println(userService.getOrderService());
    System.out.println(userService.getName());
    userService.test();
  }

}
