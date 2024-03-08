package com.vilin.future;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureApiDemo4 {

  public static void main(String[] args) {
    ExecutorService threadPool = Executors.newFixedThreadPool(3);
    CompletableFuture<String> playA = CompletableFuture.supplyAsync(() -> {
      try {
        log.debug("A come in");
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        log.error(Arrays.toString(e.getStackTrace()));
      }
      return "playA";
    }, threadPool);

    CompletableFuture<String> playB = CompletableFuture.supplyAsync(() -> {
      try {
        log.debug("B come in");
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        log.error(Arrays.toString(e.getStackTrace()));
      }
      return "playB";
    }, threadPool);

    CompletableFuture<String> result = playA.applyToEither(playB, f -> {
      return f + " is winner";
    });

    threadPool.shutdown();

    /**
     * A come in
     * B come in
     * main-----------winner:playA is winner
     */
    log.debug("{}-----------winner: {}", Thread.currentThread().getName(), result.join());
  }
}
