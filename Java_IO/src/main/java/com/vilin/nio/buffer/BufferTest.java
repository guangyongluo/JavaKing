package com.vilin.nio.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BufferTest {

  public static void main(String[] args) {

    // FileChannel
    try (FileChannel channel = FileChannel.open(
        Path.of(BufferTest.class.getClassLoader().getResource("test.txt").getPath()),
        StandardOpenOption.READ)) {
      // Buffer
      ByteBuffer buffer = ByteBuffer.allocate(10);

      while(true) {
        // read from file write to buffer.
        int length = channel.read(buffer);
        log.debug("read content to buffer length : {}", length);
        if (length == -1) {
          break;
        }
        // print buffer content.
        // switch mode to read.
        buffer.flip();
        // check has content in buffer.
        while (buffer.hasRemaining()) {
          byte b = buffer.get();
          log.debug("get a byte from buffer : {}", (char) b);
        }

        buffer.clear();
      }
    } catch (IOException e) {
      log.error("ERROR : can not read byte from buffer.");
    }
  }
}
