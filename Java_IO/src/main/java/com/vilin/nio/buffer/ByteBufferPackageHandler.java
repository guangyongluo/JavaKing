package com.vilin.nio.buffer;

import java.nio.ByteBuffer;

public class ByteBufferPackageHandler {

  public static void main(String[] args) {
    ByteBuffer source = ByteBuffer.allocate(32);

    source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
    split(source);

    source.put("w are you?\nhaha!\n".getBytes());
    split(source);
  }

  /**
   * handle the packet fragmentation in the network communications
   * @param source : network packet mock.
   */
  public static void split(ByteBuffer source) {
    source.flip();

    for(int i = 0; i < source.limit(); i++){

      if (source.get(i) == '\n') {

        int length = i + 1 - source.position();

        ByteBuffer target = ByteBuffer.allocate(length);

        for(int j = 0; j < length; j++){
          target.put(source.get());
        }

        ByteBufferUtil.debugAll(target);
      }
    }


    source.compact();
  }
}
