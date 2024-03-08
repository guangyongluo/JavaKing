package com.vilin.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureDemo {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    FutureTask<String> futureTask = new FutureTask(new MyThread());
    Thread t1 = new Thread(futureTask); //开启一个异步线程
    t1.start();

    System.out.println(futureTask.get()); //有返回hello Callable
  }
}

class MyThread implements Callable<String> {

  @Override
  public String call() throws Exception {
    System.out.println("--------come in callable method---------");
    return "hello Callable";
  }
}
