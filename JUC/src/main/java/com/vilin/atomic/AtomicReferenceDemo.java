package com.vilin.atomic;

import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import lombok.Data;

public class AtomicReferenceDemo {
  public static void main(String[] args) {
    AtomicReference<User> atomicReference = new AtomicReference<>();
    User z3 = new User("z3", 22);
    User li4 = new User("li4", 25);

    atomicReference.set(z3);
    System.out.println(atomicReference.compareAndSet(z3, li4) + "\t" + atomicReference.get().toString());//true	User(userName=li4, age=25)
    System.out.println(atomicReference.compareAndSet(z3, li4) + "\t" + atomicReference.get().toString());//false	User(userName=li4, age=25)

  }
}

@Data
@AllArgsConstructor
class User {
  String userName;
  int age;
}
