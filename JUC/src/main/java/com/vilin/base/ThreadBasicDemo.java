package com.vilin.base;

public class ThreadBasicDemo {


  public static void main(String[] args) {
    Thread thread = new Thread(() -> {}, "t1");
  }
}
