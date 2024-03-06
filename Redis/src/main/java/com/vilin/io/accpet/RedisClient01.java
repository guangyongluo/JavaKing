package com.vilin.io.accpet;

import java.io.IOException;
import java.net.Socket;

public class RedisClient01 {
  public static void main(String[] args) throws IOException
  {
    System.out.println("------RedisClient02 start");
    Socket socket = new Socket("127.0.0.1", 6379);
  }
}
