package com.github.dedis.student21_pop.model.network.method;

/** Subscribe to a channel */
public final class Subscribe extends Query {

  public Subscribe(String channel, int id) {
    super(channel, id);
  }

  @Override
  public String getMethod() {
    return Method.SUBSCRIBE.getMethod();
  }
}
