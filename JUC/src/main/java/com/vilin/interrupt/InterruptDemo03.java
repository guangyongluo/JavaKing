package com.vilin.interrupt;

import java.util.concurrent.TimeUnit;

public class InterruptDemo03 {
  public static void main(String[] args) {
    Thread t1 = new Thread(() -> {
      while (true) {
        if (Thread.currentThread().isInterrupted()) {
          System.out.println(Thread.currentThread().getName() + " isInterrupted()的值被改为true，t1程序停止");
          break;
        }
        System.out.println("-----------hello isInterrupted()");
      }
    }, "t1");
    t1.start();

    try {
      TimeUnit.MILLISECONDS.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    //t2向t1放出协商，将t1中的中断标识位设为true，希望t1停下来
    new Thread(() -> t1.interrupt(), "t2").start();

    // 当然，也可以t1自行设置
    // t1.interrupt();

  }
}
