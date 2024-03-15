package com.vilin.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Book {
  private int id;
  private String bookName;
}
public class AtomicStampedReferenceDemo {
  public static void main(String[] args) {
    Book javaBook = new Book(1, "javaBook");
    AtomicStampedReference<Book> atomicStampedReference = new AtomicStampedReference<>(javaBook, 1);
    System.out.println(atomicStampedReference.getReference() + "\t" + atomicStampedReference.getStamp());

    Book mysqlBook = new Book(2, "mysqlBook");
    boolean b;
    b = atomicStampedReference.compareAndSet(javaBook, mysqlBook, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
    System.out.println(b + "\t" + atomicStampedReference.getReference() + "\t" + atomicStampedReference.getStamp());

    b = atomicStampedReference.compareAndSet(mysqlBook, javaBook, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
    System.out.println(b + "\t" + atomicStampedReference.getReference() + "\t" + atomicStampedReference.getStamp());
  }
}
