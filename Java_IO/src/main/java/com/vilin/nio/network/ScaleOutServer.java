package com.vilin.nio.network;

import com.vilin.nio.buffer.ByteBufferPackageHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScaleOutServer {
  public static void main(String[] args) {

    // 1. create server socket and selector.
    try (ServerSocketChannel ssc = ServerSocketChannel.open(); Selector selector = Selector.open()) {

      // 2. bind server socket on port 8888.
      ssc.bind(new InetSocketAddress(8888));

      // 3. set server socket non-blocking.
      ssc.configureBlocking(false);

      // 4. register ssc to selector and handler accept event.
      SelectionKey sscKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
      log.debug("server socket register in selector and the selection key is  : {}", sscKey);

      while (true) {
        // 5. blocking method, listen to the registry event.
        int select = selector.select();
        log.debug("listen all registered event number : {}", select);

        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();

          // 6. handle all event.
          switch (key.interestOps()) {
            case SelectionKey.OP_ACCEPT: {
              ServerSocketChannel channel = (ServerSocketChannel) key.channel();
              SocketChannel socketChannel = channel.accept();
              socketChannel.configureBlocking(false);
              ByteBuffer byteBuffer = ByteBuffer.allocate(16);
              SelectionKey scKey = socketChannel.register(selector, SelectionKey.OP_READ, byteBuffer);
              log.debug("client socket register in selector and the selection key is  : {}", scKey);
              break;
            }
            case SelectionKey.OP_READ: {
              log.debug("server handle read event : {}", key);
              SocketChannel channel = (SocketChannel) key.channel();
              ByteBuffer buffer = (ByteBuffer) key.attachment();
              int length = channel.read(buffer);
              if (length == -1) {
                key.cancel();
                channel.close();
              } else {
                ByteBufferPackageHandler.split(buffer);
                if(buffer.position() == buffer.limit()){
                  ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                  buffer.flip();
                  newBuffer.put(buffer);
                  key.attach(newBuffer);
                }
              }
              break;
            }
          }

          // 7. finished handle event must remove the selection key in selectedKeys.
          iterator.remove();
        }
      }


    } catch (IOException e) {
      log.error("open selector failed!");
    }


  }
}
