package com.vilin.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SpinLockDemo {

  AtomicReference<Thread> atomicReference = new AtomicReference<>();
  public void lock() {
    Thread thread = Thread.currentThread();
    System.out.println(Thread.currentThread().getName() + "\t --------come in");
    while (!atomicReference.compareAndSet(null, thread)) {

    }
  }

  public void unLock() {
    Thread thread = Thread.currentThread();
    atomicReference.compareAndSet(thread, null);
    System.out.println(Thread.currentThread().getName() + "\t --------task over,unLock.........");
  }

  public static void main(String[] args) {
    SpinLockDemo spinLockDemo = new SpinLockDemo();
    new Thread(() -> {
      spinLockDemo.lock();
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      spinLockDemo.unLock();
    }, "A").start();


    try {
      TimeUnit.MILLISECONDS.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    new Thread(() -> {
      spinLockDemo.lock();
      spinLockDemo.unLock();
    }, "B").start();
  }
}
