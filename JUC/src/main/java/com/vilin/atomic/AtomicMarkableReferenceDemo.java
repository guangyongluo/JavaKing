package com.vilin.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicMarkableReferenceDemo {
  static AtomicMarkableReference markableReference = new AtomicMarkableReference(100, false);

  public static void main(String[] args) {
    new Thread(() -> {
      boolean marked = markableReference.isMarked();
      System.out.println(Thread.currentThread().getName() + "\t" + "默认标识: " + marked);//t1	默认标识: false
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      markableReference.compareAndSet(100, 1000, marked, !marked);//t2	默认标识: false

    }, "t1").start();

    new Thread(() -> {
      boolean marked = markableReference.isMarked();
      System.out.println(Thread.currentThread().getName() + "\t" + "默认标识: " + marked);//t2	t2线程CASResult：false
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      boolean b = markableReference.compareAndSet(100, 2000, marked, !marked);
      System.out.println(Thread.currentThread().getName() + "\t" + "t2线程CASResult：" + b);
      System.out.println(Thread.currentThread().getName() + "\t" + markableReference.isMarked());//t2	true
      System.out.println(Thread.currentThread().getName() + "\t" + markableReference.getReference());//t2	1000

    }, "t2").start();
  }
}
