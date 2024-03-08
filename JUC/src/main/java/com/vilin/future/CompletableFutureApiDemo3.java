package com.vilin.future;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureApiDemo3 {

  public static void main(String[] args) {
    System.out.println(CompletableFuture.supplyAsync(() -> "result").thenRun(() -> {
    }).join()); //null
    System.out.println(
        CompletableFuture.supplyAsync(() -> "result").thenAccept(r -> System.out.println(r))
            .join()); //result null
    System.out.println(
        CompletableFuture.supplyAsync(() -> "result").thenApply(f -> f + 2).join()); //result2
  }
}
