package com.vilin.my.spring;

import lombok.Data;

@Data
public class BeanDefinition {

  private Class<?> clazz;
  private String scope;
  private Boolean lazy;

}
