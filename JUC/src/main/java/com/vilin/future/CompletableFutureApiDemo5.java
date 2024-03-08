package com.vilin.future;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureApiDemo5 {

  public static void main(String[] args) {
    CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
      log.debug("{} start", Thread.currentThread().getName());
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        log.error(Arrays.toString(e.getStackTrace()));
      }
      return 10;
    });

    CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
      log.debug("{} start", Thread.currentThread().getName());
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        log.error(Arrays.toString(e.getStackTrace()));
      }
      return 20;
    });

    CompletableFuture<Integer> finalResult = completableFuture1.thenCombine(completableFuture2,
        (x, y) -> {
          System.out.println("----------merge two completable future results...");
          return x + y;
        });
    log.debug("result = {}", finalResult.join());

  }
}
