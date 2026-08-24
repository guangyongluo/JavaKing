package com.vilin.my.spring;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class AopBeanPostProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    // 动态代理
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(bean.getClass());
    enhancer.setCallback(new MethodInterceptor() {
      @Override
      public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
          throws Throwable {
        System.out.println("before method " + method.getName());
        Object result =  method.invoke(bean, args);
        System.out.println("after method " + method.getName());
        return result;
      }
    });
    return enhancer.create();
  }

}
