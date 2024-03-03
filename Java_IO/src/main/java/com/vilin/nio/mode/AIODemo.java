package com.vilin.nio.mode;

import static com.vilin.nio.buffer.ByteBufferUtil.debugAll;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AIODemo {

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);

    try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(
        Path.of(AIODemo.class.getClassLoader().getResource("data.txt").getPath()),
        StandardOpenOption.READ)) {

      ByteBuffer buffer = ByteBuffer.allocate(16);
      log.debug("begin...");
      asynchronousFileChannel.read(buffer, 0, null, new CompletionHandler<Integer, ByteBuffer>() {
        @Override
        public void completed(Integer result, ByteBuffer attachment) {
          buffer.flip();
          debugAll(buffer);
          latch.countDown();
          log.debug("read completed...");
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
          log.debug("read failed...");
        }
      });

    } catch (IOException e) {
      log.error("can not found the file : {}");
    }
    log.debug("end...");

    latch.await();
  }
}
