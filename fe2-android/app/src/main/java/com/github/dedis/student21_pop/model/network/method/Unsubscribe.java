package com.github.dedis.student21_pop.model.network.method;

/** Unsubscribe from a channel */
public final class Unsubscribe extends Query {

  public Unsubscribe(String channel, int id) {
    super(channel, id);
  }

  @Override
  public String getMethod() {
    return Method.UNSUBSCRIBE.getMethod();
  }
}
