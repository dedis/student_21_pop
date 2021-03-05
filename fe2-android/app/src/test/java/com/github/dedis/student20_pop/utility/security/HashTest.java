package com.github.dedis.student20_pop.utility.security;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class HashTest {

  @Test
  public void hashNullDataTest() {
    assertThrows(NullPointerException.class, () -> Hash.hash(null, null));
  }

  @Test
  public void hashTest() {
    assertNotNull(Hash.hash("Data to hash"));
  }

  @Test
  public void escapingTest() {
    assertNotEquals(Hash.hash("test", "10"), Hash.hash("test10"));
  }
}
