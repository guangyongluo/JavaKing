package com.vilin.nio.buffer;

import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufferReader {

  public static void main(String[] args) {
    ByteBuffer buffer = ByteBuffer.allocate(10);
    buffer.put(new byte[]{0x61, 0x62, 0x63, 0x64});
    ByteBufferUtil.debugAll(buffer);

    byte[] bytes = new byte[4];
    buffer.flip();
    buffer.get(bytes);
    ByteBufferUtil.debugAll(buffer);

    log.debug("read byte array from byte buffer : {}", bytes);

    buffer.rewind();
    ByteBufferUtil.debugAll(buffer);

    buffer.get(new byte[2]);
    buffer.mark();
    ByteBufferUtil.debugAll(buffer);

    bytes = new byte[2];
    buffer.get(bytes);
    log.debug("read byte array from byte buffer : {}", bytes);
    buffer.reset();
    buffer.get(bytes);
    log.debug("read byte array from byte buffer : {}", bytes);

    byte b = buffer.get(2);
    log.debug("read specify index byte : {}", (char) b);
  }

}
