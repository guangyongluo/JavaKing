package com.vilin.nio.channel;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilesIterator {
  public static void main(String[] args) throws IOException {
    Path path = Paths.get("/Library/Java/JavaVirtualMachines/jdk-21.jdk");
    AtomicInteger dirCount = new AtomicInteger();
    AtomicInteger fileCount = new AtomicInteger();
    AtomicInteger jmodCount = new AtomicInteger();
    Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
          throws IOException {
        log.debug("Iterate Directory : {}", dir);
        dirCount.incrementAndGet();
        return super.preVisitDirectory(dir, attrs);
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
          throws IOException {
        log.debug("Iterate file : {}", file);
        fileCount.incrementAndGet();
        if (file.toFile().getName().endsWith(".jmod")) {
          jmodCount.incrementAndGet();
        }
        return super.visitFile(file, attrs);
      }
    });

    log.debug("Java 21 Directory count : {}", dirCount); // 91
    log.debug("Java 21 File count : {}", fileCount); // 395
    log.debug("Java 21 jmod count : {}", jmodCount);
  }
}
