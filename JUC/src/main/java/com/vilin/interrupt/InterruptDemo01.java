package com.vilin.interrupt;

import java.util.concurrent.TimeUnit;

public class InterruptDemo01 {
  static volatile boolean isStop = false; //volatile表示的变量具有可见性

  public static void main(String[] args) {
    new Thread(() -> {
      while (true) {
        if (isStop) {
          System.out.println(Thread.currentThread().getName() + " isStop的值被改为true，t1程序停止");
          break;
        }
        System.out.println("-----------hello volatile");
      }
    }, "t1").start();
    try {
      TimeUnit.MILLISECONDS.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    new Thread(() -> {
      isStop = true;
    }, "t2").start();

  }
}
