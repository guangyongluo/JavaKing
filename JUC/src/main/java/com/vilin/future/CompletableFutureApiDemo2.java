package com.vilin.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureApiDemo2 {

  public static void main(String[] args) {
    ExecutorService threadPool = Executors.newFixedThreadPool(3);
    CompletableFuture.supplyAsync(() -> {
      return 1;
    }, threadPool).thenApply(f -> {
      return f + 2;
    }).thenApply(f -> {
      return f + 2;
    }).thenAccept(r -> {
      log.debug("result = {}", r);//5
    });

    threadPool.shutdown();
  }
}
