package com.vilin.nio.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriteServer {

  public static void main(String[] args) {

    try (ServerSocketChannel ssc = ServerSocketChannel.open(); Selector selector = Selector.open()) {

      ssc.bind(new InetSocketAddress(8888));
      ssc.configureBlocking(false);
      SelectionKey sscKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
      log.debug("server socket register in selector and the selection key is  : {}", sscKey);

      while (true) {
        selector.select();
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();

          if (key.isAcceptable()) {
            SocketChannel channel = ssc.accept();
            channel.configureBlocking(false);
            SelectionKey scKey = channel.register(selector, SelectionKey.OP_READ);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 5000000; i++) {
              stringBuilder.append("a");
            }

            ByteBuffer buffer = StandardCharsets.UTF_8.encode(stringBuilder.toString());
            int length = channel.write(buffer);
            log.debug("server write byte count : {}", length);

            if (buffer.hasRemaining()) {
              scKey.interestOps(scKey.interestOps() | SelectionKey.OP_WRITE);
              scKey.attach(buffer);
            }

          } else if (key.isWritable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            int length = channel.write(buffer);
            log.debug("server write byte count : {}", length);

            if(!buffer.hasRemaining()){
              key.attach(null);
              key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
            }
          }
          iterator.remove();
        }
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}