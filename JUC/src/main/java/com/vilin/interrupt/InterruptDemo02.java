package com.vilin.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class InterruptDemo02 {
  static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

  public static void main(String[] args) {
    new Thread(() -> {
      while (true) {
        if (atomicBoolean.get()) {
          System.out.println(Thread.currentThread().getName() + " atomicBoolean的值被改为true，t1程序停止");
          break;
        }
        System.out.println("-----------hello atomicBoolean");
      }
    }, "t1").start();
    try {
      TimeUnit.MILLISECONDS.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    new Thread(() -> {
      atomicBoolean.set(true);
    }, "t2").start();

  }
}
