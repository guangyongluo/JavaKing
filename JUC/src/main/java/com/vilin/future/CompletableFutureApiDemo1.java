package com.vilin.future;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureApiDemo1 {
  public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
    ExecutorService threadPool = Executors.newFixedThreadPool(3);
    CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return 1;
    }, threadPool).thenApply(f -> {
      log.debug("step 2");
      return f + 2;
    }).handle((f, e) -> {
      log.debug("step 3");
      int i=10/0;
      return f + 2;

//             thenApply(f -> {
//            System.out.println("step 3");
//            return f + 2;
    }).whenComplete((v, e) -> {
      if (e == null) {
        log.debug("calculate result : " + v);
      }
    }).exceptionally(e -> {
      log.error(Arrays.toString(e.getStackTrace()));
      return null;
    });

    threadPool.shutdown();
    log.debug("{}------主线程先去做其他事情", Thread.currentThread().getName());
  }
}
