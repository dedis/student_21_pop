package com.github.dedis.student21_pop.model.data;

import com.github.dedis.student21_pop.model.network.GenericMessage;
import com.github.dedis.student21_pop.model.network.method.Message;
import com.tinder.scarlet.ws.Receive;
import com.tinder.scarlet.ws.Send;
import io.reactivex.Observable;

public interface LAOService {

  @Send
  void sendMessage(Message msg);

  @Receive
  Observable<GenericMessage> observeMessage();
}
