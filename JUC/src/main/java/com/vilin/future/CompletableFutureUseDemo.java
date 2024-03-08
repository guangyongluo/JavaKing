package com.vilin.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureUseDemo {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
      log.debug("{} -----come in CompletableFuture-----", Thread.currentThread().getName());
      int result = ThreadLocalRandom.current().nextInt(10);
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (result > 5) { //模拟产生异常情况
        int i = 10 / 0;
      }
      log.debug("----------1秒钟后出结果: {}", result);
      return result;
    }, executorService).whenComplete((v, e) -> {
      if (e == null) {
        log.debug("计算完成 更新系统：{}", v);
      }
    }).exceptionally(e -> {
      e.printStackTrace();
      log.error("异常情况：{}, {}", e.getCause(), e.getMessage());
      return null;
    });
    log.debug("{} 先去完成其他任务", Thread.currentThread().getName());
    executorService.shutdown();
  }
}
