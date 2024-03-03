package com.vilin.nio.network;

import static com.vilin.nio.buffer.ByteBufferUtil.debugRead;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {

  public static void main(String[] args) {
    // 1. create byte buffer
    ByteBuffer buffer = ByteBuffer.allocate(16);

    // 2. create server socket channel
    try (ServerSocketChannel ssc = ServerSocketChannel.open()) {

      // 3. set non-blocking mode for server socket channel.
      ssc.configureBlocking(false);

      // 4. bind port number.
      ssc.bind(new InetSocketAddress(8888));

      List<SocketChannel> channels = new ArrayList<>();

      while (true) {

        // 5. build connection
        SocketChannel sc = ssc.accept();

        if(null != sc){
          sc.configureBlocking(false);
          channels.add(sc);
          log.debug("server build a new connection : {}", sc);
        }

        for (SocketChannel channel : channels) {
          int length = channel.read(buffer);
          if(length > 0) {
            buffer.flip();
            debugRead(buffer);
            log.debug("server read a buffer from connection : {}", channel);
          }
        }

        TimeUnit.MILLISECONDS.sleep(100L);
      }

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
