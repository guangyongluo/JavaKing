package com.vilin.nio.buffer;

import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufferTest {

  public static void main(String[] args) {
    ByteBuffer buffer = ByteBuffer.allocate(10);
    buffer.put((byte) 0x61); //a
    ByteBufferUtil.debugAll(buffer);
    buffer.put(new byte[] {0x62, 0x63, 0x64}); // b c d
    ByteBufferUtil.debugAll(buffer);
    buffer.flip();
    log.debug("buffer.get() = {}", buffer.get());
    ByteBufferUtil.debugAll(buffer);
    buffer.compact();
    ByteBufferUtil.debugAll(buffer);
    buffer.put(new byte[]{0x65, 0x66});
    ByteBufferUtil.debugAll(buffer);
  }
}
