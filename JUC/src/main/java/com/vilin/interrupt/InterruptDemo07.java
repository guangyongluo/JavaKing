package com.vilin.interrupt;

public class InterruptDemo07 {
  public static void main(String[] args) {
    /**
     * main	false
     * main	false
     * -----------1
     * -----------2
     * main	true
     * main	false
     */
    System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());//false
    System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());//false
    System.out.println("-----------1");
    Thread.currentThread().interrupt();
    System.out.println("-----------2");
    System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());//true
    System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());//false

  }
}
