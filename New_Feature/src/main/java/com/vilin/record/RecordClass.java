package com.vilin.record;

public record RecordClass(String name, Integer age) {

  public RecordClass {
    System.out.println("my name is " + name);
  }

  public static void main(String[] args) {
    RecordClass recordClass = new RecordClass("Leo", 38);
    Integer age = recordClass.age();
    String name = recordClass.name();
    System.out.println("my name is " + name + ", I am " + age + " years old.");
  }

}
