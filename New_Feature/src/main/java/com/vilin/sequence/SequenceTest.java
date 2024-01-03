package com.vilin.sequence;

import java.util.ArrayList;
import java.util.List;

public class SequenceTest {

  public static void main(String[] args) {
    List<String> list = new ArrayList<>();

    list.addFirst("hello");
    list.addFirst("world");
    list.addLast("Java");
    list.addLast("King");

    System.out.println(list);
  }


}
