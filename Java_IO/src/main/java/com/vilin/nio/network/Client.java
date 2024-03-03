package com.vilin.nio.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {

  public static void main(String[] args) {
    try (SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 8888))) {

      channel.write(StandardCharsets.UTF_8.encode("server connected"));

      log.debug("client connection : ", channel);

    } catch (IOException e) {
     log.error("can not connect to server!");
    }
  }
}
