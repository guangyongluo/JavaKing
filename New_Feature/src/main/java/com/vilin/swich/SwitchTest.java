package com.vilin.swich;

import java.util.Random;

public class SwitchTest {

  public static void main(String[] args) {
    var level = new Random().nextInt(4);
    var cnLevel = "";
    switch (level) {
      case 1 -> cnLevel = "very good";
      case 2 -> cnLevel = "good";
      case 3 -> cnLevel = "ok";
      default -> cnLevel = "error!";
    }
    System.out.println(cnLevel);

    var egLevel = switch (level) {
      case 1 -> "very good";
      case 2 -> "good";
      case 3 -> "ok";
      default -> "error!";
    };
    System.out.println(egLevel);
  }
}
