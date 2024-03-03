package com.vilin.nio.channel;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelTest {

  public static void main(String[] args) {
    try (FileChannel channelFrom = FileChannel.open(
        Path.of(ChannelTest.class.getClassLoader().getResource("data.txt").getPath()),
        StandardOpenOption.READ);
        FileChannel channelTo = FileChannel.open(
            Path.of(ClassLoader.getSystemResource("").getPath().concat("transfer.txt")),
            StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
      log.debug("channelFrom position = {}", channelFrom.position());
      log.debug("channelFrom size = {}", channelFrom.size());
      log.debug("channelTo position = {}", channelTo.position());
      log.debug("channelTo size = {}", channelTo.size());

      // use OS zero copy and transfer limitation 2G.
      long size = channelFrom.size();
      // left bytes size.
      for (long left = size; left > 0; ) {
        log.debug("position: {}, left: {} ", size - left, left);
        left -= channelFrom.transferTo((size - left), left, channelTo);
      }

      log.debug("channelFrom position = {}", channelFrom.position());
      log.debug("channelFrom size = {}", channelFrom.size());
      log.debug("channelTo position = {}", channelTo.position());
      log.debug("channelTo size = {}", channelTo.size());

    } catch (IOException e) {
      log.error("ERROR : can not read byte from buffer.");
    }
  }
}
