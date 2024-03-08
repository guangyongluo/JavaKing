package com.vilin.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutureTaskAdvantage {

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    long start = System.currentTimeMillis();

    FutureTask<String> task1 = new FutureTask<>(() -> {
      try {
        TimeUnit.MILLISECONDS.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "task1 over!";
    });

    Thread thread1 = new Thread(task1);

    FutureTask<String> task2 = new FutureTask<>(() -> {
      try {
        TimeUnit.MILLISECONDS.sleep(300);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "task2 over!";
    });

    Thread thread2 = new Thread(task2);

    FutureTask<String> task3 = new FutureTask<>(() -> {
      try {
        TimeUnit.MILLISECONDS.sleep(300);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "task3 over!";
    });

    Thread thread3 = new Thread(task3);

    thread1.start();
    thread2.start();
    thread3.start();

    log.debug(task1.get());
    log.debug(task2.get());
    log.debug(task3.get());

    long end = System.currentTimeMillis();

    log.debug("finished three tasks use {} milliseconds", end - start);
  }
}
