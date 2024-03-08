package com.vilin.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutureApiDemo {
  public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
    FutureTask<String> futureTask = new FutureTask<>(() -> {
      System.out.println(Thread.currentThread().getName() + "--------come in future task---------");
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return "task over";
    });

    Thread t1 = new Thread(futureTask, "t1");
    t1.start();

//        System.out.println(futureTask.get());//这样会有阻塞的可能，在程序没有计算完毕的情况下。
    System.out.println(Thread.currentThread().getName() + " ------忙其他任务");
//        System.out.println(futureTask.get(3,TimeUnit.SECONDS));//只愿意等待三秒，计算未完成直接抛出异常
    while (true) {//轮询
      if (futureTask.isDone()) {
        System.out.println(futureTask.get());
        break;
      } else {
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("正在处理中。。。");
      }
    }
  }
}
