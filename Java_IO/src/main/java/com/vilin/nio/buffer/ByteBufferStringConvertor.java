package com.vilin.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufferStringConvertor {

  public static void main(String[] args) {
    ByteBuffer buffer1 = ByteBuffer.allocate(10);
    // ---------String convert to ByteBuffer----------

    // 1. String.getBytes()
    buffer1.put("Hello".getBytes());
    ByteBufferUtil.debugAll(buffer1);

    // 2. StandardCharsets
    ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("Hello");
    ByteBufferUtil.debugAll(buffer2);

    // 3. ByteBuffer.wrap()
    ByteBuffer buffer3 = ByteBuffer.wrap("Hello".getBytes());
    ByteBufferUtil.debugAll(buffer3);

    // ---------ByteBuffer convert to String----------
    buffer1.flip();
    CharBuffer decode1 = StandardCharsets.UTF_8.decode(buffer1);
    log.debug("decode byte buffer1 : {}", decode1);

    CharBuffer decode2 = StandardCharsets.UTF_8.decode(buffer2);
    log.debug("decode byte buffer2 : {}", decode2);

    CharBuffer decode3 = StandardCharsets.UTF_8.decode(buffer3);
    log.debug("decode byte buffer3 : {}", decode3);
  }
}
