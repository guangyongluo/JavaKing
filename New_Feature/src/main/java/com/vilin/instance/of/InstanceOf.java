package com.vilin.instance.of;

public class InstanceOf {

  public static void main(String[] args) {

    Fruit apple = new Apple();
    if (apple instanceof Apple) {
      Apple a = (Apple) apple;
      a.exec();
    }

    if (apple instanceof Apple app) {
      app.exec();
    }
  }

}

interface Fruit {

}

class Apple implements Fruit {

  public void exec() {
    System.out.println("exec ...");
  }
}