package com.vilin.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABADemo {
  static AtomicInteger atomicInteger = new AtomicInteger(100);
  static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

  public static void main(String[] args) {

    abaNoHappen();

  }

  private static void abaNoHappen() {
    new Thread(() -> {
      int stamp = atomicStampedReference.getStamp();
      System.out.println(Thread.currentThread().getName() + "\t" + "首次版本号: " + stamp);
      try {
        TimeUnit.MILLISECONDS.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      atomicStampedReference.compareAndSet(100, 101, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
      System.out.println(Thread.currentThread().getName() + "\t" + "2次版本号: " + atomicStampedReference.getStamp());
      atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
      System.out.println(Thread.currentThread().getName() + "\t" + "3次版本号: " + atomicStampedReference.getStamp());
    }, "t3").start();


    new Thread(() -> {
      int stamp = atomicStampedReference.getStamp();
      System.out.println(Thread.currentThread().getName() + "\t" + "首次版本号: " + stamp);
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      boolean b = atomicStampedReference.compareAndSet(100, 200, stamp, stamp + 1);
      System.out.println(b + "\t" + atomicStampedReference.getReference() + "\t" + atomicStampedReference.getStamp());
    }, "t4").start();
  }

  private static void abaHappen() {
    new Thread(() -> {
      atomicInteger.compareAndSet(100, 101);
      try {
        TimeUnit.MILLISECONDS.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      atomicInteger.compareAndSet(101, 100);
    }, "t1").start();


    new Thread(() -> {
      try {
        TimeUnit.MILLISECONDS.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(atomicInteger.compareAndSet(100, 2023) + "\t" + atomicInteger.get());//true	2023
    }, "t2").start();
  }
}
