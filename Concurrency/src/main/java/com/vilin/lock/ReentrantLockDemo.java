package com.vilin.lock;

public class ReentrantLockDemo {

  private Object obj = new Object();

  public static void main(String[] args) {
    ReentrantLockDemo reentrantLockDemo = new ReentrantLockDemo();
    reentrantLockDemo.reentrantLock1();
    reentrantLockDemo.reentrantLock2();
  }

  private void reentrantLock1() {

    new Thread(() -> {
      synchronized (obj) {
        System.out.println("调用外层获得obj对象锁");
        synchronized (obj) {
          System.out.println("调用中层获得obj对象锁");
          synchronized (obj) {
            System.out.println("调用内层获得obj对象锁");
          }
        }
      }
    }).run();
  }

  private synchronized void reentrantLock2() {
    System.out.println("使用synchronized给reentrantLock方法对象加锁");
    m1();
  }

  private synchronized void m1() {
    System.out.println("使用synchronized给reentrantLock方法内部调用m1方法加锁");
    m2();
  }

  private synchronized void m2() {
    System.out.println("使用synchronized给reentrantLock方法内部调用m1方法的内部调用方法m2加锁");
  }
}
