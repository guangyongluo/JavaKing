package com.vilin.record;

import java.util.Objects;

public final class RecordTest {

  private final String name;

  private final Integer age;

  public RecordTest(String name, Integer age) {
    this.name = name;
    this.age = age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RecordTest that = (RecordTest) o;
    return Objects.equals(name, that.name) && Objects.equals(age, that.age);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age);
  }
}
