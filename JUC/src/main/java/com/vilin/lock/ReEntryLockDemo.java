package com.vilin.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class ReEntryLockDemo {

  public static void main(String[] args) {
    final Object o = new Object();
    /**
     * ---------------外层调用
     * ---------------中层调用
     * ---------------内层调用
     */
    new Thread(() -> {
      synchronized (o) {
        System.out.println("---------------外层调用");
        synchronized (o) {
          System.out.println("---------------中层调用");
          synchronized (o) {
            System.out.println("---------------内层调用");
          }
        }
      }
    }, "t1").start();

    /**
     * 注意：加锁几次就需要解锁几次
     * ---------------外层调用
     * ---------------中层调用
     * ---------------内层调用
     */
    Lock lock = new ReentrantLock();
    new Thread(() -> {
      lock.lock();
      try {
        System.out.println("---------------外层调用");
        lock.lock();
        try {
          System.out.println("---------------中层调用");
          lock.lock();
          try {
            System.out.println("---------------内层调用");
          } finally {
            lock.unlock();
          }
        } finally {
          lock.unlock();
        }
      } finally {
        lock.unlock();
      }
    }, "t2").start();
  }
}
