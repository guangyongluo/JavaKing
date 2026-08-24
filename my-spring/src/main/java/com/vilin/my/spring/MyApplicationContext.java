package com.vilin.my.spring;

import com.vilin.my.spring.annotation.Autowired;
import com.vilin.my.spring.annotation.Component;
import com.vilin.my.spring.annotation.ComponentScan;
import com.vilin.my.spring.annotation.Lazy;
import com.vilin.my.spring.annotation.PostConstruct;
import com.vilin.my.spring.annotation.Scope;
import com.vilin.my.spring.aware.ApplicationContext;
import com.vilin.my.spring.aware.ApplicationContextAware;
import com.vilin.my.spring.aware.BeanNameAware;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

public class MyApplicationContext implements ApplicationContext {

  private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

  private Map<String, Object> singletonObjects = new HashMap<>();

  public MyApplicationContext(Class<?> configClass) {

    scanSpringBean(configClass);

    // 3.把这些类实例化，并且放到一个map中，key是beanName，value是bean实例
    for (Entry<String, BeanDefinition> stringBeanDefinitionEntry : beanDefinitionMap.entrySet()) {
      String beanName = stringBeanDefinitionEntry.getKey();
      BeanDefinition beanDefinition = stringBeanDefinitionEntry.getValue();

      if (beanDefinition.getScope().equals("singleton") && !beanDefinition.getLazy()) {
        Object singletonObject = createBean(beanName, beanDefinition);
        singletonObjects.put(beanName, singletonObject);
      }
    }
  }

  // Bean声明周期
  public Object createBean(String beanName, BeanDefinition beanDefinition) {
    Class<?> beanClass = beanDefinition.getClazz();
    try {
      // 实例化
      Object instance = beanClass.getConstructor().newInstance();
      // 依赖注入
      for (Field field : beanClass.getDeclaredFields()) {
        if (field.isAnnotationPresent(Autowired.class)) {
          String fieldName = field.getName();
          field.setAccessible(true);
          field.set(instance, getBean(fieldName));
        }
      }

      // 初始化前
      for (Method method : beanClass.getDeclaredMethods()) {
        if (method.isAnnotationPresent(PostConstruct.class)) {
          method.invoke(instance);
        }
      }

      //初始化
      if (instance instanceof InitializingBean) {
        ((InitializingBean) instance).afterPropertiesSet();
      }

      if (instance instanceof BeanNameAware beanNameAware) {
        beanNameAware.setBeanName(beanName);
      }

      if (instance instanceof ApplicationContextAware applicationContextAware) {
        applicationContextAware.setApplicationContext(this);
      }

      // 初始化后 AOP
      AopBeanPostProcessor aopBeanPostProcessor = new AopBeanPostProcessor();
      instance = aopBeanPostProcessor.postProcessAfterInitialization(instance, beanName);
      return instance;
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
             NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private void scanSpringBean(Class<?> configClass) {
    // 1.扫描配置类所在的包，获取所有的类
    if (configClass.isAnnotationPresent(ComponentScan.class)) {
      ComponentScan componentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
      String basePackage = componentScanAnnotation.value().replace(".", "/");

      ClassLoader classLoader = this.getClass().getClassLoader();
      URL resource = classLoader.getResource(basePackage);
      File file = new File(resource.getFile());

      for (File listFile : file.listFiles()) {
        String absolutePath = listFile.getAbsolutePath();
        String classPath = absolutePath.substring(absolutePath.indexOf("com"),
            absolutePath.indexOf(".class"));
        classPath = classPath.replace("/", ".");

        try {
          Class<?> clazz = classLoader.loadClass(classPath);
          // 2.判断哪些类上面有@Component注解
          if (clazz.isAnnotationPresent(Component.class)) {

            String beanName = clazz.getAnnotation(Component.class).value();

            //代表是一个Bean
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setClazz(clazz);

            if (clazz.isAnnotationPresent(Scope.class)) {
              beanDefinition.setScope(clazz.getAnnotation(Scope.class).value());
            } else {
              beanDefinition.setScope("singleton");
            }

            if (clazz.isAnnotationPresent(Lazy.class)) {
              beanDefinition.setLazy(clazz.getAnnotation(Lazy.class).value());
            } else {
              beanDefinition.setLazy(false);
            }
            if (StringUtils.isEmpty(beanName)) {
              beanName = StringUtils.uncapitalize(
                  StringUtils.substringAfterLast(clazz.getName(), "."));
            }
            beanDefinitionMap.put(beanName, beanDefinition);
          }
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }

      }

    }
  }

  public Object getBean(String beanName) {
    if (!beanDefinitionMap.containsKey(beanName)) {
      throw new IllegalArgumentException("beanName not found");
    }

    BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

    if (beanDefinition.getScope().equals("singleton")) {
      Object object = singletonObjects.get(beanName);
      if (object != null) {
        return object;
      } else {
        Object singletonObject = createBean(beanName, beanDefinition);
        singletonObjects.put(beanName, singletonObject);
        return singletonObject;
      }
    } else {
      return createBean(beanName, beanDefinition);
    }
  }

}
