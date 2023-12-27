package com.vilin.classloader;

public class ClassLoaderDemo {

  public static void main(String[] args) throws ClassNotFoundException {

    // 父子关系 AppClassLoader <- ExtClassLoader <- BootStrap ClassLoader
    ClassLoader cl1 = ClassLoaderDemo.class.getClassLoader();
    System.out.println("cl1 > " + cl1);
    System.out.println("parent of cl1 > " + cl1.getParent());

    // BootStrap ClassLoader由C++开发，是JVM虚拟机的一部分，本身不是JAVA类
    System.out.println("grant parent of cl1 > " + cl1.getParent().getParent());

    // String, Integer等基础类由BootStrap ClassLoader加载
    ClassLoader cl2 = String.class.getClassLoader();
    System.out.println("cl2 > " + cl2);
    System.out.println(cl1.loadClass("java.util.List").getClass().getClassLoader());

    // java指令可以通过增加-verbose:class -verbose:gc 参数在启动时打印出类加载情况
    // BootStrap ClassLoader加载java基础类，这个属性不能在java指令中指定，推断不是由java语言处理。
    System.out.println("BootStrap ClassLoader加载目录：" + System.getProperty("sun.boot.library.path"));
    // Extension ClassLoader加载JAVA_HOME/ext下的jar包，可以通过-D java.ext.dirs指定
    System.out.println("Extension ClassLoader加载目录：" + System.getProperty("java.ext.dirs"));
    // AppClassLoader加载CLASSPATH，应用下的jar包。可以通过-D java.class.path指定
    System.out.println("AppClassLoader加载目录：" + System.getProperty("java.library.path"));

  }

}
