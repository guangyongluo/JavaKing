package com.vilin.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureBuildDemo {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(3);

    CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
      log.debug(Thread.currentThread().getName());
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    },executorService);

    log.debug("completableFuture.get() = {}", completableFuture.get()); //null


    CompletableFuture<String> objectCompletableFuture = CompletableFuture.supplyAsync(()->{
      log.debug(Thread.currentThread().getName());
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return "hello supplyAsync";
    },executorService);

    log.debug("completableFuture.get() = {}", objectCompletableFuture.get());//hello supplyAsync

    executorService.shutdown();

  }
}
