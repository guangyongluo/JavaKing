package com.vilin.nio.network;

import static com.vilin.nio.buffer.ByteBufferUtil.debugAll;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiThreadServer {

  public static void main(String[] args) {
    try (ServerSocketChannel ssc = ServerSocketChannel.open();
        Selector serverSelector = Selector.open()) {
      ssc.configureBlocking(false);
      ssc.bind(new InetSocketAddress(8888));

      ssc.register(serverSelector, SelectionKey.OP_ACCEPT);
      Worker worker = new Worker("work-01");
      while (true) {
        serverSelector.select();

        Iterator<SelectionKey> iterator = serverSelector.selectedKeys().iterator();
        while(iterator.hasNext()) {
          SelectionKey key = iterator.next();
          if(key.isAcceptable()) {
            log.debug("connected to client...");
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            log.debug("before worker register...");
            worker.register(sc);
            log.debug("after worker register...");
          }

          iterator.remove();
        }
      }


    } catch (IOException e) {
      log.error("server socket open failed.");
    }

  }

  private static class Worker implements Runnable {

    private String name;

    private Thread worker;

    private Selector selector;

    private Boolean start = false;

    private ConcurrentLinkedQueue<Runnable> queue;

    public Worker(String name){
      this.name = name;
    }

    public void register(SocketChannel sc) throws ClosedChannelException {
      if(!start) {
        try {
          selector = Selector.open();
        } catch (IOException e) {
          log.error("can not open worker selector");
          throw new RuntimeException(e);
        }
        queue = new ConcurrentLinkedQueue<>();
        worker = new Thread(this, name);
        worker.start();
        log.debug("start read worker task...");
        start = true;
      }

      queue.add(() -> {
        try {
          sc.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
          throw new RuntimeException(e);
        }
      });

      selector.wakeup();
    }

    @Override
    public void run() {
      while(true) {
        log.debug("worker start to work...");

        Runnable task = queue.poll();
        if(task != null) {
          log.debug("register sc to work selector...");
          task.run();
        }

        try {
          selector.select();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        log.debug("worker selector keys : {}", iterator);
        while(iterator.hasNext()) {
          SelectionKey key = iterator.next();
          if (key.isReadable()) {
            log.debug("worker listen read event...");
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(16);
            try {
              int read = channel.read(buffer);
              if (read == -1) {
                key.cancel();
                channel.close();
              } else {
                buffer.flip();
                debugAll(buffer);
              }
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }

          iterator.remove();
        }
      }
    }
  }
}
