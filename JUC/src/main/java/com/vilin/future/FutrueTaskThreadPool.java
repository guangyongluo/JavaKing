package com.vilin.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutrueTaskThreadPool {

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    long start = System.currentTimeMillis();

    ExecutorService threadPool = Executors.newFixedThreadPool(2);

    FutureTask<String> task1 = new FutureTask<>(() -> {
      try {
        TimeUnit.MILLISECONDS.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "task1 over!";
    });

    FutureTask<String> task2 = new FutureTask<>(() -> {
      try {
        TimeUnit.MILLISECONDS.sleep(300);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return "task2 over!";
    });

    threadPool.submit(task1);
    threadPool.submit(task2);

    log.debug(task1.get());
    log.debug(task2.get());

    long end = System.currentTimeMillis();

    threadPool.shutdown();

    log.debug("finished three tasks use {} milliseconds", end - start);
  }
}
