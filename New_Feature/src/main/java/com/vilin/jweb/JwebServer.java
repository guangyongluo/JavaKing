package com.vilin.jweb;

import com.sun.net.httpserver.SimpleFileServer;
import com.sun.net.httpserver.SimpleFileServer.OutputLevel;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class JwebServer {

  public static void main(String[] args) {
    var fileServer = SimpleFileServer.createFileServer(
        new InetSocketAddress(9000),
        Path.of("/Users/luowei/Downloads/"), OutputLevel.INFO);

    fileServer.start();
  }

}
