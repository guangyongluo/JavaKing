package com.vilin.nio.buffer;

import java.nio.ByteBuffer;

public class ByteBufferGatheringWriter {

  public static void main(String[] args) {
    ByteBuffer buffer1 = ByteBuffer.allocate(5);
    buffer1.put(new byte[]{'H', 'e', 'l', 'l', 'o'});
    ByteBuffer buffer2 = ByteBuffer.allocate(5);
    buffer2.put(new byte[]{'W', 'r', 'o', 'l', 'd'});


  }
}
