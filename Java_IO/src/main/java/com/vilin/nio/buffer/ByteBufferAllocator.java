package com.vilin.nio.buffer;

import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufferAllocator {

  public static void main(String[] args) {
    /**
     * class java.nio.HeapByteBuffer -> use JVM heap memory with low efficiency, JVM can GC this memory.
     * class java.nio.DirectByteBuffer -> use Direct system memory with high efficiency, Direct memory
     * invoke system kernel to allocate so need more cup time, and Direct memory must be collected when
     * no need to use.
     */
    log.debug("ByteBuffer.allocate() use heap memory. -> {}", ByteBuffer.allocate(16).getClass());
    log.debug("ByteBuffer.allocateDirect() use system memory. -> {}", ByteBuffer.allocateDirect(16).getClass());
  }
}
