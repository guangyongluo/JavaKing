package com.vilin.nio.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufferScatteringReader {

  // understand the scattered read from a channel, this way make read operate efficiency.
  public static void main(String[] args) {
    try (FileChannel channel = FileChannel.open(
        Path.of(BufferTest.class.getClassLoader().getResource("words.txt").getPath()),
        StandardOpenOption.READ)) {
      // prepare three buffers.
      ByteBuffer buffer1 = ByteBuffer.allocate(3);
      ByteBuffer buffer2 = ByteBuffer.allocate(3);
      ByteBuffer buffer3 = ByteBuffer.allocate(5);

      channel.read(new ByteBuffer[]{buffer1, buffer2, buffer3});

      ByteBufferUtil.debugAll(buffer1);
      ByteBufferUtil.debugAll(buffer2);
      ByteBufferUtil.debugAll(buffer3);
    }catch (IOException e) {
      log.error("ERROR : can not read byte from buffer.");
    }
  }
}
