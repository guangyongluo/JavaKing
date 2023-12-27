package com.vilin.classloader;

import org.openjdk.jol.info.ClassLayout;

public class JOLDemo {

  private String id;

  private String name;

  public static void main(String[] args) {
    JOLDemo jolDemo = new JOLDemo();
    System.out.println(ClassLayout.parseInstance(jolDemo).toPrintable());

    synchronized (jolDemo) {
      System.out.println(ClassLayout.parseInstance(jolDemo).toPrintable());
    }
  }

}
