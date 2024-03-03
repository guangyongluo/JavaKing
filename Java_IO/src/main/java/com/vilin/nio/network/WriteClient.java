package com.vilin.nio.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriteClient {

  public static void main(String[] args) {
    try (Selector selector = Selector.open(); SocketChannel channel = SocketChannel.open(
        new InetSocketAddress("localhost", 8888))) {
      channel.configureBlocking(false);
      channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
      int count = 0;

      while(true){
        selector.select();
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isConnectable()) {
            log.debug("client finished this connection : {}", channel.finishConnect());
          } else if (key.isReadable()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            count += channel.read(buffer);
            buffer.clear();
            log.debug("client read from server byte count : {}", count);
          }
        }
      }
    } catch (IOException e) {
      log.error("can not read from server!");
    }
  }
}
