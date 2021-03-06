package com.github.dedis.student20_pop.model.data;

import com.github.dedis.student20_pop.model.Lao;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * LAOState is a wrapper class which stores a reference to a LAO and an RxJava subject that
 * publishes the state whenever the lao is updated
 */
public class LAOState {

  private Subject<Lao> publisher;
  private Lao lao;

  /**
   * Instantiates a new Lao state.
   *
   * @param lao the lao
   */
  public LAOState(Lao lao) {
    this.lao = lao;
    this.publisher = BehaviorSubject.createDefault(lao);
  }

  /**
   * getObservable returns an observable which subscribers can use to listen to updates on the LAO
   *
   * @return the observable
   */
  public Observable<Lao> getObservable() {
    return publisher;
  }

  /** publish is used to publish a LAO state update to all observers. */
  public void publish() {
    publisher.onNext(lao);
  }

  /**
   * getLao returns the internal lao state instance.
   *
   * @return the lao
   */
  public Lao getLao() {
    return lao;
  }
}
